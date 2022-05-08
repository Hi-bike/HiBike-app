package com.roundG0929.hibike.api.information.requestBody;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostInformation {  //위험정보 등록 body
    @Expose
    @SerializedName("id")
    String id;
    @Expose
    @SerializedName("latitude")
    double latitude;
    @Expose
    @SerializedName("longitude")
    double longitude;
    @Expose
    @SerializedName("title")
    String title;
    @Expose
    @SerializedName("contents")
    String contents;

//    public PostInformation() {
//        this.id = "";
//        this.latitude = 0;
//        this.longitude = 0;
//        this.title = "";
//        this.contents = "";
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
