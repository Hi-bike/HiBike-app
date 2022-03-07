package com.roundG0929.hibike.activities.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
                //TODO: 회원가입 가능 여부 체크
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
                            Toast.makeText(getApplicationContext(), "서버 에러", Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Signup> call, Throwable t) {}
                });
            }
        });
        //내용이 있을 때만 버튼 색 활성화
        btnSignup.setClickable(false);
        btnSignup.setBackgroundColor(Color.parseColor("#DDDDDD"));

        etId.addTextChangedListener(textWatcher);
        etNickname.addTextChangedListener(textWatcher);
        etPassword.addTextChangedListener(textWatcher);
        etPasswordCheck.addTextChangedListener(textWatcher);

        //툴바
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기
        getSupportActionBar().setDisplayShowTitleEnabled(false); //기본타이틀x
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
    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            if(editable.length() > 0) {
                btnSignup.setClickable(true);
                btnSignup.setBackgroundColor(Color.parseColor("#6CD1FF"));
            }

        }
    };

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}