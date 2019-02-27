package com.sauce.inunion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 123 on 2018-08-26.
 */

public class ContactModify extends Activity {

    Retrofit retrofitSelect;
    Retrofit retrofitModify;
    ContactInterface serviceSelect;
    ContactInterface serviceModify;

    EditText editName;
    EditText editPhoneNumber;
    EditText editEmail;
    EditText editPosition;
    EditText editEtc;
    String department;
    String addressId;

//    public static ContactModify newInstance() {
//        return new ContactModify();
//    }

    @Override
    public void onCreate(@Nullable Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.contact_modify);

//        final Toolbar toolbarActivity = (Toolbar) getActivity().findViewById(R.id.toolbar);
//        toolbarActivity.setVisibility(View.GONE);

        ImageView imageBack = (ImageView) findViewById(R.id.toolbar_back);
        TextView textSave = (TextView) findViewById(R.id.toolbar_save);

        editName = (EditText) findViewById(R.id.contact_name);
        editPhoneNumber = (EditText) findViewById(R.id.contact_phonenumber);
        editEmail = (EditText) findViewById(R.id.contact_email);
        editPosition = (EditText) findViewById(R.id.contact_position);
        editEtc = (EditText) findViewById(R.id.contact_etc);

        Intent intent = getIntent();
        final String Id = intent.getStringExtra("id");

        retrofitSelect = new Retrofit.Builder()
                .baseUrl("http://117.16.231.66:7001")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        serviceSelect = retrofitSelect.create(ContactInterface.class);

        serviceSelect.addressSelect(Id).enqueue(new Callback<List<RetrofitContact>>() {
            @Override
            public void onResponse(Call<List<RetrofitContact>> call, Response<List<RetrofitContact>> response) {
                Log.d("contact", "연결 성공"+response.code());

                List<RetrofitContact> res = response.body();
                editName.setText(res.get(0).name);
                editPosition.setText(res.get(0).position);
                editPhoneNumber.setText(res.get(0).phoneNumber);
                editEmail.setText(res.get(0).email);
                editEtc.setText(res.get(0).etc);
                department= res.get(0).department;
//                addressId= res.get(0).addressId;
                Log.d("test", Id);
            }

            @Override
            public void onFailure(Call<List<RetrofitContact>> call, Throwable t) {
                Log.d("contact", ""+t);
            }
        });

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

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Contact contact = new Contact();
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_container, contact)
//                        .addToBackStack(null)
//                        .commit();
//                toolbarActivity.setVisibility(View.VISIBLE);
                onBackPressed();
            }
        });
        retrofitModify = new Retrofit.Builder()
                .baseUrl("http://117.16.231.66:7001")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        serviceModify = retrofitModify.create(ContactInterface.class);
        textSave.setOnClickListener(clickListener);

    }

    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            serviceModify.addressModify(editName.getText().toString(),editPhoneNumber.getText().toString(),editEmail.getText().toString(),editPosition.getText().toString(),editEtc.getText().toString(),department, addressId).enqueue(new Callback<RetrofitContact>() {
                @Override
                public void onResponse(Call<RetrofitContact> call, Response<RetrofitContact> response) {
                    Log.d("test", editName.getText().toString());
                    Toast.makeText(getApplicationContext(), "저장완료", Toast.LENGTH_SHORT).show();
//                        Contact contact = new Contact();
//                        getActivity().getSupportFragmentManager().beginTransaction()
//                                .replace(R.id.fragment_container, contact)
//                                .addToBackStack(null)
//                                .commit();
//                        toolbarActivity.setVisibility(View.VISIBLE);
                    finish();
                }

                @Override
                public void onFailure(Call<RetrofitContact> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "저장실패", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.contact_modify, container, false); // 여기서 UI를 생성해서 View를 return
//
//
//        return view;
//    }
}