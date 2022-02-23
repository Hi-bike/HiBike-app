package com.roundG0929.hibike.activities.map_route;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

public class FindPathActivity extends AppCompatActivity implements OnMapReadyCallback {
    NaverMap naverMapObj;
    MapFragment mapFragment;
    private FusedLocationSource fusedLocationSource;
    private static final int NAVER_LOCATION_PERMISSION_CODE = 1000;
    private FusedLocationProviderClient fusedLocationProviderClient;
    boolean trackingflag = false;

    MapSetting mapSetting = new MapSetting();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpath);

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
        fusedLocationSource = new FusedLocationSource(this, NAVER_LOCATION_PERMISSION_CODE);
        //맵객체 설정
        mapFragment.getMapAsync(this::onMapReady);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Location location = fusedLocationSource.getLastLocation();
                EditText editText = findViewById(R.id.startText);
                editText.setText("");
                editText.append(location.getLatitude() + ", " + location.getLongitude());
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
}
