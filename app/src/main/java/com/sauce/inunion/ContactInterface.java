package com.sauce.inunion;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 123 on 2018-08-26.
 */

public interface ContactInterface {
    @FormUrlEncoded
    @POST("/addressSort")
    public Call<List<RetrofitContact>> addressSort(@Field("department") String department);

    @FormUrlEncoded
    @POST("/addressModify")
    public Call<RetrofitContact> addressModify(@Field("name") String name, @Field("phoneNumber") String phoneNumber, @Field("email") String email, @Field("position") String position, @Field("etc") String etc, @Field("department") String department, @Field("addressId") String adressId);

    @FormUrlEncoded
    @POST("/addressSelect")
    public Call<List<RetrofitContact>> addressSelect(@Field("name") String name);

    @FormUrlEncoded
    @POST("/addressSave")
    public Call<RetrofitContact> addressSave(@Field("name") String name, @Field("phoneNumber") String phoneNumber, @Field("email") String email, @Field("position") String position, @Field("etc") String etc, @Field("department") String department);

    @FormUrlEncoded
    @POST("/addressDelete")
    public Call<RetrofitContact> addressDelete(@Field("addressId") String addressId);
}
