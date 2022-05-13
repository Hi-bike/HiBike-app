package com.roundG0929.hibike.activities.board;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.roundG0929.hibike.R;
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
import com.roundG0929.hibike.api.server.dto.SendPost;
import com.roundG0929.hibike.api.server.dto.Signin;
import com.roundG0929.hibike.api.server.ApiInterface;
import com.roundG0929.hibike.R;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WritePostActivity extends AppCompatActivity {
    ApiInterface api;
    TextView btnSend;
    TextView cancel;
    EditText editTitle, editContents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendpost);
        //유저 아이디 저장
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        String id = pref.getString("id", "");

        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        editTitle = (EditText) findViewById(R.id.edit_title);
        editContents = (EditText) findViewById(R.id.edit_contents);

        cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnSend = findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SendPost data = new SendPost();
                data.setTitle(editTitle.getText().toString());
                data.setContents(editContents.getText().toString());
                data.setId(id);

                api.sendPost(data).enqueue(new Callback<SendPost>() {
                    @Override
                    public void onResponse(Call<SendPost> call, Response<SendPost> response) {
                        if (response.isSuccessful()) {
                            Intent intent = new Intent(getApplicationContext(), ListViewActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            Toast toast = Toast.makeText(getApplicationContext(), "실패 (" + response.message() + ")", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.TOP, 0, 0);
                            toast.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<SendPost> call, Throwable t) {
                        //통신 실패
                        Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
