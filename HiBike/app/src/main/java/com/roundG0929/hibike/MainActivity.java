package com.roundG0929.hibike;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.roundG0929.hibike.activities.auth.SigninActivity;
import com.roundG0929.hibike.api.map_route.navermap.FirstNaverMapSet;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button mainSigninBtn;

    private static final int NAVER_LOCATION_PERMISSION_CODE = 1000;
    private FusedLocationSource fusedLocationSource;


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
        setContentView(R.layout.activity_main);



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

        //초기맵설정
        //맵 뷰 객체 할당
        FragmentManager fragmentManager = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fragmentManager.beginTransaction().add(R.id.map, mapFragment).commit();
        }
        //위치추적기능 설정 객체
        fusedLocationSource = new FusedLocationSource(this, NAVER_LOCATION_PERMISSION_CODE);
        //firstMapSet 객체
        FirstNaverMapSet firstNaverMapSet = new FirstNaverMapSet(fusedLocationSource,getApplicationContext());
        //맵객체 설정
        mapFragment.getMapAsync(firstNaverMapSet);



        mainSigninBtn = (Button) findViewById(R.id.mainSigninBtn);
        mainSigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SigninActivity.class);
                startActivity(intent);
            }
        });
    }

    //권한요청결과 리스너(안드로이드 내장) tedpermission과 무관
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //위치추적기능 설정 객체 권한설정 대응
        if(fusedLocationSource.onRequestPermissionsResult(requestCode,permissions,grantResults)){
            if(!fusedLocationSource.isActivated()){
                //firstMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    //네이버맵 테스트 CallBack
    public class TestNaverMapCallback implements OnMapReadyCallback {

        @Override
        public void onMapReady(@NonNull NaverMap naverMap) {
            double[] latlngList = startLocation();
            LatLng latLng = new LatLng(latlngList[0],latlngList[1]);


            CameraUpdate cameraUpdate = CameraUpdate.scrollTo(latLng);
            LocationOverlay locationOverlay = naverMap.getLocationOverlay();

            locationOverlay.setVisible(true);
            locationOverlay.setPosition(latLng);
            naverMap.moveCamera(cameraUpdate);
        }
    }


    //현재위치 가져오기   --- TEST ---
    public double[] startLocation() {
        double[] latlng = new double[2];
        latlng[0] = 0;latlng[1]=0;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location;

        //위치권한 확인
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"권한 허용이 필요합니다.",Toast.LENGTH_SHORT).show();
            return latlng;
        }

        //위치가져오기 GPS 수신 없으면 NETWORK 위치 사용 후 알림
        try {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(getApplicationContext(), "위치정보가 정확하지 않습니다 야외에서 사용해주세요", Toast.LENGTH_SHORT).show();
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }else{
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Toast.makeText(getApplicationContext(), "use gps", Toast.LENGTH_SHORT).show();
            }

            if(location != null){
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                latlng[0] = latitude;
                latlng[1] = longitude;
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return latlng;
    }


}