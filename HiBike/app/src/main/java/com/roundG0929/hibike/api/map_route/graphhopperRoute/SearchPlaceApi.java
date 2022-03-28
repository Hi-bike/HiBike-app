package com.roundG0929.hibike.api.map_route.graphhopperRoute;

import com.roundG0929.hibike.api.map_route.graphhopperRoute.map_routeDto.GraphhopperResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

public class SearchPlaceApi {


    //retrofit 인터페이스
    public interface MapRouteApiInterface {
        //json객체 그대로 받기
        @GET("https://maps.googleapis.com/maps/api/place/findplacefromtext/json?" +
                "")
        Call<Object> getdata(@Path("startpoint") String startpoint, @Path("endpoint") String endpoint);

//        //DTO클래스로 받기(동적url사용)
//        @GET
//        Call<GraphhopperResponse> getResponseObject(@Url String url);


    }
}
