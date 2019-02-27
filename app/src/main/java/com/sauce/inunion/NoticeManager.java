package com.sauce.inunion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;


public class NoticeManager extends Fragment {

    public static List<NoticeImageItem> subList1;
    InputMethodManager imm;
    SharedPreferences pref;
    Retrofit retrofit;
    NoticeInterface service;
    public static NoticeManager newInstance() {
        return new NoticeManager();
    }

    @Override
    public void onCreate(@Nullable Bundle saveInstanceState){
        super.onCreate(saveInstanceState);

    }

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice_manager, container, false); // 여기서 UI를 생성해서 View를 return

        final Toolbar toolbarActivity = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbarActivity.setVisibility(View.VISIBLE);

        TextView tv_major = (TextView) view.findViewById(R.id.major_name);
        pref = getActivity().getSharedPreferences("first", Activity.MODE_PRIVATE);
        String department = pref.getString("App_department",null);
        tv_major.setText(department);

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        final FrameLayout mainLayout = (FrameLayout) view.findViewById(R.id.fragment_notice);
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
            }
        });

        TextView mtitle = (TextView) getActivity().findViewById(R.id.toolbar_title);
        mtitle.setText("공지사항");

        retrofit = new Retrofit.Builder()
                .baseUrl("http://117.16.231.66:7001")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(NoticeInterface.class);

        ImageView imageView = (ImageView) view.findViewById(R.id.notice_write);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext().getApplicationContext(), // 현재 화면의 제어권자
                        NoticeWrite.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다
//                NoticeWrite noticeWrite = new NoticeWrite();
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_container, noticeWrite)
//                        .addToBackStack(null)
//                        .commit();
            }
        });

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.notice_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutFrozen(true);

        final NoticeListAdapterManager recyclerViewAdapter = new NoticeListAdapterManager(getActivity(),getFragmentManager());
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        Call<List<RetrofitNotice>> call = service.boardSort(department);
        call.enqueue(new Callback<List<RetrofitNotice>>() {
            @Override
            public void onResponse(Call<List<RetrofitNotice>> call,
                                   Response<List<RetrofitNotice>> response) {
                Log.d("notice", "연결 성공"+response.code());
                final List<RetrofitNotice> res = response.body();
                for (int i=0; i<res.size();i++){
                    String count;
                    String stringDate = res.get(i).time;
                    Log.v("시간",stringDate);
                    ArrayList<String> newFileName = new ArrayList<>();
                    if (res.get(i).fileName.size() <= 4){
                        count = "";
                        for (int j=0; j<res.get(i).fileName.size(); j++){
                            newFileName.add(res.get(i).fileName.get(j));
                            Log.v("파일",i +", " + res.get(i).fileName.get(j));
                        }
                    }
                    else{
                        count = "+"+Integer.toString(res.get(i).fileName.size() - 4);
                        for (int j=0; j < 4; j++){
                            newFileName.add(res.get(i).fileName.get(j));
                            Log.v("파일",i +", " + res.get(i).fileName.get(j));
                        }
                    }
                    recyclerViewAdapter.addItem(new NoticeListItem(res.get(i).title,
                            res.get(i).content,
                            formatTimeString(stringToDate(stringDate)),
                            res.get(i).content_serial_id,
                            count,
                            newFileName));
                    recyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<RetrofitNotice>> call, Throwable t) {
//                Toast.makeText(getActivity(),""+t,Toast.LENGTH_SHORT).show();
                Log.v("테스트",""+t);
            }
        });

        final EditText editText = (EditText) view.findViewById(R.id.search_notice);
        final ImageView SearchIcon = (ImageView ) view.findViewById(R.id.search_notice_icon);
        final TextView textView = (TextView) view.findViewById(R.id.search_tv);

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    int d = 280;
                    float mScale = getResources().getDisplayMetrics().density;
                    int calWidth = (int)(d*mScale);
                    int p = 43;
                    float mScale2 = getResources().getDisplayMetrics().density;
                    int calLeft = (int)(p*mScale2);
                    ViewGroup.LayoutParams param = editText.getLayoutParams();
                    param.width = calWidth;
                    editText.setLayoutParams(param);
                    //editText.setMaxWidth(calWidth);
                    editText.setHintTextColor(getResources().getColor(R.color.search_hint_focus));
                    editText.setBackgroundResource(R.drawable.shape_search_focus);
                    editText.setPadding(calLeft,0,0,0);
                    textView.setVisibility(View.VISIBLE);
                    SearchIcon.setImageResource(R.drawable.ic_search);

                    imm.showSoftInput(v, 0);
                }
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.clearFocus();
                imm.hideSoftInputFromWindow( editText.getWindowToken(), 0);
                int d = 328;
                float mScale = getResources().getDisplayMetrics().density;
                int calWidth = (int)(d*mScale);
                int p = 43;
                float mScale2 = getResources().getDisplayMetrics().density;
                int calLeft = (int)(p*mScale2);
                ViewGroup.LayoutParams param = editText.getLayoutParams();
                param.width = calWidth;
                editText.setLayoutParams(param);
                //editText.setMaxWidth(calWidth);
                editText.setHintTextColor(getResources().getColor(R.color.search_hint));
                editText.setBackgroundResource(R.drawable.shape_search);
                editText.setPadding(calLeft,0,0,0);
                editText.setText("");
                textView.setVisibility(View.GONE);
                SearchIcon.setImageResource(R.drawable.ic_unactive_search);
            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable edit) {
                String text = editText.getText().toString()
                        .toLowerCase(Locale.getDefault());
                recyclerViewAdapter.filter(text);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });


        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Enter key Action
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_SEARCH)) {

                    imm.hideSoftInputFromWindow( editText.getWindowToken(), 0);    //hide keyboard
                    return true;
                }
                return false;
            }
        });

        return view;
    }


}
