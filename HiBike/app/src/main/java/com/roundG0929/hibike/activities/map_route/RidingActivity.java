package com.roundG0929.hibike.activities.map_route;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.PolylineOverlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.roundG0929.hibike.MainActivity;
import com.roundG0929.hibike.R;
import com.roundG0929.hibike.api.map_route.navermap.MapSetting;

import java.util.ArrayList;

public class RidingActivity extends AppCompatActivity implements OnMapReadyCallback {

    //일반변수 선언
    NaverMap naverMapObj;
    MapFragment mapFragment;
    private FusedLocationSource fusedLocationSource;
    private static final int NAVER_LOCATION_PERMISSION_CODE = 1000;
    MapSetting mapSetting = new MapSetting();
    LatLng beforeLatLng = new LatLng(0,0);   //속도측정
    LatLng nowLatLng = new LatLng(0,0);   //속도측정
    double speed;
    boolean ridingStartFlag = true;
    ArrayList<LatLng> ridingPointRecord = new ArrayList<>(); //주행시 경위도 저장 list
    double totalDistance = 0; //주행총거리
    double pointDistance = 0; //속도측정, 총거리측정용 순간 거리
    long starTime;
    long endTime;
    int totalTime_second;


    //ui 객체 선언
    TextView speedText;
    TextView totalDistanceText;
    TextView totalTimeText;
    TextView averageSpeedText;
    Button ridingStopButton;
    PolylineOverlay ridingPointRecordLine = new PolylineOverlay(); //경로선
    LinearLayout resultLayout;
    FrameLayout speedLayout;
    LocationOverlay locationOverlay; // 현재위치 표시 오버레이


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riding);

        //ui 객체 할당
        speedText = findViewById(R.id.speedText);
        ridingStopButton = findViewById(R.id.ridingStopButton);
        totalDistanceText = findViewById(R.id.totalDistanceText);
        totalTimeText = findViewById(R.id.totalTimeText);
        averageSpeedText = findViewById(R.id.averageSpeedText);
        resultLayout = findViewById(R.id.resultLayout);
        speedLayout = findViewById(R.id.speedLayout);

        //경로선 속성지정
        ridingPointRecordLine.setColor(Color.GREEN); //색상



        //위치권환 확인
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

        //Toast.makeText(getApplicationContext(),"속도와 위치정보는 정확하지 않을 수 있습니다.",Toast.LENGTH_SHORT).show();


        ridingStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMessage();
            }
        });


        //실시간 속도 표출 thread
        Handler ridingHandler = new Handler();
        Thread ridingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (fusedLocationSource.getLastLocation() == null){
                    Log.d("checkfusedLocationSource", "run");
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                ridingHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"주행시작",Toast.LENGTH_SHORT).show();
                    }
                });

                starTime = System.currentTimeMillis();

                beforeLatLng = new LatLng(fusedLocationSource.getLastLocation());
                nowLatLng = new LatLng(fusedLocationSource.getLastLocation());
                ridingPointRecord.add(nowLatLng);
                while (ridingStartFlag){
                    try {
                        Thread.sleep(995);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    beforeLatLng = nowLatLng;
                    nowLatLng = new LatLng(fusedLocationSource.getLastLocation());
                    ridingPointRecord.add(nowLatLng);


                    //네이버 자체 메소드 사용
                    //speed = Double.parseDouble(String.format("%.1f", nowLatLng.distanceTo(beforeLatLng)*3.6));

                    //외부함수 사용
                    pointDistance = distance(beforeLatLng.latitude,beforeLatLng.longitude, nowLatLng.latitude,nowLatLng.longitude,"meter");

                    //총거리
                    totalDistance += pointDistance;
                    //속도 km/h
                    speed = Double.parseDouble(String.format("%.1f", pointDistance * 3.6));


                    //ui 반영
                    ridingHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            speedText.setText(speed+"");
                            ridingPointRecordLine.setCoords(ridingPointRecord);
                            ridingPointRecordLine.setMap(naverMapObj);
                        }
                    });
                    Log.d("ridingThread", "run: ");
                }




            }
        });
        ridingThread.start();



        //----------test code--------------
        //point1 : 37.37611121808619, 126.63768694859772
        //point2 : 37.379294093829515, 126.63247938605043
        //distance : 약 583m

//        double mDistance = distance(37.37611121808619, 126.63768694859772,37.379294093829515, 126.63247938605043,"kilometer");
//        double speed = Double.parseDouble(String.format("%.1f", mDistance*180));
//
//        speedText.setText(Double.toString(speed));
    }//onCreate



    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        naverMapObj = naverMap;
        locationOverlay = naverMap.getLocationOverlay();
        mapSetting.routeActivityMapSet(naverMapObj,getApplicationContext(),fusedLocationSource);
        //naverMap.getUiSettings().setLocationButtonEnabled(false);
    }


    //주행종료 다이얼로그
    private void showMessage(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("주행을 종료하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                endTime = System.currentTimeMillis();
                ridingStartFlag = false;

                totalTime_second = (int) ((endTime - starTime)/1000);
                int result_second = totalTime_second % 60;
                int result_minute = totalTime_second / 60;
                speedLayout.setVisibility(View.GONE);
                resultLayout.setVisibility(View.VISIBLE);
                double averageSpeed = (totalDistance/totalTime_second)*3.6;

                totalDistance = Double.parseDouble(String.format("%.1f", totalDistance));

                totalDistanceText.setText(totalDistance+"m");
                totalTimeText.setText(result_minute +" : "+result_second);
                averageSpeedText.setText(Double.parseDouble(String.format("%.1f", averageSpeed))+"");

                locationOverlay.setVisible(false);
                naverMapObj.setLocationTrackingMode(LocationTrackingMode.None);

                ArrayList<LatLng> forMakeLatLngBounds = getLatLngBounds(ridingPointRecord);
                LatLngBounds latLngBounds = new LatLngBounds(forMakeLatLngBounds.get(0),forMakeLatLngBounds.get(1));
                CameraUpdate cameraUpdate = CameraUpdate.fitBounds(latLngBounds,
                        convertDpToPx(getApplicationContext(),30),
                        convertDpToPx(getApplicationContext(),100),
                        convertDpToPx(getApplicationContext(),30),
                        convertDpToPx(getApplicationContext(),100));
                cameraUpdate.animate(CameraAnimation.Fly);
                naverMapObj.moveCamera(cameraUpdate);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    //남서쪽 북동쪽 좌표 구하기 FOR 주행종료후 카메라 위치이동
    private ArrayList<LatLng> getLatLngBounds(ArrayList<LatLng> record){
        double minLng = record.get(0).longitude;
        double minLat = record.get(0).latitude;
        double maxLng = record.get(0).longitude;
        double maxLat = record.get(0).latitude;
        for(int i = 0;record.size()>i;i++){
            if(minLng>record.get(i).longitude){minLng=record.get(i).longitude;}
            if(minLat>record.get(i).latitude){minLat=record.get(i).latitude;}
            if(maxLng<record.get(i).longitude){maxLng=record.get(i).longitude;}
            if(maxLat<record.get(i).latitude){maxLat=record.get(i).latitude;}
        }
        LatLng latLng_SW = new LatLng(minLat,minLng);
        LatLng latLng_NE = new LatLng(maxLat,maxLng);

        ArrayList<LatLng> resultList = new ArrayList<>();
        resultList.add(latLng_SW);
        resultList.add(latLng_NE);

        return resultList;
    }


    //위경도 사용 거리 구하는 메소드
    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;

        if (unit == "kilometer") {
            dist = dist * 1.609344;
        } else if(unit == "meter"){
            dist = dist * 1609.344;
        }

        return (dist);
    }
    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }


    public int convertDpToPx(Context context,int dp){
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}
