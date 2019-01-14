package com.sauce.inunion;

import com.google.gson.annotations.SerializedName;

public class CalendarSaveResult {
    @SerializedName("ans")
    public String ans;



    public CalendarSaveResult(String ans){
        this.ans = ans;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans= ans;
    }

}
