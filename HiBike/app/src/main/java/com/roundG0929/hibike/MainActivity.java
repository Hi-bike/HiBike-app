package com.roundG0929.hibike;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.util.FusedLocationSource;
import com.roundG0929.hibike.activities.auth.BasicProfileActivity;
import com.roundG0929.hibike.activities.auth.SigninActivity;
import com.roundG0929.hibike.activities.map_route.FindPathActivity;
import com.roundG0929.hibike.activities.map_route.RidingActivity;
import com.roundG0929.hibike.activities.riding_record.RidingRecordListActivity;
import com.roundG0929.hibike.api.map_route.graphhopperRoute.map_routeDto.GraphhopperResponse;
import com.roundG0929.hibike.api.map_route.navermap.FirstNaverMapSet;
import com.roundG0929.hibike.api.map_route.navermap.MapSetting;
import com.roundG0929.hibike.api.server.ApiInterface;
import com.roundG0929.hibike.api.server.RetrofitClient;
import com.roundG0929.hibike.api.server.dto.BasicProfile;
import com.roundG0929.hibike.api.server.fuction.ImageApi;
import com.roundG0929.hibike.activities.board.ListViewActivity;
import com.roundG0929.hibike.api.weather.WeatherApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity{
    //hibike server api
    ApiInterface api;
    //Navigation drawer 여는 버튼
    CardView btn_open;
    //Navigation 안에 있는 버튼들

    TextView  btnRidingRecord;//로그인 버튼
    TextView btnSigninOrNickname; // 주행기록, 로그인, 프로필변경
    CardView btnPosts;//게시판
    ImageView ivProfileImage;
    String id;
    ImageApi imageApi;



    //다른 activity에서 main component 접근에 이용용
    public static Context context_main;


    //Navigation drawer
    private DrawerLayout drawerLayout;
    private View drawerView;

    //뒤로가기 두번 누를시, 앱 종료
    private long backKeyPressedTime = 0;
    Toast toast;
    boolean isDrawerOpened;


    //권한 요청 후 승인, 거부 리스너
    PermissionListener permissionlistener = new PermissionListener() {
        @Override
        public void onPermissionGranted() {
        }

        @Override
        public void onPermissionDenied(List<String> deniedPermissions) {
            Toast.makeText(MainActivity.this, "권한을 허용하지 않으면 이용할 수 없습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_2);
        context_main = this;

        getWindow().setNavigationBarColor(Color.WHITE);//네이게이션바 투명

        //로그인 성공시, 유저 아이디 핸드폰에 저장
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        id = pref.getString("id", "");

        api = RetrofitClient.getRetrofit().create(ApiInterface.class);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerView = (View) findViewById(R.id.drawer);

        //Navigation drawer 여는 버튼
        btn_open = findViewById(R.id.btn_open);
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(drawerView);
                isDrawerOpened = true;
            }
        });

        // drawer 리스너
        drawerLayout.setDrawerListener(listener);
        drawerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        btnSigninOrNickname = (TextView) findViewById(R.id.btn_signin_or_nickname);

        if(id == ""){
            btnSigninOrNickname.setText("로그인");
            btnSigninOrNickname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
                    startActivity(intent);
                }
            });
        }else{
            getProfile();
            //프로필 이미지 설정
            ivProfileImage = (ImageView)findViewById(R.id.iv_drawer_profile_image);
            imageApi = new ImageApi();
            imageApi.getImage(ivProfileImage, imageApi.getProfileImageUrl(id));

            btnRidingRecord = (TextView) findViewById(R.id.btn_riding_record);
            btnRidingRecord.setText("주행 기록");
            btnRidingRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), RidingRecordListActivity.class);
                    startActivity(intent);
                }
            });

            btnPosts = findViewById(R.id.btn_posts);
            //btnPosts.setText("자유게시판");

            btnPosts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ListViewActivity.class);
                    startActivity(intent);
                }
            });

            btnSigninOrNickname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), BasicProfileActivity.class);
                    startActivity(intent);
                }
            });
        }

        //권한요청, 확인
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setPermissions(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .setDeniedMessage("권한 허용이 필요합니다.\n허용하지 않으면 앱이 종료됩니다.")
                .check();


        //길찾기버튼
        CardView routebutton = findViewById(R.id.routeButton);
        routebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent findPathIntent = new Intent(getApplicationContext(), FindPathActivity.class);

                startActivity(findPathIntent);
                overridePendingTransition(0, 0);

            }
        });

        //주행버튼
        CardView ridingButton = findViewById(R.id.ridingButton);
        ridingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ridingIntent = new Intent(getApplicationContext(), RidingActivity.class);

                startActivity(ridingIntent);
            }
        });

        //=================== TEST =======================
        //날씨버튼
        CardView weatherButton = findViewById(R.id.weatherCard);
        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new WeatherApi().getApiRaw().enqueue(new Callback<Object>() {
                    @Override
                    public void onResponse(Call<Object> call, Response<Object> response) {

                    }

                    @Override
                    public void onFailure(Call<Object> call, Throwable t) {

                    }
                });
            }
        });

    }//onCreate()

    //권한요청결과 리스너(안드로이드 내장) tedpermission과 무관
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    //Drawer 리스너
    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {}
        @Override
        public void onDrawerOpened(@NonNull View drawerView) {}
        @Override
        public void onDrawerClosed(@NonNull View drawerView) {}
        @Override
        public void onDrawerStateChanged(int newState) {}
    };

    @Override
    public void onBackPressed() {
        if(isDrawerOpened){
            drawerLayout.closeDrawer(Gravity.LEFT);
            isDrawerOpened = false;
        }
        else if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast = Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        else if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            toast.cancel();
        }
    }

    private void getProfile(){
        api.getProfile(id).enqueue(new Callback<BasicProfile>() {
            @Override
            public void onResponse(Call<BasicProfile> call, Response<BasicProfile> response) {
                if(response.isSuccessful()){
                    String nickname = response.body().getNickname();
                    btnSigninOrNickname.setText(nickname);
                }else{
                    btnSigninOrNickname.setText(id);
                }
            }
            @Override
            public void onFailure(Call<BasicProfile> call, Throwable t) {
                btnSigninOrNickname.setText(id);
            }
        });
    }
}