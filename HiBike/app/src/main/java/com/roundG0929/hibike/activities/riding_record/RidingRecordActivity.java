package com.roundG0929.hibike.activities.riding_record;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.roundG0929.hibike.HibikeUtils;
import com.roundG0929.hibike.R;
import com.roundG0929.hibike.api.server.ApiInterface;
import com.roundG0929.hibike.api.server.RetrofitClient;
import com.roundG0929.hibike.api.server.dto.GetRidingOne;
import com.roundG0929.hibike.api.server.fuction.ImageApi;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RidingRecordActivity extends AppCompatActivity {
    String uniqueId; // 주행 고유 번호
    ImageApi imageApi;
    ApiInterface api;

    //ui
    TextView tvRoute, tvCreateTime, tvTime, tvDistance, tvSpeed;
    ImageView ivRidingImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riding_record);

        Intent intent = getIntent();
        uniqueId = intent.getStringExtra("uniqueId");

        tvRoute = findViewById(R.id.tv_route);
        tvCreateTime = findViewById(R.id.tv_create_time);
        tvTime = findViewById(R.id.tv_time);
        tvDistance = findViewById(R.id.tv_distance);
        tvSpeed = findViewById(R.id.tv_speed);
        ivRidingImage = findViewById(R.id.iv_riding_image);

        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        api.getRidingInfoOne(uniqueId).enqueue(new Callback<GetRidingOne>() {
            @Override
            public void onResponse(Call<GetRidingOne> call, Response<GetRidingOne> response) {
                if (response.isSuccessful()) {
                    String json = HibikeUtils.objectToJson(response.body());
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        //TODO: 데이터 출력
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetRidingOne> call, Throwable t) {

            }
        });

        imageApi = new ImageApi();
        imageApi.getImage(ivRidingImage, imageApi.getRidingImageUrl(uniqueId));


    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }
}