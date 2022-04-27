package com.roundG0929.hibike.api.map_route.navermap;


import android.content.res.AssetManager;

import com.roundG0929.hibike.api.server.ApiInterface;
import com.roundG0929.hibike.api.server.RetrofitClient;

import java.io.InputStream;

public class ReverseGeocoding {
    ApiInterface api;
    String curl; // naver api 요청 uri
    String latitude; //위도
    String longitude; // 경도
//    AssetManager am = getResources().getAssets();


    ReverseGeocoding() {
        this.api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        this.curl = "https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc?request=coordsToaddr&sourcecrs=epsg:4326&output=json&orders=addr,admcode,roadaddr&coords=";
    }

    public String exec(){
        return "";
    }

    public String getClientSecret() {
        return "";
    }

}
