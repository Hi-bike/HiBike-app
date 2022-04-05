package com.roundG0929.hibike.api.map_route.kakaoLocal.searchPlaceDto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Document {
    @SerializedName("place_name")
    @Expose
    String place_name;
    @SerializedName("distance")
    @Expose
    String distance;
    @SerializedName("place_url")
    @Expose
    String place_url;
    @SerializedName("category_name")
    @Expose
    String category_name;
    @SerializedName("address_name")
    @Expose
    String address_name;
    @SerializedName("road_address_name")
    @Expose
    String road_address_name;
    @SerializedName("id")
    @Expose
    String id;
    @SerializedName("phone")
    @Expose
    String phone;
    @SerializedName("category_group_code")
    @Expose
    String category_group_code;
    @SerializedName("category_group_name")
    @Expose
    String category_group_name;
    @SerializedName("x")
    @Expose
    String x;
    @SerializedName("y")
    @Expose
    String y;




    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPlace_url() {
        return place_url;
    }

    public void setPlace_url(String place_url) {
        this.place_url = place_url;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getAddress_name() {
        return address_name;
    }

    public void setAddress_name(String address_name) {
        this.address_name = address_name;
    }

    public String getRoad_address_name() {
        return road_address_name;
    }

    public void setRoad_address_name(String road_address_name) {
        this.road_address_name = road_address_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCategory_group_code() {
        return category_group_code;
    }

    public void setCategory_group_code(String category_group_code) {
        this.category_group_code = category_group_code;
    }

    public String getCategory_group_name() {
        return category_group_name;
    }

    public void setCategory_group_name(String category_group_name) {
        this.category_group_name = category_group_name;
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x;
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y;
    }

    public String getPlace_name() {
        return place_name;
    }

    public void setPlace_name(String place_name) {
        this.place_name = place_name;
    }
}
