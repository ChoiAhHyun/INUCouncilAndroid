package com.sauce.inunion;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TaTCalendarActivity extends Fragment implements  TaTCalendarFragment.OnFragmentListener {

    private static final String TAG = "TaTCalendarActivity";
    private static final int COUNT_PAGE = 12;
    private ViewPager viewPager;
    private TaTCalendarAdapter taTCalendarAdapter;
    TextView month;
    RecyclerView recyclerView;
    CalendarScheduleRecyclerAdapter adapter;
    String scheduletitle;
    String memo;
    String year_month;
    RetrofitService retrofitCalendarSelectService;
    RetrofitService retrofitCalendarDeleteService;

    static String department;
    ArrayList<CalendarScheduleRecyclerAdapter.Myscheduleitem> Myitems = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_calendar, container, false); // 여기서 UI를 생성해서 View를 return

        final Toolbar toolbarActivity = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbarActivity.setVisibility(View.VISIBLE);
        TextView mtitle = (TextView) getActivity().findViewById(R.id.toolbar_title);
        mtitle.setText("캘린더");

        Button left_btn = view.findViewById(R.id.left_btn);
        Button right_btn = view.findViewById(R.id.right_btn);
        ImageView add_schedule_btn = view.findViewById(R.id.add_schedule_btn);

        recyclerView = view.findViewById(R.id.recycler_calendar);
        viewPager = view.findViewById(R.id.calendar_pager);
        taTCalendarAdapter = new TaTCalendarAdapter(getActivity().getSupportFragmentManager());
        SharedPreferences pref = getActivity().getSharedPreferences("first", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        department = pref.getString("App_department",null);
        //Log.d("app-depar",department);
        Intent intent = new Intent("Appdepartment");
        intent.putExtra("app_department",department);
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).sendBroadcast(intent);

        setCalendar();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        Myitems = new ArrayList<>();


        String title = taTCalendarAdapter.getMonthDisplayed(COUNT_PAGE);
        year_month = title;
        month = view.findViewById(R.id.Calendar_Month_text);
        month.setText(title);

        add_schedule_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity().getApplicationContext(),AddScheduleActivity.class);
                intent.putExtra("dialog_limit_Date",year_month);
                startActivityForResult(intent, 200);
            }
        });
//        Bitmap bmp = BitmapFactory.decodeResource(getResources(),R.drawable.add_blue);
//        Bitmap bmp2 = addShadow(bmp, bmp.getHeight(), bmp.getWidth(),2,3,3,Color.BLUE,80);
//        add_schedule_btn.setImageBitmap(bmp2);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                year_month = taTCalendarAdapter.getMonthDisplayed(position);
                month.setText(year_month);
                if (position == 0) {
                    taTCalendarAdapter.addPrev();
                    viewPager.setCurrentItem(COUNT_PAGE, false);
                    // Log.d("TaTCalendarActivity","position("+position+") COUNT_PAGE("+COUNT_PAGE+")");
                } else if (position == taTCalendarAdapter.getCount() - 1) {
                    taTCalendarAdapter.addNext();
                    viewPager.setCurrentItem(taTCalendarAdapter.getCount() - (COUNT_PAGE + 1), false);
                    // Log.d("TaTCalendarActivity","position("+position+") COUNT_PAGE("+(taTCalendarAdapter.getCount() - (COUNT_PAGE + 1))+")");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        left_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
            }
        });
        right_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
            }
        });

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver,
                new IntentFilter("select"));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mSecondBroadcastReceiver,
                new IntentFilter("click_delete"));


        return view;

    }

    private void setCalendar() {
        viewPager.setAdapter(taTCalendarAdapter);
        taTCalendarAdapter.setOnFragmentListener(this);
        taTCalendarAdapter.setNumOfMonth(COUNT_PAGE);
        viewPager.setCurrentItem(COUNT_PAGE);
        viewPager.setClipToPadding(false);
        viewPager.setPadding(80, 0, 0, 0);
        viewPager.setPageMargin(40);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("calendar",resultCode+"");
        Log.d("calendar",requestCode+"");
        if(requestCode == 200 && resultCode == 200){
//            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
//            setCalendar();
//            MainActivityManager activityManager = new MainActivityManager();
//            activityManager.loadFragment(this);
        }
    }
    private BroadcastReceiver mSecondBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String data = intent.getStringExtra("delete_position");
            //Log.d("test2",Myitems.get(Integer.parseInt(data)).scheduleId+"");
            String id = Myitems.get(Integer.parseInt(data)).scheduleId.toString();
            final String startDate = Myitems.get(Integer.parseInt(data)).startDay.toString();
            final String endDate = Myitems.get(Integer.parseInt(data)).endDay.toString();
            retrofitCalendarDeleteService = new Retrofit.Builder().baseUrl("http://117.16.231.66:7001").addConverterFactory(GsonConverterFactory.create()).build().create(RetrofitService.class);
            retrofitCalendarDeleteService.calendardelete(id).enqueue(new Callback<RetrofitResult>() {
                @Override
                public void onResponse(Call<RetrofitResult> call, Response<RetrofitResult> response) {
                    if(response.isSuccessful()){
                        RetrofitResult result = response.body();
                        //Log.d("delete",result.ans);
//                        getFragmentManager().beginTransaction().detach(TaTCalendarActivity.this).attach(TaTCalendarActivity.this).commit();
//                        setCalendar();
//                        MainActivityManager activityManager = new MainActivityManager();
//                        activityManager.loadFragment(TaTCalendarActivity.this);
                        Intent intent = new Intent("Delete");
                        intent.putExtra("startDay",startDate);
                        intent.putExtra("endDay",endDate);
                        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).sendBroadcast(intent);
                        Log.d("test",startDate);
                        Log.d("test",endDate);

                    }
                }

                @Override
                public void onFailure(Call<RetrofitResult> call, Throwable t) {
                    //Log.d("delete",t+"");
                }
            });

            Myitems.remove(Integer.parseInt(data));
            adapter = new CalendarScheduleRecyclerAdapter(Myitems, getActivity(), getFragmentManager());
            recyclerView.setAdapter(adapter);
        }
    };

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("selected");
            final String data2 = intent.getStringExtra("selected_id");
            //Log.d("data",data);
            //Log.d("data2",data2);
            if(data.equals("true")){
                retrofitCalendarSelectService = new Retrofit.Builder().baseUrl("http://117.16.231.66:7001").addConverterFactory(GsonConverterFactory.create()).build().create(RetrofitService.class);
                retrofitCalendarSelectService.calendarselect(department).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        JsonArray array = response.body();
                        String sD;
                        String eD;
                        Myitems.clear();
                        for(int i=0; i < array.size();i++){
                            JsonObject object = array.get(i).getAsJsonObject();
                            sD = removeHyphen(object.get("startDate").getAsString());
                            eD = removeHyphen(object.get("endDate").getAsString());
                            //Log.d("startDayDay",sD);
                            //Log.d("endDayDay",eD);
                            if(sD.equals(data2)){
                                /*Log.d("test",sD+","
                                        +object.get("scheduleTitle").getAsString()+","
                                        +sortingTM(object.get("startTime").getAsString())+","
                                        +object.get("scheduleId").getAsString()+","
                                        +object.get("memo").getAsString()
                                );*/
                                Myitems.add(new CalendarScheduleRecyclerAdapter.Myscheduleitem(object.get("scheduleTitle").getAsString(),
                                        sortingTM(object.get("startTime").getAsString()),
                                        object.get("scheduleId").getAsString(),
                                        object.get("memo").getAsString(),
                                        sD,
                                        eD
                                ));
                                adapter = new CalendarScheduleRecyclerAdapter(Myitems, getActivity(), getFragmentManager());

                            }
                            else if((Integer.parseInt(data2) > Integer.parseInt(sD)) && Integer.parseInt(data2) <= Integer.parseInt(eD)){
                                Myitems.add(new CalendarScheduleRecyclerAdapter.Myscheduleitem(object.get("scheduleTitle").getAsString(),
                                        "진행 중",
                                        object.get("scheduleId").getAsString(),
                                        object.get("memo").getAsString(),
                                        sD,
                                        eD));
                                adapter = new CalendarScheduleRecyclerAdapter(Myitems, getActivity(), getFragmentManager());
                            }
                        }
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {
                        Log.d("calendar",t+"");
                    }
                });
            }
        }
    };

    @Override
    public void onFragmentListener(View view) {
        resizeHeight(view);
    }

    public void resizeHeight(View mRootView) {

        if (mRootView.getHeight() < 1) {
            return;
        }
        ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
        if (layoutParams.height <= 0) {
            layoutParams.height = mRootView.getHeight();
            viewPager.setLayoutParams(layoutParams);
            return;
        }
        ValueAnimator anim = ValueAnimator.ofInt(viewPager.getLayoutParams().height, mRootView.getHeight());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = viewPager.getLayoutParams();
                layoutParams.height = 1300;//val;
                viewPager.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(200);
        anim.start();
    }


    public String sortingTM(String str) {
        String string = new String();
        for (int i = 0; i < str.length(); i++)
        {
            if (i < 5)
                string += str.charAt(i);
        }
        StringBuffer sb = new StringBuffer(string);
//        if (string.length() == 4) {
//            if (string.charAt(0) == '1' || string.charAt(0) == '2') {
//        sb.insert(2, ":");
//        sb.append("");
//            }
//        } else {
//            sb.insert(1, ":");
//            sb.append("");
//        }

        String sorted = sb.toString();
        return sorted;
    }

    private String removeHyphen(String startDate) {
        String result = new String();
        for(int i = 0 ; i < 10; i ++)
        {
            if(startDate.charAt(i) != '-')
                result += startDate.charAt(i);
        }
        return result;
    }

    public String removeZero(String string){
        StringBuffer sb = new StringBuffer(6);
        for(int i = 0 ; i < 6; i ++)
        {
            if(i < 4 )
                sb.insert(i + 1,string.charAt(i));

        }
        String result = sb.toString();
        return result;
    }
}