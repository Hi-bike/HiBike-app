package com.roundG0929.hibike.api.server.dto;

import com.google.gson.annotations.SerializedName;

public class GetRidingTotal {
    @SerializedName("user_id") String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
