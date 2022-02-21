package com.roundG0929.hibike.api.server.dto;

import com.google.gson.annotations.SerializedName;

public class BasicProfile {
    @SerializedName("id") private String id;
    @SerializedName("nickname") private String nickname;
    @SerializedName("image") private String image;

    public String getId(){
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getNickname(){
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getImage(){ return image; }
    public void setImage(String image) { this.image = image; }
}
