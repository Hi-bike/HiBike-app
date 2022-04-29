package com.roundG0929.hibike.api.server.dto;

import com.google.gson.annotations.SerializedName;

public class RidingRegion {
    @SerializedName("region") String region;
    @SerializedName("unique_id") String uniqueId;

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }


    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
