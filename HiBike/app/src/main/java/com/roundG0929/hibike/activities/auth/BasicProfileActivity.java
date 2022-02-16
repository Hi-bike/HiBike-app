package com.roundG0929.hibike.activities.auth;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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

public class BasicProfileActivity extends Activity {

    ApiInterface api;
    TextView textId, btnSignout;
    EditText editNickname;
    ImageButton btnClose;
    Button btnOk;
    ImageView viewImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_basic_profile);

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String id = pref.getString("id", "");

        //hibike server api
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        //닉네임, 프로필 이미지, 아이디 받아오기
        getProfile(id);

        textId = (TextView) findViewById(R.id.text_profile_id);
        editNickname = (EditText) findViewById(R.id.edit_profile_nickname);

        //닫기
        btnClose = (ImageButton)findViewById(R.id.btn_profile_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

        //프로필이미지 변경
        viewImage = (ImageView) findViewById(R.id.btn_profile_image);
        viewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO:프로필 이미지 변경
            }
        });

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BasicProfile nicknameProfile = new BasicProfile();
                nicknameProfile.setId(id);
                nicknameProfile.setNickname(editNickname.getText().toString());

                api.setNickname(nicknameProfile).enqueue(new Callback<BasicProfile>() {
                    @Override
                    public void onResponse(Call<BasicProfile> call, Response<BasicProfile> response) {

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<BasicProfile> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), "서버 에러", Toast.LENGTH_SHORT);
                    }
                });
            }
        });

        //로그아웃 버튼
        btnSignout = (TextView) findViewById(R.id.btn_signout);
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

    }
    public void getProfile(String id){
        api.getProfile(id).enqueue(new Callback<BasicProfile>() {
            @Override
            public void onResponse(Call<BasicProfile> call, Response<BasicProfile> response) {
                if(response.isSuccessful()){
                    String id = response.body().getId();
                    String nickname = response.body().getNickname();
                    textId.setText(id);
                    editNickname.setText(nickname);
                }else{
                    editNickname.setText("알 수 없는 오류");
                }
            }
            @Override
            public void onFailure(Call<BasicProfile> call, Throwable t) {
                editNickname.setText("알 수 없는 오류");
            }
        });
    }
}