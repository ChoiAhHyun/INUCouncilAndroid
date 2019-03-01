package com.sauce.inunion;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddScheduleActivity extends Activity implements DatePickerDialog.OnDateSetListener{
    Retrofit retrofit;
    RetrofitService retrofitCalendarSaveService;
    TextView startDay;
    TextView startTime;
    TextView endDay;
    TextView endTime;
    SharedPreferences pref;
    String department;
    String[] startDateString = new String[3];
    String[] endDateString = new String[3];
    String startDate8;
    String endDate8;
    String[] startTimeString = new String[2];
    String[] endTimeString = new String[2];
    String startTime6;
    String endTime6;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        final EditText scheduleTitle = findViewById(R.id.scheduleTitle);
//        final TextView start_time = findViewById(R.id.start_time);
//        final TextView end_time = findViewById(R.id.end_time);
        final EditText position = findViewById(R.id.position);
        final EditText memo = findViewById(R.id.memo);
        pref = getSharedPreferences("first", Activity.MODE_PRIVATE);
        final String department = pref.getString("App_department",null);
        final Calendar calendar = Calendar.getInstance();
        final TextView confirm_btn = findViewById(R.id.confirm_btn);
        ImageView cancel_btn = findViewById(R.id.cancel_btn);
        final Calendar minDate = Calendar.getInstance();
        final Calendar maxDate = Calendar.getInstance();
        String month = null;
        Intent intent = getIntent();
        String year_month = removeHangul_String(intent.getStringExtra("dialog_limit_Date"));
        final String year = year_month.substring(0,4);
        if(year_month.charAt(4) != '1') {
            month = year_month.substring(5);
        }
        else{
            month = year_month.substring(4);
        }
        int maxOfMonth = 0;
        Log.d("calendar",year_month);
        Log.d("year",year);
        Log.d("month",month);
        if(month.equals("1") || month.equals("3")||month.equals("5")||month.equals("7")||month.equals("8")||month.equals("10")||month.equals("12")){
            maxOfMonth = 31;
        }
        else if(month.equals("2")){
            maxOfMonth = 28;
        }
        else{
            maxOfMonth = 30;
        }
        Log.d("month",maxOfMonth+"");
        retrofit = new Retrofit.Builder().baseUrl("http://117.16.231.66:7001").addConverterFactory(GsonConverterFactory.create()).build();
        retrofitCalendarSaveService = retrofit.create(RetrofitService.class);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (scheduleTitle.getText().toString() == null || startDate8 == null || startTime6 == null || endDate8 == null || endTime6 == null) {
                    Toast.makeText(getApplicationContext(), "제목과 날짜를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    final Intent intent1 = getIntent();
                    Intent intent2 = new Intent("Confirm");
                    intent2.putExtra("confirm","true");
                    intent2.putExtra("startDay",removeHangul(startDay));
                    intent2.putExtra("endDay",removeHangul(endDay));
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent2);
                    Log.d("calendar",scheduleTitle.getText().toString()+" "+startDate8+" "+startTime6+" "+endDate8+" "+endTime6+" "+position.getText().toString()+" "+memo.getText().toString()+" "+department);
                    retrofitCalendarSaveService.calendarsave(scheduleTitle.getText().toString(), startDate8, startTime6, endDate8, endTime6, position.getText().toString(), memo.getText().toString(), department).enqueue(new Callback<RetrofitResult>() {
                        @Override
                        public void onResponse(Call<RetrofitResult> call, Response<RetrofitResult> response) {
                            if (response.isSuccessful()){
                                RetrofitResult result = response.body();
                                Log.d("calendar",result.ans);
                                setResult(200, intent1);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<RetrofitResult> call, Throwable t) {
                            Log.d("calendar",t+"");
                        }
                    });
                }


            }
        });
        startDay = findViewById(R.id.start_day);

        final int finalMaxOfMonth = maxOfMonth;
        final String finalMonth = month;
        startDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(AddScheduleActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        startDate8 = setStartDate(year, month, dayOfMonth);
                        setYMD(startDay, year, month, dayOfMonth);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                minDate.set(Integer.parseInt(year),Integer.parseInt(finalMonth)-1,1);
                datePickerDialog.getDatePicker().setMinDate(new Date().getTime());

//                maxDate.set(Integer.parseInt(year),Integer.parseInt(finalMonth)-1, finalMaxOfMonth);
//                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
            }
        });


        startTime = findViewById(R.id.start_time);
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddScheduleActivity.this,AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        startTime6 = setStartTime(hourOfDay,minute);
                        setTM(startTime,hourOfDay,minute);
                    }
                },0,0,false);
                timePickerDialog.show();
            }
        });

        endDay = findViewById(R.id.end_day);
        final String finalMonth1 = month;
        endDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddScheduleActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                        endDate8 = setEndDate(year, month, dayOfMonth);
                        setYMD(endDay, year, month, dayOfMonth);
                    }
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                minDate.set(Integer.parseInt(year),Integer.parseInt(finalMonth1)-1,1);
                datePickerDialog.getDatePicker().setMinDate(new Date().getTime());

//                maxDate.set(Integer.parseInt(year),Integer.parseInt(finalMonth1)-1, finalMaxOfMonth);
//                datePickerDialog.getDatePicker().setMaxDate(null);
                datePickerDialog.show();
            }
        });

        endTime = findViewById(R.id.end_time);
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(AddScheduleActivity.this,AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        endTime6 = setEndTime(hourOfDay,minute);
                        setTM(endTime,hourOfDay,minute);
                    }
                },0,0,false);
                timePickerDialog.show();
            }
        });

        final EditText schedule_memo = findViewById(R.id.memo);
        final TextView length = findViewById(R.id.length);
        schedule_memo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length() == 0){
                    length.setText("0");
                }
                else{
                    length.setText(schedule_memo.getText().toString().length()+"");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    public String removeHangul(TextView textView){
        String string = new String();
        for(int i = 0 ; i < textView.getText().toString().length(); i ++)
        {
            // 48 ~ 57은 아스키 코드로 0~9이다.
            if(48 <= textView.getText().toString().charAt(i) && textView.getText().toString().charAt(i) <= 57)
                string += textView.getText().toString().charAt(i);
        }
        return string;
    }
    public String removeHangul_String(String string){
        String temp = new String();
        for(int i = 0 ; i < string.length(); i ++)
        {
            // 48 ~ 57은 아스키 코드로 0~9이다.
            if(48 <= string.charAt(i) && string.charAt(i) <= 57)
                temp += string.charAt(i);
        }
        return temp;
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }
    public void setYMD(TextView textView, int year, int month, int dayOfMonth){
        textView.setText(year+"년 "+(month+1)+"월 "+dayOfMonth+"일");
    }
    public void setTM(TextView textView, int hourOfDay, int minute){
        if(minute<10){
            textView.setText(hourOfDay+"시 "+"0"+minute+"분");
        }
        else
        {
            textView.setText(hourOfDay+"시 "+minute+"분");
        }

    }
    public String setStartDate(int year, int month, int dayOfMonth){
        startDateString[0] = year + "";
        int temp = month + 1;
        if(month < 10) {
            startDateString[1] = "0" + temp + "";
        }
        else{
            startDateString[1] = temp + "";
        }
        if(dayOfMonth < 10) {
            startDateString[2] = "0" + dayOfMonth + "";
        }
        else
            startDateString[2] = dayOfMonth + "";
        return startDateString[0] + startDateString[1] + startDateString[2];
    }
    public String setEndDate(int year, int month, int dayOfMonth){
        endDateString[0] = year + "";
        int temp = month + 1;
        if(month < 10) {
            endDateString[1] = "0" + temp + "";
        }
        else{
            endDateString[1] = temp + "";
        }
        if(dayOfMonth < 10) {
            endDateString[2] = "0" + dayOfMonth + "";
        }
        else
            endDateString[2] = dayOfMonth + "";
        return endDateString[0] + endDateString[1] + endDateString[2];
    }
    public String setStartTime(int hourOfDay, int minute){
        if(hourOfDay < 10) {
            startTimeString[0] = "0" + hourOfDay + "";
        }
        else
            startTimeString[0] = hourOfDay + "";
        if(minute < 10) {
            startTimeString[1] = "0" + minute + "";
        }
        else
            startTimeString[1] = minute + "";
        return startTimeString[0] + startTimeString[1] + "00";
    }
    public String setEndTime(int hourOfDay, int minute){
        if(hourOfDay < 10) {
            endTimeString[0] = "0" + hourOfDay + "";
        }
        else
            endTimeString[0] = hourOfDay + "";
        if(minute < 10) {
            endTimeString[1] = "0" + minute + "";
        }
        else
            endTimeString[1] = minute + "";
        return endTimeString[0] + endTimeString[1] + "00";
    }
} // end of class

