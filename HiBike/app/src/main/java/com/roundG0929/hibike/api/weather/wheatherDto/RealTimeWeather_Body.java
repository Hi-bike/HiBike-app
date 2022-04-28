package com.roundG0929.hibike.api.weather.wheatherDto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RealTimeWeather_Body {
    @SerializedName("dataType")
    @Expose
    public String dataType;

    @SerializedName("items")
    @Expose
    public Items items;

    public Items getItems() {
        return items;
    }

    public void setItems(Items items) {
        this.items = items;
    }

    public class Items{
        @SerializedName("item")
        @Expose
        public ArrayList<Item> item;

        public ArrayList<Item> getItem() {
            return item;
        }

        public void setItem(ArrayList<Item> item) {
            this.item = item;
        }
    }
}
