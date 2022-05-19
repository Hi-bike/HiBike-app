package com.roundG0929.hibike.api.server.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetDangerOne {
    @Expose
    @SerializedName("result")
    Result result;

    public Result getResult() {
        return result;
    }

    public class Result {
        @Expose
        @SerializedName("title")
        String title;

        @Expose
        @SerializedName("contents")
        String contents;

        @Expose
        @SerializedName("time")
        String time;

        @Expose
        @SerializedName("is_delete")
        String isDelete;

        @Expose
        @SerializedName("image")
        String image;

        @Expose
        @SerializedName("region")
        String region;

        @Expose
        @SerializedName("region_detail")
        String regionDetail;

        @Expose
        @SerializedName("period")
        int period;

        @Expose
        @SerializedName("nickname")
        String nickname;

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

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

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

        public int getPeriod() {
            return period;
        }

        public void setPeriod(int period) {
            this.period = period;
        }

        public String getIsDelete() {
            return isDelete;
        }

        public void setIsDelete(String isDelete) {
            this.isDelete = isDelete;
        }

        public String getRegionDetail() {
            return regionDetail;
        }

        public void setRegionDetail(String regionDetail) {
            this.regionDetail = regionDetail;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
