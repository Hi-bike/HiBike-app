package com.roundG0929.hibike.api.weather.airconditionDto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Aircondition_Body {
    @SerializedName("items")
    @Expose
    public ArrayList<AirconditionItem> items;
}
