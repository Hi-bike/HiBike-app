package com.roundG0929.hibike.api.map_route.graphhopperRoute.map_routeDto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

//For GraphhopperResponse
public class Path {
    @SerializedName("distance")
    @Expose
    double distance;
    @SerializedName("weight")
    @Expose
    double weight;
    @SerializedName("time")
    @Expose
    int time;
    @SerializedName("transfers")
    @Expose
    int transfers;
    @SerializedName("points_encoded")
    @Expose
    boolean points_encoded;
    @SerializedName("bbox")
    @Expose
    ArrayList<Double> bbox = new ArrayList<>();
    @SerializedName("points")
    @Expose
    Points points = new Points();

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTransfers() {
        return transfers;
    }

    public void setTransfers(int transfers) {
        this.transfers = transfers;
    }

    public boolean isPoints_encoded() {
        return points_encoded;
    }

    public void setPoints_encoded(boolean points_encoded) {
        this.points_encoded = points_encoded;
    }

    public ArrayList<Double> getBbox() {
        return bbox;
    }

    public void setBbox(ArrayList<Double> bbox) {
        this.bbox = bbox;
    }

    public Points getPoints() {
        return points;
    }

    public void setPoints(Points points) {
        this.points = points;
    }
}
