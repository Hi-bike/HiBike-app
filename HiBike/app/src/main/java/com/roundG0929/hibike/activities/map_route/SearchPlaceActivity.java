package com.roundG0929.hibike.activities.map_route;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.roundG0929.hibike.R;
import com.roundG0929.hibike.api.map_route.kakaoLocal.SearchPlaceApi;
import com.roundG0929.hibike.api.map_route.kakaoLocal.searchPlaceDto.KakaoLocalSearch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPlaceActivity extends AppCompatActivity {

    Button button;
    TextView apiText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchplace);

        button = findViewById(R.id.btn1);
        apiText.setText("apiTest\n");


        SearchPlaceApi searchPlaceApi = new SearchPlaceApi();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
    }
}
