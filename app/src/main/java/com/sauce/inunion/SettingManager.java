package com.sauce.inunion;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;



public class SettingManager extends Fragment {
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting_manager, container, false); // 여기서 UI를 생성해서 View를 return

        final Toolbar toolbarActivity = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbarActivity.setVisibility(View.VISIBLE);
        TextView mtitle = (TextView) getActivity().findViewById(R.id.toolbar_title);
        mtitle.setText("설정");

        pref = getActivity().getSharedPreferences("first", Activity.MODE_PRIVATE);
        editor = pref.edit();

        Switch sw = (Switch) view.findViewById(R.id.switch2);
        if (pref.getString("message","on").equals("on")){
            sw.setChecked(true);
            Log.d("message","check");
        }
        else if (pref.getString("message","on").equals("off")){
            sw.setChecked(false);
            Log.d("message","uncheck");
        }
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor.putString("message","on");
                    editor.apply();
                    Log.d("message",pref.getString("message",null));
                } else {
                    editor.putString("message","off");
                    editor.apply();
                    Log.d("message",pref.getString("message", null));
                }
            }
        });

        Button logout_btn = view.findViewById(R.id.manager_setting_logout_btn);
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                editor.putString("App_department", null);
                editor.putString("Login","false");
                editor.apply();
                Intent intent = new Intent(getActivity(),SplashChoiceActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);

            }
        });

        return view;
    }
}