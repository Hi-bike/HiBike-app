package com.roundG0929.hibike.api.map_route.graphhopperRoute;

import android.graphics.Color;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.overlay.PathOverlay;
import com.roundG0929.hibike.api.map_route.graphhopperRoute.map_routeDto.GraphhopperResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;

//전반적인 길찾기 통신(Graphhopper)
public class MapRouteApi {
    private LatLng startPoint = null;
    private LatLng endPoint = null;


    Retrofit retrofit;
    MapRouteApiInterface mapRouteApiInterface;



    public MapRouteApi() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://graphhopper.com/api/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mapRouteApiInterface = retrofit.create(MapRouteApi.MapRouteApiInterface.class);
    }
    public MapRouteApi(@NonNull LatLng startPoint,@NonNull LatLng endPoint) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        retrofit = new Retrofit.Builder()
                .baseUrl("https://graphhopper.com/api/1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mapRouteApiInterface = retrofit.create(MapRouteApi.MapRouteApiInterface.class);
    }

    //retrofit 인터페이스
    public interface MapRouteApiInterface {
        //37.385543484360994, 126.63869459743177
        //37.37206394611004, 126.63389587406694
        //json객체 그대로 받기
        @GET("route?point={startpoint}&point={endpoint}&profile=bike&locale=kr&calc_points=true&points_encoded=false&key=8543250c-bd7b-4aa7-a013-ad631125b667")
        Call<Object> getdata(@Path("startpoint") String startpoint,@Path("endpoint") String endpoint);

        //DTO클래스로 받기(동적url사용)
        @GET
        Call<GraphhopperResponse> getResponseObject(@Url String url);

        //3point test api
        @GET("route?point=37.37921020923355, 126.63248066405454&point=37.38557988521586, 126.63044381523052&point=37.38547474419894, 126.63936686377967&profile=bike&locale=kr&calc_points=true&points_encoded=false&key=8543250c-bd7b-4aa7-a013-ad631125b667")
        Call<GraphhopperResponse> getdata_3pointTest();
    }

    //길찾기url(String)생성
    public String makeUrl(LatLng startPoint, LatLng endPoint){
        String requestUrl = null;

        requestUrl = "route?"
                    + "point=" + String.format("%.5f",startPoint.latitude) + "," +String.format("%.5f",startPoint.longitude)
                    + "&point=" + String.format("%.5f",endPoint.latitude) + "," +String.format("%.5f",endPoint.longitude)
                    + "&profile=bike&locale=kr&calc_points=true&points_encoded=false&key=8543250c-bd7b-4aa7-a013-ad631125b667" ;

        return requestUrl;
    }
    public String makeUrl4point(LatLng startPoint, LatLng endPoint,LatLng sub1,LatLng sub2){
        String requestUrl = null;

        requestUrl = "route?" + "point=" + String.format("%.5f",startPoint.latitude) + "," +String.format("%.5f",startPoint.longitude);

        if(sub1 != null) { requestUrl = requestUrl + "&point=" + String.format("%.5f",sub1.latitude) + "," +String.format("%.5f",sub1.longitude); }
        if(sub2 != null) { requestUrl = requestUrl + "&point=" + String.format("%.5f",sub2.latitude) + "," +String.format("%.5f",sub2.longitude); }

        requestUrl = requestUrl + "&point=" + String.format("%.5f",endPoint.latitude) + "," +String.format("%.5f",endPoint.longitude)
                + "&profile=bike&locale=kr&calc_points=true&points_encoded=false&key=8543250c-bd7b-4aa7-a013-ad631125b667" ;

        return requestUrl;
    }

    //길찾기 api 호출 함수
    public Call<GraphhopperResponse> getApi(){
        Call<GraphhopperResponse> responseCall = mapRouteApiInterface.getResponseObject(makeUrl(startPoint,endPoint));
        return responseCall;
    }
        //시작위치, 도착위치 직접지정
    public Call<GraphhopperResponse> getApi(LatLng startPoint,LatLng endPoint){
        Call<GraphhopperResponse> responseCall = mapRouteApiInterface.getResponseObject(makeUrl(startPoint,endPoint));
        return responseCall;
    }
    public Call<GraphhopperResponse> getApi(LatLng startPoint,LatLng endPoint,LatLng sub1,LatLng sub2){
        Call<GraphhopperResponse> responseCall = mapRouteApiInterface.getResponseObject(makeUrl4point(startPoint,endPoint,sub1,sub2));
        return responseCall;
    }

    //3point test method
    public Call<GraphhopperResponse> getApi_Test(){
        Call<GraphhopperResponse> responseCall = mapRouteApiInterface.getdata_3pointTest();
        return  responseCall;
    }






    public LatLng getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(LatLng startPoint) {
        this.startPoint = startPoint;
    }

    public LatLng getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(LatLng endPoint) {
        this.endPoint = endPoint;
    }

}
