package com.roundG0929.hibike.api.server.dto;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class GetRidingAll {
    @SerializedName("user_id") String userId;
    @SerializedName("page") int page;
    @SerializedName("result") Object result;

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

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
