package com.roundG0929.hibike.activities.board;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.roundG0929.hibike.R;

public class ViewContentsActivity extends AppCompatActivity {
    TextView titleTextView;
    TextView contentsTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_board);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if( bundle != null){
            String title = bundle.getString("Title");
            String content = bundle.getString("Content");
            content = content.replaceAll("\\\"","");
            titleTextView = (TextView) findViewById(R.id.bdTitle);
            contentsTextView = (TextView) findViewById(R.id.bdContents);
            titleTextView.setText(title);
            contentsTextView.setText(content);
        }
    }
}
