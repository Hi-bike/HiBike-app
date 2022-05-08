package com.roundG0929.hibike.api.information;

import com.roundG0929.hibike.api.information.dto.DangerInformation_Points;
import com.roundG0929.hibike.api.information.requestBody.PostInformation;
import com.roundG0929.hibike.api.map_route.graphhopperRoute.MapRouteApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class InformationApi {

    Retrofit retrofit;
    InformationApi.InformationApiInterface informationApiInterfaceApiInterface;
    public DangerInformationRequest dangerInformationRequest;


    public InformationApi() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://132.226.232.31/")
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        informationApiInterfaceApiInterface =
                retrofit.create(InformationApi.InformationApiInterface.class);
        dangerInformationRequest = new DangerInformationRequest();
        dangerInformationRequest.danger_range = new ArrayList<>();
    }

    public interface InformationApiInterface{
        @POST("api/board/danger")
        Call<Object> getDangerPoints(@Body DangerInformationRequest danger_range);

        @POST("/api/board/post-danger")
        Call<Object> postDangerInformation(@Body PostInformation postInformation);
    }

    public Call<Object> getDangerPointsApiRaw(DangerInformationRequest dangerInformationRequest){
        Call<Object> responseCallRaw = informationApiInterfaceApiInterface.getDangerPoints(dangerInformationRequest);

        return  responseCallRaw;
    }

    public Call<Object> postDangerInformation(PostInformation postInformation){
        return informationApiInterfaceApiInterface.postDangerInformation(postInformation);
    }




    //위험정보 지점 요청위한 객체 (4개 좌표으로 정의된 영역)
    // ex) <<경도,위도,경도,위도,경도,위도,경도,위도>,<경도,위도,경도,위도,경도,위도,경도,위도>,<경도,위도,경도,위도,경도,위도,경도,위도>...>
    public class DangerInformationRequest{
        public List<List<Double>> danger_range;
    }
}
