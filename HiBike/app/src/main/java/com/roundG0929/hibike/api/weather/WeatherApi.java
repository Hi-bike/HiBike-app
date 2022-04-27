package com.roundG0929.hibike.api.weather;

import com.roundG0929.hibike.api.map_route.kakaoLocal.SearchPlaceApi;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;

public class WeatherApi {

    Retrofit retrofit;
    WeatherApi.WeatherApiInterface weatherApiInterface;


    public WeatherApi(){
        retrofit = new Retrofit.Builder()
                .baseUrl("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        weatherApiInterface = retrofit.create(WeatherApi.WeatherApiInterface.class);
    }
    //retrofit 인터페이스
    public interface WeatherApiInterface {
        //raw 값 받기
        @GET("getUltraSrtFcst?serviceKey=hHO+WrAENkUlQ/R4n9dnfra0CPKeDrh8CsoJKNHCTCJC+WQr0wDIzTj8UVxTvI6n6qD/7cAwwSf/AxknbJlsrQ==&pageNo=1&numOfRows=100&dataType=JSON&base_date=20220427&base_time=1300&nx=55&ny=124")
        Call<Object> getWeather();

    }



    public Call<Object> getApiRaw(){
        Call<Object> responseCallRaw = weatherApiInterface.getWeather();

        return  responseCallRaw;
    }

}
