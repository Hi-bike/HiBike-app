package com.roundG0929.hibike.api.server.dto;

import com.google.gson.annotations.SerializedName;

public class PostRiding {
    @SerializedName("user_id")String userId;
    @SerializedName("unique_id")String uniqueId;
    @SerializedName("riding_time")String ridingTime;
    @SerializedName("ave_speed")String aveSpeed;
    @SerializedName("distance")String distance;
    @SerializedName("starting_point")String startingPoint;
    @SerializedName("end_point")String endPoint;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getStartingPoint() {
        return startingPoint;
    }

    public void setStartingPoint(String startingPoint) {
        this.startingPoint = startingPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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


}
