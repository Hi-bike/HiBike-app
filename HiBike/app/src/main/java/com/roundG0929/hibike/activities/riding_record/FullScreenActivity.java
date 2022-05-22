package com.roundG0929.hibike.activities.riding_record;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.roundG0929.hibike.R;
import com.roundG0929.hibike.api.server.fuction.ImageApi;

public class FullScreenActivity extends AppCompatActivity {
    ImageView fullScreen;
    ImageView fullScreenFinish;
    ImageApi imageApi;
    String uniqueId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        Intent intent = getIntent();
        uniqueId = intent.getStringExtra("uniqueId");

        fullScreen = findViewById(R.id.fullscreen);

        imageApi = new ImageApi();
        imageApi.setImageOnImageView(this, fullScreen, imageApi.getRidingImageUrl(uniqueId));
        fullScreenFinish = findViewById(R.id.fullscreenFinish);
        fullScreenFinish.bringToFront();
        fullScreenFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }


}