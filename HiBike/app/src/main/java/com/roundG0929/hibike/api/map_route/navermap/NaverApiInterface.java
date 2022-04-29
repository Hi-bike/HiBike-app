package com.roundG0929.hibike.api.map_route.navermap;

import com.roundG0929.hibike.api.server.dto.ReverseGeocodingDto;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.QueryMap;

public interface NaverApiInterface {
    @GET("map-reversegeocode/v2/gc")
    Call<ReverseGeocodingDto> getRegion(@HeaderMap Map<String, String> headers, @QueryMap(encoded = true) Map<String, String> queries );
}
