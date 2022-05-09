package com.roundG0929.hibike.activities.map_route;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.gun0912.tedpermission.normal.TedPermission;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.widget.CompassView;
import com.naver.maps.map.widget.ZoomControlView;
import com.roundG0929.hibike.MainActivity;
import com.roundG0929.hibike.R;
import com.roundG0929.hibike.api.information.InformationApi;
import com.roundG0929.hibike.api.information.dto.DangerInformation_Points;
import com.roundG0929.hibike.api.map_route.graphhopperRoute.MapRouteApi;
import com.roundG0929.hibike.api.map_route.graphhopperRoute.map_routeDto.GraphhopperResponse;
import com.roundG0929.hibike.api.map_route.navermap.AfterRouteMap;
import com.roundG0929.hibike.api.map_route.navermap.FirstNaverMapSet;
import com.roundG0929.hibike.api.map_route.navermap.MapSetting;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import gun0912.tedkeyboardobserver.BaseKeyboardObserver;
import gun0912.tedkeyboardobserver.TedKeyboardObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FindPathActivity extends AppCompatActivity implements OnMapReadyCallback {
    //지도, 길찾기 변수(객체)
    NaverMap naverMapObj;
    MapFragment mapFragment;
    private FusedLocationSource fusedLocationSource;
    private static final int NAVER_LOCATION_PERMISSION_CODE = 1000;
    private FusedLocationProviderClient fusedLocationProviderClient;
    boolean trackingflag = false;  //위치추적flag
    Handler     handler=new Handler();
    MapSetting mapSetting = new MapSetting();
    LatLng[] startEndPoint = new LatLng[2]; //0 start, 1 end
    int startOrendFlag = -1; //0 start, 1 end
    ArrayList<ArrayList<Double>> routePoints_ForIntent = new ArrayList<>(); //라이딩 액티비티로 넘기기위한 경로 좌표
    ArrayList<ArrayList<Double>> markerPoints_ForIntent = new ArrayList<>(); //라이딩 액티비티로 넘기기위한 마커 좌표


    //ui 객체
    EditText startText;
    EditText endText;
    ImageButton startTextCancle;
    ImageButton endTextCancle;
    ImageButton findButton;
    ImageButton changeButton;
    LinearLayout uiLayout;
    CardView findPathCardView;
    LinearLayout selectlayout;
    Button nowlocationButton;
    Button frommapButton;
    Button fromsearchButton;
    Button fromMapSelectButton;//지도에서선택 이후 결정 버튼
    ImageView targetPoint;
    Button ridingStartButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpath);

        //ui 객체 할당
        startText = findViewById(R.id.startText);
        endText = findViewById(R.id.endText);
        startTextCancle = findViewById(R.id.startTextCancle);
        endTextCancle = findViewById(R.id.endTextCancle);
        findButton = findViewById(R.id.findButton);
        changeButton = findViewById(R.id.changeButton);
        uiLayout = findViewById(R.id.uiLayout);
        findPathCardView = findViewById(R.id.findPathCardView);
        selectlayout = findViewById(R.id.selectlayout);
        nowlocationButton = findViewById(R.id.nowlocationButton);
        frommapButton = findViewById(R.id.frommapButton);
        fromsearchButton = findViewById(R.id.fromsearchButton);
        fromMapSelectButton = findViewById(R.id.fromMapSelectButton);
        targetPoint = findViewById(R.id.targetPoint);
        ridingStartButton = findViewById(R.id.ridingStartButton);

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
        Button button = findViewById(R.id.button);
        button.setText("test");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(trackingflag == false){
                    trackingflag = true;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (trackingflag){
                                Location location = fusedLocationSource.getLastLocation();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        startText.setText(location.getLatitude() + ", " + location.getLongitude());
                                    }
                                });
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                }
                else if(trackingflag == true){trackingflag = false;}
//                Location location = fusedLocationSource.getLastLocation();
//                EditText editText = findViewById(R.id.startText);
//                editText.setText("");
//                editText.append(location.getLatitude() + ", " + location.getLongitude());
            }
        });

            //출발, 도착 editText
        startText.setTextIsSelectable(true);
        startText.setShowSoftInputOnFocus(false);
        startText.setLongClickable(false);
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
        startText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(startText.isFocused()){
                    startText.clearFocus();
                }
            }
        });
        endText.setTextIsSelectable(true);
        endText.setShowSoftInputOnFocus(false);
        endText.setLongClickable(false);
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
        endText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(endText.isFocused()){
                    endText.clearFocus();
                }
            }
        });
        new TedKeyboardObserver(this).listen(new BaseKeyboardObserver.OnKeyboardListener() { //키보드 상태변환 리스너(show, hide)
            @Override
            public void onKeyboardChange(boolean isShow) {
                if(!isShow){
                    startText.clearFocus();
                    endText.clearFocus();
                }
            }
        });

            //start, end 취소 버튼
        startTextCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startText.setText("");
                startEndPoint[0] = null;
            }
        });
        endTextCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                endText.setText("");
                startEndPoint[1] = null;
            }
        });

            //위치소스선택 버튼
                //내위치
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

                startText.clearFocus();
                endText.clearFocus();
            }
        });
                //지도에서 선택
        frommapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(startText.isFocused()){startOrendFlag = 0;}
                else if(endText.isFocused()){startOrendFlag = 1;}
                findPathCardView.setVisibility(View.GONE);
                fromMapSelectButton.setVisibility(View.VISIBLE);
                targetPoint.setVisibility(View.VISIBLE);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(startText.getWindowToken(),0);

                startText.clearFocus();
                endText.clearFocus();
            }
        });
                //검색
        fromsearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent searchPlaceintent = new Intent(getApplicationContext(),SearchPlaceActivity.class);
                Location location = fusedLocationSource.getLastLocation();
                searchPlaceintent.putExtra("longitude",location.getLongitude());
                searchPlaceintent.putExtra("latitude",location.getLatitude());

                //0 이면 출발(start), 1 이면 도착(end)
                if(startText.isFocused()){
                    searchPlaceintent.putExtra("startOrend",0);
                }else if(endText.isFocused()){
                    searchPlaceintent.putExtra("startOrend",1);
                }

                startText.clearFocus();
                endText.clearFocus();

                searchPlaceForResult.launch(searchPlaceintent);
            }
        });
        //fromMapButton 후 위치 결정 버튼
        fromMapSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(startOrendFlag == 0){
                    setStartOrend(0,naverMapObj.getCameraPosition().target);
                }else if(startOrendFlag == 1){
                    setStartOrend(1,naverMapObj.getCameraPosition().target);
                }
                startOrendFlag = -1;
                findPathCardView.setVisibility(View.VISIBLE);
                fromMapSelectButton.setVisibility(View.GONE);
                targetPoint.setVisibility(View.GONE);
            }
        });

            //길찾기버튼 findButton, 선그리기 객체
        PathOverlay pathOverlay = new PathOverlay(); //경로그리기 객체
        ArrayList<PathOverlay> areaTestPathOverlay = new ArrayList<>(); //영역그리기 객체
        ArrayList<Marker> informationMarkerList = new ArrayList<>();
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(areaTestPathOverlay.size() != 0){
//                    for(int i = 0; coordsForDrawLine.size()-1>i;i++){
//                        areaTestPathOverlay.get(i).setMap(null);
//                    }
//                }

                if(startEndPoint[0] == null || startEndPoint[1] == null){
                    Toast.makeText(getApplicationContext(),"경로 지정이 필요합니다.",Toast.LENGTH_SHORT).show();
                }else{
                    new MapRouteApi().getApi(startEndPoint[0],startEndPoint[1]).enqueue(new Callback<GraphhopperResponse>() {
                        @Override
                        public void onResponse(Call<GraphhopperResponse> call, Response<GraphhopperResponse> response) {
                            GraphhopperResponse graphhopperResponse = new GraphhopperResponse();
                            graphhopperResponse = response.body();
                            ArrayList<ArrayList<Double>> pointList = new ArrayList<>();
                            pointList = graphhopperResponse.getPaths().get(0).getPoints().getCoordinates();
                            ArrayList<LatLng> coordsForDrawLine = new ArrayList<>();

                            for(int i = 0;i<pointList.size();i++){
                                coordsForDrawLine.add(new LatLng(pointList.get(i).get(1),pointList.get(i).get(0)));
                                routePoints_ForIntent.add(new ArrayList<>(Arrays.asList(pointList.get(i).get(1),pointList.get(i).get(0))));
                            }
                            for (int j = 0;j<coordsForDrawLine.size();j++){

                            }

                            //경로그리기
                            pathOverlay.setCoords(coordsForDrawLine);
                            pathOverlay.setColor(Color.BLUE);
                            pathOverlay.setMap(naverMapObj);


                            //위험정보 요청 바디 설정
                            InformationApi informationApi = new InformationApi();


                            //위험정보요청 영역 지정
                            for(int i = 0; coordsForDrawLine.size()-1>i;i++){
                                areaTestPathOverlay.add(new PathOverlay());
                                areaTestPathOverlay.get(i).setColor(Color.GREEN);
                                areaTestPathOverlay.get(i).setCoords(new PathAreaCheckTest().getAreaPoints(coordsForDrawLine.get(i),coordsForDrawLine.get(i+1)));
                                areaTestPathOverlay.get(i).setMap(naverMapObj);


                                List<LatLng> informationAreaPoints_Latlng = new PathAreaCheckTest().getAreaPoints(coordsForDrawLine.get(i),coordsForDrawLine.get(i+1));
                                List<Double> informationAreaPoints_Double = new ArrayList<>();
                                for(int j = 0;j<4;j++){
                                    informationAreaPoints_Double.add(informationAreaPoints_Latlng.get(j).latitude);
                                    informationAreaPoints_Double.add(informationAreaPoints_Latlng.get(j).longitude);
                                }
                                informationApi.dangerInformationRequestBody.danger_range.add(informationAreaPoints_Double);


//                                PathOverlay areaTestPathOverlay = new PathOverlay();
//                                areaTestPathOverlay.setColor(Color.GREEN);
//                                areaTestPathOverlay.setCoords(new PathAreaCheckTest().getAreaPoints(coordsForDrawLine.get(i),coordsForDrawLine.get(i+1)));
//                                areaTestPathOverlay.setMap(naverMapObj);


                            }

                            //위험정보 요청
                            informationApi.getDangerPointsApiRaw(informationApi.dangerInformationRequestBody).enqueue(new Callback<DangerInformation_Points>() {
                                @Override
                                public void onResponse(Call<DangerInformation_Points> call, Response<DangerInformation_Points> response) {
                                    if(response.body().danger_list.size() != 0){
                                        for(int i =0;i<response.body().danger_list.size();i++){
                                            informationMarkerList.add(
                                                    new Marker(
                                                            new LatLng(response.body().danger_list.get(i).get(0),
                                                                    response.body().danger_list.get(i).get(1))));
                                            markerPoints_ForIntent.add(
                                                    new ArrayList<>(
                                                            Arrays.asList(
                                                                    response.body().danger_list.get(i).get(0),
                                                                    response.body().danger_list.get(i).get(1))));
                                        }
                                        for(int j=0;j<informationMarkerList.size();j++){
                                            informationMarkerList.get(j).setMap(naverMapObj);
                                        }
                                    }
                                }

                                @Override
                                public void onFailure(Call<DangerInformation_Points> call, Throwable t) {

                                }
                            });



                            //마커클릭 리스너
//                            Marker marker = new Marker();
//                            marker.setPosition(startEndPoint[0]);
//                            marker.setMap(naverMapObj);
//                            marker.setOnClickListener(new Overlay.OnClickListener() {
//                                @Override
//                                public boolean onClick(@NonNull Overlay overlay) {
//                                    naverMapObj.setContentPadding(0,convertDpToPx(getApplicationContext(),140),
//                                            0,convertDpToPx(getApplicationContext(),300));
//                                    CameraUpdate cameraUpdate = CameraUpdate.scrollTo(marker.getPosition());
//                                    naverMapObj.moveCamera(cameraUpdate);
//                                    LinearLayout linearLayout = findViewById(R.id.informationBottomSheet);
//                                    BottomSheetBehavior behavior = BottomSheetBehavior.from(linearLayout);
//                                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
//                                    naverMapObj.setContentPadding(0,0,0,0);
//                                    return false;
//                                }
//                            });

                            //ui개선을 위한 경로선 보이기
                            ArrayList<LatLng> forMakeLatLngBounds = getLatLngBounds(coordsForDrawLine);
                            LatLngBounds latLngBounds = new LatLngBounds(forMakeLatLngBounds.get(0),forMakeLatLngBounds.get(1));
                            CameraUpdate cameraUpdate = CameraUpdate.fitBounds(latLngBounds,
                                    convertDpToPx(getApplicationContext(),60),
                                    convertDpToPx(getApplicationContext(),200),
                                    convertDpToPx(getApplicationContext(),60),
                                    convertDpToPx(getApplicationContext(),100));
                            cameraUpdate.animate(CameraAnimation.Easing);
                            naverMapObj.moveCamera(cameraUpdate);


                            //주행시작 버튼 표시
                            ridingStartButton.setVisibility(View.VISIBLE);
                        }
                        @Override
                        public void onFailure(Call<GraphhopperResponse> call, Throwable t) {
                            Toast.makeText(getApplicationContext(),"불러오기 실패",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
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

        ridingStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String routePointsString = "";
                for(int i = 0; i<routePoints_ForIntent.size();i++){
                    routePointsString = routePointsString + routePoints_ForIntent.get(i).get(0) + ", " + routePoints_ForIntent.get(i).get(1) + "\n";
                }
                Log.d("POINTS", "routePointsString: " + routePointsString);

                String markerPointsSting = "";
                for(int i = 0; i<markerPoints_ForIntent.size();i++){
                    markerPointsSting = markerPointsSting + routePoints_ForIntent.get(i).get(0) + ", " + routePoints_ForIntent.get(i).get(1) + "\n";
                }
                Log.d("POINTS", "markerPointsSting: " + markerPointsSting);

                Intent intent = new Intent(getApplicationContext(),RidingActivity.class);
                intent.putExtra("route_point",routePoints_ForIntent);
                intent.putExtra("marker_point",markerPoints_ForIntent);
                
            }
        });




        // test--------------------------------------------------------------  위험지점






    }//onCreate()

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }//onFinish()
    //Lifecycle
    //Lifecycle



    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        naverMapObj = naverMap;

        mapSetting.routeActivityMapSet(naverMapObj,getApplicationContext(),fusedLocationSource);

        CompassView compassView = findViewById(R.id.compass);
        compassView.setMap(naverMapObj);

        naverMapObj.addOnCameraIdleListener(new NaverMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                naverMap.addOnCameraIdleListener(() -> {
                    CameraPosition position = naverMap.getCameraPosition();
                    if(targetPoint.getVisibility()==View.VISIBLE){

                    }
                });
            }
        });

    }



    private void setStartOrend(int startOrend,LatLng latLng){
        LatLng inputLatlng = new LatLng(Double.parseDouble(String.format("%.6f",latLng.latitude)),
                                        Double.parseDouble(String.format("%.6f",latLng.longitude)));
        String latlngString = inputLatlng.latitude + ", " + inputLatlng.longitude;
        if(startOrend == 0){
            startEndPoint[0] = inputLatlng;
            startText.setText(latlngString);
        }else if(startOrend == 1){
            startEndPoint[1] = inputLatlng;
            endText.setText(latlngString);
        }
    }


    //검색 액티비티
    ActivityResultLauncher<Intent> searchPlaceForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == 101){
                        Intent intent = result.getData();
                        int startOrend = intent.getIntExtra("startOrend",-1);
                        LatLng searchplaceLatlng = new LatLng(Double.parseDouble(intent.getStringExtra("y")),Double.parseDouble(intent.getStringExtra("x")));
                        String place_name = intent.getStringExtra("place_name");
                        Toast.makeText(getApplicationContext(),place_name,Toast.LENGTH_SHORT).show();
                        if(startOrend == 0){
                            startEndPoint[0] = searchplaceLatlng;
                            startText.setText(searchplaceLatlng.latitude + ", " + searchplaceLatlng.longitude);

                        }else if(startOrend == 1){
                            startEndPoint[1] = searchplaceLatlng;
                            endText.setText(searchplaceLatlng.latitude + ", " + searchplaceLatlng.longitude);
                        }else{
                            Toast.makeText(getApplicationContext(),"검색 위치 불러오기 오류입니다.",Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
    );


    //남서쪽 북동쪽 좌표 구하기 (FOR 길찾기 후 카메라 위치이동)
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
    //dp단위 px로 변환
    public int convertDpToPx(Context context,int dp){
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
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
