package com.roundG0929.hibike.activities.information;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.roundG0929.hibike.R;
import com.roundG0929.hibike.api.information.InformationApi;
import com.roundG0929.hibike.api.information.requestBody.PostInformation;

import org.w3c.dom.Text;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InformationWriteActivity extends AppCompatActivity implements OnMapReadyCallback {
    int SELECT_IMAGE = 2000; //이미지선택 코드


    //request Body
    PostInformation postInformation = new PostInformation();

    //네이버맵 객체
    NaverMap naverMapObj;
    MapFragment mapFragment;

    //ui객체
    EditText titleText;
    TextView latitudeText;
    TextView longitudeText;
    ImageView infoImage;
    Button selectImageButton;
    Button newImageButton;
    EditText contentText;
    Button postButton;

    FrameLayout mapLayout;
    Button selectLocationButton;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informationwrite);

        //로그인, 아이디확인
        postInformation.setId(getSharedPreferences("pref",MODE_PRIVATE).getString("id",""));
        if(postInformation.getId() == ""){
            Toast.makeText(getApplicationContext(),"로그인후 이용하세요.",Toast.LENGTH_SHORT).show();
            finish();
        }else{
            Toast.makeText(getApplicationContext(),postInformation.getId()+" 님",Toast.LENGTH_SHORT).show();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.informap);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fragmentManager.beginTransaction().add(R.id.informap, mapFragment).commit();
        }
        mapFragment.getMapAsync(this::onMapReady);

        titleText = findViewById(R.id.titleText);
        latitudeText = findViewById(R.id.latitudeText);
        longitudeText = findViewById(R.id.longitudeText);
        infoImage = findViewById(R.id.infoImage);
        selectImageButton = findViewById(R.id.selectImageButton);
        newImageButton = findViewById(R.id.newImageButton);
        contentText = findViewById(R.id.contentText);

        mapLayout = findViewById(R.id.mapLayout);
        postButton = findViewById(R.id.postButton);
        selectLocationButton = findViewById(R.id.selectLocationButton);



        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECT_IMAGE);
            }
        });



        //경위도 텍스트 클릭시 맵뷰 보이기
        View.OnClickListener viewMap = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapLayout.setVisibility(View.VISIBLE);
            }
        };
        longitudeText.setOnClickListener(viewMap);
        latitudeText.setOnClickListener(viewMap);

        //맵에서 위치결정 버튼
        selectLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double latitude = naverMapObj.getCameraPosition().target.latitude;
                double longitude = naverMapObj.getCameraPosition().target.longitude;

                latitudeText.setText(latitude+"");
                longitudeText.setText(longitude+"");

                postInformation.setLatitude(latitude);
                postInformation.setLongitude(longitude);

                mapLayout.setVisibility(View.GONE);
            }
        });


        //등록버튼(POST call)
        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(postInformation.getLatitude() == 0 || postInformation.getLongitude() == 0){
                    Toast.makeText(getApplicationContext(),"위치 지정이 필요합니다.",Toast.LENGTH_SHORT).show();
                }else if(titleText.getText().toString().equals("") && contentText.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"제목 또는 내용을 입력해주세요.",Toast.LENGTH_SHORT).show();
                } else{
                    postInformation.setTitle(titleText.getText().toString());
                    postInformation.setContents(contentText.getText().toString());

                    new InformationApi().postDangerInformation(postInformation).enqueue(new Callback<Object>() {
                        @Override
                        public void onResponse(Call<Object> call, Response<Object> response) {

                            if(response.isSuccessful()){
                                Toast.makeText(getApplicationContext(),response.code()+"등록완료.",Toast.LENGTH_SHORT).show();
                            }
                            Toast.makeText(getApplicationContext(),response.code()+"",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Call<Object> call, Throwable t) {

                        }
                    });
                }
            }
        });




    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        naverMapObj = naverMap;

        FusedLocationProviderClient fusedLocationClient;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(InformationWriteActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location);
                Log.d("LATLNG", "onSuccess: " + latLng.latitude+" "+latLng.longitude);
                if(naverMapObj != null){
                    naverMapObj.moveCamera(CameraUpdate.scrollTo(latLng));
                }

            }
        });
    }
}
