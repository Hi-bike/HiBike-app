package com.roundG0929.hibike.api.server.dto;

import com.google.gson.annotations.SerializedName;

public class Signout {
    @SerializedName("result") private String result;
    public String getResult(){ return result; }
}
