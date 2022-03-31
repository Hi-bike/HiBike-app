package com.roundG0929.hibike.activities.map_route;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.roundG0929.hibike.R;
import com.roundG0929.hibike.api.map_route.graphhopperRoute.SearchPlaceApi;

import java.security.MessageDigest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPlaceActivity extends AppCompatActivity {

    Button button;
    TextView apiText;
    EditText editText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchplace);

        button = findViewById(R.id.btn1);
        apiText = findViewById(R.id.apiView);
        editText = findViewById(R.id.apiViewEdit);

        apiText.setText("apiTest\n");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SearchPlaceApi().getApi().enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {
                        editText.append("\n");
                        editText.append(response.body().toString()+"\n");
                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {
                        apiText.append(t.toString()+"\n");
                    }
                });
            }
        });
    }
}
