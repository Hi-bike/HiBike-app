package com.roundG0929.hibike.api.server.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PostRidingMulti {
    @Expose
    @SerializedName("result")
    String result;

    String userId;
    String uniqueId;
    String ridingTime;
    String aveSpeed;
    String distance;
    String startingRegion;
    String endRegion;
    String northeastLati;
    String northeastLong;
    String southwestLati;
    String southwestLong;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getRidingTime() {
        return ridingTime;
    }

    public void setRidingTime(String ridingTime) {
        this.ridingTime = ridingTime;
    }

    public String getAveSpeed() {
        return aveSpeed;
    }

    public void setAveSpeed(String aveSpeed) {
        this.aveSpeed = aveSpeed;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getStartingRegion() {
        return startingRegion;
    }

    public void setStartingRegion(String startingRegion) {
        this.startingRegion = startingRegion;
    }

    public String getEndRegion() {
        return endRegion;
    }

    public void setEndRegion(String endRegion) {
        this.endRegion = endRegion;
    }

    public String getNortheastLati() {
        return northeastLati;
    }

    public void setNortheastLati(String northeastLati) {
        this.northeastLati = northeastLati;
    }

    public String getNortheastLong() {
        return northeastLong;
    }

    public void setNortheastLong(String northeastLong) {
        this.northeastLong = northeastLong;
    }

    public String getSouthwestLati() {
        return southwestLati;
    }

    public void setSouthwestLati(String southwestLati) {
        this.southwestLati = southwestLati;
    }

    public String getSouthwestLong() {
        return southwestLong;
    }

    public void setSouthwestLong(String southwestLong) {
        this.southwestLong = southwestLong;
    }
}
