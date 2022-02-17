package com.roundG0929.hibike.api.server.dto;

import com.google.gson.annotations.SerializedName;

public class NicknameProfile {
    @SerializedName("id") private String id;
    @SerializedName("nickname") private String nickname;

    public String getId(){
        return id;
    }
    public void setId(String nickname) {
        this.nickname = nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getNickname(){
        return nickname;
    }
}
