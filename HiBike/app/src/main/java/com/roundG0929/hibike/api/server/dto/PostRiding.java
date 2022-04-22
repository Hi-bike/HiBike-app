package com.roundG0929.hibike.api.server.dto;

import com.google.gson.annotations.SerializedName;

public class PostRiding {
    @SerializedName("user_id")String userId;
    @SerializedName("riding_time")String ridingTime;
    @SerializedName("ave_speed")String aveSpeed;
    @SerializedName("ave_distance")String aveDistance;

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

    public String getAveDistance() {
        return aveDistance;
    }

    public void setAveDistance(String aveDistance) {
        this.aveDistance = aveDistance;
    }
}
