package com.sauce.inunion;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;


public class MainActivityManager extends FragmentActivity {
    FragmentTransaction transaction;
    private final long FINSH_INTERVAL_TIME = 2000; //2초
    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new NoticeManager());

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        BottomNavigationViewHelper.disableShiftMode(navigation);

        /*BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        for (int i = 0; i < menuView.getChildCount(); i++) {
            final View iconView = menuView.getChildAt(i).findViewById(android.support.design.R.id.icon);
            final ViewGroup.LayoutParams layoutParams = iconView.getLayoutParams();
            final DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            layoutParams.height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 21, displayMetrics);
            layoutParams.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 21, displayMetrics);
            iconView.setLayoutParams(layoutParams);
        }*/

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

                                                           @Override
                                                           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                                                               Fragment fragment;
                                                               switch (item.getItemId()) {
                                                                   case R.id.navigation_notice:
                                                                       fragment = new NoticeManager();
                                                                       loadFragment(fragment);
                                                                       break;
                                                                   case R.id.navigation_calendar:
                                                                       fragment = new TaTCalendarActivity();
                                                                       loadFragment(fragment);
                                                                       break;
                                                                   case R.id.navigation_contact:
                                                                       fragment = new ContactManager();
                                                                       loadFragment(fragment);
                                                                       break;
                                                                   case R.id.navigation_setting:
                                                                       fragment = new SettingManager();
                                                                       loadFragment(fragment);
                                                                       break;
                                                               }
                                                               return true;
                                                           }
                                                       }
        );

    }
    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime) {
            moveTaskToBack(true);
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "\'뒤로\' 버튼을 한 번 더 누르시면 종료됩니다.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /*
        private void replaceFragment(Fragment fragment) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment).commit();

        }
    */
    public void loadFragment(Fragment fragment) {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();


    }
}



