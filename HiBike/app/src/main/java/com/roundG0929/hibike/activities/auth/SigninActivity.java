package com.roundG0929.hibike.activities.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.roundG0929.hibike.MainActivity;
import com.roundG0929.hibike.R;
import com.roundG0929.hibike.api.server.RetrofitClient;
import com.roundG0929.hibike.api.server.dto.SigninDto;
import com.roundG0929.hibike.api.server.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigninActivity extends AppCompatActivity {

    ApiInterface api;
    Button btn_signin;
    EditText editId;
    EditText editPwd;
    TextView textSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        editId = (EditText) findViewById(R.id.edit_id);
        editPwd = (EditText) findViewById(R.id.edit_password);
        textSignup = (TextView) findViewById(R.id.text_signup);

        btn_signin = (Button) findViewById(R.id.btn_signin);

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SigninDto data = new SigninDto();
                data.setId(editId.getText().toString());
                data.setPassword(editPwd.getText().toString());

                api.signin(data).enqueue(new Callback<SigninDto>() {
                    @Override
                    public void onResponse(Call<SigninDto> call, Response<SigninDto> response) {
                        if (response.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "로그인 정보가 일치하지 않습니다. (" + response.message() + ")", Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.TOP|Gravity.LEFT, 200, 200);
                            toast.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SigninDto> call, Throwable t) {
                        //통신 실패
                        Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}