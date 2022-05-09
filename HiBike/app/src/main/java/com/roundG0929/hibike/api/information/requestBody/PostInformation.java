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
    @Expose
    @SerializedName("image")
    String image;
    @Expose
    @SerializedName("region")
    String region;
    @Expose
    @SerializedName("region_detail")
    String regionDetail;
    @Expose
    @SerializedName("period")
    int period;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getRegionDetail() {
        return regionDetail;
    }

    public void setRegionDetail(String regionDetail) {
        this.regionDetail = regionDetail;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}
