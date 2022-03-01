package com.roundG0929.hibike.api.map_route.graphhopperRoute.map_routeDto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

//길찾기 DTO
public class GraphhopperResponse {
    @SerializedName("hints")
    Hints hints = new Hints();
    @SerializedName("paths")
    ArrayList<Path> paths = new ArrayList<>();

    public Hints getHints() {
        return hints;
    }

    public void setHints(Hints hints) {
        this.hints = hints;
    }

    public ArrayList<Path> getPaths() {
        return paths;
    }

    public void setPaths(ArrayList<Path> paths) {
        this.paths = paths;
    }
}
