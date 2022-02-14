package com.roundG0929.hibike.activities.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.roundG0929.hibike.R;
import com.roundG0929.hibike.api.server.RetrofitClient;
import com.roundG0929.hibike.api.server.dto.Signin;
import com.roundG0929.hibike.api.server.ApiInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigninActivity extends AppCompatActivity {

    ApiInterface api;
    Button signinBtn;
    EditText editId;
    EditText editPwd;
    TextView signupText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        editId = (EditText) findViewById(R.id.editId);
        editPwd = (EditText) findViewById(R.id.editPassword);
        signupText = (TextView) findViewById(R.id.signup);

        signinBtn = (Button) findViewById(R.id.signinBtn);

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Signin data = new Signin();
                data.setId(editId.getText().toString());
                data.setPassword(editPwd.getText().toString());

                api.signin(data).enqueue(new Callback<Signin>() {
                    @Override
                    public void onResponse(Call<Signin> call, Response<Signin> response) {
                        if(response.isSuccessful()){
                            signupText.setText("success");
                        }else{
                            signupText.setText(response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<Signin> call, Throwable t) {
                        //통신 실패
                        signupText.setText(t.toString());
                    }
                });
            }
        });
    }
}