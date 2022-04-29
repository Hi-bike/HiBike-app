package com.roundG0929.hibike.api.map_route.navermap;


import android.content.res.AssetManager;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ReverseGeocodingGenerator extends AppCompatActivity {

    Map<String, String> headers;
    Map<String, String> queries;
    String clientSecert="f405zE41iO7jICqa4n23ZD1zLyZiLGxJRk03FlNG";

    public ReverseGeocodingGenerator(Double longitude,Double latitude) {
        this.headers = new HashMap<>();
        this.headers.put("X-NCP-APIGW-API-KEY-ID", "6xi4dmacb3");
        this.headers.put("X-NCP-APIGW-API-KEY", clientSecert);

        this.queries = new HashMap<>();
        this.queries.put("request", "coordsToaddr");
        this.queries.put("coords", longitude+","+latitude);
        this.queries.put("sourcecrs","epsg:4326");
        this.queries.put("output", "json");
        this.queries.put("orders", "addr,admcode,roadaddr");

        Log.d("clientSecret", clientSecert);

    }

    public Map<String, String> getHeaders() {return headers;}
    public Map<String, String> getQueries() {
        return queries;
    }
}
