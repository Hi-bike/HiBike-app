package com.roundG0929.hibike.api.map_route.graphhopperRoute.map_routeDto;


import com.google.gson.annotations.SerializedName;


//For GraphhopperResponse
public class Hints {
    @SerializedName("visited_nodes.sum")
    private int visited_nodes_sum;
    @SerializedName("visited_nodes.average")
    private double visited_nodes_average;

    public int getVisited_nodes_sum() {
        return visited_nodes_sum;
    }

    public void setVisited_nodes_sum(int visited_nodes_sum) {
        this.visited_nodes_sum = visited_nodes_sum;
    }

    public double getVisited_nodes_average() {
        return visited_nodes_average;
    }

    public void setVisited_nodes_average(double visited_nodes_average) {
        this.visited_nodes_average = visited_nodes_average;
    }
}
