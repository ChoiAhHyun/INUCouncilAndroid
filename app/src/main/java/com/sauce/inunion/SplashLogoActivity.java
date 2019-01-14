package com.sauce.inunion;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;

/**
 * Created by 123 on 2018-08-29.
 */

public class SplashLogoActivity extends Activity {
    SharedPreferences pref;
    private final int SPLASH_DISPLAY_LENGTH = 1400;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_logo);

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
