package com.roundG0929.hibike.api.server.dto;

import com.google.gson.annotations.SerializedName;

public class PostDanger {
    @SerializedName("result") private String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
