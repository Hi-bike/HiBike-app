package com.roundG0929.hibike.activities.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.roundG0929.hibike.MainActivity;
import com.roundG0929.hibike.R;
import com.roundG0929.hibike.api.server.ApiInterface;
import com.roundG0929.hibike.api.server.RetrofitClient;
import com.roundG0929.hibike.api.server.dto.BasicProfile;
import com.roundG0929.hibike.api.server.dto.Signout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasicProfileActivity extends AppCompatActivity {
    ApiInterface api;
    TextView textId;
    TextView textNickname;
    ImageButton btnBack;
    Button btnSignout;
    ImageView viewImage;
    ImageButton btnSetNickname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_profile);

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        //hibike server api
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);

        textId = (TextView) findViewById(R.id.text_profile_id);
        textNickname = (TextView) findViewById(R.id.text_profile_nickname);

        //뒤로가기 버튼(메인엑티비티)
        btnBack = (ImageButton) findViewById(R.id.btn_profile_back);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        //닉네임 변경 버튼
        btnSetNickname = (ImageButton) findViewById(R.id.btn_set_nickname);
        //TODO: 변경 추가 해야됨

        String id = pref.getString("id", "");
        //로그아웃 버튼
        btnSignout = (Button) findViewById(R.id.btn_signout);
        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                api.signout(id).enqueue(new Callback<Signout>() {
                    @Override
                    public void onResponse(Call<Signout> call, Response<Signout> response) {
                        if (response.isSuccessful()) {}
                        else {
                            Toast.makeText(getApplicationContext(), "서버 에러", Toast.LENGTH_SHORT);
                        }
                    }
                    @Override
                    public void onFailure(Call<Signout> call, Throwable t) {
                        System.out.println("error "+t.toString());
                    }
                });
                editor.remove("id");
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        //서버 데이터 베이스에서 닉네임, 아이디 가져오기
        api.getProfile(id).enqueue(new Callback<BasicProfile>() {
            @Override
            public void onResponse(Call<BasicProfile> call, Response<BasicProfile> response) {
                if(response.isSuccessful()){
                    String id = response.body().getId();
                    String nickname = response.body().getNickname();
                    textId.setText(id);
                    textNickname.setText(nickname);
                }else{
                    textNickname.setText("알 수 없는 오류");
                }
            }
            @Override
            public void onFailure(Call<BasicProfile> call, Throwable t) {
                textNickname.setText("알 수 없는 오류");
            }
        });
    }
}