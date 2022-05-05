package com.roundG0929.hibike.api.server;


import com.roundG0929.hibike.api.server.dto.BasicProfile;
import com.roundG0929.hibike.api.server.dto.GetPost;
import com.roundG0929.hibike.api.server.dto.GetRidingAll;
import com.roundG0929.hibike.api.server.dto.GetRidingOne;
import com.roundG0929.hibike.api.server.dto.GetRidingTotal;
import com.roundG0929.hibike.api.server.dto.PostRiding;
import com.roundG0929.hibike.api.server.dto.ProfileImage;
import com.roundG0929.hibike.api.server.dto.ReverseGeocodingDto;
import com.roundG0929.hibike.api.server.dto.RidingImage;
import com.roundG0929.hibike.api.server.dto.RidingRegion;
import com.roundG0929.hibike.api.server.dto.SendReply;
import com.roundG0929.hibike.api.server.dto.Signin;
import com.roundG0929.hibike.api.server.dto.Signout;
import com.roundG0929.hibike.api.server.dto.Signup;
import com.roundG0929.hibike.api.server.dto.SendPost;
import com.roundG0929.hibike.api.server.dto.GetReply;
import com.roundG0929.hibike.api.server.dto.GetReplyContent;
import com.roundG0929.hibike.api.server.dto.GetPostContent;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

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

    @GET("api/board/posts/{page}")
    //Call<Object> getdata();
    Call<GetPost> getPost (@Path("page") int page);

    @GET("api/board/reply/{page}/{post_id}")
        //Call<Object> getdata();
    Call<GetReply> getReply (@Path("page") int page, @Path("post_id") int post_id);

    @GET("api/board/post_content/{post_id}")
        //Call<Object> getdata();
    Call<GetPostContent> getPostContent (@Path("post_id") int post_id);

    @GET("api/board/reply_content/{reply_id}")
        //Call<Object> getdata();
    Call<GetReplyContent> getReplyContent (@Path("reply_id") int reply_id);

    @POST("api/board/post")
    Call<SendPost> sendPost(@Body SendPost data);

    @POST("api/board/reply")
    Call<SendReply> sendReply(@Body SendReply data);

    @GET("api/auth/rone/{unique_id}")
    Call<GetRidingOne> getRidingInfoOne(@Path("unique_id") String uniqueId);

    @POST("api/auth/rone")
    Call<PostRiding> postRiding(@Body PostRiding data);

    @GET("api/auth/rtotal/{userId}")
    Call<GetRidingTotal> getRidingTotal(@Path("userId") String userId);

    @GET("api/auth/rall/{userId}/{page}")
    Call<GetRidingAll> getRidingAll(@Path("userId") String userId, @Path("page") int page);

    @Multipart
    @POST("api/auth/rimage") //프로필 사진 업데이트
    Call<RidingImage> setRidingImage (@Part MultipartBody.Part file, @Part("unique_id") RequestBody param);

    @POST("api/auth/sregion")
    Call<RidingRegion> setRidingSRegion(@Body RidingRegion ridingRegion);

    @POST("api/auth/eregion")
    Call<RidingRegion> setRidingERegion(@Body RidingRegion ridingRegion);
}
