package com.roundG0929.hibike.activities.board;

import androidx.appcompat.app.AppCompatActivity;

import com.roundG0929.hibike.MainActivity;
import com.roundG0929.hibike.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ListView;
import android.widget.Toast;

import com.roundG0929.hibike.api.server.RetrofitClient;
import com.roundG0929.hibike.api.server.dto.GetPost;
import com.roundG0929.hibike.api.server.ApiInterface;
import com.roundG0929.hibike.api.server.dto.Signin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListViewActivity extends AppCompatActivity {
    ApiInterface api;
    private ListView listview ;
    private ListViewAdapter adapter;
    int page = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        adapter = new ListViewAdapter();
        GetPost data = new GetPost();
        data.setPage(page);

        api.getPost(1).enqueue(new Callback<GetPost>() {
            @Override
            public void onResponse(Call<GetPost> call, Response<GetPost> response) {
                if (response.isSuccessful()) {
                    System.out.println(response.body().getResult());
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "실패 (" + response.message() + ")", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                }
            }

            @Override
            public void onFailure(Call<GetPost> call, Throwable t) {
                //통신 실패
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });

        // 리스트뷰 객체 생성 및 Adapter 설정
        listview = (ListView) findViewById(R.id.list_view);
        listview.setAdapter(adapter);
        adapter.addItem(R.drawable.icons_profile,"테스트1","관리자1");
        adapter.addItem(R.drawable.icons_profile,"테스트2","관리자2");
        adapter.addItem(R.drawable.icons_profile,"테스트3","관리자3");
        adapter.addItem(R.drawable.icons_profile,"테스트4","관리자4");
        adapter.addItem(R.drawable.icons_profile,"테스트5","관리자5");
        // 리스트 뷰 아이템 추가.

    }
}