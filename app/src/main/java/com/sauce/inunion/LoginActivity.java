package com.sauce.inunion;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends Activity {
    Retrofit retrofit;
    RetrofitService retrofitLoginService;
    FireBaseMessagingService fireBaseMessagingService = new FireBaseMessagingService();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        retrofit = new Retrofit.Builder().baseUrl("http://117.16.231.66:7001").addConverterFactory(GsonConverterFactory.create()).build();
        retrofitLoginService = retrofit.create(RetrofitService.class);
        final TextView warning_text = findViewById(R.id.warning_text);
        final EditText id_edit = findViewById(R.id.id_edit);
        final EditText pw_edit = findViewById(R.id.pw_edit);
        final Button buttonLogin = findViewById(R.id.login_btn);
        final Button buttonBack = findViewById(R.id.login_back_btn);
        id_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length()>=6){

                    if(pw_edit.getText().toString().length()>=6){
                        buttonLogin.setTextColor(Color.WHITE);
                        buttonLogin.setBackgroundColor(Color.rgb(76,110,245));
                    }
                }
                else{
                    buttonLogin.setTextColor(Color.rgb(108,117,153));
                    buttonLogin.setBackgroundColor(Color.rgb(45,66,147));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        pw_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() >= 6) {

                    if(id_edit.getText().toString().length()>=6){
                        buttonLogin.setTextColor(Color.WHITE);
                        buttonLogin.setBackgroundColor(Color.rgb(76,110,245));
                        buttonLogin.setEnabled(true);
                    }
                }
                else{

                    buttonLogin.setTextColor(Color.rgb(108,117,153));
                    buttonLogin.setBackgroundColor(Color.rgb(45,66,147));
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                retrofitLoginService.login(id_edit.getText().toString(),pw_edit.getText().toString()).enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                        if (response.isSuccessful()) {
                            LoginResult result = response.body();
                            if (result.ans.equals("true")) {
                                Log.d("test", result.department);
                                SharedPreferences pref = getSharedPreferences("first", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.putString("App_department", result.department);
                                editor.putString("Login",result.ans);
                                editor.apply();
                                fireBaseMessagingService.sendRegistrationToServer(LoginActivity.this,pref.getString("App_department",null));
                                ChooseMajorActivity.isStudentMode = false;
                                Intent intent = new Intent(LoginActivity.this, MainActivityManager.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                                SplashChoiceActivity activity = (SplashChoiceActivity) SplashChoiceActivity.choiceActivity;
                                if(activity!=null){
                                    activity.finish();
                                }
                                MainActivity activity2 = (MainActivity) MainActivity.mainActivity;
                                if(activity2!=null){
                                    activity2.finish();
                                }
                                finish();
                            } else {
                                warning_text.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<LoginResult> call, Throwable t) {
                        Log.d("테스트", ""+t);
                    }

                });
            }
        });
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
