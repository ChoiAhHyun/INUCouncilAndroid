package com.sauce.inunion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.w3c.dom.Text;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TaTCalendarFragment extends Fragment {
    private int position;
    private long timeByMillis;
    private OnFragmentListener onFragmentListener;
    private View mRootView;
    private TaTCalendarView calendarView;
    public TaTCalendarItemView[] taTCalendarItemViews = new TaTCalendarItemView[31];
    int maxDateOfMonth;
    RetrofitService retrofitCalendarSelectService;
    //    String department;
    public void setOnFragmentListener(OnFragmentListener onFragmentListener) {
        this.onFragmentListener = onFragmentListener;
    }

    public interface OnFragmentListener{
        public void onFragmentListener(View view);
    }

    public static TaTCalendarFragment newInstance(int position) {
        TaTCalendarFragment frg = new TaTCalendarFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        frg.setArguments(bundle);
        return frg;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("poisition");
        LocalBroadcastManager.getInstance(TaTCalendarFragment.this.getContext()).registerReceiver(mBroadcastReceiver,
                new IntentFilter("Confirm"));
//        LocalBroadcastManager.getInstance(TaTCalendarFragment.this.getContext()).registerReceiver(mSecondBroadcastReceiver,
//                new IntentFilter("department_change"));

    }
    //    private BroadcastReceiver mSecondBroadcastReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String data = intent.getStringExtra("changed");
//            if(data != null){
//                if(data.equals("true")){
//                    Log.d("change",data+"");
//                    retrofitCalendarSelectService = new Retrofit.Builder().baseUrl("http://117.16.231.66:7001").addConverterFactory(GsonConverterFactory.create()).build().create(RetrofitService.class);
//                    retrofitCalendarSelectService.calendarselect(TaTCalendarActivity.department).enqueue(new Callback<JsonArray>() {
//                        @Override
//                        public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
//                            boolean ing = false;
//                            JsonArray array = response.body();
//                            for(int i=0; i < array.size();i++){
//                                JsonObject object = array.get(i).getAsJsonObject();
//                                Log.d("startDay",object.get("startDate").getAsString());
//                                Log.d("endDay",object.get("endDate").getAsString());
//                                if(object.get("startDate").getAsString().equals(object.get("endDate").getAsString())){
//                                    for (int j = 0; j < maxDateOfMonth; j++) {
//                                        if (object.get("startDate").getAsString().equals(taTCalendarItemViews[j].getId() + "")) {
//                                            taTCalendarItemViews[j].setEvent(R.color.colorPrimaryDark);
//                                        }
//                                    }
//                                }
//                                else{
//                                    for(int j=0; j<maxDateOfMonth; j++){
//                                        if(ing == false){
//                                            if(object.get("startDate").getAsString().equals(taTCalendarItemViews[j].getId()+"")){
//                                                taTCalendarItemViews[j].setEvent(R.color.colorPrimaryDark);
//                                                ing = true;
//                                            }
//                                        } else if(ing == true && object.get("endDate").getAsString().equals(taTCalendarItemViews[j].getId()+"")){
//                                            taTCalendarItemViews[j].setEvent(R.color.colorPrimaryDark);
//                                            ing = false;
//                                        }
//                                        else{
//                                            taTCalendarItemViews[j].setEvent(R.color.colorPrimaryDark);
//                                        }
//                                    }
//                                }
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Call<JsonArray> call, Throwable t) {
//
//                        }
//                    });
//                }
//            }
//        }
//    };
    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(TaTCalendarFragment.this.getContext()).unregisterReceiver(mBroadcastReceiver);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_tat_calendar, null);
        calendarView = (TaTCalendarView) mRootView.findViewById(R.id.calendarview);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeByMillis);
        calendar.set(Calendar.DATE, 1);
//        Log.d("TaTCanlendarFragment",calendar.toString());
        // 1일의 요일
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        ///< 1일이 일요일이 아닐 경우 이전월의 날짜를 넣기 위한 개수 구하기.
        int preOfDay = dayOfWeek-1;
        //이달의 마지막 날
        maxDateOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(timeByMillis);
        calendar1.set(Calendar.DATE, maxDateOfMonth);
        int nextOfDay = 0;
        ///< 마지막 날이 토요일이 아닐경우 다음월의 날짜를 넣기 위한 개수 구하기
        if(maxDateOfMonth == 31){
            if(preOfDay <= 4 ){
                nextOfDay = 14 - calendar1.get(Calendar.DAY_OF_WEEK);
            }
            else{
                nextOfDay = 7 - calendar1.get(Calendar.DAY_OF_WEEK);
            }
        }
        else if(maxDateOfMonth == 28){
            nextOfDay = 14 - calendar1.get(Calendar.DAY_OF_WEEK);
        }
        else{
            if(preOfDay <= 5){
                nextOfDay = 14 - calendar1.get(Calendar.DAY_OF_WEEK);
            }
            else{
                nextOfDay = 7 - calendar1.get(Calendar.DAY_OF_WEEK);
            }
        }
//        int nextOfDay = 7 - calendar1.get(Calendar.DAY_OF_WEEK);
        //Log.d("TaTCanlendarFragment","dayofWeek("+dayOfWeek+") maxDateOfMonth("+maxDateOfMonth+") lastDayofWeek("+calendar1.get(Calendar.DAY_OF_WEEK)+")");
        calendarView.initCalendar(dayOfWeek, maxDateOfMonth);
        /**
         * 요일 설정
         */
        for(int i=0; i < 7; i++) {
            TaTCalendarItemView child = new TaTCalendarItemView(getActivity().getApplicationContext());
            child.setDate(calendar.getTimeInMillis());
            child.setDayOfWeek(i);
            calendarView.addView(child);
            calendarView.setPadding(40,45,0,0);
        }
        /**
         * 이전달에 일자를 먼저 세팅
         * - 1일이 일요일이 아니면, 비워진 칸을 이전달 마지막 일자로 세팅함
         */
        for(int i=0; i < preOfDay; i++) {
            TaTCalendarItemView child = new TaTCalendarItemView(getActivity().getApplicationContext());
            child.setTextColorChange(true);
            if(i == 0) {
                calendar.add(Calendar.DATE,-(preOfDay-i));
            }else{
                calendar.add(Calendar.DATE,1);
            }
            /**
             * 이전월 시간을 설정하기 위해서 날짜 계산 이후에 세팅함
             * - 현재 달로 설정되어 있으므로 계산 이후에 세팅함
             */
            child.setDate(calendar.getTimeInMillis());

            calendarView.addView(child);
            calendarView.setPadding(40,45,0,0);
        }

        /**
         * 이전월 세팅이 이루어지면, 현재월 세팅을 위해서 하루를 더함
         */
        if(preOfDay > 0) {
            calendar.add(Calendar.DATE,1);
        }

        /**
         * 현재월을 세팅
         */
        for (int i = 0; i < maxDateOfMonth; i++) {
            TaTCalendarItemView child = new TaTCalendarItemView(getActivity().getApplicationContext());
            child.setDate(calendar.getTimeInMillis());
            String string = null;
            if(i < 9) {
                string = ("" + calendar.get(Calendar.YEAR)) + (calendar.get(Calendar.MONTH) + 1) + "1";
            }
            else{
                string = ("" + calendar.get(Calendar.YEAR)) + (calendar.get(Calendar.MONTH) + 1) + "01";
            }
            int id = Integer.parseInt(string);
            child.setId(id+i);
            calendar.add(Calendar.DATE, 1);
            taTCalendarItemViews[i] = child;
            calendarView.addView(child);
        }

        /**
         * 현재월의 마지막이 토요일이 아닐경우 다음월 날짜를 토요일까지 세팅함
         */
        for(int i=0; i < nextOfDay; i++) {
            final TaTCalendarItemView child = new TaTCalendarItemView(getActivity().getApplicationContext());
            child.setTextColorChange(true);
            child.setDate(calendar.getTimeInMillis());
            calendar.add(Calendar.DATE, 1);
            calendarView.addView(child);
        }
//        for (int i = 0; i < maxDateOfMonth + 7; i++) {
//            TaTCalendarItemView child = new TaTCalendarItemView(getActivity().getApplicationContext());
//            if (i == 20) {
//                child.setEvent(R.color.colorPrimaryDark);
//            }
//            child.setDate(calendar.getTimeInMillis());
//            if (i < 7) {
//                child.setDayOfWeek(i);
//            } else {
//                calendar.add(Calendar.DATE, 1);
//            }
//            calendarView.addView(child);
//        }
        retrofitCalendarSelectService = new Retrofit.Builder().baseUrl("http://117.16.231.66:7001").addConverterFactory(GsonConverterFactory.create()).build().create(RetrofitService.class);
        retrofitCalendarSelectService.calendarselect(TaTCalendarActivity.department).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                boolean ing = false;
                JsonArray array = response.body();
                String sD;
                String eD;
                for(int i=0; i < array.size();i++){
                    JsonObject object = array.get(i).getAsJsonObject();
                    sD = removeHyphen(object.get("startDate").getAsString());
                    eD = removeHyphen(object.get("endDate").getAsString());
                    Log.d("startDay",sD);
                    Log.d("endDay",eD);
                    if(sD.equals(eD)){
                        for (int j = 0; j < maxDateOfMonth; j++) {
                            if (sD.equals(taTCalendarItemViews[j].getId() + "")) {
                                taTCalendarItemViews[j].setEvent(R.color.colorPrimaryDark);
                            }
                        }
                    }
                    else{
                        for(int j=0; j<maxDateOfMonth; j++){
                            if(ing == false){
                                if(sD.equals(taTCalendarItemViews[j].getId()+"")){
                                    taTCalendarItemViews[j].setEvent(R.color.colorPrimaryDark);
                                    ing = true;
                                }
                            } else if(ing == true && eD.equals(taTCalendarItemViews[j].getId()+"")){
                                taTCalendarItemViews[j].setEvent(R.color.colorPrimaryDark);
                                ing = false;
                            }
                            else{
                                taTCalendarItemViews[j].setEvent(R.color.colorPrimaryDark);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });

        return mRootView;

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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        if (isVisibleToUser && onFragmentListener != null && mRootView != null) {
            onFragmentListener.onFragmentListener(mRootView);
        }
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getUserVisibleHint()) {

            mRootView.post(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    onFragmentListener.onFragmentListener(mRootView);
                }
            });

        }
    }

    public void setTimeByMillis(long timeByMillis) {
        this.timeByMillis = timeByMillis;
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data1 = intent.getStringExtra("confirm");
            String startDay = intent.getStringExtra("startDay");
            String endDay = intent.getStringExtra("endDay");

//            Log.d("test",data1);
            //     Log.d("test2",data2);
            boolean ing = false;
            if(startDay != null && endDay != null){
                if(startDay.equals(endDay)) {
                    for (int i = 0; i < maxDateOfMonth; i++) {
                        if (startDay.equals(taTCalendarItemViews[i].getId() + "")) {
                            taTCalendarItemViews[i].setEvent(R.color.colorPrimaryDark);
                        }
                    }
                }
                else{
                    for(int i=0; i<maxDateOfMonth; i++) {

                        if (ing == false) {
                            if (startDay.equals(taTCalendarItemViews[i].getId() + "")) {
                                taTCalendarItemViews[i].setEvent(R.color.colorPrimaryDark);
                                ing = true;
                            }
                        } else if (ing == true && endDay.equals(taTCalendarItemViews[i].getId() + "")) {
                            taTCalendarItemViews[i].setEvent(R.color.colorPrimaryDark);
                            ing = false;
                        } else {
                            taTCalendarItemViews[i].setEvent(R.color.colorPrimaryDark);
                        }
                    }
                }
            }

        }
    };

}
