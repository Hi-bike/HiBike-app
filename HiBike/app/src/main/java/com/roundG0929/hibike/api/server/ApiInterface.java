package com.roundG0929.hibike.api.server;


import com.roundG0929.hibike.api.server.dto.Signin;
import com.roundG0929.hibike.api.server.dto.Signup;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("api/auth/signin")
    Call<Signin> signin(@Body Signin data);

    @POST("api/auth/signup")
    Call<Signup> signup(@Body Signup data);
}
