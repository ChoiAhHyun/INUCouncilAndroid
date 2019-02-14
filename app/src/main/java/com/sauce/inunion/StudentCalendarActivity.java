package com.sauce.inunion;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class StudentCalendarActivity extends Fragment implements TaTCalendarFragment.OnFragmentListener {

    private static final String TAG = "TaTCalendarActivity";
    private static final int COUNT_PAGE = 12;
    private ViewPager viewPager;
    private TaTCalendarAdapter taTCalendarAdapter;
    TextView month;
    RecyclerView recyclerView;
    CalendarScheduleRecyclerAdapter adapter;
    SwipeController swipeController;
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
        View view = inflater.inflate(R.layout.activity_student_calendar, container, false); // 여기서 UI를 생성해서 View를 return

        final Toolbar toolbarActivity = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbarActivity.setVisibility(View.VISIBLE);
        TextView mtitle = (TextView) getActivity().findViewById(R.id.toolbar_title);
        mtitle.setText("캘린더");

        Button left_btn = view.findViewById(R.id.student_left_btn);
        Button right_btn = view.findViewById(R.id.student_right_btn);
        recyclerView = view.findViewById(R.id.student_recycler_calendar);
        viewPager = view.findViewById(R.id.student_calendar_pager);
        taTCalendarAdapter = new TaTCalendarAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(taTCalendarAdapter);
        SharedPreferences pref = getActivity().getSharedPreferences("first", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        department = pref.getString("App_department",null);
        Log.d("app-depar",department);
        Intent intent = new Intent("Appdepartment");
        intent.putExtra("app_department",department);
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).sendBroadcast(intent);
        taTCalendarAdapter.setOnFragmentListener(this);
        taTCalendarAdapter.setNumOfMonth(COUNT_PAGE);
        viewPager.setCurrentItem(COUNT_PAGE);
        viewPager.setClipToPadding(false);
        viewPager.setPadding(80, 0, 0, 0);
        viewPager.setPageMargin(40);


        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));

        Myitems = new ArrayList<>();

        setupRecyclerView();

        String title = taTCalendarAdapter.getMonthDisplayed(COUNT_PAGE);
        year_month = title;
        month = view.findViewById(R.id.student_Calendar_Month_text);
        month.setText(title);


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

    private BroadcastReceiver mSecondBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("delete_position");
            Log.d("test2",Myitems.get(Integer.parseInt(data)).scheduleId+"");
            String id = Myitems.get(Integer.parseInt(data)).scheduleId.toString();
            retrofitCalendarDeleteService = new Retrofit.Builder().baseUrl("http://117.16.231.66:7001").addConverterFactory(GsonConverterFactory.create()).build().create(RetrofitService.class);
            retrofitCalendarDeleteService.calendardelete(id).enqueue(new Callback<RetrofitResult>() {
                @Override
                public void onResponse(Call<RetrofitResult> call, Response<RetrofitResult> response) {
                    if(response.isSuccessful()){
                        RetrofitResult result = response.body();
                        Log.d("delete",result.ans);
                    }
                }

                @Override
                public void onFailure(Call<RetrofitResult> call, Throwable t) {

                }
            });

            Myitems.remove(Integer.parseInt(data));
            adapter = new CalendarScheduleRecyclerAdapter(Myitems);
            recyclerView.setAdapter(adapter);
        }
    };

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("selected");
            final String data2 = intent.getStringExtra("selected_id");
            Log.d("data",data);
            Log.d("data2",data2);
            if(data.equals("true")){
                retrofitCalendarSelectService = new Retrofit.Builder().baseUrl("http://117.16.231.66:7001").addConverterFactory(GsonConverterFactory.create()).build().create(RetrofitService.class);
                retrofitCalendarSelectService.calendarselect(department).enqueue(new Callback<JsonArray>() {
                    @Override
                    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                        JsonArray array = response.body();
                        Myitems.clear();
                        for(int i=0; i < array.size();i++){
                            JsonObject object = array.get(i).getAsJsonObject();
                            if(object.get("startDate").getAsString().equals(data2)){
//                                Log.d("test","gogogo");
                                Myitems.add(new CalendarScheduleRecyclerAdapter.Myscheduleitem(object.get("scheduleTitle").getAsString(),
                                        sortingTM(object.get("startTime").getAsString()),
                                        object.get("scheduleId").getAsString(),
                                        object.get("memo").getAsString()));
                                adapter = new CalendarScheduleRecyclerAdapter(Myitems);

                            }
                            else if((Integer.parseInt(data2) > Integer.parseInt(object.get("startDate").getAsString())) && Integer.parseInt(data2) <= Integer.parseInt(object.get("endDate").getAsString())){
                                Myitems.add(new CalendarScheduleRecyclerAdapter.Myscheduleitem(object.get("scheduleTitle").getAsString(),
                                        "진행 중",
                                        object.get("scheduleId").getAsString(),
                                        object.get("memo").getAsString()));
                                adapter = new CalendarScheduleRecyclerAdapter(Myitems);
                            }

                        }
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(Call<JsonArray> call, Throwable t) {

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
        private void setupRecyclerView() {
        swipeController = new SwipeController(new SwipeControllerActions() {
            @Override
            public void onRightClicked(int position) {
                adapter.items.remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyItemRangeChanged(position, adapter.getItemCount());
            }
        });

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                swipeController.onDraw(c);
            }
        });
    }
    public String sortingYMD(String string){
        StringBuffer sb = new StringBuffer(string);
        if(string.charAt(4) != '1'){
            sb.insert(4,"년 ");
            sb.insert(7,"월 ");
            sb.append("일");
        }
        else{
            sb.insert(4,"년 ");
            sb.insert(8,"월 ");
            sb.append("일");
        }
        String sorted = sb.toString();
        return sorted;
    }
    public String sortingTM(String string){
        StringBuffer sb = new StringBuffer(string);
        if(string.charAt(0) == '1' || string.charAt(0) == '2'){
            sb.insert(2,"시 ");
            sb.append("분");
        }
        else{
            sb.insert(1,"시 ");
            sb.append("분");
        }
        String sorted = sb.toString();
        return sorted;
    }
}

