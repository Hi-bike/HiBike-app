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
        //json객체 그대로 받기
        @GET("route?point={startpoint}&point={endpoint}&profile=bike&locale=kr&calc_points=true&points_encoded=false&key=8543250c-bd7b-4aa7-a013-ad631125b667")
        Call<Object> getdata(@Path("startpoint") String startpoint,@Path("endpoint") String endpoint);

        //DTO클래스로 받기(동적url사용)
        @GET
        Call<GraphhopperResponse> getResponseObject(@Url String url);
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

//
//    public List<LatLng> getRoutePoints(LatLng startPoint,LatLng endPoint){
//        List<LatLng> coordsForDrawLine = new ArrayList<>();
//
////        this.getApi().enqueue(new Callback<GraphhopperResponse>() {
////            @Override
////            public void onResponse(Call<GraphhopperResponse> call, Response<GraphhopperResponse> response) {
////                GraphhopperResponse graphhopperResponse = response.body();
////                ArrayList<ArrayList<Double>> pointsList = new ArrayList<>();
////                pointsList = graphhopperResponse.getPaths().get(0).getPoints().getCoordinates();
////
////                if (coordsForDrawLine != null){
////                    coordsForDrawLine.clear();
////                }
////                for(int i = 0;i<pointsList.size();i++) {
////                    coordsForDrawLine.add(new LatLng(pointsList.get(i).get(1),pointsList.get(i).get(0)));
////                }
////            }
////
////            @Override
////            public void onFailure(Call<GraphhopperResponse> call, Throwable t) {
////                Log.d("getRoutePoints", "onFailure: " + t.toString());
////            }
////        });
//
//        try {
//            GraphhopperResponse graphhopperResponse = this.getApi().execute().body();
//            ArrayList<ArrayList<Double>> pointsList = new ArrayList<>();
//            pointsList = graphhopperResponse.getPaths().get(0).getPoints().getCoordinates();
//
//            if (coordsForDrawLine != null) {
//                coordsForDrawLine.clear();
//            }
//            for (int i = 0; i < pointsList.size(); i++) {
//                coordsForDrawLine.add(new LatLng(pointsList.get(i).get(1), pointsList.get(i).get(0)));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return coordsForDrawLine;
//    }






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
