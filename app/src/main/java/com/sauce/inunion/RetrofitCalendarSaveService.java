package com.sauce.inunion;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RetrofitCalendarSaveService {
    @FormUrlEncoded
    @POST("/calendarSave/")
    public Call<CalendarSaveResult> calendarsave(@Field("scheduleTitle") String scheduleTitle,
                                          @Field("time") String time,
                                          @Field("position") String position,
                                          @Field("memo") String memo,
                                          @Field("department") String department);
}
