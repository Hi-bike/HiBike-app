package com.roundG0929.hibike.activities.riding_record;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.roundG0929.hibike.R;

public class RidingRecordActivity extends AppCompatActivity {
    TextView tv_unique_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riding_record);

        tv_unique_id = findViewById(R.id.tv_unique_id);

        Intent intent = getIntent();
        String uniqueId = intent.getStringExtra("uniqueId");

        Log.v("uniqueId", uniqueId);
        tv_unique_id.setText(uniqueId);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0,0);
    }
}