package com.sauce.inunion;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 123 on 2018-08-08.
 */

public class ContactDetailManager  extends Fragment {
    Retrofit retrofit;
    ContactInterface service;

    String Id;
    TextView editName, editPhoneNumber, editEmail, editPosition, editEtc;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_detail_manager, container, false); // 여기서 UI를 생성해서 View를 return

        final Toolbar toolbarActivity = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbarActivity.setVisibility(View.GONE);

        TextView tv_major = (TextView) view.findViewById(R.id.major_name);
        SharedPreferences pref = getActivity().getSharedPreferences("first", Activity.MODE_PRIVATE);
        String department = pref.getString("App_department",null);
        tv_major.setText(department);


        ImageView imageBack = (ImageView) view.findViewById(R.id.toolbar_back);
        TextView textModify = (TextView) view.findViewById(R.id.toolbar_modify);
        TextView textDelete = (TextView) view.findViewById(R.id.contact_delete);

        editName = (TextView) view.findViewById(R.id.contact_name);
        editPhoneNumber = (TextView) view.findViewById(R.id.contact_phonenumber);
        editEmail = (TextView) view.findViewById(R.id.contact_email);
        editPosition = (TextView) view.findViewById(R.id.contact_position);
        editEtc = (TextView) view.findViewById(R.id.contact_etc);

        Id = getArguments().getString("id");

        retrofit = new Retrofit.Builder()
                .baseUrl("http://117.16.231.66:7001")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ContactInterface.class);

        contentUpdate();

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getActivity().onBackPressed();
                ContactManager fragment= new ContactManager();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .commit();
                toolbarActivity.setVisibility(View.VISIBLE);
            }
        });

        textModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                bundle.putString("name",editName.getText().toString());
//                ContactModify contactModify= new ContactModify();
//                contactModify.setArguments(bundle);
//
//                getActivity().getSupportFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_container, contactModify)
//                        .commit();

                Intent intent = new Intent(getActivity(),ContactModify.class);
                intent.putExtra("id",Id);
                startActivityForResult(intent, 950);
            }
        });

        textDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                service.addressDelete(Id).enqueue(new Callback<RetrofitContact>() {
                    @Override
                    public void onResponse(Call<RetrofitContact> call, Response<RetrofitContact> response) {
                        Log.d("contact", "연결 성공"+response.code());
                        ContactManager contact = new ContactManager();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, contact)
                                .commit();
                        toolbarActivity.setVisibility(View.VISIBLE);

                    }

                    @Override
                    public void onFailure(Call<RetrofitContact> call, Throwable t) {
                        Log.d("contact", ""+t);
                    }
                });


            }
        });
        ImageView imageView = (ImageView) view.findViewById(R.id.contact_write);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), // 현재 화면의 제어권자
                        ContactWrite.class); // 다음 넘어갈 클래스 지정
                startActivity(intent); // 다음 화면으로 넘어간다
            }
        });
       /* textSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                service.addressSave(editName.getText().toString(),editPhoneNumber.getText().toString(),editEmail.getText().toString(),editPosition.getText().toString(),editEtc.getText().toString()).enqueue(new Callback<RetrofitContact>() {
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
                                        .commit();
                            }
                        }else{
                            Toast.makeText(getContext().getApplicationContext(), "저장실패1", Toast.LENGTH_SHORT).show();
                        }
                        // Toast.makeText(getContext().getApplicationContext(),String.valueOf(response.isSuccessful()), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext().getApplicationContext(), "저장완료", Toast.LENGTH_SHORT).show();
                        Contact contact = new Contact();
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container, contact)
                                .commit();
                        toolbarActivity.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<RetrofitContact> call, Throwable t) {
                        Toast.makeText(getContext().getApplicationContext(), "저장실패", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });*/

        return view;
    }

    private void contentUpdate() {
        service.addressSelect(Id).enqueue(new Callback<List<RetrofitContact>>() {
            @Override
            public void onResponse(Call<List<RetrofitContact>> call, Response<List<RetrofitContact>> response) {
                Log.d("contact", "연결 성공"+response.code());

                List<RetrofitContact> res = response.body();
                editName.setText(res.get(0).name);
                editPosition.setText(res.get(0).position);
                editPhoneNumber.setText(res.get(0).phoneNumber);
                editEmail.setText(res.get(0).email);
                editEtc.setText(res.get(0).etc);
            }

            @Override
            public void onFailure(Call<List<RetrofitContact>> call, Throwable t) {
                Log.d("contact", ""+t);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("contact",resultCode+"");
        Log.d("contact",requestCode+"");
        if(requestCode == 950 && resultCode == 900){
            contentUpdate();
        }
    }
}