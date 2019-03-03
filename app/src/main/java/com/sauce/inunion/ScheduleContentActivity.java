package com.sauce.inunion;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScheduleContentActivity extends Fragment {
    RetrofitService retrofitCalendarScheduleContentService;
    String department;
    TextView schedule_content_title;
    TextView schedule_content_startdate;
    TextView schedule_content_starttime;
    TextView schedule_content_enddate;
    TextView schedule_content_endtime;
    TextView schedule_content_position;
    TextView schedule_content_memo;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_schedule_content, container, false); // 여기서 UI를 생성해서 View를 return

        final Toolbar toolbarActivity = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbarActivity.setVisibility(View.GONE);

        SharedPreferences pref = getActivity().getSharedPreferences("first", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        department = pref.getString("App_department",null);
        schedule_content_title = view.findViewById(R.id.schedule_content_title);
        schedule_content_startdate = view.findViewById(R.id.schedule_content_startdate);
        schedule_content_starttime = view.findViewById(R.id.schdule_content_starttime);
        schedule_content_enddate = view.findViewById(R.id.schedule_content_enddate);
        schedule_content_endtime = view.findViewById(R.id.schedule_content_endtime);
        schedule_content_position = view.findViewById(R.id.schedule_content_position);
        schedule_content_memo = view.findViewById(R.id.schedule_content_memo);

        ImageView cancel_btn = view.findViewById(R.id.schedule_content_cancel_btn);

        final String Id = getArguments().getString("scheduleId");
        Log.d("calendar",Id);
        retrofitCalendarScheduleContentService = new Retrofit.Builder().baseUrl("http://117.16.231.66:7001").addConverterFactory(GsonConverterFactory.create()).build().create(RetrofitService.class);
        retrofitCalendarScheduleContentService.calendarselect(department).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray array = response.body();
                for(int i=0; i < array.size();i++){
                    JsonObject object = array.get(i).getAsJsonObject();
                    if(object.get("scheduleId").getAsString().equals(Id)){
                        Log.d("calendar",Id);
                        Log.d("calendar",object.get("scheduleTitle").getAsString()+","
                                +object.get("startDate").getAsString()+","
                                +object.get("startTime").getAsString()+","
                                +object.get("endDate").getAsString()+","
                                +object.get("endTime").getAsString()+","
                                +object.get("position").getAsString()+","
                                +object.get("memo").getAsString()
                        );
                        schedule_content_title.setText(object.get("scheduleTitle").getAsString());
                        schedule_content_startdate.setText(sortingYMD(removeHyphen(object.get("startDate").getAsString())));
                        schedule_content_starttime.setText(sortingTM(removeColon(object.get("startTime").getAsString())));
                        schedule_content_enddate.setText(sortingYMD(removeHyphen(object.get("endDate").getAsString())));
                        schedule_content_endtime.setText(sortingTM(removeColon(object.get("endTime").getAsString())));
                        schedule_content_position.setText(object.get("position").getAsString());
                        schedule_content_memo.setText(object.get("memo").getAsString());
                    }

                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                Log.d("calendar",t+"");
            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StudentCalendarActivity fragment= new StudentCalendarActivity();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
                toolbarActivity.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("calendar",resultCode+"");
        Log.d("calendar",requestCode+"");
        if(requestCode == 50 && resultCode == 30){
//            Log.d("gogogo","gogogo");
            schedule_content_title.setText(data.getStringExtra("edST")+"");
            schedule_content_startdate.setText(data.getStringExtra("tvSD")+"");
            schedule_content_starttime.setText(data.getStringExtra("tvST")+"");
            schedule_content_enddate.setText(data.getStringExtra("tvED")+"");
            schedule_content_endtime.setText(data.getStringExtra("tvET")+"");
            schedule_content_position.setText(data.getStringExtra("edPS")+"");
            schedule_content_memo.setText(data.getStringExtra("edMM")+"");
        }
    }
    private String removeHyphen(String startDate) {
        String result = new String();
        for(int i = 0 ; i < startDate.length(); i ++)
        {
            if(startDate.charAt(i) != '-')
                result += startDate.charAt(i);
        }
        return result;
    }
    private String removeColon(String startDate) {
        String result = new String();
        for(int i = 0 ; i < startDate.length() - 3; i ++)
        {
            if(startDate.charAt(i) != ':')
                result += startDate.charAt(i);
        }
        return result;
    }
    public String sortingYMD(String string){
        StringBuffer sb = new StringBuffer(string);
//        for (int i = 0; i < string.length(); i++){
//            if(string.charAt(i) == '-'){
//                if (i < 4){
//                    sb.charAt(i) = '년';
//                }
//            }
//        }
//
//        sb.insert(4,"년 ");
//        sb.insert(7,"월 ");
//        sb.append("일");
//        else{
            sb.insert(4,"년 ");
            sb.insert(8,"월 ");
            sb.append("일");
//        }
        String sorted = sb.toString();
        return sorted;
    }
    public String sortingTM(String string){
        int temp = Integer.parseInt(string);
        String str;
        StringBuffer sb;
        if (temp >= 1200) {
            if (temp >= 1300)
                temp -= 1200;
            str = String.valueOf(temp);
            sb = new StringBuffer(str);
            if(temp < 1000)
                sb.insert(1, ":");
            else
                sb.insert(2, ":");
            sb.append(" p.m ");
        }
        else{
            str = String.valueOf(temp);
            sb = new StringBuffer(str);
            if (temp < 10){
                sb.insert(0, "0:0");
            }
            else if (temp < 100){
                sb.insert(0, "0:");
            }
            else if(temp < 1000){
                sb.insert(1, ":");
            }
            else
                sb.insert(2, ":");
            sb.append(" a.m ");
        }

//        if (string.length() == 4) {
//            if (string.charAt(0) == '1' || string.charAt(0) == '2') {
//                sb.insert(2, "시 ");
//                sb.append("분");
//            }
//        }
//        else{
//            sb.insert(1,"시 ");
//            sb.append("분");
//        }
        String sorted = sb.toString();
        return sorted;
    }
}
