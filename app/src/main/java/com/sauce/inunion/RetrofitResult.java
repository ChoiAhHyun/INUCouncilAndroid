package com.sauce.inunion;


import com.google.gson.annotations.SerializedName;

public class RetrofitResult {
    @SerializedName("ans")
    public String ans;



    public RetrofitResult(String ans){
        this.ans = ans;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans= ans;
    }

}
