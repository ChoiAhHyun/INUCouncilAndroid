package com.sauce.inunion;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Contact extends Fragment {

    Retrofit retrofit;
    ContactInterface service;
    InputMethodManager imm;
    ContactListAdapter mAdapter;
    String[] mNames;
    List<ContactListItem> mSectionList;
    SharedPreferences pref;

    private static final char HANGUL_BEGIN_UNICODE = 44032; // 가
    private static final char HANGUL_LAST_UNICODE = 55203; // 힣
    private static final char HANGUL_BASE_UNIT = 588;//각자음 마다 가지는 글자수

    private static final char[] INITIAL_SOUND = { 'ㄱ', 'ㄲ', 'ㄴ','ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ','ㅊ',
            'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ' };

    private String getInitialSound(char c) {
        int hanBegin = (c - HANGUL_BEGIN_UNICODE);
        int index = hanBegin / HANGUL_BASE_UNIT;
        return String.valueOf(INITIAL_SOUND[index]);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_contact, container, false); // 여기서 UI를 생성해서 View를 return

        final Toolbar toolbarActivity = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbarActivity.setVisibility(View.VISIBLE);

        TextView tv_major = (TextView) view.findViewById(R.id.major_name);
        pref = getActivity().getSharedPreferences("first", Activity.MODE_PRIVATE);
        String department = pref.getString("App_department",null);
        tv_major.setText(department);

        imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        final FrameLayout mainLayout = (FrameLayout) view.findViewById(R.id.fragment_contact);
        mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                imm.hideSoftInputFromWindow(mainLayout.getWindowToken(), 0);
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl("http://117.16.231.66:7001")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ContactInterface.class);
        //Call<RetrofitContact> call = service.addressSelect("");

        TextView mtitle = (TextView) getActivity().findViewById(R.id.toolbar_title);
        mtitle.setText("연락처");


//        mNames = new String[7];
//        mNames[0]="안";
//        mNames[1]="류준열";
//        mNames[2]="박보검";
//        mNames[3]="이동욱";
//        mNames[4]="조승우";
//        mNames[5]="하정우";
//        mNames[6]="김수현";
//        mNames[j].toString()



        final List<ContactListItem> items= new ArrayList<>();
        mSectionList = new ArrayList<>();

        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.contact_list);
//        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ContactListAdapter(getActivity(),mSectionList,getFragmentManager());
        recyclerView.setAdapter(mAdapter);

        Call<List<RetrofitContact>> call = service.addressSort(department);
        call.enqueue(new Callback<List<RetrofitContact>>() {
            @Override
            public void onResponse(Call<List<RetrofitContact>> call,
                                   Response<List<RetrofitContact>> response) {
                Log.d("contact", "연결 성공"+response.code());
//                items.add(new ContactListItem(select.toString()));
                List<RetrofitContact> res = response.body();

                for (int j=0; j<res.size();j++){
                    items.add(new ContactListItem(res.get(j).name,false,res.get(j).addressId));
                }


                mAdapter.setList(items);
                getHeaderListLatter(items);
                mAdapter = new ContactListAdapter(getActivity(),mSectionList,getFragmentManager());
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
//                for(int i=0;i<res.size();i++){
//                    mNames[i] = res[i].toString();
//                }
//                List<> strList = new ArrayList<String>();
//                strList.add("Hello");
//                strList.add("Wold!");
//                mNames = res.toArray(new String[res.size()]);
//                Collections.copy(res.toString(),items);
            }

            @Override
            public void onFailure(Call<List<RetrofitContact>> call, Throwable t) {
                Log.d("contact", ""+t);
            }
        });




        /*call.enqueue(new Callback<RetrofitContact>() {
            @Override
            public void onResponse(Call<RetrofitContact> call, Response<RetrofitContact> response) {
                Log.e("test",response.body().toString());
                items.add(new ContactListItem(response.body().toString()));
                Toast.makeText(getActivity(),"연결 성공",Toast.LENGTH_SHORT).show();
               // for(int i=0;i<sizeof.response.body();i++){
               // items.add(new ContactListItem());
               // }
            }
            @Override
            public void onFailure(Call<RetrofitContact> call, Throwable t) {
                Toast.makeText(getActivity(),"연결 실패",Toast.LENGTH_SHORT).show();
            }
        });*/



//        service.addressSelect(editName.getText().toString()).enqueue(new Callback<RetrofitContactName>() {
//            @Override
//            public void onResponse(Call<RetrofitContactName> call, Response<RetrofitContactName> response) {
//
//
//                Contact contact = new Contact();
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_container, contact)
//                        .commit();
//                toolbarActivity.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onFailure(Call<RetrofitContactName> call, Throwable t) {
//                Toast.makeText(getContext().getApplicationContext(), "연결실패", Toast.LENGTH_SHORT).show();
//            }
//        });

//        items.add(new ContactListItem("강동원"));
//        items.add(new ContactListItem("류준열"));
//        items.add(new ContactListItem("박보검"));
//        items.add(new ContactListItem("이동욱"));
//        items.add(new ContactListItem("조승우"));
//        items.add(new ContactListItem("하정우"));


//        final ContactListAdapter recyclerViewAdapter = new ContactListAdapter(getActivity(),items,getFragmentManager());
//        //recyclerView.setAdapter(recyclerViewAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//
//        //This is the code to provide a sectioned list
//        List<SimpleSectionedRecyclerViewAdapter.Section> sections =
//                new ArrayList<SimpleSectionedRecyclerViewAdapter.Section>();
//
//        int position;
//        String initial;
//
//        //Sections
//        sections.add(new SimpleSectionedRecyclerViewAdapter.Section(0,"ㄱ"));
//        //sections.add(new SimpleSectionedRecyclerViewAdapter.Section(5,"ㄴ"));
//        //sections.add(new SimpleSectionedRecyclerViewAdapter.Section(12,"ㄷ"));
//        //sections.add(new SimpleSectionedRecyclerViewAdapter.Section(14,"Section 4"));
//        //sections.add(new SimpleSectionedRecyclerViewAdapter.Section(20,"Section 5"));
//
//
//
//
//        //Add your adapter to the sectionAdapter
//        SimpleSectionedRecyclerViewAdapter.Section[] dummy = new SimpleSectionedRecyclerViewAdapter.Section[sections.size()];
//        SimpleSectionedRecyclerViewAdapter mSectionedAdapter = new
//                SimpleSectionedRecyclerViewAdapter(getActivity(),R.layout.contact_list_section,R.id.contact_section,recyclerViewAdapter);
//        mSectionedAdapter.setSections(sections.toArray(dummy));
//
//        //Apply this adapter to the RecyclerView
//        recyclerView.setAdapter(mSectionedAdapter);

//        recyclerView.addOnItemTouchListener(
//                new RecyclerItemClickListener(getActivity(), recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override public void onItemClick(View view, int position) {
//                        /*Intent intent = new Intent(getContext().getApplicationContext(), // 현재 화면의 제어권자
//                                ContactDetail.class); // 다음 넘어갈 클래스 지정
//                        startActivity(intent); // 다음 화면으로 넘어간다*/
//                        position = recyclerView.getChildLayoutPosition(view);
//
//                        Bundle bundle = new Bundle();
//                        bundle.putString("name",items.get(position).getName());
//                        ContactDetail contactDetail= new ContactDetail();
//                        contactDetail.setArguments(bundle);
//
//                        getActivity().getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.fragment_container, contactDetail)
//                                .commit();
//                    }
//
//                    @Override public void onLongItemClick(View view, int position) {
//                        // do whatever
//                    }
//                })
//        );

        final EditText editText = (EditText) view.findViewById(R.id.search_contact);

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
                mAdapter.filter(text);

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
        });

        return view;
    }
    private void getHeaderListLatter(List<ContactListItem> usersList) {

        Collections.sort(usersList, new Comparator<ContactListItem>() {

            @Override

            public int compare(ContactListItem user1, ContactListItem user2) {

                return getInitialSound(user1.name_professor.charAt(0)).compareTo(getInitialSound(user2.name_professor.charAt(0)));

            }

        });



        String lastHeader = "";



        int size = usersList.size();



        for (int i = 0; i < size; i++) {



            ContactListItem user = usersList.get(i);

            String header = getInitialSound(user.name_professor.charAt(0));



            if (!TextUtils.equals(lastHeader, header)) {

                lastHeader = header;

                mSectionList.add(new ContactListItem(header,true,null));

            }



            mSectionList.add(user);

        }

    }

}
