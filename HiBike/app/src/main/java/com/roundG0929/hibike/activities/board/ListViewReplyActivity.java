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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import com.roundG0929.hibike.api.server.RetrofitClient;
import com.roundG0929.hibike.api.server.dto.GetPostContent;
import com.roundG0929.hibike.api.server.dto.GetReply;
import com.roundG0929.hibike.api.server.ApiInterface;
import com.roundG0929.hibike.api.server.dto.GetReplyContent;
import com.roundG0929.hibike.api.server.dto.SendPost;
import com.roundG0929.hibike.api.server.dto.SendReply;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListViewReplyActivity extends AppCompatActivity {
    Button btnReply;
    EditText editReply;
    ApiInterface api;
    private ListView listview ;
    private ListViewReplyAdapter adapter;
    int page = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply_list_view);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int post_id = 0;
        if (bundle != null) {
            post_id = bundle.getInt("post_id");
        }
        adapter = new ListViewReplyAdapter();
        getItem(page, post_id);

        //유저 아이디 저장
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        String id = pref.getString("id", "");

        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        editReply = (EditText) findViewById(R.id.edit_reply);
        btnReply = (Button) findViewById(R.id.btn_reply);

        int finalPost_id = post_id;
        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendReply data = new SendReply();
                data.setPost_id(finalPost_id);
                data.setContents(editReply.getText().toString());
                data.setId(id);

                api.sendReply(data).enqueue(new Callback<SendReply>() {
                    @Override
                    public void onResponse(Call<SendReply> call, Response<SendReply> response) {
                        if (response.isSuccessful()) {
//                            Intent intent = new Intent(getApplicationContext(), ListViewReplyActivity.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
                            finish();//인텐트 종료
                            overridePendingTransition(0, 0);//인텐트 효과 없애기
                            Intent intent = getIntent(); //인텐트
                            startActivity(intent); //액티비티 열기
                            overridePendingTransition(0, 0);//인텐트 효과 없애기

                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "실패 (" + response.message() + ")", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.TOP, 0, 0);
                            toast.show();
                        }
                    }
                    @Override
                    public void onFailure(Call<SendReply> call, Throwable t) {
                        //통신 실패
                        Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void getItem(int page, int post_id){
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        GetReply data = new GetReply();
        GetReplyContent data2 = new GetReplyContent();
        data.setPost_id(post_id);
        api.getReply(page, post_id).enqueue(new Callback<GetReply>() {
            @Override
            public void onResponse(Call<GetReply> call, Response<GetReply> response) {
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

                            int reply_id = Integer.parseInt(str_id);
                            api.getReplyContent(reply_id).enqueue(new Callback<GetReplyContent>() {
                                @Override
                                public void onResponse(Call<GetReplyContent> call, Response<GetReplyContent> response) {
                                    if (response.isSuccessful()) {
                                        try {
                                            String content = response.body().getContents().toString();
                                            String nickname = response.body().getNickname().toString();

                                            // 리스트뷰 객체 생성 및 Adapter 설정
                                            listview = (ListView) findViewById(R.id.list_view);
                                            listview.setAdapter(adapter);
                                            // 리스트 뷰 아이템 추가.
                                            adapter.addItem(R.drawable.icons_user2,content,nickname);

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
                                public void onFailure(Call<GetReplyContent> call, Throwable t) {
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
            public void onFailure(Call<GetReply> call, Throwable t) {
                //통신 실패
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }
}