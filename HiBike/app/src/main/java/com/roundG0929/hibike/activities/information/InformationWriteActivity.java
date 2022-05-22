package com.roundG0929.hibike.activities.information;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.location.Location;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.core.content.FileProvider;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.roundG0929.hibike.HibikeUtils;
import com.roundG0929.hibike.R;
import com.roundG0929.hibike.api.information.InformationApi;
import com.roundG0929.hibike.api.information.requestBody.PostInformation;
import com.roundG0929.hibike.api.map_route.navermap.MapSetting;
import com.roundG0929.hibike.api.map_route.navermap.NaverApiInterface;
import com.roundG0929.hibike.api.map_route.navermap.NaverRetrofitClient;
import com.roundG0929.hibike.api.map_route.navermap.ReverseGeocodingGenerator;
import com.roundG0929.hibike.api.server.ApiInterface;
import com.roundG0929.hibike.api.server.RetrofitClient;
import com.roundG0929.hibike.api.server.dto.PostDanger;
import com.roundG0929.hibike.api.server.dto.ReverseGeocodingDto;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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
    NaverApiInterface napi;
    ApiInterface api;
    FusedLocationProviderClient fusedLocationClient;

    //ui객체
    EditText titleText;
    ImageView infoImage;
    TextView newImageButton;
    EditText contentText;
    TextView postButton;
    TextView locationText;
    EditText periodText;
    EditText locationDetailText;
    FrameLayout mapLayout;
    Button selectLocationButton;
    TextView cancel;

    Uri imageUri;
    String imageFilePath;
    boolean isFile = false;
    boolean isMapViewOpen = false;

    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    static final int REQUEST_IMAGE_CAPTURE = 672;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informationwrite);

        //로그인, 아이디확인
        postInformation.setId(getSharedPreferences("pref",MODE_PRIVATE).getString("id",""));
        if(postInformation.getId() == ""){
            Toast.makeText(getApplicationContext(),"로그인후 이용하세요.",Toast.LENGTH_SHORT).show();
            finish();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.informap);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fragmentManager.beginTransaction().add(R.id.informap, mapFragment).commit();
        }
        mapFragment.getMapAsync(this::onMapReady);

        //위치 클라이언트
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        // 위경도 -> 지역 이름 (네이버 api)
        napi = NaverRetrofitClient.getRetrofit().create(NaverApiInterface.class);

        //server api
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);

        titleText = findViewById(R.id.titleText);
        infoImage = findViewById(R.id.infoImage);
        newImageButton = findViewById(R.id.newImageButton);
        contentText = findViewById(R.id.contentText);
        locationDetailText = findViewById(R.id.locationDetailText);
        locationText = findViewById(R.id.locationText);
        periodText = findViewById(R.id.periodText);
        cancel = findViewById(R.id.cancel);

        mapLayout = findViewById(R.id.mapLayout);
        postButton = findViewById(R.id.postButton);
        selectLocationButton = findViewById(R.id.selectLocationButton);




        //경위도 텍스트 클릭시 맵뷰 보이기
        View.OnClickListener viewMap = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapLayout.setVisibility(View.VISIBLE);
                isMapViewOpen = true;
            }
        };
        locationText.setOnClickListener(viewMap);


        fusedLocationClient.getLastLocation().addOnSuccessListener(InformationWriteActivity.this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                LatLng latLng = new LatLng(location);
                postInformation.setLatitude(latLng.latitude);
                postInformation.setLongitude(latLng.longitude);

                ReverseGeocodingGenerator nowRegion = new ReverseGeocodingGenerator(latLng.longitude, latLng.latitude);
                napi.getRegion(nowRegion.getHeaders(), nowRegion.getQueries()).enqueue(new Callback<ReverseGeocodingDto>() {
                    @Override
                    public void onResponse(Call<ReverseGeocodingDto> call, Response<ReverseGeocodingDto> response) {
                        if (response.isSuccessful()) {
                            Object obj = response.body().getResult();
                            String result = HibikeUtils.regionJsonToString(obj);
                            locationText.setText(result);
                            postInformation.setRegion(locationText.getText().toString());
                        }
                    }
                    @Override
                    public void onFailure(Call<ReverseGeocodingDto> call, Throwable t) {}
                });
            }
        });

        //맵에서 위치결정 버튼
        selectLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double latitude = naverMapObj.getCameraPosition().target.latitude;
                double longitude = naverMapObj.getCameraPosition().target.longitude;
//                latitudeText.setText(latitude+"");
//                longitudeText.setText(longitude+"");

                postInformation.setLatitude(latitude);
                postInformation.setLongitude(longitude);

                ReverseGeocodingGenerator nowRegion = new ReverseGeocodingGenerator(longitude, latitude);
                napi.getRegion(nowRegion.getHeaders(), nowRegion.getQueries()).enqueue(new Callback<ReverseGeocodingDto>() {
                    @Override
                    public void onResponse(Call<ReverseGeocodingDto> call, Response<ReverseGeocodingDto> response) {
                        if (response.isSuccessful()) {
                            Object obj = response.body().getResult();
                            String result = HibikeUtils.regionJsonToString(obj);
                            locationText.setText(result);
                            postInformation.setRegion(locationText.getText().toString());
                        }
                    }
                    @Override
                    public void onFailure(Call<ReverseGeocodingDto> call, Throwable t) {}
                });

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
                }else if(periodText.getText().equals("")){
                    Toast.makeText(getApplicationContext(),"유지 기간을 설정해주세요.",Toast.LENGTH_SHORT).show();
                } else if (!isFile) {
                    Toast.makeText(getApplicationContext(),"사진을 올려주세요.",Toast.LENGTH_SHORT).show();
                } else {
                    // 버튼 비활성화
                    postButton.setEnabled(false);
                    postButton.setTextColor(Color.parseColor("#939393"));

                    postInformation.setTitle(titleText.getText().toString());
                    postInformation.setContents(contentText.getText().toString());
                    postInformation.setPeriod(Integer.parseInt(periodText.getText().toString()));
                    postInformation.setRegionDetail(locationDetailText.getText().toString());

                    File uploadToFile = new File(imageFilePath);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), uploadToFile);
                    MultipartBody.Part imageFile = MultipartBody.Part.createFormData("file", uploadToFile.getName(), requestBody);
                    RequestBody id = RequestBody.create(MediaType.parse("text/plain"), postInformation.getId());
                    RequestBody title = RequestBody.create(MediaType.parse("text/plain"), postInformation.getTitle());
                    RequestBody content = RequestBody.create(MediaType.parse("text/plain"), postInformation.getContents());
                    RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), postInformation.getLatitude() + "");
                    RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), postInformation.getLongitude() + "");
                    RequestBody region = RequestBody.create(MediaType.parse("text/plain"), postInformation.getRegion());
                    RequestBody region_detail = RequestBody.create(MediaType.parse("text/plain"), postInformation.getRegionDetail());
                    RequestBody period = RequestBody.create(MediaType.parse("text/plain"), postInformation.getPeriod() + "");

                    api.postDanger(
                            imageFile, id, title, content, latitude, longitude, region, region_detail, period
                    ).enqueue(new Callback<PostDanger>() {
                        @Override
                        public void onResponse(Call<PostDanger> call, Response<PostDanger> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "위험요소 등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                setResult(RESULT_OK);
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), response.code() + "", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<PostDanger> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.toString());
                        }
                    });
//                    new InformationApi().postDangerInformation(postInformation).enqueue(new Callback<Object>() {
//                        @Override
//                        public void onResponse(Call<Object> call, Response<Object> response) {
//
//                            if(response.isSuccessful()){
//                                Toast.makeText(getApplicationContext(),response.code()+"등록완료.",Toast.LENGTH_SHORT).show();
//                            }
//                            Toast.makeText(getApplicationContext(),response.code()+"",Toast.LENGTH_SHORT).show();
//                            finish();
//                        }
//
//                        @Override
//                        public void onFailure(Call<Object> call, Throwable t) {
//
//                        }
//                    });
                }
            }
        });
        newImageButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                sendTakePhotoIntent();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
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

    @Override
    public void onBackPressed() {
        if (isMapViewOpen) {
            mapLayout.setVisibility(View.INVISIBLE);
            isMapViewOpen = false;
        } else {
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    //카메라 실행
    private void sendTakePhotoIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File imageFile = null;
            try {
                imageFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }

            if (imageFile != null) {
                imageUri = FileProvider.getUriForFile(this, getPackageName(), imageFile);

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    //촬영 후 처리
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            infoImage.setImageURI(imageUri);
            isFile = true; //이미지 등록 완료
            infoImage.getLayoutParams().height = 600;
            infoImage.getLayoutParams().width = 600;
            infoImage.requestLayout();
        }
    }

    //임시 파일 저장
    private File createImageFile() throws IOException {
        // 파일이름을 세팅 및 저장경로 세팅
        String imageFileName = HibikeUtils.getRandomString(6);
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }
}
