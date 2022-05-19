package com.roundG0929.hibike.api.server.dto;

import android.location.Location;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetAllDanger {
    @Expose
    @SerializedName("result")
    ArrayList<ArrayList<Double>> result;

    public ArrayList<ArrayList<Double>> getResult() {
        return result;
    }

    public void setResult(ArrayList<ArrayList<Double>> result) {
        this.result = result;
    }
}
