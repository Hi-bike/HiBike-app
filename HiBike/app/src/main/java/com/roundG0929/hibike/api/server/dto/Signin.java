package com.roundG0929.hibike.api.server.dto;


import com.google.gson.annotations.SerializedName;

public class Signin {

    @SerializedName("id") private String id;
    @SerializedName("password") private String password;
    @SerializedName("fcm_token") private String fcm_token;
    @SerializedName("result") private String result;

    public String getId(){
        return id;
    }

    public void setId(String id){
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) { this.password = password; }

    public void setFcmToken(String fcm_token) { this.fcm_token = fcm_token; }

    public String getResult(){
        return result;
    }
}

