package com.roundG0929.hibike;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.roundG0929.hibike.activities.auth.BasicProfileActivity;
import com.roundG0929.hibike.activities.auth.SigninActivity;
import com.roundG0929.hibike.activities.map_route.FindPathActivity;
import com.roundG0929.hibike.activities.map_route.RidingActivity;
import com.roundG0929.hibike.activities.riding_record.RidingRecordListActivity;
import com.roundG0929.hibike.api.server.ApiInterface;
import com.roundG0929.hibike.api.server.RetrofitClient;
import com.roundG0929.hibike.api.server.dto.BasicProfile;
import com.roundG0929.hibike.api.server.fuction.ImageApi;
import com.roundG0929.hibike.activities.board.ListViewActivity;
import com.roundG0929.hibike.api.weather.WeatherApi;
import com.roundG0929.hibike.api.weather.wheatherDto.Item;
import com.roundG0929.hibike.api.weather.wheatherDto.RealTimeWeather;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
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

        if (id == "") {
            btnSigninOrNickname.setText("로그인");
            btnSigninOrNickname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            getProfile();
            //프로필 이미지 설정
            ivProfileImage = (ImageView) findViewById(R.id.iv_drawer_profile_image);
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

        //현재위치 날씨불러오기
            //현재위치 불러오기
        TextView temperature = findViewById(R.id.temperatureText);
        TextView moisture = findViewById(R.id.moistureText);
        FusedLocationProviderClient fusedLocationClient;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        long now = System.currentTimeMillis();
                        Date date = new Date(now);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                        SimpleDateFormat timeFormat = new SimpleDateFormat("hhmm");
                        String nowDate = dateFormat.format(date);
                        date = new Date(now-3600000);
                        String nowTime = timeFormat.format(date);



                        int x = (int) convertGRID_GPS(0,location.getLatitude(),location.getLongitude()).x;
                        int y= (int) convertGRID_GPS(0,location.getLatitude(),location.getLongitude()).y;

                        //위치불러오기 성공시 날씨 불러오기
                        new WeatherApi(x,y,System.currentTimeMillis()).getApi().enqueue(new Callback<RealTimeWeather>() {
                            @Override
                            public void onResponse(Call<RealTimeWeather> call, Response<RealTimeWeather> response) {
                                ArrayList<Item> realTimeWeather = response.body().response.body.items.item;
                                //카테고리별 데이터구분
                                    //기온
                                for (int i = 0; realTimeWeather.size() > i; i++) {
                                    if (realTimeWeather.get(i).category.equals("T1H")) {
                                        temperature.setText(realTimeWeather.get(i).fcstValue+" ℃");
                                        break;
                                    }
                                }
                                    //흐림정도
                                for (int i = 0; realTimeWeather.size() > i; i++) {
                                    if (realTimeWeather.get(i).category.equals("SKY")) {
                                        int cloud_amount = Integer.parseInt(realTimeWeather.get(i).fcstValue);
                                        LottieAnimationView weatherImage = findViewById(R.id.weatherImage);
                                        if(cloud_amount<=5){
                                            weatherImage.setAnimation(R.raw.animation_sunny);
                                        }else if(cloud_amount<=8){
                                            weatherImage.setAnimation(R.raw.animation_cloudy);
                                        }else {
                                            weatherImage.setAnimation(R.raw.animation_overcast);
                                        }
                                        weatherImage.playAnimation();
                                        weatherImage.setRepeatCount(LottieDrawable.INFINITE);
                                        break;
                                    }

                                }
                                    //습도
                                for (int i = 0; realTimeWeather.size() > i; i++) {
                                    if (realTimeWeather.get(i).category.equals("REH")) {
                                        moisture.setText(realTimeWeather.get(i).fcstValue+" %");
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<RealTimeWeather> call, Throwable t) {

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



    //기상청 날씨 활용 위한 제공 메쏘드 (위경도 -> 기상청 고유 좌표계)
    private LatXLngY convertGRID_GPS(int mode, double lat_X, double lng_Y )
    {
        double RE = 6371.00877; // 지구 반경(km)
        double GRID = 5.0; // 격자 간격(km)
        double SLAT1 = 30.0; // 투영 위도1(degree)
        double SLAT2 = 60.0; // 투영 위도2(degree)
        double OLON = 126.0; // 기준점 경도(degree)
        double OLAT = 38.0; // 기준점 위도(degree)
        double XO = 43; // 기준점 X좌표(GRID)
        double YO = 136; // 기1준점 Y좌표(GRID)

        //
        // LCC DFS 좌표변환 ( code : "TO_GRID"(위경도->좌표, lat_X:위도,  lng_Y:경도), "TO_GPS"(좌표->위경도,  lat_X:x, lng_Y:y) )
        //


        double DEGRAD = Math.PI / 180.0;
        double RADDEG = 180.0 / Math.PI;

        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);
        LatXLngY rs = new LatXLngY();

        if (mode == 0) {
            rs.lat = lat_X;
            rs.lng = lng_Y;
            double ra = Math.tan(Math.PI * 0.25 + (lat_X) * DEGRAD * 0.5);
            ra = re * sf / Math.pow(ra, sn);
            double theta = lng_Y * DEGRAD - olon;
            if (theta > Math.PI) theta -= 2.0 * Math.PI;
            if (theta < -Math.PI) theta += 2.0 * Math.PI;
            theta *= sn;
            rs.x = Math.floor(ra * Math.sin(theta) + XO + 0.5);
            rs.y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
        }
        else {
            rs.x = lat_X;
            rs.y = lng_Y;
            double xn = lat_X - XO;
            double yn = ro - lng_Y + YO;
            double ra = Math.sqrt(xn * xn + yn * yn);
            if (sn < 0.0) {
                ra = -ra;
            }
            double alat = Math.pow((re * sf / ra), (1.0 / sn));
            alat = 2.0 * Math.atan(alat) - Math.PI * 0.5;

            double theta = 0.0;
            if (Math.abs(xn) <= 0.0) {
                theta = 0.0;
            }
            else {
                if (Math.abs(yn) <= 0.0) {
                    theta = Math.PI * 0.5;
                    if (xn < 0.0) {
                        theta = -theta;
                    }
                }
                else theta = Math.atan2(xn, yn);
            }
            double alon = theta / sn + olon;
            rs.lat = alat * RADDEG;
            rs.lng = alon * RADDEG;
        }
        return rs;
    }



    class LatXLngY
    {
        public double lat;
        public double lng;

        public double x;
        public double y;

    }
}