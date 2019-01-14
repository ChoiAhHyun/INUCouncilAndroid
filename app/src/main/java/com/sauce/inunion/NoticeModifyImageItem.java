package com.sauce.inunion;

import android.net.Uri;

/**
 * Created by 123 on 2018-09-22.
 */

public class NoticeModifyImageItem {
    private String notice_image;
    private Uri notice_image_uri;

    public Uri getUri(){
        return notice_image_uri;
    }

    public String getImage() {
        return "http://117.16.231.66:7001/imgload/"+notice_image;
    }
    public void setImage(String notice_image) {

        this.notice_image=notice_image;

    }

    public NoticeModifyImageItem(String notice_image,Uri notice_image_uri) {

        this.notice_image=notice_image;
        this.notice_image_uri=notice_image_uri;
    }

}
