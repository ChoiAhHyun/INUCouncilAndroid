package com.sauce.inunion;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 123 on 2018-08-11.
 */

public class RetrofitContact {
    @SerializedName("name")
    public String name;
    public String getName(){return name;}
    public RetrofitContact(String name){
        this.name = name;
    }

    @SerializedName("phoneNumber")
    String phoneNumber;
    public String getPhoneNumber(){return phoneNumber;}

    @SerializedName("email")
    String email;
    public String getEmail(){return email;}

    @SerializedName("position")
    String position;
    public String getPosition(){return position;}

    @SerializedName("etc")
    String etc;
    public String getEtc(){return etc;}

    @SerializedName("department")
    String department;
    public String getDepartment(){return department;}

    @SerializedName("addressId")
    public String addressId;
    public String getAddressId(){return addressId;}
}
