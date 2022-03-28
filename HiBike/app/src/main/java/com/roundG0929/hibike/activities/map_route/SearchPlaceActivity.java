package com.roundG0929.hibike.activities.map_route;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.roundG0929.hibike.R;

public class SearchPlaceActivity extends AppCompatActivity {
    Button button;
    EditText editText;
    TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchplace);

        button.findViewById(R.id.button);
        editText.findViewById(R.id.editText);
        textView.findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
