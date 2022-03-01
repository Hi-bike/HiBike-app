package com.roundG0929.hibike.api.map_route.navermap;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.core.app.ActivityCompat;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.util.MarkerIcons;
import com.roundG0929.hibike.MainActivity;
import com.roundG0929.hibike.R;

public class FirstNaverMapSet implements OnMapReadyCallback {
    NaverMap firstMap;
    FusedLocationSource fusedLocationSource;
    Context mContext;
    private int checkStartEndClick;
    Activity activity;


    public FirstNaverMapSet(Context context, FusedLocationSource fusedLocationSource, Activity activity) {
        this.fusedLocationSource = fusedLocationSource;
        this.mContext = context;
        checkStartEndClick = 0;
        this.activity = activity;
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        double[] latlngList = startLocation();
        LatLng latLng = new LatLng(latlngList[0],latlngList[1]);

        firstMap = naverMap;

        //오버레이 설정
        firstMap.setLayerGroupEnabled(NaverMap.LAYER_GROUP_BICYCLE, true);

        //내 위치오버레이 설정
        LocationOverlay locationOverlay = firstMap.getLocationOverlay();
        locationOverlay.setVisible(true);

        //현재위치바로가기 (ux개선)
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(latLng);
        locationOverlay.setPosition(latLng);
        naverMap.moveCamera(cameraUpdate);

        //내 위치 트래킹 설정
        firstMap.setLocationSource(fusedLocationSource);
        firstMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        //ui 설정
        UiSettings uiSettings = firstMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);

        //리스너
            //롱클릭리스너
        Marker startMarker = new Marker();
        startMarker.setIcon(MarkerIcons.GREEN);
        Marker endMarker = new Marker();
        startMarker.setIcon(MarkerIcons.RED);
        firstMap.setOnMapLongClickListener(new NaverMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
                Toast.makeText(mContext,"this \nlatitude : "+latLng.latitude+"\nlongitude : "+latLng.longitude,Toast.LENGTH_SHORT).show();
                Marker marker = new Marker();
                marker.setPosition(latLng);
                marker.setMap(firstMap);
            }
        });

    }


    //현재위치 가져오기
    public double[] startLocation() {
        double[] latlng = new double[2];
        latlng[0] = 0;latlng[1]=0;
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        Location location;

        //위치권한 확인
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(mContext,"권한 허용이 필요합니다.",Toast.LENGTH_SHORT).show();
            return latlng;
        }

        //위치가져오기 GPS 수신 없으면 NETWORK 위치 사용 후 알림
        try {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Toast.makeText(mContext, "위치정보가 정확하지 않습니다 야외에서 사용해주세요", Toast.LENGTH_SHORT).show();
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }else{
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Toast.makeText(mContext, "use gps", Toast.LENGTH_SHORT).show();
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
