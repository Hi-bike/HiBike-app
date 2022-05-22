package com.roundG0929.hibike.activities.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Api;
import com.roundG0929.hibike.R;
import com.roundG0929.hibike.api.server.ApiInterface;
import com.roundG0929.hibike.api.server.RetrofitClient;
import com.roundG0929.hibike.api.server.dto.GetDangerOne;
import com.roundG0929.hibike.api.server.fuction.ImageApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyDangerOne extends AppCompatActivity {
    int dangerId;
    ApiInterface api;
    ImageApi imageApi;

    ImageView myDangerBack; //뒤로가기
    ImageView myDangerOneImage; //이미지 이름

    TextView myDangerOneTitle; //제목
    TextView myDangerOneRegion; //지역
    TextView myDangerOneTime; //시간
    TextView myDangerOneContent; //내용

    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_danger_one);

        activity = this;

        Intent intent = getIntent();
        dangerId = intent.getIntExtra("dangerId",0);

        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        imageApi = new ImageApi();

        myDangerOneTitle = findViewById(R.id.myDangerOneTitle);
        myDangerOneRegion = findViewById(R.id.myDangerOneRegion);
        myDangerOneTime = findViewById(R.id.myDangerOneTime);
        myDangerOneContent = findViewById(R.id.myDangerOneContent);
        myDangerOneImage = findViewById(R.id.myDangerOneImage);
        myDangerBack = findViewById(R.id.myDangerBack);

        api.getDangerOne(dangerId).enqueue(new Callback<GetDangerOne>() {
            @Override
            public void onResponse(Call<GetDangerOne> call, Response<GetDangerOne> response) {
                GetDangerOne.Result result = response.body().getResult();

                if (response.isSuccessful()) {
                    myDangerOneTitle.setText(result.getTitle());
                    myDangerOneRegion.setText(result.getRegion() + " " + result.getRegionDetail());

                    String[] times = result.getTime().split(" ");
                    myDangerOneTime.setText(times[2] + " " + times[1] + " " + times[0]);


                    if (!result.getContents().equals("")) {
                        myDangerOneContent.setText(result.getContents());
                    }

                    imageApi.setImageOnImageView(activity, myDangerOneImage, imageApi.getDangerImageUrl(result.getImage()));
                } else {
                    Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<GetDangerOne> call, Throwable t) {
                Log.e("getDangerOne", t.toString());
            }
        });

        myDangerBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}