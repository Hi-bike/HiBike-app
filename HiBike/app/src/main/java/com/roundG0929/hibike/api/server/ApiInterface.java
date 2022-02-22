package com.roundG0929.hibike.api.server;


import com.roundG0929.hibike.api.server.dto.BasicProfile;
import com.roundG0929.hibike.api.server.dto.ProfileImage;
import com.roundG0929.hibike.api.server.dto.Signin;
import com.roundG0929.hibike.api.server.dto.Signout;
import com.roundG0929.hibike.api.server.dto.Signup;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiInterface {
    @POST("api/auth/signin")
    Call<Signin> signin(@Body Signin data);

    @GET("api/auth/signout")
    Call<Signout> signout(@Query("id") String id);

    @POST("api/auth/signup")
    Call<Signup> signup(@Body Signup data);

    @GET("api/auth/current-user")
    Call<BasicProfile> getProfile(@Query("id") String id);

    @POST("api/auth/setting")
    Call<BasicProfile> setNickname(@Body BasicProfile data);

    @Multipart
    @POST("/api/auth/image") //프로필 사진 업데이트
    Call<ProfileImage> setProfile (@Part MultipartBody.Part file, @Part("id") RequestBody param);
}