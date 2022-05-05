package com.roundG0929.hibike.api.server.dto;

import com.google.gson.annotations.SerializedName;

public class GetRidingTotal {
    @SerializedName("user_id") String userId;
    @SerializedName("total_time") String totalTime;
    @SerializedName("total_distance") String totalDistance;
    @SerializedName("count") int count;
    public String getUserId() {
        return userId;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(String totalTime) {
        this.totalTime = totalTime;
    }

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
