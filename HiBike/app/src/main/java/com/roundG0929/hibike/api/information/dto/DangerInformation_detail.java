package com.roundG0929.hibike.api.information.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DangerInformation_detail {
    @Expose
    @SerializedName("result")
    public Result result;


    public class Result{
        @Expose
        @SerializedName("nickname")
        String nickname;
        @Expose
        @SerializedName("title")
        String title;
        @Expose
        @SerializedName("contents")
        String contents;
        @Expose
        @SerializedName("latitude")
        double latitude;
        @Expose
        @SerializedName("longitude")
        double longitude;
        @Expose
        @SerializedName("time")
        String time;
        @Expose
        @SerializedName("image")
        String image;
        @Expose
        @SerializedName("region")
        String region;
        @Expose
        @SerializedName("region_detail")
        String region_detail;
        @Expose
        @SerializedName("period")
        int period;

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

        public String getRegion_detail() {
            return region_detail;
        }

        public void setRegion_detail(String region_detail) {
            this.region_detail = region_detail;
        }

        public int getPeriod() {
            return period;
        }

        public void setPeriod(int period) {
            this.period = period;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
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

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

}
