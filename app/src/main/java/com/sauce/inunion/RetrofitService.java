package com.sauce.inunion;
import com.google.gson.JsonArray;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitService {
    @FormUrlEncoded
    @POST("/login/")
    public Call<LoginResult> login(@Field("username") String username, @Field("password") String password);

    @FormUrlEncoded
    @POST("/calendarSave/")
    public Call<RetrofitResult> calendarsave(@Field("scheduleTitle") String scheduleTitle,
                                             @Field("startDate") String startDate,
                                             @Field("startTime") String startTime,
                                             @Field("endDate") String endDate,
                                             @Field("endTime") String endTime,
                                             @Field("position") String position,
                                             @Field("memo") String memo,
                                             @Field("department") String department);

    @FormUrlEncoded
    @POST("/calendarSelect/")
    public Call<JsonArray> calendarselect(@Field("department") String department);

    @FormUrlEncoded
    @POST("/calendarDelete/")
    public Call<RetrofitResult> calendardelete(@Field("scheduleId") String scheduleId);

    @FormUrlEncoded
    @POST("/calendarModify/")
    public Call<RetrofitResult> calendarmodify(@Field("scheduleTitle") String scheduleTitle,
                                               @Field("startDate") String startDate,
                                               @Field("startTime") String startTime,
                                               @Field("endDate") String endDate,
                                               @Field("endTime") String endTime,
                                               @Field("position") String position,
                                               @Field("memo") String memo,
                                               @Field("department") String department,
                                               @Field("scheduleId") String scheduleId);
}
