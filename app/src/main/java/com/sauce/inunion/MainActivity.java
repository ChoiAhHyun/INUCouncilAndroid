package com.sauce.inunion;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    FragmentTransaction transaction;
    private final long FINSH_INTERVAL_TIME = 2000; //2초
    private long backPressedTime = 0;

    public static Activity mainActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainActivity = MainActivity.this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(new Notice());

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
                        fragment = new Notice();
                        loadFragment(fragment);
                        break;
                    case R.id.navigation_calendar:
                        fragment = new StudentCalendarActivity();
                        loadFragment(fragment);
                        break;
                    case R.id.navigation_contact:
                        fragment = new Contact();
                        loadFragment(fragment);
                        break;
                    case R.id.navigation_setting:
                        fragment = new Setting();
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

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        Log.v("main", fragment.toString());
//        if (fragment instanceof Notice || fragment instanceof StudentCalendarActivity || fragment instanceof Contact || fragment instanceof Setting){
            if (0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime) {
                moveTaskToBack(true);
                ActivityCompat.finishAffinity(this);
//            android.os.Process.killProcess(android.os.Process.myPid());
            } else {
                backPressedTime = tempTime;
                Toast.makeText(getApplicationContext(), "\'뒤로\' 버튼을 한 번 더 누르시면 종료됩니다.",
                        Toast.LENGTH_SHORT).show();
            }
//        }

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
        transaction.commit();

        }
    }



