package com.roundG0929.hibike.api.weather;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.roundG0929.hibike.api.weather.airconditionDto.Aircondition;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class AirconditionApi {
    Retrofit retrofit;
    AirconditionApiInterface airconditionApiInterface;

    public AirconditionApi(){
        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/")
                .addConverterFactory(GsonConverterFactory.create())
                //.addConverterFactory(ScalarsConverterFactory.create())
                .build();
        airconditionApiInterface = retrofit.create(AirconditionApiInterface.class);
    }

    public interface AirconditionApiInterface{
        @GET("getCtprvnRltmMesureDnsty?serviceKey=hHO%2BWrAENkUlQ%2FR4n9dnfra0CPKeDrh8CsoJKNHCTCJC%2BWQr0wDIzTj8UVxTvI6n6qD%2F7cAwwSf%2FAxknbJlsrQ%3D%3D&returnType=JSON&numOfRows=100&pageNo=1&sidoName=인천&ver=1.0")
        Call<Aircondition> getAircondition();
    }

    public Call<Aircondition> getApi(){
        return airconditionApiInterface.getAircondition();
    }

}
