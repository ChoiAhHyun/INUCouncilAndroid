package com.sauce.inunion;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ChooseMajorActivity extends Activity {
    RecyclerView recyclerView = null;
    MajorRecyclerAdapter adapter = null;
    boolean visible = false;
    String firstmajor;
    EditText editText;
    Button choose_btn;
    static boolean isStudentMode = false;
    ImageView arrow;
    String[] major = {
            "건설환경공학부","경영학부","경제학과","공연예술학과","국어교육과","국어국문학과","기계공학과",
            "도시건축학부","도시공학과","도시행정학과","독어독문학과","동북아국제통상학부","디자인학부",
            "메카트로닉스공학","무역학부", "문헌정보학과","물리학과",
            "바이오경영학과","법학과","불어불문학과",
            "사회복지학과","산업경영공학과","생명공학부", "생명과학부","세무회계학과","소비자ㆍ아동학과", "수학과","수학교육과","신문방송학과","신소재공학과",
            "안전공학과", "에너지화학공학과", "역사교육과", "영어교육과", "영어영문학과", "운동건강학부", "유아교육과", "윤리교육과", "일어교육과", "일어일문학과", "임베디드시스템공학과",
            "전기공학과", "전자공학과", "정보통신공학과", "정치외교학과", "조형예술학부", "중어중국학과",
            "창의인재개발학과", "체육교육과", "체육학부",
            "컴퓨터공학부",
            "테크노경영학과",
            "패션산업학과",
            "한국통상전공", "해양학과", "행정학과", "화학과"
    };
    FireBaseMessagingService fireBaseMessagingService = new FireBaseMessagingService();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_major);

        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        final EditText editText = findViewById(R.id.search_major);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        final ImageView SearchIcon = (ImageView ) findViewById(R.id.search_notice_icon);
        final TextView textView = (TextView) findViewById(R.id.search_tv);

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

        recyclerView = findViewById(R.id.recycler);
        choose_btn = findViewById(R.id.choose_btn);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        final ArrayList<Myitem> Myitems = new ArrayList<>();
        final ArrayList<Myitem> temp = new ArrayList<>();
        for(int i=0; i<major.length; i++){
            Myitems.add(new Myitem(major[i]));
            temp.add(new Myitem(major[i]));
        }
        adapter = new MajorRecyclerAdapter(Myitems);
        List<SimpleSectionedRecyclerViewAdapter.Section> sections =
                new ArrayList<SimpleSectionedRecyclerViewAdapter.Section>();
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0,"ㄱ"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(7,"ㄷ"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(13,"ㅁ"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(17,"ㅂ"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(20,"ㅅ"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(30,"ㅇ"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(41,"ㅈ"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(47,"ㅊ"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(50,"ㅋ"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(51,"ㅌ"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(52,"ㅍ"));
        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(53,"ㅎ"));
        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
        final SimpleSectionedRecyclerViewAdapter mSectionedAdapter = new
                SimpleSectionedRecyclerViewAdapter(this,R.layout.section,R.id.section_text,adapter);
        mSectionedAdapter.setSections(sections.toArray(dummy));

        //Sections

        recyclerView.setAdapter(mSectionedAdapter);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editText.getText().toString()
                        .toLowerCase(Locale.getDefault());
                Myitems.clear();
                if (text.length() == 0) {
                    Myitems.addAll(temp);
                    adapter = new MajorRecyclerAdapter(Myitems);
                    List<SimpleSectionedRecyclerViewAdapter.Section> sections =
                            new ArrayList<SimpleSectionedRecyclerViewAdapter.Section>();
                    sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0,"ㄱ"));
                    sections.add(new SimpleSectionedRecyclerViewAdapter.Section(7,"ㄷ"));
                    sections.add(new SimpleSectionedRecyclerViewAdapter.Section(13,"ㅁ"));
                    sections.add(new SimpleSectionedRecyclerViewAdapter.Section(17,"ㅂ"));
                    sections.add(new SimpleSectionedRecyclerViewAdapter.Section(20,"ㅅ"));
                    sections.add(new SimpleSectionedRecyclerViewAdapter.Section(30,"ㅇ"));
                    sections.add(new SimpleSectionedRecyclerViewAdapter.Section(41,"ㅈ"));
                    sections.add(new SimpleSectionedRecyclerViewAdapter.Section(47,"ㅊ"));
                    sections.add(new SimpleSectionedRecyclerViewAdapter.Section(50,"ㅋ"));
                    sections.add(new SimpleSectionedRecyclerViewAdapter.Section(51,"ㅌ"));
                    sections.add(new SimpleSectionedRecyclerViewAdapter.Section(52,"ㅍ"));
                    sections.add(new SimpleSectionedRecyclerViewAdapter.Section(53,"ㅎ"));
                    SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
                    final SimpleSectionedRecyclerViewAdapter mSectionedAdapter = new
                            SimpleSectionedRecyclerViewAdapter(getApplicationContext(),R.layout.section,R.id.section_text,adapter);
                    mSectionedAdapter.setSections(sections.toArray(dummy));

                    //Sections

                    recyclerView.setAdapter(mSectionedAdapter);
                } else {
                    Log.d("test",text);
                    Log.d("test",temp.get(1).getName());
                    for (int i=0; i<57; i++) {
                        String name = temp.get(i).getName();
                        Log.d("test",name);
                        if (name.contains(text)) {
                            Log.d("test",name);
                            Myitems.add(temp.get(i));
                        }
                    }
                    adapter = new MajorRecyclerAdapter(Myitems);
                    recyclerView.setAdapter(adapter);
                }
            }
        });

        choose_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (choose_btn.getText()=="다음으로"){
//                    SharedPreferences pref = getSharedPreferences("first", Activity.MODE_PRIVATE);
//                    SharedPreferences.Editor editor = pref.edit();
//                    editor.putString("firstMajor",firstmajor);
//                    editor.putString("App_department",firstmajor);
//                    editor.apply();
//                    TaTCalendarActivity.department = firstmajor;
//                    isStudentMode = true;
//                    Intent intent = new Intent(ChooseMajorActivity.this,ChooseSecondMajorActivity.class);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
//                    finish();
//                }
//                else{
//
//                }
                SharedPreferences pref = getSharedPreferences("first", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("firstMajor",firstmajor);
                editor.putString("App_department",firstmajor);
                editor.apply();
                fireBaseMessagingService.sendRegistrationToServer(ChooseMajorActivity.this,pref.getString("App_department",null));
                TaTCalendarActivity.department = firstmajor;
                isStudentMode = true;
                Intent intent = new Intent(ChooseMajorActivity.this,ChooseSecondMajorActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_left);
                SplashChoiceActivity activity = (SplashChoiceActivity) SplashChoiceActivity.choiceActivity;
                activity.finish();
                finish();
            }
        });
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver,
                new IntentFilter("choosemajor"));


        ImageView backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        arrow = findViewById(R.id.iv_arrow);
    }
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String data = intent.getStringExtra("choose");
            String data2 = intent.getStringExtra("majorname");
            if(data != null){
                if(data.equals("true")){
                    choose_btn.setTextColor(Color.WHITE);
                    choose_btn.setBackgroundColor(Color.rgb(76,110,245));
                    choose_btn.setText("다음으로");
                    choose_btn.setEnabled(true);
                    arrow.setVisibility(View.VISIBLE);
                }
                else{
                    choose_btn.setTextColor(Color.rgb(76,110,245));
                    choose_btn.setBackground(getDrawable(R.drawable.border));
                    choose_btn.setText("선택해주세요");
                    arrow.setVisibility(View.GONE);
                }
            }
            if(data2 != null){
                firstmajor = data2;
            }

        }
    };

}