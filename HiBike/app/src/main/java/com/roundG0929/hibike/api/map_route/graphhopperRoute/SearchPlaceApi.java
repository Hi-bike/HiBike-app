package com.roundG0929.hibike.api.map_route.graphhopperRoute;

import com.google.android.datatransport.runtime.retries.Retries;
import com.roundG0929.hibike.api.map_route.graphhopperRoute.map_routeDto.GraphhopperResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public class SearchPlaceApi {

    Retrofit retrofit;
    SearchPlaceApiInterface searchPlaceApiInterface;

    String apikey = "KakaoAK 5945e53d2e9fea1339bf03b9fc766330";
    String apikeyword = "인천대";

    public SearchPlaceApi(){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://dapi.kakao.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        searchPlaceApiInterface = retrofit.create(SearchPlaceApi.SearchPlaceApiInterface.class);
    }


    //retrofit 인터페이스
    public interface SearchPlaceApiInterface {
        @GET("v2/local/search/keyword.json")
        Call<Object> searchPlaceKeyword(@Header("Authorization") String key,
                                        @Query("query")String keyword);
    }

    public Call<Object> getApi(){
        Call<Object> responseCall = searchPlaceApiInterface.searchPlaceKeyword(apikey,apikeyword);
        return  responseCall;
    }
}
