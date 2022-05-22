package com.roundG0929.hibike.activities.riding_record;


import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
    ImageView ivRidingBack;

    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riding_record);

        activity = this;

        Intent intent = getIntent();
        uniqueId = intent.getStringExtra("uniqueId");

        tvRoute = findViewById(R.id.tv_route);
        tvCreateTime = findViewById(R.id.tv_create_time);
        tvTime = findViewById(R.id.tv_time);
        tvDistance = findViewById(R.id.tv_distance);
        tvSpeed = findViewById(R.id.tv_speed);
        ivRidingImage = findViewById(R.id.iv_riding_image);
        ivRidingBack = findViewById(R.id.iv_riding_back);

        ivRidingBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imageApi = new ImageApi();

        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        api.getRidingInfoOne(uniqueId).enqueue(new Callback<GetRidingOne>() {
            @Override
            public void onResponse(Call<GetRidingOne> call, Response<GetRidingOne> response) {
                if (response.isSuccessful()) {

                    String json = HibikeUtils.objectToJson(response.body().getResult());
                    try {
                        JSONObject jsonObject = new JSONObject(json);
                        Log.d("json", json);
                        //TODO: 데이터 출력
                        String createTime = jsonObject.getString("create_time");
                        String distance = jsonObject.getString("distance");
                        String aveSpeed = jsonObject.getString("ave_speed");
                        String ridingTime = jsonObject.getString("riding_time");
                        String startingRegion = jsonObject.getString("starting_region");
                        String endRegion = jsonObject.getString("end_region");

                        String[] splitedTime = createTime.split(" ");
                        createTime = splitedTime[0] + " " + splitedTime[1] + " " + splitedTime[2] + " " + splitedTime[3];

                        String[] splitedRidingTime = ridingTime.split(" : ");
                        ridingTime = splitedRidingTime[0] + "분 " + splitedRidingTime[1]+"초 ";

                        tvRoute.setText(startingRegion + " > " + endRegion);
                        tvCreateTime.setText(createTime);
                        tvTime.setText(tvTime.getText() +"    "+ ridingTime);
                        tvSpeed.setText(tvSpeed.getText() +"    "+ aveSpeed+" km/h");
                        tvDistance.setText(tvDistance.getText() +"    "+ distance+" m");

                        imageApi.setImageOnImageView(activity, ivRidingImage, imageApi.getRidingImageUrl(uniqueId));
                    } catch (JSONException e) {
                        Log.e("eeee", e.toString());
                    }
                } else {
                    Log.e("error", response.message());
                }
            }

            @Override
            public void onFailure(Call<GetRidingOne> call, Throwable t) {
                Log.e("tttt", t.toString());
            }
        });

        ivRidingImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FullScreenActivity.class);
                intent.putExtra("uniqueId", uniqueId);
                intent.addFlags (Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
            }
        });

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }
}