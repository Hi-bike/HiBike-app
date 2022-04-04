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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Button;

import com.roundG0929.hibike.api.server.RetrofitClient;
import com.roundG0929.hibike.api.server.dto.GetReply;
import com.roundG0929.hibike.api.server.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListViewReplyActivity extends AppCompatActivity {
    Button btnSend;
    EditText editReply;
    ApiInterface api;
    private ListView listview ;
    private ListViewReplyAdapter adapter;
    int page = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_list_view);

        editReply = (EditText) findViewById(R.id.edit_reply);
        adapter = new ListViewReplyAdapter();
        getItem(page);
    }
    public void getItem(int page){
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        GetReply data = new GetReply();
        data.setPage(page);
        data.setPost_id(1);
        api.getReply(page,1).enqueue(new Callback<GetReply>() {
            @Override
            public void onResponse(Call<GetReply> call, Response<GetReply> response) {
                if (response.isSuccessful()) {
                    try{
                        for (int i=1; i<=5; i++){
                            String res = response.body().getResult().toString();
                            System.out.println(res);
                            JsonParser jsonParser = new JsonParser();
                            JsonObject jsonObject = (JsonObject) jsonParser.parse(res.replaceAll("\\s",""));
                            JsonObject dataObject = (JsonObject) jsonObject.get(Integer.toString(i));

                            JsonElement nickname = dataObject.get("nickname");
                            JsonElement content = dataObject.get("contents");

                            // 리스트뷰 객체 생성 및 Adapter 설정
                            listview = (ListView) findViewById(R.id.list_view);
                            listview.setAdapter(adapter);
                            // 리스트 뷰 아이템 추가.
                            System.out.println(nickname);
                            adapter.addItem(R.drawable.icons_user2,content.toString().replaceAll("\\\"",""),nickname.toString().replaceAll("\\\"",""));
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
            public void onFailure(Call<GetReply> call, Throwable t) {
                //통신 실패
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}