package com.sauce.inunion;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by 123 on 2018-08-29.
 */

public class SplashLogoActivity extends AppCompatActivity {
    SharedPreferences pref;
    private final int SPLASH_DISPLAY_LENGTH = 1200;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_logo);

        ImageView imageView = (ImageView) findViewById(R.id.iv_splash);
        Glide.with(getApplicationContext())
                .load(R.drawable.splash_logo)
                .into(imageView);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {

//                Intent mainIntent = new Intent(SplashLogoActivity.this,SplashChoiceActivity.class);
//                SplashLogoActivity.this.startActivity(mainIntent);
//                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
//                SplashLogoActivity.this.finish();
                pref = getSharedPreferences("first", Activity.MODE_PRIVATE);
                if (pref.getString("App_department",null)==null){

                    Intent mainIntent = new Intent(SplashLogoActivity.this,SplashChoiceActivity.class);
                    SplashLogoActivity.this.startActivity(mainIntent);
                    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                    SplashLogoActivity.this.finish();
                }
                else{
                    if (pref.getString("Login","false").equals("true")){
                        Intent mainIntent = new Intent(SplashLogoActivity.this,MainActivityManager.class);
                        SplashLogoActivity.this.startActivity(mainIntent);
                        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                        SplashLogoActivity.this.finish();
                    }
                    else{
                        Intent mainIntent = new Intent(SplashLogoActivity.this,MainActivity.class);
                        SplashLogoActivity.this.startActivity(mainIntent);
                        overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                        SplashLogoActivity.this.finish();
                    }
                }
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}
