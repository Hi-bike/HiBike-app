package com.roundG0929.hibike.api.information.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class DangerInformation_Points {
    @Expose
    @SerializedName("danger_list")
    public List<List<Double>> danger_list;

    @Expose
    @SerializedName("danger_count")
    public List<Integer> danger_count;
}
