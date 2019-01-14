package com.sauce.inunion;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 123 on 2018-08-03.
 */

public class NoticeListItem {
    private String notice_title;
    private String notice_content;
    private String notice_time;
    private String notice_id;
    private String notice_count;
    private ArrayList<String> notice_image_list;

    public String getTitle() {
        return notice_title;
    }

    public String getContent() {
        return notice_content;
    }


    public String getTime(){
        return notice_time;
    }

    public String getId(){
        return notice_id;
    }

    public String getCount(){
        return notice_count;
    }

    public ArrayList<String> getNoticeImageList(){ return notice_image_list;}

    public NoticeListItem(String notice_title,String notice_content,String notice_time,String notice_id,String notice_count, ArrayList<String> notice_image_list) {

        this.notice_title = notice_title;
        this.notice_content=notice_content;
        this.notice_time=notice_time;
        this.notice_id=notice_id;
        this.notice_count=notice_count;
        this.notice_image_list = notice_image_list;


    }
}
