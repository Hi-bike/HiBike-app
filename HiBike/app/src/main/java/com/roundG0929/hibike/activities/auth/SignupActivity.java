package com.roundG0929.hibike.activities.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.roundG0929.hibike.R;
import com.roundG0929.hibike.api.server.ApiInterface;
import com.roundG0929.hibike.api.server.RetrofitClient;
import com.roundG0929.hibike.api.server.dto.Signup;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    EditText etId, etNickname, etPassword, etPasswordCheck;
    Button btnSignup;
    ApiInterface api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        etId = (EditText) findViewById(R.id.edit_signup_id);
        etNickname = (EditText) findViewById(R.id.edit_signup_nickname);
        etPassword = (EditText) findViewById(R.id.edit_signup_password);
        etPasswordCheck = (EditText) findViewById(R.id.edit_signup_password_check);

        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nullCheck = nullCheck();
                if (!nullCheck.equals("")) {
                    Toast.makeText(getApplicationContext(), nullCheck, Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!etPassword.getText().toString().equals(etPasswordCheck.getText().toString())){
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Signup data = new Signup();
                data.setId(etId.getText().toString());
                data.setNickname(etNickname.getText().toString());
                data.setPassword(etPassword.getText().toString());

                api.signup(data).enqueue(new Callback<Signup>() {
                    @Override
                    public void onResponse(Call<Signup> call, Response<Signup> response) {
                        if (response.isSuccessful()) {
                            System.out.println(response.code());
                            Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), response.message(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Signup> call, Throwable t) {}
                });
            }
        });
    }
    private String nullCheck(){
        if(etId.getText().toString().equals("")){
            return "아이디를 적어주세요.";
        }
        if (etNickname.getText().toString().equals("")){
            return "닉네임을 적어주세요.";
        }
        if (etPassword.getText().toString().equals("")){
            return "비밀번호를 적어주세요.";
        }
        if (etPasswordCheck.getText().toString().equals("")){
            return "비밀번호를 확인해주세요";
        }
        return "";
    }
}