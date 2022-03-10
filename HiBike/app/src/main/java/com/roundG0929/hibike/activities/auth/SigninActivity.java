package com.roundG0929.hibike.activities.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.roundG0929.hibike.MainActivity;
import com.roundG0929.hibike.api.server.RetrofitClient;
import com.roundG0929.hibike.api.server.dto.Signin;
import com.roundG0929.hibike.api.server.ApiInterface;
import com.roundG0929.hibike.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.google.firebase.iid.FirebaseInstanceId; //파이어베이스 토큰 관리

public class SigninActivity extends AppCompatActivity {
    ApiInterface api;
    Button btnSignin;
    EditText editId, editPwd;
    TextView textSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String fcm_token = FirebaseInstanceId.getInstance().getToken(); //파이어베이스 토큰 get
        setContentView(R.layout.activity_signin);

        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        editId = (EditText) findViewById(R.id.edit_id);
        editPwd = (EditText) findViewById(R.id.edit_password);

        //유저 아이디 저장
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        String id = pref.getString("id", "");

        btnSignin = (Button) findViewById(R.id.btn_signin);
        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Signin data = new Signin();
                data.setId(editId.getText().toString());
                data.setPassword(editPwd.getText().toString());
                data.setFcmToken(fcm_token);

                api.signin(data).enqueue(new Callback<Signin>() {
                    @Override
                    public void onResponse(Call<Signin> call, Response<Signin> response) {
                        if (response.isSuccessful()) {
                            editor.putString("id", editId.getText().toString());
                            editor.apply();

                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "로그인 정보가 일치하지 않습니다. (" + response.message() + ")", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.TOP, 0, 0);
                            toast.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Signin> call, Throwable t) {
                        //통신 실패
                        Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        textSignup = (TextView) findViewById(R.id.text_signup);
        textSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}