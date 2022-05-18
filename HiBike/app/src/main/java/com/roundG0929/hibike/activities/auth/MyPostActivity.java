package com.roundG0929.hibike.activities.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.roundG0929.hibike.R;

import java.util.Arrays;
import java.util.List;

/*
* tab 에 fragment 올리는 방식
* */

public class MyPostActivity extends AppCompatActivity {
    private ViewPager2 viewPager; //xml viewpager2
    private ViewPagerAdapter pagerAdapter; //viewpager2 adapter
    private MyDangerFragment myDangerFragment; //위험요소 fragment(fragment1)
    private MyBoardFragment myBoardFragment; // 게시물 fragment(fragment2)
    private TabLayout tabLayout; //xml tabLayout
    ImageView myPostBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_post);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager_container);

        myDangerFragment = MyDangerFragment.newInstance(0);
        myBoardFragment = MyBoardFragment.newInstance(1);

        pagerAdapter = new ViewPagerAdapter(this);
        pagerAdapter.addFragment(myDangerFragment); // adapter 에 fragment1 추가
        pagerAdapter.addFragment(myBoardFragment); // adapter 에 fragment2 추가

        viewPager.setAdapter(pagerAdapter); //viewPager 에 adapter 세팅

        final List<String> tabElement = Arrays.asList("위험요소", "게시물"); // tap에 들어갈 문자 설정

        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                TextView textView = new TextView(MyPostActivity.this);
                textView.setText(tabElement.get(position));
                textView.setTextColor(Color.BLACK);
                textView.setGravity(Gravity.CENTER);
                tab.setCustomView(textView);
            }
        }).attach();

        myPostBack = findViewById(R.id.myPostBack);
        myPostBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}