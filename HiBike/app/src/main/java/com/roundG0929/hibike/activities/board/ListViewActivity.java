package com.roundG0929.hibike.activities.board;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.roundG0929.hibike.R;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Button;

import com.roundG0929.hibike.api.server.RetrofitClient;
import com.roundG0929.hibike.api.server.dto.GetPost;
import com.roundG0929.hibike.api.server.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListViewActivity extends AppCompatActivity {
    Button btnNext;
    ApiInterface api;
    private ListView listview ;
    private ListViewAdapter adapter;
    int page = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        adapter = new ListViewAdapter();

        btnNext = (Button) findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page += 1;
                getItem(page);
            }
        });
        getItem(page);
    }
    public void getItem(int page){
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        GetPost data = new GetPost();
        data.setPage(page);
        api.getPost(page).enqueue(new Callback<GetPost>() {
            @Override
            public void onResponse(Call<GetPost> call, Response<GetPost> response) {
                if (response.isSuccessful()) {
                    try{
                        for (int i=1; i<=5; i++){
                            String res = response.body().getResult().toString();
                            System.out.println(res);
                            JsonParser jsonParser = new JsonParser();
                            JsonObject jsonObject = (JsonObject) jsonParser.parse(res);
                            JsonObject dataObject = (JsonObject) jsonObject.get(Integer.toString(i));

                            JsonElement nickname = dataObject.get("nickname");
                            JsonElement title = dataObject.get("title");
                            JsonElement content = dataObject.get("contents");

                            // 리스트뷰 객체 생성 및 Adapter 설정
                            listview = (ListView) findViewById(R.id.list_view);
                            listview.setAdapter(adapter);
                            // 리스트 뷰 아이템 추가.
                            adapter.addItem(R.drawable.icons_profile,content.toString(),title.toString().replaceAll("\\\"",""),nickname.toString().replaceAll("\\\"",""));
                        }
                    }
                    catch (Exception e){
                        Toast toast = Toast.makeText(getApplicationContext(), "마지막 페이지", Toast.LENGTH_LONG);
                        toast.show();
                    }

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
    }
}