package com.sauce.inunion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 123 on 2018-08-29.
 */

public class SplashChoiceActivity extends Activity{
    public static Activity choiceActivity;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        choiceActivity = SplashChoiceActivity.this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_choice);

        TextView admin = (TextView) findViewById(R.id.button_admin);
        TextView student = (TextView) findViewById(R.id.button_student);

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), // 현재 화면의 제어권자
                        LoginActivity.class); // 다음 넘어갈 클래스 지정
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });

        student.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), // 현재 화면의 제어권자
                        ChooseMajorActivity.class); // 다음 넘어갈 클래스 지정
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });
    }
}
