package com.roundG0929.hibike.api.server;


import com.roundG0929.hibike.api.server.dto.SigninDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("api/auth/signin")
    Call<SigninDto> postData(@Body SigninDto data);
}
