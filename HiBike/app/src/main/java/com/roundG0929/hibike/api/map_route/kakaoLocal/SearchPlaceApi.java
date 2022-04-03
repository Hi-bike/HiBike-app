package com.roundG0929.hibike.api.map_route.kakaoLocal;

import com.roundG0929.hibike.api.map_route.kakaoLocal.searchPlaceDto.KakaoLocalSearch;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public class SearchPlaceApi {

    Retrofit retrofit;
    SearchPlaceApiInterface searchPlaceApiInterface;

    private String apikey = "KakaoAK 5945e53d2e9fea1339bf03b9fc766330";
    public String apikeyword = "인천대";

    //매개변수 없이 생성시 키워드 "인천대"
    public SearchPlaceApi(){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://dapi.kakao.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        searchPlaceApiInterface = retrofit.create(SearchPlaceApi.SearchPlaceApiInterface.class);
    }
    //매개변수가 검색할 키워드.
    public SearchPlaceApi(String apikeyword){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://dapi.kakao.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        searchPlaceApiInterface = retrofit.create(SearchPlaceApi.SearchPlaceApiInterface.class);

        this.apikeyword = apikeyword;
    }
    //retrofit 인터페이스
    public interface SearchPlaceApiInterface {
        @GET("v2/local/search/keyword.json")
        Call<Object> searchPlaceKeywordRaw(@Header("Authorization") String key,
                                        @Query("query")String keyword);

        @GET("v2/local/search/keyword.json")
        Call<KakaoLocalSearch> searchPlaceKeyword(@Header("Authorization") String key,
                                                  @Query("query")String keyword);

    }



    public Call<Object> getApiRaw(){
        Call<Object> responseCallRaw = searchPlaceApiInterface.searchPlaceKeywordRaw(apikey,apikeyword);

        return  responseCallRaw;
    }
    public Call<KakaoLocalSearch> getApi(){
        Call<KakaoLocalSearch> responseCall = searchPlaceApiInterface.searchPlaceKeyword(apikey,apikeyword);

        return responseCall;
    }

    public void setApikeyword(String apikeyword) {
        this.apikeyword = apikeyword;
    }
}