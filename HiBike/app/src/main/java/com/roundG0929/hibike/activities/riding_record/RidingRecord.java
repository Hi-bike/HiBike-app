package com.roundG0929.hibike.activities.riding_record;

public class RidingRecord {
    private int ridingId;
    private String createTime;
    private String ridingTime;
    private String aveSpeed;
    private String distance;
    private String starting_point;
    private String end_point;
    private String uniqueId;

    RidingRecord(int ridingId, String createTime, String ridingTime, String aveSpeed, String distance, String starting_point, String end_point, String uniqueId){
        this.ridingId = ridingId;
        this.createTime = createTime;
        this.ridingTime = ridingTime;
        this.aveSpeed = aveSpeed;
        this.distance = distance;
        this.starting_point = starting_point;
        this.end_point = end_point;
        this.uniqueId = uniqueId;
    }

    public int getRidingId() {
        return ridingId;
    }

    public void setRidingId(int ridingId) {
        this.ridingId = ridingId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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
    public String getStarting_point() {
        return starting_point;
    }

    public void setStarting_point(String starting_point) {
        this.starting_point = starting_point;
    }

    public String getEnd_point() {
        return end_point;
    }

    public void setEnd_point(String end_point) {
        this.end_point = end_point;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
