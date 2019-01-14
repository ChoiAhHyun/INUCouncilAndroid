package com.sauce.inunion;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ScheduleContentActivity extends Activity {
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_content);
        SharedPreferences pref = getSharedPreferences("first", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        department = pref.getString("App_department",null);
        schedule_content_title = findViewById(R.id.schedule_content_title);
        schedule_content_startdate = findViewById(R.id.schedule_content_startdate);
        schedule_content_starttime = findViewById(R.id.schdule_content_starttime);
        schedule_content_enddate = findViewById(R.id.schedule_content_enddate);
        schedule_content_endtime = findViewById(R.id.schedule_content_endtime);
        schedule_content_position = findViewById(R.id.schedule_content_position);
        schedule_content_memo = findViewById(R.id.schedule_content_memo);
        TextView delete_btn = findViewById(R.id.delete_event_btn);
        ImageView cancel_btn = findViewById(R.id.schedule_content_cancel_btn);
        TextView edit_btn = findViewById(R.id.schedule_content_edit_btn);
        Button add_btn = findViewById(R.id.add_schedule_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent3 = new Intent(getApplicationContext(),AddScheduleActivity.class);
                SimpleDateFormat format = new SimpleDateFormat("yyyy년 MM월");
                long currentMonth = System.currentTimeMillis();
                String year_month = format.format(currentMonth);
                intent3.putExtra("dialog_limit_Date",year_month);
                startActivity(intent3);
            }
        });
        if(ChooseMajorActivity.isStudentMode == true){
            delete_btn.setVisibility(View.GONE);
            delete_btn.setEnabled(false);
            edit_btn.setVisibility(View.GONE);
            edit_btn.setEnabled(false);
        }
        final Intent intent = getIntent();
        final String schedule_id = intent.getStringExtra("scheduleId");
        Log.d("test",intent.getStringExtra("scheduleId"));
        retrofitCalendarScheduleContentService = new Retrofit.Builder().baseUrl("http://117.16.231.66:7001").addConverterFactory(GsonConverterFactory.create()).build().create(RetrofitService.class);
        retrofitCalendarScheduleContentService.calendarselect(department).enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray array = response.body();
                for(int i=0; i < array.size();i++){
                    JsonObject object = array.get(i).getAsJsonObject();
                    if(object.get("scheduleId").getAsString().equals(schedule_id)){
                        Log.d("test","gogogo");
                        schedule_content_title.setText(object.get("scheduleTitle").getAsString());
                        schedule_content_startdate.setText(sortingYMD(object.get("startDate").getAsString()));
                        schedule_content_starttime.setText(sortingTM(object.get("startTime").getAsString()));
                        schedule_content_enddate.setText(sortingYMD(object.get("endDate").getAsString()));
                        schedule_content_endtime.setText(sortingTM(object.get("endTime").getAsString()));
                        schedule_content_position.setText(object.get("position").getAsString());
                        schedule_content_memo.setText(object.get("memo").getAsString());
                    }

                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {

            }
        });
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                retrofitCalendarScheduleContentService.calendardelete(schedule_id).enqueue(new Callback<RetrofitResult>() {
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
                finish();
            }
        });
        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(ScheduleContentActivity.this,ScheduleEditActivity.class);
                intent2.putExtra("IdOfSchedule",schedule_id);
                intent2.putExtra("ScheduleContentTitle",schedule_content_title.getText().toString());
                intent2.putExtra("ScheduleContentStartDate",schedule_content_startdate.getText().toString());
                intent2.putExtra("ScheduleContentStartTime",schedule_content_starttime.getText().toString());
                intent2.putExtra("ScheduleContentEndDate",schedule_content_enddate.getText().toString());
                intent2.putExtra("ScheduleContentEndTime",schedule_content_endtime.getText().toString());
                intent2.putExtra("ScheduleContentPosition",schedule_content_position.getText().toString());
                intent2.putExtra("ScheduleContentMemo",schedule_content_memo.getText().toString());
                startActivityForResult(intent2,50);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("test1",resultCode+"");
        Log.d("test2",requestCode+"");
        if(requestCode == 50 && resultCode == 30){
            Log.d("gogogo","gogogo");
            schedule_content_title.setText(data.getStringExtra("edST")+"");
            schedule_content_startdate.setText(data.getStringExtra("tvSD")+"");
            schedule_content_starttime.setText(data.getStringExtra("tvST")+"");
            schedule_content_enddate.setText(data.getStringExtra("tvED")+"");
            schedule_content_endtime.setText(data.getStringExtra("tvET")+"");
            schedule_content_position.setText(data.getStringExtra("edPS")+"");
            schedule_content_memo.setText(data.getStringExtra("edMM")+"");
        }
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
        if (string.length() == 4) {
            if (string.charAt(0) == '1' || string.charAt(0) == '2') {
                sb.insert(2, "시 ");
                sb.append("분");
            }
        }
        else{
            sb.insert(1,"시 ");
            sb.append("분");
        }
        String sorted = sb.toString();
        return sorted;
    }
}
