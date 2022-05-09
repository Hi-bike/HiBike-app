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
