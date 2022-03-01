package com.roundG0929.hibike.api.map_route.graphhopperRoute;

import com.roundG0929.hibike.api.map_route.graphhopperRoute.map_routeDto.GraphhopperResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MapRouteApiInterface {

    //json객체 그대로 받기
    @GET("route?point=37.4586,126.6772&point=37.4476,126.6507&profile=bike&locale=kr&calc_points=true&points_encoded=false&key=8543250c-bd7b-4aa7-a013-ad631125b667")
    Call<Object> getdata();

    //DTO클래스로 받기
    @GET("route?point=37.4586,126.6772&point=37.4476,126.6507&profile=bike&locale=kr&calc_points=true&points_encoded=false&key=8543250c-bd7b-4aa7-a013-ad631125b667")
    Call<GraphhopperResponse> getResponseObject();
}
