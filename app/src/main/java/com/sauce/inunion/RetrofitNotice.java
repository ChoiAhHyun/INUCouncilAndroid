package com.sauce.inunion;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2018-08-18.
 */

public class RetrofitNotice {
    @SerializedName("title")
    String title;
    public String getTitle(){return title;}

    @SerializedName("content")
    String content;
    public String getContent(){return content;}

    @SerializedName("department")
    String department;
    public String getDepartment(){return department;}

    @SerializedName("content_serial_id")
    String content_serial_id;
    public String getContent_serial_id(){return content_serial_id;}

    @SerializedName("fileName")
    ArrayList<String> fileName;
    public ArrayList<String> getFileName(){return fileName;}

    @SerializedName("date")
    String time;
    public String getTime(){return time;}
}
