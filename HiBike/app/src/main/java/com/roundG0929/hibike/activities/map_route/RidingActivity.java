package com.roundG0929.hibike.activities.map_route;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
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
import com.roundG0929.hibike.api.server.ApiInterface;
import com.roundG0929.hibike.api.server.RetrofitClient;
import com.roundG0929.hibike.api.server.dto.PostRiding;
import com.roundG0929.hibike.api.server.dto.ProfileImage;
import com.roundG0929.hibike.api.server.dto.RidingImage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RidingActivity extends AppCompatActivity implements OnMapReadyCallback {

    //일반변수 선언
    NaverMap naverMapObj;
    MapFragment mapFragment;
    ApiInterface api;
    private FusedLocationSource fusedLocationSource;
    private static final int NAVER_LOCATION_PERMISSION_CODE = 1000;
    MapSetting mapSetting = new MapSetting();
    LatLng beforeLatLng = new LatLng(0,0);   //속도측정
    LatLng nowLatLng = new LatLng(0,0);   //속도측정
    LatLng startingPoint = new LatLng(0,0);;
    LatLng endPoint = new LatLng(0,0);;

    double speed;
    boolean ridingStartFlag = false;
    ArrayList<LatLng> ridingPointRecord = new ArrayList<>(); //주행시 경위도 저장 list
    double totalDistance = 0; //주행총거리
    double pointDistance = 0; //속도측정, 총거리측정용 순간 거리
    long starTime;
    long endTime;
    int totalTime_second;
    String userId, uniqueId;

    //ui 객체 선언
    TextView speedText;
    TextView totalDistanceText;
    TextView totalTimeText;
    TextView averageSpeedText;
    Button ridingGoAndStopButton;
    PolylineOverlay ridingPointRecordLine = new PolylineOverlay(); //경로선
    LinearLayout resultLayout;
    FrameLayout speedLayout;
    LocationOverlay locationOverlay; // 현재위치 표시 오버레이

    //test ui
    TextView distText;//timeText,


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riding);

        //userId
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        userId = pref.getString("id", "");

        //ui 객체 할당
        speedText = findViewById(R.id.speedText);
        ridingGoAndStopButton = findViewById(R.id.ridingGoAndStopButton);
        totalDistanceText = findViewById(R.id.totalDistanceText);
        totalTimeText = findViewById(R.id.totalTimeText);
        averageSpeedText = findViewById(R.id.averageSpeedText);
        resultLayout = findViewById(R.id.resultLayout);
        speedLayout = findViewById(R.id.speedLayout);

        //test ui
//        timeText = findViewById(R.id.timeText);
        distText = findViewById(R.id.distText);

        //server connect
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);

        //경로선 속성지정
        ridingPointRecordLine.setColor(Color.BLUE); //색상

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

                //시작 지점
                startingPoint = nowLatLng;

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
                            distText.setText(String.format("%.1f", pointDistance)+"");
                            ridingPointRecordLine.setCoords(ridingPointRecord);
                            ridingPointRecordLine.setMap(naverMapObj);
                        }
                    });
                    Log.d("ridingThread", "run: ");
                }
            }
        });

        ridingGoAndStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!ridingStartFlag) {
                    ridingStartFlag = true;
                    ridingGoAndStopButton.setText("주행 종료");
                    ridingThread.start();
                } else if(ridingStartFlag){
                    showMessage();
                }

            }
        });

        //----------test code--------------
        //point1 : 37.37611121808619, 126.63768694859772
        //point2 : 37.379294093829515, 126.63247938605043
        //distance : 약 583m

//        double mDistance = distance(37.37611121808619, 126.63768694859772,37.379294093829515, 126.63247938605043,"kilometer");
//        double speed = Double.parseDouble(String.format("%.1f", mDistance*180));
//
//        speedText.setText(Double.toString(speed));
    }//onCreate

    // 주행 중, 뒤로가기 버튼 누를 시
    @Override
    public void onBackPressed() {
        if(ridingStartFlag) {
            showMessage();
        }else{
            finish();
        }
    }


    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        naverMapObj = naverMap;
        locationOverlay = naverMap.getLocationOverlay();
        mapSetting.routeActivityMapSet(naverMapObj,getApplicationContext(),fusedLocationSource);
        CameraUpdate cameraUpdate = CameraUpdate.zoomTo(17.0)
                .animate(CameraAnimation.Easing);
        naverMap.moveCamera(cameraUpdate);
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
                cameraUpdate.animate(CameraAnimation.Easing);
                naverMapObj.moveCamera(cameraUpdate);

                startingPoint = ridingPointRecord.get(0);
                endPoint = ridingPointRecord.get(ridingPointRecord.size() - 1);

                uniqueId = getUniqueId();

                PostRiding data = new PostRiding();
                data.setUserId(userId);
                data.setUniqueId(uniqueId);
                data.setRidingTime(result_minute +" : "+result_second);
                data.setAveSpeed(Double.parseDouble(String.format("%.1f", averageSpeed))+"");
                data.setDistance(totalDistance+"");
                data.setStartingPoint(startingPoint.latitude+","+startingPoint.longitude);
                data.setEndPoint(endPoint.latitude+","+endPoint.longitude);

                Log.e("uniqueId", uniqueId);

                api.postRiding(data).enqueue(new Callback<PostRiding>() {
                    @Override
                    public void onResponse(Call<PostRiding> call, Response<PostRiding> response) {
                        if(!response.isSuccessful()){
                            Toast toast = Toast.makeText(getApplicationContext(), "error. (" + response.message() + ")", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.TOP, 0, 0);
                            toast.show();
                        }
                    }
                    @Override
                    public void onFailure(Call<PostRiding> call, Throwable t) {}
                });

                //TODO: 주행 경로 캡쳐 후 이미지 저장
                postRidingImage();

                ridingGoAndStopButton.setText("나가기");
                ridingGoAndStopButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                    }
                });
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    //남서쪽 북동쪽 좌표 구하기 (FOR 주행종료후 카메라 위치이동)
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

    public String getUniqueId() {
        String uniqueId = UUID.randomUUID().toString().substring(0,8);
        return uniqueId;
    }

    public void postRidingImage(){
        View rootView = getWindow().getDecorView();
        File file = ScreenShot(rootView);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody idToUpload = RequestBody.create(MediaType.parse("text/plain"), uniqueId);
        api.setRidingImage(fileToUpload,idToUpload).enqueue(new Callback<RidingImage>() {
            @Override
            public void onResponse(Call<RidingImage> call, Response<RidingImage> response) {
                if (response.isSuccessful()) {
                    Log.v("image", "success");
                } else {
                    Log.v("image", response.message());
                }
            }
            @Override
            public void onFailure(Call<RidingImage> call, Throwable t) {}
        });
    }
    public File ScreenShot(View view){
        view.setDrawingCacheEnabled(true);  //화면에 뿌릴때 캐시를 사용하게 한다
        Bitmap screenBitmap = view.getDrawingCache();   //캐시를 비트맵으로 변환
        String filename = "screenshot.png";
        File file = new File(Environment.getExternalStorageDirectory()+"/Pictures", filename);  //Pictures폴더 screenshot.png 파일
        FileOutputStream os = null;
        try{
            os = new FileOutputStream(file);
            screenBitmap.compress(Bitmap.CompressFormat.PNG, 90, os);   //비트맵을 PNG파일로 변환
            os.close();
        }catch (IOException e){
            e.printStackTrace();
            return null;
        }

        view.setDrawingCacheEnabled(false);
        return file;
    }


}
