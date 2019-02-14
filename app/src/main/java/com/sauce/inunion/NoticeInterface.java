package com.sauce.inunion;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * Created by 123 on 2018-08-18.
 */

public interface NoticeInterface {

//    @Multipart
//    @FormUrlEncoded
//    @POST("/boardSave")
//    public Call<RetrofitNotice> boardSave(@Field("title") String title, @Field("content") String content, @Field("department") String department, @Part("fileName") MultipartBody.Part file);

    @Multipart
    @POST("/boardSave/")
    public Call<RetrofitNotice> boardSave(@Part("title") RequestBody title, @Part("content") RequestBody content, @Part("department") RequestBody department,
                                          @Part List<MultipartBody.Part> file);

    @Multipart
    @POST("/boardModify/")
    public Call<RetrofitNotice> boardModify(@Part("title") RequestBody title, @Part("content") RequestBody content, @Part("content_serial_id") RequestBody content_serial_id, @Part("department") RequestBody department,
                                            @Part List<MultipartBody.Part> file);

    @FormUrlEncoded
    @POST("/boardDelete/")
    public Call<RetrofitNotice> boardDelete(@Field("content_serial_id") String content_serial_id);

    @FormUrlEncoded
    @POST("/boardSelectOne/")
    public Call<RetrofitNotice> boardSelect(@Field("content_serial_id") String content_serial_id, @Field("department") String department);

    @FormUrlEncoded
    @POST("/boardSelect/")
    public Call<List<RetrofitNotice>> boardSort(@Field("department") String department);

    @GET
    public Call<ResponseBody> imgLoad(@Url String url);

//    @Multipart
//    @POST("/imgload")
//    Call<ResponseBody> upload(
//
////            @Part("fileName") MultipartBody.Part file2
//    );
}
