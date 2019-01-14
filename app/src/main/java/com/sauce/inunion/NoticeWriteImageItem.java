package com.sauce.inunion;

import android.net.Uri;


/**
 * Created by 123 on 2018-08-28.
 */

public class NoticeWriteImageItem {
    private Uri notice_image;

    public Uri getImage() {
        return notice_image;
    }
    public NoticeWriteImageItem(Uri notice_image) {

        this.notice_image=notice_image;

    }

}
