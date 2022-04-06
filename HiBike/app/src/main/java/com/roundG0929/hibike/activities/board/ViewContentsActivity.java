package com.roundG0929.hibike.activities.board;

import android.content.Intent;
import android.os.Bundle;
<<<<<<< HEAD
import android.view.MenuItem;
=======
import android.view.View;
import android.widget.Button;
>>>>>>> 15f7d44d084df8f74ea4482e55756faf7f8bfa9b
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.roundG0929.hibike.R;
import com.roundG0929.hibike.api.server.dto.SendPost;

public class ViewContentsActivity extends AppCompatActivity {
    TextView titleTextView;
    TextView contentsTextView;
    Button btnViewReply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_board);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        int post_id = 0;
        if (bundle != null) {
            String title = bundle.getString("Title");
            String content = bundle.getString("Content");
            content = content.replaceAll("\\\"", "");
            post_id = bundle.getInt("post_id");
            titleTextView = (TextView) findViewById(R.id.bdTitle);
            contentsTextView = (TextView) findViewById(R.id.bdContents);
            titleTextView.setText(title);
            contentsTextView.setText(content);
        }
<<<<<<< HEAD
        //툴바
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기
        getSupportActionBar().setDisplayShowTitleEnabled(false); //기본타이틀x

        btnViewReply = (Button) findViewById(R.id.btn_view_reply);
        int finalPost_id = post_id;
        btnViewReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ListViewReplyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Bundle bundle = new Bundle();
                bundle.putInt("post_id", finalPost_id);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
>>>>>>> 15f7d44d084df8f74ea4482e55756faf7f8bfa9b
    }
    //툴바 뒤로가기
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
