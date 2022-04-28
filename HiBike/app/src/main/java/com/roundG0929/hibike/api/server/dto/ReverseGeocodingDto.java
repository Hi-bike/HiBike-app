package com.roundG0929.hibike.api.server.dto;

import com.google.gson.annotations.SerializedName;

public class ReverseGeocodingDto {
    @SerializedName("status") Object status;
    @SerializedName("results") Object result;

    public Object getStatus() {
        return status;
    }

    public void setStatus(Object status) {
        this.status = status;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
