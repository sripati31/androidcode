package com.divinetechs.gotstart.Model.LoginRegister;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginRegiModel {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("User_id")
    @Expose
    private String User_id;
    @SerializedName("is_premium")
    @Expose
    private String is_premium;

    public String getIs_premium() {
        return is_premium;
    }

    public void setIs_premium(String is_premium) {
        this.is_premium = is_premium;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserid() {
        return User_id;
    }

    public void setUserid(String userid) {
        this.User_id = userid;
    }

}
