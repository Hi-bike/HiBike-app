package com.roundG0929.hibike.api.weather;

import android.provider.ContactsContract;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.roundG0929.hibike.api.weather.wheatherDto.RealTimeWeather;

import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class WeatherApi {

    Retrofit retrofit;
    WeatherApi.WeatherApiInterface weatherApiInterface;
    String pageNo="1";
    String numOfRows = "100";
    String dataType = "JSON";
    String base_date = "20220428";
    String base_time = "0420";
    String nx = "55";
    String ny = "124";


    public WeatherApi(){
        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/")
                .addConverterFactory(GsonConverterFactory.create())
                //.addConverterFactory(ScalarsConverterFactory.create())
                .build();
        weatherApiInterface = retrofit.create(WeatherApi.WeatherApiInterface.class);
    }
    public WeatherApi(int nx,int ny,long now){
        Gson gson = new GsonBuilder().setLenient().create();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/")
                .addConverterFactory(GsonConverterFactory.create())
                //.addConverterFactory(ScalarsConverterFactory.create())
                .build();
        weatherApiInterface = retrofit.create(WeatherApi.WeatherApiInterface.class);

        this.nx = Integer.toString(nx);
        this.ny = Integer.toString(ny);

        Date date = new Date(now-3600000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");
        base_date = dateFormat.format(date);
//        date = new Date(now-3600000);
        base_time = timeFormat.format(date);

        Log.d("TAG", "realTimeWeathercheck: " +nx +" "+ny+" "+base_date+" "+base_time);
    }
    //retrofit 인터페이스
    public interface WeatherApiInterface {
        //raw 값 받기
        @GET("getUltraSrtFcst?serviceKey=hHO%2BWrAENkUlQ%2FR4n9dnfra0CPKeDrh8CsoJKNHCTCJC%2BWQr0wDIzTj8UVxTvI6n6qD%2F7cAwwSf%2FAxknbJlsrQ%3D%3D&pageNo=1&numOfRows=100&dataType=JSON&base_date=20220427&base_time=1300&nx=55&ny=124")
        Call<Object> getWeatherRaw();

        //&pageNo=1&numOfRows=100&dataType=JSON&base_date=20220428&base_time=0300&nx=55&ny=124
        @GET("getUltraSrtFcst?serviceKey=hHO%2BWrAENkUlQ%2FR4n9dnfra0CPKeDrh8CsoJKNHCTCJC%2BWQr0wDIzTj8UVxTvI6n6qD%2F7cAwwSf%2FAxknbJlsrQ%3D%3D")
        Call<RealTimeWeather> getWeather(@Query("pageNo") String pageNo,
                                         @Query("numOfRows") String numOfRows,
                                         @Query("dataType") String dataType,
                                         @Query("base_date") String base_date,
                                         @Query("base_time") String base_time,
                                         @Query("nx") String nx,
                                         @Query("ny") String ny);


    }



    public Call<RealTimeWeather> getApi(){
        Call<RealTimeWeather> responseCall = weatherApiInterface.getWeather(
                pageNo,
                numOfRows,
                dataType,
                base_date,
                base_time,
                nx,
                ny
        );

        return  responseCall;
    }

    public Call<Object> getApiRaw(){
        Call<Object> responseCallRaw = weatherApiInterface.getWeatherRaw();

        return  responseCallRaw;
    }

}
