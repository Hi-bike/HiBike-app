package com.roundG0929.hibike.activities.map_route;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.CompassView;
import com.naver.maps.map.widget.ZoomControlView;
import com.roundG0929.hibike.MainActivity;
import com.roundG0929.hibike.R;
import com.roundG0929.hibike.api.map_route.navermap.FirstNaverMapSet;
import com.roundG0929.hibike.api.map_route.navermap.MapSetting;

import gun0912.tedkeyboardobserver.BaseKeyboardObserver;
import gun0912.tedkeyboardobserver.TedKeyboardObserver;

public class FindPathActivity extends AppCompatActivity implements OnMapReadyCallback {
    //지도, 길찾기 변수(객체)
    NaverMap naverMapObj;
    MapFragment mapFragment;
    private FusedLocationSource fusedLocationSource;
    private static final int NAVER_LOCATION_PERMISSION_CODE = 1000;
    private FusedLocationProviderClient fusedLocationProviderClient;
    boolean trackingflag = false;
    Handler handler=new Handler();
    MapSetting mapSetting = new MapSetting();
    LatLng[] startEndPoint = new LatLng[2]; //0 start, 1 end

    //ui 객체
    EditText startText;
    EditText endText;
    ImageButton findButton;
    ImageButton changeButton;
    LinearLayout selectlayout;
    Button nowlocationButton;
    Button frommapButton;
    Button fromsearchButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpath);

        //ui 객체 할당
        startText = findViewById(R.id.startText);
        endText = findViewById(R.id.endText);
        findButton = findViewById(R.id.findButton);
        changeButton = findViewById(R.id.changeButton);
        selectlayout = findViewById(R.id.selectlayout);
        nowlocationButton = findViewById(R.id.nowlocationButton);
        frommapButton = findViewById(R.id.frommapButton);
        fromsearchButton = findViewById(R.id.fromsearchButton);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }


        //초기맵설정
            //맵 뷰 객체 할당
        FragmentManager fragmentManager = getSupportFragmentManager();
        mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fragmentManager.beginTransaction().add(R.id.map, mapFragment).commit();
        }
            //위치소스
        fusedLocationSource = new FusedLocationSource(this, NAVER_LOCATION_PERMISSION_CODE);
            //맵객체 설정
        mapFragment.getMapAsync(this::onMapReady);


        //각 ui listener
            //출발, 도착 editText
        startText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    selectlayout.setVisibility(View.VISIBLE);
                }else{
                    selectlayout.setVisibility(View.GONE);
                }
            }
        });
        endText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    selectlayout.setVisibility(View.VISIBLE);
                }else{
                    selectlayout.setVisibility(View.GONE);
                }
            }
        });
        new TedKeyboardObserver(this).listen(new BaseKeyboardObserver.OnKeyboardListener() {
            @Override
            public void onKeyboardChange(boolean isShow) {
                if(!isShow){
                    startText.clearFocus();
                    endText.clearFocus();
                }
            }
        });
            //위치소스선택 버튼
        nowlocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(startText.isFocused()){
                    Location location = fusedLocationSource.getLastLocation();
                    LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                    startEndPoint[0] = latLng;
                    startText.setText(startEndPoint[0].latitude +", "+startEndPoint[0].longitude);
                }else if(endText.isFocused()){
                    Location location = fusedLocationSource.getLastLocation();
                    LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
                    startEndPoint[1] = latLng;
                    endText.setText(startEndPoint[1].latitude +", "+startEndPoint[1].longitude);
                }

            }
        });
        frommapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        fromsearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
            //길찾기버튼 findButton
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
            //출발,도착지점 변경버튼 changeButton
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng latLng = startEndPoint[0];
                startEndPoint[0] = startEndPoint[1];
                startEndPoint[1] = latLng;
                if(startEndPoint[0] == null){
                    startText.setText("");
                }else{
                    startText.setText(startEndPoint[0].latitude +", "+startEndPoint[0].longitude);
                }
                if(startEndPoint[1] == null){
                    endText.setText("");
                }else{
                    endText.setText(startEndPoint[1].latitude +", "+startEndPoint[1].longitude);
                }
            }
        });


    }//onCreate()

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }//onFinish()
    //Lifecycle

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        naverMapObj = naverMap;

        mapSetting.routeActivityMapSet(naverMapObj,getApplicationContext(),fusedLocationSource);

        CompassView compassView = findViewById(R.id.compass);
        compassView.setMap(naverMapObj);

    }

//    //EditText 외부 터치시 키보드 숨기기
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        View view = getCurrentFocus();
//        if (view != null
//                && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE)
//                && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
//            int scrcoords[] = new int[2];
//            view.getLocationOnScreen(scrcoords);
//            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
//            float y = ev.getRawY() + view.getTop() - scrcoords[1];
//            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
//                ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
//        }
//        return super.dispatchTouchEvent(ev);
//    }
}
