package com.roundG0929.hibike.api.weather.wheatherDto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RealTimeWeather {
    @SerializedName("response")
    @Expose
    public RealTimeWeather_Response response;
}
