package com.roundG0929.hibike.activities.map_route;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.roundG0929.hibike.R;
import com.roundG0929.hibike.api.map_route.kakaoLocal.SearchPlaceApi;
import com.roundG0929.hibike.api.map_route.kakaoLocal.searchPlaceDto.KakaoLocalSearch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPlaceActivity extends AppCompatActivity {

    EditText searchText;
    Button button;
    RecyclerView placeListRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchplace);

        searchText = findViewById(R.id.searchText);
        button = findViewById(R.id.btn1);
        placeListRecyclerView = findViewById(R.id.placeListRecyclerView);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        placeListRecyclerView.setLayoutManager(layoutManager);
        SearchPlaceAdapter searchPlaceAdapter = new SearchPlaceAdapter();


        SearchPlaceApi searchPlaceApi = new SearchPlaceApi();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchPlaceAdapter.clearItem();
                placeListRecyclerView.setAdapter(searchPlaceAdapter);

                //검색창에 찾을 텍스트 스트링변환
                String searchString = searchText.getText().toString();

                if(searchString != null){
                    new SearchPlaceApi(searchString).getApi().enqueue(new Callback<KakaoLocalSearch>() {
                        @Override
                        public void onResponse(Call<KakaoLocalSearch> call, Response<KakaoLocalSearch> response) {
                            KakaoLocalSearch kakaoLocalSearch = response.body();
                            for (int i = 0; kakaoLocalSearch.getDocuments().size() > i; i++) {
                                searchPlaceAdapter.addItem(kakaoLocalSearch.getDocuments().get(i));
                            }
                            placeListRecyclerView.setAdapter(searchPlaceAdapter);
                        }

                        @Override
                        public void onFailure(Call<KakaoLocalSearch> call, Throwable t) {

                        }
                    });
                }

            }
        });
    }
}
