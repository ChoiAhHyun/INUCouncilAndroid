package com.sauce.inunion;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class Setting extends Fragment {
    TextView major_first;
    TextView major_second;
    TextView major_third;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    FireBaseMessagingService fireBaseMessagingService = new FireBaseMessagingService();
    public static Setting newInstance() {
        return new Setting();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false); // 여기서 UI를 생성해서 View를 return

        final Toolbar toolbarActivity = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbarActivity.setVisibility(View.VISIBLE);
        TextView mtitle = (TextView) getActivity().findViewById(R.id.toolbar_title);
        mtitle.setText("설정");

        major_first = view.findViewById(R.id.major_first);
        major_second = view.findViewById(R.id.major_second);
        major_third = view.findViewById(R.id.major_third);
        pref = getActivity().getSharedPreferences("first", Activity.MODE_PRIVATE);
        editor = pref.edit();
        Button delete1 = view.findViewById(R.id.delete1);
        Button delete2 = view.findViewById(R.id.delete2);
        Button delete3 = view.findViewById(R.id.delete3);
        LinearLayout add_major_btn = view.findViewById(R.id.add_major_btn);
        Button login_btn = view.findViewById(R.id.setting_login_btn);
        major_first.setText(pref.getString("firstMajor","주 전공"));
        major_second.setText(pref.getString("secondMajor","복수 전공"));
        major_third.setText(pref.getString("thirdMajor","부 전공"));
        if(pref.getString("App_department",null).equals(major_first.getText().toString())){
            major_first.setTextColor(Color.rgb(76,110,245));
            fireBaseMessagingService.sendRegistrationToServer(getActivity(),pref.getString("App_department",null));
        }
        else if(pref.getString("App_department",null).equals(major_second.getText().toString())){
            major_second.setTextColor(Color.rgb(76,110,245));
            fireBaseMessagingService.sendRegistrationToServer(getActivity(),pref.getString("App_department",null));
        }
        else if(pref.getString("App_department",null).equals(major_third.getText().toString())){
            major_third.setTextColor(Color.rgb(76,110,245));
            fireBaseMessagingService.sendRegistrationToServer(getActivity(),pref.getString("App_department",null));
        }
        else{}
        major_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!major_first.getText().toString().equals("주 전공")){
                    major_first.setTextColor(Color.rgb(76,110,245));
                    major_second.setTextColor(Color.rgb(173,181,189));
                    major_third.setTextColor(Color.rgb(173,181,189));
                    editor.putString("App_department",major_first.getText().toString());
                    editor.apply();
                    fireBaseMessagingService.sendRegistrationToServer(getActivity(),pref.getString("App_department",null));
                    Log.d("App_department",pref.getString("App_department",""));
                    Intent intent = new Intent("department_change");
                    intent.putExtra("changed","true");
                    LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).sendBroadcast(intent);
                }
            }
        });
        major_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!major_second.getText().toString().equals("복수 전공")){
                    major_second.setTextColor(Color.rgb(76,110,245));
                    major_first.setTextColor(Color.rgb(173,181,189));
                    major_third.setTextColor(Color.rgb(173,181,189));
                    editor.putString("App_department",major_second.getText().toString());
                    editor.apply();
                    fireBaseMessagingService.sendRegistrationToServer(getActivity(),pref.getString("App_department",null));
                    Log.d("App_department",pref.getString("App_department",null));
                    Intent intent = new Intent("department_change");
                    intent.putExtra("changed","true");
                    LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).sendBroadcast(intent);
                }
            }
        });
        major_third.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!major_third.getText().toString().equals("부 전공")){
                    major_third.setTextColor(Color.rgb(76,110,245));
                    major_second.setTextColor(Color.rgb(173,181,189));
                    major_first.setTextColor(Color.rgb(173,181,189));
                    editor.putString("App_department",major_third.getText().toString());
                    editor.apply();
                    fireBaseMessagingService.sendRegistrationToServer(getActivity(),pref.getString("App_department",null));
                    Intent intent = new Intent("department_change");
                    intent.putExtra("changed","true");
                    LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).sendBroadcast(intent);
                }
            }
        });
        delete1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pref.getString("App_department",null).equals(major_first.getText().toString())){
                    if(!major_second.getText().toString().equals("복수 전공")){
                        major_second.setTextColor(Color.rgb(76,110,245));
                        major_first.setTextColor(Color.rgb(173,181,189));
                        major_third.setTextColor(Color.rgb(173,181,189));
                        editor.putString("App_department",major_second.getText().toString());
                        editor.apply();
                        fireBaseMessagingService.sendRegistrationToServer(getActivity(),pref.getString("App_department",null));
                        Log.d("App_department",pref.getString("App_department",null));
                        Intent intent = new Intent("department_change");
                        intent.putExtra("changed","true");
                        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).sendBroadcast(intent);
                    }
                    else if(!major_third.getText().toString().equals("부 전공")){
                        major_third.setTextColor(Color.rgb(76,110,245));
                        major_second.setTextColor(Color.rgb(173,181,189));
                        major_first.setTextColor(Color.rgb(173,181,189));
                        editor.putString("App_department",major_third.getText().toString());
                        editor.apply();
                        fireBaseMessagingService.sendRegistrationToServer(getActivity(),pref.getString("App_department",null));
                        Intent intent = new Intent("department_change");
                        intent.putExtra("changed","true");
                        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).sendBroadcast(intent);
                    }
                    else{
                        Intent intent = new Intent(getContext().getApplicationContext(), // 현재 화면의 제어권자
                                ChooseMajorActivity.class); // 다음 넘어갈 클래스 지정
                        startActivity(intent); // 다음 화면으로 넘어간다
                    }
                }
                major_first.setText("주 전공");
                major_first.setTextColor(Color.rgb(173,181,189));
                editor.putString("firstMajor",null);
                editor.apply();
            }
        });
        delete2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pref.getString("App_department",null).equals(major_second.getText().toString())){
                    if(!major_first.getText().toString().equals("주 전공")){
                        major_first.setTextColor(Color.rgb(76,110,245));
                        major_second.setTextColor(Color.rgb(173,181,189));
                        major_third.setTextColor(Color.rgb(173,181,189));
                        editor.putString("App_department",major_first.getText().toString());
                        editor.apply();
                        fireBaseMessagingService.sendRegistrationToServer(getActivity(),pref.getString("App_department",null));
                        Log.d("App_department",pref.getString("App_department",""));
                        Intent intent = new Intent("department_change");
                        intent.putExtra("changed","true");
                        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).sendBroadcast(intent);
                    }
                    else if(!major_third.getText().toString().equals("부 전공")){
                        major_third.setTextColor(Color.rgb(76,110,245));
                        major_second.setTextColor(Color.rgb(173,181,189));
                        major_first.setTextColor(Color.rgb(173,181,189));
                        editor.putString("App_department",major_third.getText().toString());
                        editor.apply();
                        fireBaseMessagingService.sendRegistrationToServer(getActivity(),pref.getString("App_department",null));
                        Intent intent = new Intent("department_change");
                        intent.putExtra("changed","true");
                        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).sendBroadcast(intent);
                    }
                }
                major_second.setText("복수 전공");
                major_second.setTextColor(Color.rgb(173,181,189));
                editor.putString("secondMajor",null);
                editor.apply();
            }
        });
        delete3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pref.getString("App_department",null).equals(major_third.getText().toString())){
                    if(!major_first.getText().toString().equals("주 전공")){
                        major_first.setTextColor(Color.rgb(76,110,245));
                        major_second.setTextColor(Color.rgb(173,181,189));
                        major_third.setTextColor(Color.rgb(173,181,189));
                        editor.putString("App_department",major_first.getText().toString());
                        editor.apply();
                        fireBaseMessagingService.sendRegistrationToServer(getActivity(),pref.getString("App_department",null));
                        Log.d("App_department",pref.getString("App_department",""));
                        Intent intent = new Intent("department_change");
                        intent.putExtra("changed","true");
                        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).sendBroadcast(intent);
                    }
                    else if(!major_second.getText().toString().equals("복수 전공")){
                        major_second.setTextColor(Color.rgb(76,110,245));
                        major_first.setTextColor(Color.rgb(173,181,189));
                        major_third.setTextColor(Color.rgb(173,181,189));
                        editor.putString("App_department",major_second.getText().toString());
                        editor.apply();
                        fireBaseMessagingService.sendRegistrationToServer(getActivity(),pref.getString("App_department",null));
                        Log.d("App_department",pref.getString("App_department",null));
                        Intent intent = new Intent("department_change");
                        intent.putExtra("changed","true");
                        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).sendBroadcast(intent);
                    }
                }
                major_third.setText("부 전공");
                major_third.setTextColor(Color.rgb(173,181,189));
                editor.putString("thirdMajor",null);
                editor.apply();
            }
        });
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),LoginActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);

            }
        });
        add_major_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),AddMajorActivity.class);
                startActivity(intent);
            }
        });

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver,
                new IntentFilter("add_major"));

        return view;
    }
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("isAdded");
            if(data != null){
                if(data.equals("true")){
//                    Log.d("test","gogogogogo");
                    if(major_first.getText().toString().equals("주 전공")){
                        major_first.setText(pref.getString("temp_Major","주 전공"));
                        editor.putString("firstMajor",pref.getString("temp_Major","주 전공"));
                        editor.apply();
                    }
                    else{
                        if(major_second.getText().toString().equals("복수 전공")){
                            major_second.setText(pref.getString("temp_Major","부 전공"));
                            editor.putString("secondMajor",pref.getString("temp_Major","복수 전공"));
                            editor.apply();
                        }
                        else{
                            major_third.setText(pref.getString("temp_Major","부 전공"));
                            editor.putString("thirdMajor",pref.getString("temp_Major","부 전공"));
                            editor.apply();
                        }
                    }
                }
            }
        }
    };
}