package com.sauce.inunion;
import com.google.gson.annotations.SerializedName;




public class LoginResult {
    @SerializedName("ans")
    public String ans;



    @SerializedName("department")
    public String department;


    public LoginResult(String ans, String department){
        this.ans = ans;
        this.department = department;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans= ans;
    }

    public String getDepartment() {
        return department;
    }
    public  void setDepartment(){
        this.department = department;
    }
}
