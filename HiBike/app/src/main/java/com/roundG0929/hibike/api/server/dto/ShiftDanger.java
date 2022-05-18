package com.roundG0929.hibike.api.server.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShiftDanger {
    @Expose
    @SerializedName("danger_id")
    int dangerId;

    public int getDangerId() {
        return dangerId;
    }

    public void setDangerId(int dangerId) {
        this.dangerId = dangerId;
    }
}
