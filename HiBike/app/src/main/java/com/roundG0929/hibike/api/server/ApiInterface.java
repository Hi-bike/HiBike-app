package com.roundG0929.hibike.api.server;


import com.roundG0929.hibike.api.server.dto.SigninDto;
import com.roundG0929.hibike.api.server.dto.SignupDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("api/auth/signin")
    Call<SigninDto> signin(@Body SigninDto data);

    @POST("api/auth/signup")
    Call<SignupDto> signup(@Body SignupDto data);
}
