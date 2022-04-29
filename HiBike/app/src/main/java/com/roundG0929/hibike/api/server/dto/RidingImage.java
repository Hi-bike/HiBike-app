package com.roundG0929.hibike.api.server.dto;

import com.google.gson.annotations.SerializedName;

public class RidingImage {
    @SerializedName("id") private String id;
    @SerializedName("file") private String file;

    public String getId(){
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getImage(){
        return file;
    }
    public void setImage(String image) {
        this.file = image;
    }
}
