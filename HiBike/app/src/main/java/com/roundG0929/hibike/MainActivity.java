package com.roundG0929.hibike;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.roundG0929.hibike.activities.auth.SigninActivity;

public class MainActivity extends AppCompatActivity {
//    Fragment profileFragment;
//    Fragment communityFragment;
//    Fragment aroundFragment;
//    Fragment favoritesFragment;
//    Fragment gpsFragment;
    Button mainSigninBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        profileFragment = new Fragment();
//        communityFragment = new Fragment();
//        aroundFragment = new Fragment();
//        favoritesFragment = new Fragment();
//        gpsFragment = new Fragment();
        mainSigninBtn = (Button) findViewById(R.id.mainSigninBtn);
        mainSigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(intent);
            }
        });

//        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom);
//        bottomNavigationView.setOnNavigationItemSelectedListener(
//                new BottomNavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                        switch (item.getItemId()){
//                            case R.id.menu_around:
//                                getSupportFragmentManager().beginTransaction().replace(R.id.container, aroundFragment).commit();
//                                return true;
//
//                            case R.id.menu_community:
//                                getSupportFragmentManager().beginTransaction().replace(R.id.container, communityFragment).commit();
//                                return true;
//
//                            case R.id.menu_favorites:
//                                getSupportFragmentManager().beginTransaction().replace(R.id.container, favoritesFragment).commit();
//                                return true;
//
//                            case R.id.menu_gps:
//                                getSupportFragmentManager().beginTransaction().replace(R.id.container, gpsFragment).commit();
//                                return true;
//
//                            case R.id.menu_profile:
//                                getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
//                                return true;
//                        }
//                        return false;
//                    }});

    }
}