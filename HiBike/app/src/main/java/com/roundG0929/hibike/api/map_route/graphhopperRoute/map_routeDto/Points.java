package com.roundG0929.hibike.api.map_route.graphhopperRoute.map_routeDto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

//For GraphhopperResponse
public class Points {
    @SerializedName("type")
    String type;
    @SerializedName("coordinates")
    ArrayList<ArrayList<Double>> coordinates = new ArrayList<>();

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<ArrayList<Double>> getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(ArrayList<ArrayList<Double>> coordinates) {
        this.coordinates = coordinates;
    }
}
