package com.roundG0929.hibike.activities.map_route;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.overlay.PolylineOverlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.roundG0929.hibike.HibikeUtils;
import com.roundG0929.hibike.MainActivity;
import com.roundG0929.hibike.R;
import com.roundG0929.hibike.activities.information.InformationWriteActivity;
import com.roundG0929.hibike.api.map_route.navermap.MapSetting;
import com.roundG0929.hibike.api.map_route.navermap.NaverApiInterface;
import com.roundG0929.hibike.api.map_route.navermap.NaverRetrofitClient;
import com.roundG0929.hibike.api.map_route.navermap.ReverseGeocodingGenerator;
import com.roundG0929.hibike.api.server.ApiInterface;
import com.roundG0929.hibike.api.server.RetrofitClient;
import com.roundG0929.hibike.api.server.dto.GetAllDanger;
import com.roundG0929.hibike.api.server.dto.PostRidingMulti;
import com.roundG0929.hibike.api.server.dto.ReverseGeocodingDto;
import com.roundG0929.hibike.api.server.dto.RidingRegion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RidingActivity extends AppCompatActivity implements OnMapReadyCallback, RidingBottomSheetFragment.IsDeleteListener {

    //일반변수 선언
    NaverMap naverMapObj;
    MapFragment mapFragment;
    ApiInterface api;
    NaverApiInterface nApi;
    private FusedLocationSource fusedLocationSource;
    private static final int NAVER_LOCATION_PERMISSION_CODE = 1000;
    MapSetting mapSetting = new MapSetting();
    LatLng beforeLatLng = new LatLng(0,0);   //속도측정
    LatLng nowLatLng = new LatLng(0,0);   //속도측정
    LatLng startingPoint = new LatLng(0,0);;
    LatLng endPoint = new LatLng(0,0);;
    //길찾기에서 넘어왔을경우 필요한 변수
    ArrayList<LatLng> routePoints = new ArrayList<>();  //실질적 경로배열
    PathOverlay routeline = new PathOverlay(); //경로 오버레이 객체
    ArrayList<Marker> markerPoints = new ArrayList<>(); //실질 마커(위험정보) 배열
    ArrayList<Boolean> dangerPointSpeakFlag = new ArrayList<>();
    ArrayList<Marker> informationMarkerList = new ArrayList<>();  //위험정보 마커객체

    double speed;
    boolean ridingStartFlag = false;
    ArrayList<LatLng> ridingPointRecord = new ArrayList<>(); //주행시 경위도 저장 list
    double totalDistance = 0; //주행총거리
    double pointDistance = 0; //속도측정, 총거리측정용 순간 거리
    long starTime;
    long endTime;
    int totalTime_second, result_second, result_minute;;
    String userId, uniqueId;
    double averageSpeed;

    TextToSpeech tts;

    //ui 객체 선언
    TextView speedText;
    TextView totalDistanceText;
    TextView totalTimeText;
    TextView averageSpeedText;
    Button ridingGoAndStopButton;
    Button postDangerButton;
    PolylineOverlay ridingPointRecordLine = new PolylineOverlay(); //경로선
    LinearLayout resultLayout;
    FrameLayout speedLayout;
    LocationOverlay locationOverlay; // 현재위치 표시 오버레이
    boolean sw = true;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    //test ui
    TextView distText;//timeText,

    PostRidingMulti postRidingMulti = new PostRidingMulti();
    File file;
    AlertDialog endDialog;
    String fromString;
    boolean isDangerDeleted = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riding);

        //어떤 액티비티에서 넘어왔는지 구분후 길찾기에서 넘어왔으면 경로, 위험정보 데이터 표기
        Intent intent = getIntent();
        fromString = intent.getStringExtra("from");
        if(fromString.equals("findpath")){
            ArrayList<ArrayList<Double>> routePoints_double = (ArrayList<ArrayList<Double>>) intent.getSerializableExtra("route_point");
            ArrayList<ArrayList<Double>> markerPoints_double = (ArrayList<ArrayList<Double>>) intent.getSerializableExtra("marker_point");
//            Toast.makeText(getApplicationContext(),markerPoints_double.get(0).get(0)+" ",Toast.LENGTH_SHORT).show();

            for(int i =0;i<routePoints_double.size();i++){  //경로배열 할당
                routePoints.add(new LatLng(routePoints_double.get(i).get(0),
                                    routePoints_double.get(i).get(1)));
            }
            Log.d("TAG", "routepoints: "
            +routePoints.get(0).latitude
                    +"  "+routePoints.get(0).longitude);
            routeline.setCoords(routePoints);

            if(!markerPoints_double.isEmpty()){
                for(int i =0;i<markerPoints_double.size();i++){ //마커배열 할당
                    Marker marker = new Marker(new LatLng(markerPoints_double.get(i).get(0), markerPoints_double.get(i).get(1)));
                    marker.setIcon(OverlayImage.fromResource(R.drawable.marker_danger));
                    marker.setHeight(convertDpToPx(getApplicationContext(),40));
                    marker.setWidth(convertDpToPx(getApplicationContext(),40));

                    Log.v("markerPoints", String.valueOf(markerPoints_double.get(i)));

                    markerPoints.add(marker);
                    dangerPointSpeakFlag.add(false);

                    int finalI = i;
                    markerPoints.get(i).setOnClickListener(new Overlay.OnClickListener() {
                        @Override
                        public boolean onClick(@NonNull Overlay overlay) {
                            RidingBottomSheetFragment bottomSheetFragment = new RidingBottomSheetFragment();
                            Bundle bundle = new Bundle();

                            bundle.putInt("dangerId", -1);
                            bundle.putInt("index", finalI);

                            bundle.putString("userId", userId);

                            double[] dangerLocation = {markerPoints_double.get(finalI).get(0), markerPoints_double.get(finalI).get(1)};
                            bundle.putDoubleArray("dangerLocation", dangerLocation);

                            Location location = fusedLocationSource.getLastLocation();
                            double[] myLocation = {location.getLatitude(), location.getLongitude()};
                            bundle.putDoubleArray("myLocation", myLocation);

                            bottomSheetFragment.setArguments(bundle);
                            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());

                            return false;
                        }
                    });
                }
            }
        }

        //text to speak 객체 초기화
        tts = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR){
                    tts.setLanguage(Locale.KOREA);
                    tts.setPitch(1.0f);
                    tts.setSpeechRate(1.0f);
                }
            }
        });


        //userId
        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();

        userId = pref.getString("id", "");

        //ui 객체 할당
        speedText = findViewById(R.id.speedText);
        ridingGoAndStopButton = findViewById(R.id.ridingGoAndStopButton);
        postDangerButton = findViewById(R.id.postDangerButton);
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

        //naver connect
        nApi = NaverRetrofitClient.getRetrofit().create(NaverApiInterface.class);

        //경로선 속성지정
        ridingPointRecordLine.setColor(Color.RED); //색상

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


        //위험 요소 거리 확인 쓰레드
        Handler dangerInfoNearCheckHandler = new Handler();
        Thread dangerInfoNearCheck = new Thread(new Runnable() {
            @Override
            public void run() {
                while(ridingStartFlag){
                    dangerInfoNearCheckHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            for(int j=0;j<markerPoints.size();j++){
                                if(dangerPointSpeakFlag.get(j) == false){
                                    if(50 > (markerPoints.get(j).getPosition().distanceTo(new LatLng(fusedLocationSource.getLastLocation())))){
                                        tts.speak("위험 요소가 근방에 있습니다. 주의하십시오",TextToSpeech.QUEUE_ADD,null);
                                        dangerPointSpeakFlag.set(j,true);
                                    }
                                }
                            }
                        }
                    });
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


        //실시간 속도 표출 thread
        Handler ridingHandler = new Handler();
        Thread ridingThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (fusedLocationSource.isActivated() == false){
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
                        Thread.sleep(1000);
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
                    if(!Double.isNaN(pointDistance)){ //isNaN 에러 대응
                        totalDistance += pointDistance;
                    }

                    //속도 km/h
                    speed = Double.parseDouble(String.format("%.1f", pointDistance * 3.6));

                    //ui 반영
                    ridingHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            speedText.setText(speed+"");
                            distText.setText(String.format("%.1f", pointDistance)+"");
                            if(Double.isNaN(pointDistance)){
                                speedText.setText("0");
                                distText.setText("0");
                            }
                            ridingPointRecordLine.setCoords(ridingPointRecord);
                            ridingPointRecordLine.setMap(naverMapObj);

                            Log.d("RIDING THREAD",
                                    "\ndistance:" + totalDistance +
                                    "\npoint:"+pointDistance+
                                    "\nbefore:"+beforeLatLng.latitude+beforeLatLng.longitude+
                                    "\nnow:"+nowLatLng.latitude+nowLatLng.longitude);
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
                    tts.speak("주행을 시작합니다.",TextToSpeech.QUEUE_FLUSH,null);

                    ridingStartFlag = true;
                    ridingGoAndStopButton.setText("주행 종료");
                    ridingThread.start();

                    if(fromString.equals("findpath")){dangerInfoNearCheck.start();}
                } else if(ridingStartFlag){
                    showMessage();
                }
            }
        });

        postDangerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), InformationWriteActivity.class);
                launcher.launch(intent1);
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

        if(!routePoints.isEmpty()){
            routeline.setColor(Color.BLUE);
            routeline.setMap(naverMap);
        }
        if(!markerPoints.isEmpty()){
            for(int i =0;i<markerPoints.size();i++){
                markerPoints.get(i).setMap(naverMapObj);
            }
        }
        if (!fromString.equals("findpath")) {
            setDangerOnMap();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (endDialog != null && endDialog.isShowing()) {
            endDialog.dismiss();
        }
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
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
                result_second = totalTime_second % 60;
                result_minute = totalTime_second / 60;
                speedLayout.setVisibility(View.GONE);
                resultLayout.setVisibility(View.VISIBLE);
                averageSpeed = (totalDistance/totalTime_second)*3.6;

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
                cameraUpdate.finishCallback(new CameraUpdate.FinishCallback() {
                    @Override
                    public void onCameraUpdateFinish() {

                        //지도 캡쳐
                        naverMapObj.takeSnapshot(false, new NaverMap.SnapshotReadyCallback() {
                            @SuppressLint("WrongThread")
                            @Override
                            public void onSnapshotReady(@NonNull Bitmap bitmap) {
                                //임시 파일 생성 후 서버로 전송
                                file = new File(getApplicationContext().getCacheDir(), HibikeUtils.getRandomString(5)+".png");
                                try {
                                    file.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                //Convert bitmap to byte array
                                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                                    bitmap.compress(Bitmap.CompressFormat.WEBP_LOSSLESS, 100, bos);
                                } else {
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                                }
                                byte[] bitmapdata = bos.toByteArray();

                                //write the bytes in file
                                FileOutputStream fos = null;
                                try {
                                    fos = new FileOutputStream(file);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                                try {
                                    fos.write(bitmapdata);
                                    fos.flush();
                                    fos.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        postRidingMulti.setSouthwestLati(forMakeLatLngBounds.get(0).latitude+"");
                        postRidingMulti.setSouthwestLong(forMakeLatLngBounds.get(0).longitude+"");
                        postRidingMulti.setNortheastLati(forMakeLatLngBounds.get(1).latitude+"");
                        postRidingMulti.setNortheastLong(forMakeLatLngBounds.get(1).longitude+"");

                        startingPoint = ridingPointRecord.get(0);
                        endPoint = ridingPointRecord.get(ridingPointRecord.size() - 1);
                        uniqueId = getUniqueId();

                        // 시작 좌표 -> 지역이름
                        ReverseGeocodingGenerator sprg = new ReverseGeocodingGenerator(startingPoint.longitude, startingPoint.latitude);
                        nApi.getRegion(sprg.getHeaders(), sprg.getQueries()).enqueue(new Callback<ReverseGeocodingDto>() {
                            @Override
                            public void onResponse(Call<ReverseGeocodingDto> call, Response<ReverseGeocodingDto> response) {
                                if (response.isSuccessful()) {
                                    Object obj = response.body().getResult();
                                    String result = HibikeUtils.regionJsonToString(obj);
                                    postRidingMulti.setStartingRegion(result);
                                }
                            }
                            @Override
                            public void onFailure(Call<ReverseGeocodingDto> call, Throwable t) {}
                        });

                        // 종료 좌표 -> 지역이름
                        ReverseGeocodingGenerator eprg = new ReverseGeocodingGenerator(endPoint.longitude, endPoint.latitude);

                        nApi.getRegion(eprg.getHeaders(), eprg.getQueries()).enqueue(new Callback<ReverseGeocodingDto>() {
                            @Override
                            public void onResponse(Call<ReverseGeocodingDto> call, Response<ReverseGeocodingDto> response) {
                                if (response.isSuccessful()) {
                                    Object obj = response.body().getResult();
                                    String result = HibikeUtils.regionJsonToString(obj);
                                    postRidingMulti.setEndRegion(result);
                                }
                            }
                            @Override
                            public void onFailure(Call<ReverseGeocodingDto> call, Throwable t) {}
                        });

                        postRidingMulti.setUserId(userId);
                        postRidingMulti.setUniqueId(uniqueId);
                        postRidingMulti.setRidingTime(result_minute +" : "+result_second);
                        postRidingMulti.setAveSpeed(Double.parseDouble(String.format("%.1f", averageSpeed))+"");
                        postRidingMulti.setDistance(totalDistance+"");

                        //ridingGoal ridingAchievement
                        int ridingGoal = pref.getInt("ridingGoal", 0);
                        if (ridingGoal != 0) {
                            int distanceInt = (int)totalDistance;
                            int nowRidingAchievement = pref.getInt("ridingAchievement",0);
                            nowRidingAchievement += distanceInt;

                            editor.putInt("ridingAchievement",nowRidingAchievement);
                            editor.apply();

                            double percentRiding = (double) nowRidingAchievement / (double) ridingGoal * 100;
                            ProgressBar mainProgressBar = ((MainActivity) MainActivity.context_main).findViewById(R.id.mainProgressBar);

                            mainProgressBar.setProgress((int)percentRiding);
                            TextView mainRidingAchievement = ((MainActivity) MainActivity.context_main).findViewById(R.id.mainRidingAchievement);
                            mainRidingAchievement.setText(Double.parseDouble(String.format("%.1f", (double) nowRidingAchievement / 1000))+"km");
                        }

                        ridingGoAndStopButton.setText("나가기");
                        ridingGoAndStopButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                saveRidingInfo();
                            }
                        });
                    }
                });
                naverMapObj.moveCamera(cameraUpdate);


            }
        }); //종료 확인
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void saveRidingInfo(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("저장하시겠습니까?");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                RequestBody userId = RequestBody.create(MediaType.parse("text/plain"), postRidingMulti.getUserId());
                RequestBody uniqueId = RequestBody.create(MediaType.parse("text/plain"), postRidingMulti.getUniqueId());
                RequestBody ridingTime = RequestBody.create(MediaType.parse("text/plain"), postRidingMulti.getRidingTime());
                RequestBody aveSpeed = RequestBody.create(MediaType.parse("text/plain"), postRidingMulti.getAveSpeed());
                RequestBody distance = RequestBody.create(MediaType.parse("text/plain"), postRidingMulti.getDistance());
                RequestBody startingRegion = RequestBody.create(MediaType.parse("text/plain"), postRidingMulti.getStartingRegion());
                RequestBody endRegion = RequestBody.create(MediaType.parse("text/plain"), postRidingMulti.getEndRegion());
                RequestBody northeastLati = RequestBody.create(MediaType.parse("text/plain"), postRidingMulti.getNortheastLati());
                RequestBody northeastLong = RequestBody.create(MediaType.parse("text/plain"), postRidingMulti.getNortheastLong());
                RequestBody southwestLati = RequestBody.create(MediaType.parse("text/plain"), postRidingMulti.getSouthwestLati());
                RequestBody southwestLong = RequestBody.create(MediaType.parse("text/plain"), postRidingMulti.getSouthwestLong());

                api.postRidingMulti(
                    userId, uniqueId, ridingTime, aveSpeed, distance,
                    startingRegion, endRegion, northeastLati, northeastLong,southwestLati, southwestLong,
                    fileToUpload
                ).enqueue(new Callback<PostRidingMulti>() {
                    @Override
                    public void onResponse(Call<PostRidingMulti> call, Response<PostRidingMulti> response) {
                        if (!response.isSuccessful()) {
                            Log.e("error", response.message());
                        }
                    }
                    @Override
                    public void onFailure(Call<PostRidingMulti> call, Throwable t) {
                        Log.e("t", t.getMessage());
                    }
                });
                finish();
            }
        });

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        endDialog = builder.create();
        endDialog.show();
    }

    ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    setDangerOnMap();
                }
            }
        });

    public void setDangerOnMap() {
        // 위험요소 화면에 표시
        api.getDangerAll().enqueue(new Callback<GetAllDanger>() {
            @Override
            public void onResponse(Call<GetAllDanger> call, Response<GetAllDanger> response) {
                ArrayList<ArrayList<Double>> resultList = response.body().getResult();
                int markerIndex = 0;
                for (ArrayList<Double> result : resultList) {
                    Marker marker = new Marker(new LatLng(result.get(0), result.get(1)));
                    marker.setIcon(OverlayImage.fromResource(R.drawable.marker_danger));
                    marker.setWidth(convertDpToPx(getApplicationContext(),40));
                    marker.setHeight(convertDpToPx(getApplicationContext(),40));

                    informationMarkerList.add(marker);
                    marker.setMap(naverMapObj);

                    int finalIndex = markerIndex;
                    marker.setOnClickListener(new Overlay.OnClickListener() {
                        @Override
                        public boolean onClick(@NonNull Overlay overlay) {
                            RidingBottomSheetFragment bottomSheetFragment = new RidingBottomSheetFragment();
                            Bundle bundle = new Bundle();

                            bundle.putInt("dangerId", result.get(2).intValue());
                            bundle.putInt("index", finalIndex);

                            bundle.putString("userId", userId);

                            double[] dangerLocation = {result.get(0), result.get(1)};
                            bundle.putDoubleArray("dangerLocation", dangerLocation);

                            Location location = fusedLocationSource.getLastLocation();
                            double[] myLocation = {location.getLatitude(), location.getLongitude()};
                            bundle.putDoubleArray("myLocation", myLocation);

                            bottomSheetFragment.setArguments(bundle);
                            bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());

                            return false;
                        }
                    });
                    markerIndex++;
                }
            }
            @Override
            public void onFailure(Call<GetAllDanger> call, Throwable t) {}
        });
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

    //dp단위 px로 변환
    public int convertDpToPx(Context context,int dp){
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    public String getUniqueId() {
        String uniqueId = UUID.randomUUID().toString().substring(0,8);
        return uniqueId;
    }

    @Override
    public void isDelete(boolean isDelete, int index) {
        if (fromString.equals("findpath")) {
            isDangerDeleted = isDelete;
            markerPoints.get(index).setMap(null);
            markerPoints.remove(index);
        } else {
            isDangerDeleted = isDelete;
            informationMarkerList.get(index).setMap(null);
            informationMarkerList.remove(index);
        }

        Toast.makeText(getApplicationContext(), "위험요소가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
    }

    private class SRGCall extends AsyncTask<Call, Void, String> {
        @Override
        public String doInBackground(Call[] params) {
            try {
                Call<ReverseGeocodingDto> call = params[0];
                Response<ReverseGeocodingDto> response = call.execute();
                Object result = response.body().getResult();
                return HibikeUtils.objectToJson(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(String result) {
            String json = result;
            try {
                JSONArray jsonArray = new JSONArray(json);
                JSONObject jsonObject = (JSONObject) jsonArray.opt(0);
                JSONObject regionObject = jsonObject.getJSONObject("region");
                String area1 = regionObject.getJSONObject("area1").getString("name");
                String area2 = regionObject.getJSONObject("area2").getString("name");
                String area3 = regionObject.getJSONObject("area3").getString("name");

                RidingRegion ridingRegion = new RidingRegion();

                ridingRegion.setRegion(area1 + " " + area2 + " " + area3);
                ridingRegion.setUniqueId(uniqueId);
                api.setRidingSRegion(ridingRegion).enqueue(new Callback<RidingRegion>() {
                    @Override
                    public void onResponse(Call<RidingRegion> call, Response<RidingRegion> response) {
                        if (response.isSuccessful()) {
                            Log.v("secess", response.message());
                        }else{
                            Log.e("ridingRigion", response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<RidingRegion> call, Throwable t) {
                        Log.e("ridingRigion", t.toString());
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private class ERGCall extends AsyncTask<Call, Void, String> {
        @Override
        public String doInBackground(Call[] params) {
            try {
                Call<ReverseGeocodingDto> call = params[0];
                Response<ReverseGeocodingDto> response = call.execute();
                Object result = response.body().getResult();
                return HibikeUtils.objectToJson(result);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(String result) {
            String json = result;
            try {
                JSONArray jsonArray = new JSONArray(json);
                JSONObject jsonObject = (JSONObject) jsonArray.opt(0);
                JSONObject regionObject = jsonObject.getJSONObject("region");
                String area1 = regionObject.getJSONObject("area1").getString("name");
                String area2 = regionObject.getJSONObject("area2").getString("name");
                String area3 = regionObject.getJSONObject("area3").getString("name");

                RidingRegion ridingRegion = new RidingRegion();

                ridingRegion.setRegion(area1 + " " + area2 + " " + area3);
                ridingRegion.setUniqueId(uniqueId);
                api.setRidingERegion(ridingRegion).enqueue(new Callback<RidingRegion>() {
                    @Override
                    public void onResponse(Call<RidingRegion> call, Response<RidingRegion> response) {
                            if (response.isSuccessful()) {
                                Log.v("secess", response.message());
                            }else{
                            Log.e("ridingRigion", response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<RidingRegion> call, Throwable t) {
                        Log.e("ridingRigion", t.toString());
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
