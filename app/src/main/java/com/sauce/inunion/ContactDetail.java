package com.sauce.inunion;

import android.content.Intent;
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
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 123 on 2018-08-08.
 */

public class ContactDetail  extends Fragment {
    Retrofit retrofit;
    ContactInterface service;
    ContactInterface serviceD;

    public static ContactDetail newInstance() {
        return new ContactDetail();
    }

    @Override
    public void onCreate(@Nullable Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_detail, container, false); // 여기서 UI를 생성해서 View를 return

        final Toolbar toolbarActivity = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbarActivity.setVisibility(View.GONE);

        ImageView imageBack = (ImageView) view.findViewById(R.id.toolbar_back);

        final TextView editName = (TextView) view.findViewById(R.id.contact_name);
        final TextView editPhoneNumber = (TextView) view.findViewById(R.id.contact_phonenumber);
        final TextView editEmail = (TextView) view.findViewById(R.id.contact_email);
        final TextView editPosition = (TextView) view.findViewById(R.id.contact_position);
        final TextView editEtc = (TextView) view.findViewById(R.id.contact_etc);

        final String Id = getArguments().getString("id");


        retrofit = new Retrofit.Builder()
                .baseUrl("http://117.16.231.66:7001")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(ContactInterface.class);
        serviceD = retrofit.create(ContactInterface.class);

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

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                getActivity().onBackPressed();
                Contact fragment= new Contact();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit();
                toolbarActivity.setVisibility(View.VISIBLE);
            }
        });


        return view;
    }
}