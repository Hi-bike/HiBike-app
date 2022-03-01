package com.roundG0929.hibike.api.server;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit;

    public static Retrofit getRetrofit(){
        if(retrofit==null){
            Retrofit.Builder builder = new Retrofit.Builder();
            builder.baseUrl("http://132.226.232.31/");
//            builder.baseUrl("http://10.0.2.2:5000/"); //로컬
            builder.addConverterFactory(GsonConverterFactory.create());

            retrofit = builder.build();
        }
        return retrofit;
    }
}
