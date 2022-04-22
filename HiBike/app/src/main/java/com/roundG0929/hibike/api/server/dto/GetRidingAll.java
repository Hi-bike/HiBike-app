package com.roundG0929.hibike.api.server.dto;

import com.google.gson.annotations.SerializedName;

public class GetRidingAll {
    @SerializedName("user_id") String userId;
    @SerializedName("page") int page;

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getUserId() {
        return userId;
    }

    public int getPage() {
        return page;
    }

}
