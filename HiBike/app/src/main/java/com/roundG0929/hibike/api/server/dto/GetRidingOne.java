package com.roundG0929.hibike.api.server.dto;

import com.google.gson.annotations.SerializedName;

public class GetRidingOne {
    @SerializedName("id") int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
