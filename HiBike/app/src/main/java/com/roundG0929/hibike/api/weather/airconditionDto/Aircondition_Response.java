package com.roundG0929.hibike.api.weather.airconditionDto;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Aircondition_Response {
    @SerializedName("body")
    @Expose
    public Aircondition_Body body;
}
