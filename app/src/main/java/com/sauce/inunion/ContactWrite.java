package com.sauce.inunion;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 123 on 2018-08-08.
 */

public class ContactWrite extends Activity {

    Retrofit retrofit;
    ContactInterface service;
    SharedPreferences pref;

//    public static ContactWrite newInstance() {
//        return new ContactWrite();
//    }

    @Override
    public void onCreate(@Nullable Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.contact_writing);
//        final Toolbar toolbarActivity = (Toolbar) findViewById(R.id.toolbar);
//        toolbarActivity.setVisibility(View.GONE);
//        final BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
//        navigation.setVisibility(View.GONE);
//        final View shadow = (View) findViewById(R.id.shadow);
//        shadow.setVisibility(View.GONE);

        pref = getSharedPreferences("first", Activity.MODE_PRIVATE);
        final String department = pref.getString("App_department",null);

        ImageView imageBack = (ImageView) findViewById(R.id.toolbar_back);
        TextView textSave = (TextView) findViewById(R.id.toolbar_save);

        final EditText editName = (EditText) findViewById(R.id.contact_name);
        final EditText editPhoneNumber = (EditText) findViewById(R.id.contact_phonenumber);
        final EditText editEmail = (EditText) findViewById(R.id.contact_email);
        final EditText editPosition = (EditText) findViewById(R.id.contact_position);
        final EditText editEtc = (EditText) findViewById(R.id.contact_etc);

        final TextView length = findViewById(R.id.length);
        editEtc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().length() == 0){
                    length.setText("0");
                }
                else{
                    length.setText(editEtc.getText().toString().length()+"");
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl("http://117.16.231.66:7001")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ContactInterface.class);


        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Contact contact = new Contact();
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_container, contact)
//                        .addToBackStack(null)
//                        .commit();
//                toolbarActivity.setVisibility(View.VISIBLE);
//                navigation.setVisibility(View.VISIBLE);
//                shadow.setVisibility(View.VISIBLE);
                onBackPressed();
            }
        });

        textSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editName.getText().toString().length() == 0 ) {
                    Toast.makeText(getApplicationContext(), "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    service.addressSave(editName.getText().toString(), editPhoneNumber.getText().toString(), editEmail.getText().toString(), editPosition.getText().toString(), editEtc.getText().toString(), department).enqueue(new Callback<RetrofitContact>() {
                        @Override
                        public void onResponse(Call<RetrofitContact> call, Response<RetrofitContact> response) {
                       /* if(response.isSuccessful()){
                            RetrofitContact result = response.body();
                            Log.d("test",""+result.name);
                            if(result.name == "true") {
                                Toast.makeText(getContext().getApplicationContext(), "저장완료", Toast.LENGTH_SHORT).show();
                                Contact contact = new Contact();
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.fragment_container, contact)
                                        .addToBackStack(null)
                                        .commit();
                            }
                        }else{
                            Toast.makeText(getContext().getApplicationContext(), "저장실패1", Toast.LENGTH_SHORT).show();
                        }*/
                            // Toast.makeText(getContext().getApplicationContext(),String.valueOf(response.isSuccessful()), Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "저장완료", Toast.LENGTH_SHORT).show();
//                        Contact contact = new Contact();
//                        getActivity().getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.fragment_container, contact)
//                                .addToBackStack(null)
//                                .commit();
//                        toolbarActivity.setVisibility(View.VISIBLE);
//                        navigation.setVisibility(View.VISIBLE);
//                        shadow.setVisibility(View.VISIBLE);
                            Intent saveIntent = new Intent();
                            setResult(500, saveIntent);
                            finish();
                        }

                        @Override
                        public void onFailure(Call<RetrofitContact> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "저장실패 " + t, Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.contact_writing, container, false); // 여기서 UI를 생성해서 View를 return
//
//
//
//
//        return view;
//    }
}