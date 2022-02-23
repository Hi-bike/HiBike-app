package com.roundG0929.hibike.activities.board;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
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

import org.json.JSONException;
import org.json.JSONObject;

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

        api.getPost(page).enqueue(new Callback<GetPost>() {
            @Override
            public void onResponse(Call<GetPost> call, Response<GetPost> response) {
                if (response.isSuccessful()) {
                    try{
                        for (int i=1; i<=5; i++){
                            String res = response.body().getResult().toString();
                            JsonParser jsonParser = new JsonParser();

                            JsonObject jsonObject = (JsonObject) jsonParser.parse(res);
                            JsonObject dataObject = (JsonObject) jsonObject.get(Integer.toString(i));

                            JsonElement nickname = dataObject.get("nickname");
                            JsonElement title = dataObject.get("title");
                            // 리스트뷰 객체 생성 및 Adapter 설정
                            listview = (ListView) findViewById(R.id.list_view);
                            listview.setAdapter(adapter);
                            // 리스트 뷰 아이템 추가.
                            adapter.addItem(R.drawable.icons_profile,title.toString().replaceAll("\\\"",""),nickname.toString().replaceAll("\\\"",""));
                        }
                    }
                    catch (Exception e){
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