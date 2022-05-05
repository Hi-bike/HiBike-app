package com.roundG0929.hibike.api.weather.wheatherDto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RealTimeWeather_Response {
    @SerializedName("body")
    @Expose
    public RealTimeWeather_Body body;
}
