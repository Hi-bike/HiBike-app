package com.roundG0929.hibike.api.server.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class PostMyDanger {
    @Expose
    @SerializedName("result")
    ArrayList<Result> results = new ArrayList<>();

    public ArrayList<Result> getResults() {
        return results;
    }

    public class Result{
        @Expose
        @SerializedName("nickname")
        String nickname;

        @Expose
        @SerializedName("title")
        String title;

        @Expose
        @SerializedName("time")
        String time;

        @Expose
        @SerializedName("is_delete")
        String isDelete;

        @Expose
        @SerializedName("region")
        String region;

        @Expose
        @SerializedName("region_detail")
        String regionDetail;

        @Expose
        @SerializedName("danger_id")
        int dangerId;

        @Expose
        @SerializedName("count")
        int count;

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

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(String isDelete) {
            this.isDelete = isDelete;
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

        public int getDangerId() {
            return dangerId;
        }

        public void setDangerId(int dangerId) {
            this.dangerId = dangerId;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }

}
