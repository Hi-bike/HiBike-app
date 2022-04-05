package com.roundG0929.hibike.activities.map_route;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.naver.maps.geometry.LatLng;
import com.roundG0929.hibike.R;
import com.roundG0929.hibike.api.map_route.kakaoLocal.SearchPlaceApi;
import com.roundG0929.hibike.api.map_route.kakaoLocal.searchPlaceDto.Document;
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

        Intent receiveIntent = getIntent();
        int startOrend = receiveIntent.getIntExtra("startOrend",2); //0 이면 출발(start), 1 이면 도착(end)
        double nowLongitude = receiveIntent.getDoubleExtra("longitude",0);
        double nowLatitude = receiveIntent.getDoubleExtra("latitude",0);
        LatLng nowlatlng = new LatLng(nowLatitude,nowLongitude);

        //테스트토스트
        //Toast.makeText(getApplicationContext(),"x : " +nowLongitude+" / y : "+nowLatitude,Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),startOrend+" : 커서",Toast.LENGTH_SHORT).show();

        searchText = findViewById(R.id.searchText);
        button = findViewById(R.id.btn1);
        placeListRecyclerView = findViewById(R.id.placeListRecyclerView);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        placeListRecyclerView.setLayoutManager(layoutManager);
        SearchPlaceAdapter searchPlaceAdapter = new SearchPlaceAdapter();


        //리싸이클러뷰 클릭 리스너
        searchPlaceAdapter.setOnItemClickListener(new OnPlaceItemClickListener() {
            @Override
            public void onItemClick(SearchPlaceAdapter.ViewHolder holder, View view, int position) {
                Document item = searchPlaceAdapter.getItem(position);
                //Toast.makeText(getApplicationContext(),"선택된 장소 : " + item.getPlace_name(),Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(),"거리 : " + item.getDistance(),Toast.LENGTH_SHORT).show();

                Intent resultIntent = new Intent(SearchPlaceActivity.this,FindPathActivity.class);
                resultIntent.putExtra("startOrend",startOrend);
                resultIntent.putExtra("place_name",item.getPlace_name());
                resultIntent.putExtra("address_name",item.getAddress_name());
                resultIntent.putExtra("x",item.getX());
                resultIntent.putExtra("y",item.getY());

                setResult(101,resultIntent);
                finish();
            }
        });


        //검색버튼, 검색통신
        SearchPlaceApi searchPlaceApi = new SearchPlaceApi();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchPlaceAdapter.clearItem();
                placeListRecyclerView.setAdapter(searchPlaceAdapter);

                //검색창에 찾을 텍스트 스트링변환
                String searchString = searchText.getText().toString();

                if(searchText.length() != 0){
                    new SearchPlaceApi().getApi(searchString,Double.toString(nowLongitude),Double.toString(nowLatitude)).enqueue(new Callback<KakaoLocalSearch>() {
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
                }else{
                    Toast.makeText(getApplicationContext(),"검색어를 입력하세요.",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
