package com.roundG0929.hibike.api.weather.airconditionDto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AirconditionItem {
    @SerializedName("sidoName")
    @Expose
    public String sidoName;

    @SerializedName("stationName")
    @Expose
    public String stationName;

    @SerializedName("pm10Value")
    @Expose
    public String pm10Value;

    @SerializedName("pm25Value")
    @Expose
    public String pm25Value;
}
