package com.roundG0929.hibike.api.server.dto;

import com.google.gson.annotations.SerializedName;

public class GetRidingOne {
    @SerializedName("create_time") String createTime;
    @SerializedName("distance") String distance;
    @SerializedName("ave_speed") String aveSpeed;
    @SerializedName("riding_time")String ridingTime;
    @SerializedName("startingRegion")String starting_region;
    @SerializedName("endRegion")String end_region;
    @SerializedName("unique_id") String uniqueId;

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAveSpeed() {
        return aveSpeed;
    }

    public void setAveSpeed(String aveSpeed) {
        this.aveSpeed = aveSpeed;
    }

    public String getRidingTime() {
        return ridingTime;
    }

    public void setRidingTime(String ridingTime) {
        this.ridingTime = ridingTime;
    }

    public String getStarting_region() {
        return starting_region;
    }

    public void setStarting_region(String starting_region) {
        this.starting_region = starting_region;
    }

    public String getEnd_region() {
        return end_region;
    }

    public void setEnd_region(String end_region) {
        this.end_region = end_region;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
