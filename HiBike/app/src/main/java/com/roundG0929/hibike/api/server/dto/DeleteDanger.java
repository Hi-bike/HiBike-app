package com.roundG0929.hibike.api.server.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteDanger {
    @Expose
    @SerializedName("user_id")
    String userId;

    @Expose
    @SerializedName("latitude")
    double latitude;

    @Expose
    @SerializedName("longitude")
    double longitude;

    @Expose
    @SerializedName("my_latitude")
    double myLatitude;

    @Expose
    @SerializedName("my_longitude")
    double myLongitude;

    @Expose
    @SerializedName("result")
    String result;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public double getMyLatitude() {
        return myLatitude;
    }

    public void setMyLatitude(double myLatitude) {
        this.myLatitude = myLatitude;
    }

    public double getMyLongitude() {
        return myLongitude;
    }

    public void setMyLongitude(double myLongitude) {
        this.myLongitude = myLongitude;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
