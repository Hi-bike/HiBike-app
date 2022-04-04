package com.roundG0929.hibike.activities.board;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
        if (bundle != null) {
            String title = bundle.getString("Title");
            String content = bundle.getString("Content");
            content = content.replaceAll("\\\"", "");
            titleTextView = (TextView) findViewById(R.id.bdTitle);
            contentsTextView = (TextView) findViewById(R.id.bdContents);
            titleTextView.setText(title);
            contentsTextView.setText(content);
        }
        btnViewReply = (Button) findViewById(R.id.btn_view_reply);
        btnViewReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ListViewReplyActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}
