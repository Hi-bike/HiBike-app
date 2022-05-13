package com.roundG0929.hibike.activities.board;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.roundG0929.hibike.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Button;

import com.roundG0929.hibike.activities.auth.BasicProfileActivity;
import com.roundG0929.hibike.api.server.RetrofitClient;
import com.roundG0929.hibike.api.server.dto.BasicProfile;
import com.roundG0929.hibike.api.server.dto.GetPost;
import com.roundG0929.hibike.api.server.ApiInterface;
import com.roundG0929.hibike.api.server.dto.GetPostContent;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListViewActivity extends AppCompatActivity {
    ImageView btnNext;
    ImageView btnPost;
    ApiInterface api;
    ImageView boardBack;
    private ListView listview ;
    private ListViewAdapter adapter;
    int page = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        adapter = new ListViewAdapter();

        btnPost = findViewById(R.id.btn_post);
        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WritePostActivity.class);
                startActivity(intent);
            }
        });

        btnNext = findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page += 1;
                getItem(page);
            }
        });
        getItem(page);

        boardBack = findViewById(R.id.boardBack);
        boardBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void getItem(int page){
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        GetPost data = new GetPost();
        GetPostContent data2 = new GetPostContent();
        data.setPage(page);
        api.getPost(page).enqueue(new Callback<GetPost>() {
            @Override
            public void onResponse(Call<GetPost> call, Response<GetPost> response) {
                if (response.isSuccessful()) {
                    try{
                        for (int i=1; i<=5; i++){
                            String res = response.body().getResult().toString();
                            //System.out.println(res);
                            JsonParser jsonParser = new JsonParser();
                            JsonObject jsonObject = (JsonObject) jsonParser.parse(res.replaceAll("\\s",""));
                            JsonObject dataObject = (JsonObject) jsonObject.get(Integer.toString(i));
                            JsonElement id = dataObject.get("id");
                            String str_id = id.toString();
                            str_id = str_id.replaceAll(".0","");

                            int post_id = Integer.parseInt(str_id);
                            data2.setPost_id(post_id);
                            api.getPostContent(post_id).enqueue(new Callback<GetPostContent>() {
                                @Override
                                public void onResponse(Call<GetPostContent> call, Response<GetPostContent> response) {
                                    if (response.isSuccessful()) {
                                        try {
                                            String title = response.body().getTitle().toString();
                                            String content = response.body().getContents().toString();
                                            String nickname = response.body().getNickname().toString();

                                            // 리스트뷰 객체 생성 및 Adapter 설정
                                            listview = (ListView) findViewById(R.id.list_view);
                                            listview.setAdapter(adapter);
                                            // 리스트 뷰 아이템 추가.
                                            adapter.addItem(R.drawable.icons_user2,content,title,nickname,post_id);

                                        } catch (Exception e) {
                                            Toast toast = Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG);
                                            toast.show();
                                        }
                                    }
                                    else {
                                        Toast toast = Toast.makeText(getApplicationContext(), "실패 (" + response.message() + ")", Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.TOP, 0, 0);
                                        toast.show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<GetPostContent> call, Throwable t) {
                                    //통신 실패
                                    Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                                }
                            });
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