package com.sauce.inunion;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NoticeDetailManager extends Fragment {

    SharedPreferences pref;

    Retrofit retrofit;
    NoticeInterface service;
    private Date stringToDate(String stringDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(stringDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    private static class TIME_MAXIMUM{
        public static final int SEC = 60;
        public static final int MIN = 60;
        public static final int HOUR = 24;
        public static final int DAY = 30;
        public static final int MONTH = 12;
    }
    public static String formatTimeString(Date tempDate){
        long curTime = System.currentTimeMillis();
        long regTime = tempDate.getTime();
        long diffTime = (curTime - regTime) / 1000;
        String msg = null;
        if (diffTime < TIME_MAXIMUM.SEC){
            // sec
            msg = "방금 전";
        }
        else if ((diffTime /= TIME_MAXIMUM.SEC) < TIME_MAXIMUM.MIN){
            // min
            msg = diffTime + "분 전";
        }
        else if ((diffTime /= TIME_MAXIMUM.MIN) < TIME_MAXIMUM.HOUR){
            // hour
            msg = (diffTime) + "시간 전";
        }
        else if ((diffTime /= TIME_MAXIMUM.HOUR) < TIME_MAXIMUM.DAY){
            // day
            msg = (diffTime) + "일 전";
        }
        else if ((diffTime /= TIME_MAXIMUM.DAY) < TIME_MAXIMUM.MONTH){
            // month
            msg = (diffTime) + "달 전";
        }
        else {
            msg = (diffTime) + "년 전";
        }
        return msg;
    }
    
    public static NoticeDetailManager newInstance() {
        return new NoticeDetailManager();
    }

    @Override
    public void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.notice_detail_manager, container, false); // 여기서 UI를 생성해서 View를 return

        final Toolbar toolbarActivity = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbarActivity.setVisibility(View.GONE);

        final String Id = getArguments().getString("id");

        TextView tv_major = (TextView) view.findViewById(R.id.major_name);
        pref = getActivity().getSharedPreferences("first", Activity.MODE_PRIVATE);
        String department = pref.getString("App_department","");
        tv_major.setText(department);

        ImageView imageBack = (ImageView) view.findViewById(R.id.toolbar_back);
        TextView textModify = (TextView) view.findViewById(R.id.toolbar_modify);
        ImageView imageDelete = (ImageView) view.findViewById(R.id.notice_detail_delete);

        final TextView choiceTitle = (TextView) view.findViewById(R.id.choice_title);
        final TextView choiceTime = (TextView) view.findViewById(R.id.choice_time);
        final TextView choiceContent = (TextView) view.findViewById(R.id.choice_content);
        //Toast.makeText(getActivity(), position +"", Toast.LENGTH_LONG).show();


        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getActivity().onBackPressed();
                NoticeManager fragment= new NoticeManager();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
                toolbarActivity.setVisibility(View.VISIBLE);
            }
        });
        retrofit = new Retrofit.Builder()
                .baseUrl("http://117.16.231.66:7001")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(NoticeInterface.class);

        final NoticeImageAdapter recyclerViewAdapter= new NoticeImageAdapter(getActivity());
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.notice_detail_image_rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        service.boardSelect(Id,department).enqueue(new Callback<RetrofitNotice>() {
            @Override
            public void onResponse(Call<RetrofitNotice> call, Response<RetrofitNotice> response) {
//                Toast.makeText(getActivity(),"연결 성공",Toast.LENGTH_SHORT).show();

                RetrofitNotice res = response.body();
                choiceTitle.setText(res.title);
                choiceContent.setText(res.content);
                String stringDate = res.time;
                choiceTime.setText(formatTimeString(stringToDate(stringDate)));
                for (int i=0;i<res.fileName.size();i++){
                    recyclerViewAdapter.addItem(new NoticeImageItem(res.fileName.get(i)));
                }

            }

            @Override
            public void onFailure(Call<RetrofitNotice> call, Throwable t) {
                Toast.makeText(getContext().getApplicationContext(), ""+t, Toast.LENGTH_SHORT).show();
            }
        });


        textModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext().getApplicationContext(),NoticeModify.class);
                intent.putExtra("id",Id);
                startActivity(intent);

            }
        });
        imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                service.boardDelete(Id).enqueue(new Callback<RetrofitNotice>() {
                    @Override
                    public void onResponse(Call<RetrofitNotice> call, Response<RetrofitNotice> response) {
                        Toast.makeText(getActivity(),"연결 성공",Toast.LENGTH_SHORT).show();
                        NoticeManager notice = new NoticeManager();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, notice)
                                .addToBackStack(null)
                                .commit();
                        toolbarActivity.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onFailure(Call<RetrofitNotice> call, Throwable t) {
                        Toast.makeText(getContext().getApplicationContext(), ""+t, Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });


        return view;
    }

}