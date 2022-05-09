package com.roundG0929.hibike.api.information;

import com.roundG0929.hibike.api.information.dto.DangerInformation_Points;
import com.roundG0929.hibike.api.information.dto.DangerInformation_detail;
import com.roundG0929.hibike.api.information.requestBody.Danger_infoBody;
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
    public DangerInformationRequestBody dangerInformationRequestBody;


    public InformationApi() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://132.226.232.31/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        informationApiInterfaceApiInterface =
                retrofit.create(InformationApi.InformationApiInterface.class);
        dangerInformationRequestBody = new DangerInformationRequestBody();
        dangerInformationRequestBody.danger_range = new ArrayList<>();
    }

    public interface InformationApiInterface{
        //위험지점 요청
        @POST("api/board/danger")
        Call<DangerInformation_Points> getDangerPoints(@Body DangerInformationRequestBody danger_range);

//        //위험정보 등록
//        @POST("/api/board/post-danger")
//        Call<Object> postDangerInformation(@Body PostInformation postInformation);

        //위험정보 상세 요청
        @POST("/api/board/danger-info")
        Call<DangerInformation_detail> getDangerInfo(@Body Danger_infoBody danger_infoBody);
    }

    public Call<DangerInformation_Points> getDangerPointsApiRaw(DangerInformationRequestBody dangerInformationRequest){
        Call<DangerInformation_Points> responseCallRaw = informationApiInterfaceApiInterface.getDangerPoints(dangerInformationRequest);

        return  responseCallRaw;
    }

//    public Call<Object> postDangerInformation(PostInformation postInformation){
//        return informationApiInterfaceApiInterface.postDangerInformation(postInformation);
//    }

    public Call<DangerInformation_detail> getDangetInformationDetail(Danger_infoBody danger_infoBody){
        return informationApiInterfaceApiInterface.getDangerInfo(danger_infoBody);
    }




    //위험정보 지점 요청위한 객체 (4개 좌표으로 정의된 영역)
    // ex) <<경도,위도,경도,위도,경도,위도,경도,위도>,<경도,위도,경도,위도,경도,위도,경도,위도>,<경도,위도,경도,위도,경도,위도,경도,위도>...>
    public class DangerInformationRequestBody{
        public List<List<Double>> danger_range;
    }
}
