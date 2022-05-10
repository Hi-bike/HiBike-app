package com.roundG0929.hibike.api.weather.airconditionDto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Aircondition {
    @SerializedName("response")
    @Expose
    public Aircondition_Response response;
}
