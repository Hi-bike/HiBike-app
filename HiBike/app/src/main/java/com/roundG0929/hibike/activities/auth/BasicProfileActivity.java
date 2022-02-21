package com.roundG0929.hibike.activities.auth;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.roundG0929.hibike.MainActivity;
import com.roundG0929.hibike.R;
import com.roundG0929.hibike.api.server.ApiInterface;
import com.roundG0929.hibike.api.server.RetrofitClient;
import com.roundG0929.hibike.api.server.dto.BasicProfile;
import com.roundG0929.hibike.api.server.dto.ProfileImage;
import com.roundG0929.hibike.api.server.dto.Signout;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasicProfileActivity extends Activity {

    ApiInterface api;
    TextView textId, btnSignout;
    EditText editNickname;
    ImageButton btnClose;
    Button btnOk;
    ImageView ivProfileImage;
    String oriNickname, mediaPath, id;
    boolean isImageChanged=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_basic_profile);

        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        id = pref.getString("id", "");

        //hibike server api
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);
        //닉네임, 프로필 이미지, 아이디 받아오기
        getProfile(id);

        textId = (TextView) findViewById(R.id.text_profile_id);
        editNickname = (EditText) findViewById(R.id.edit_profile_nickname);

        //닫기
        btnClose = (ImageButton)findViewById(R.id.btn_profile_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

        //프로필이미지 변경
        ivProfileImage = (ImageView) findViewById(R.id.iv_profile_image);
        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isImageChanged = true;
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

        TextView btnSigninOrNickname = ((MainActivity)MainActivity.context_main).findViewById(R.id.btn_signin_or_nickname);
        ImageView profileImage = ((MainActivity)MainActivity.context_main).findViewById(R.id.iv_profile_image);
        oriNickname = btnSigninOrNickname.getText().toString();

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newNickname = editNickname.getText().toString();
                //닉네임 변경 여부
                if(!(oriNickname.equals(newNickname) || newNickname.equals(""))){
                    //닉네임 변경 api
                    BasicProfile nicknameProfile = new BasicProfile();
                    nicknameProfile.setId(id);
                    nicknameProfile.setNickname(newNickname);
                    api.setNickname(nicknameProfile).enqueue(new Callback<BasicProfile>() {
                        @Override
                        public void onResponse(Call<BasicProfile> call, Response<BasicProfile> response) {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }

                        @Override
                        public void onFailure(Call<BasicProfile> call, Throwable t) {
                            Toast.makeText(getApplicationContext(), "통신 실패", Toast.LENGTH_SHORT);
                        }
                    });
                }
                //이미지 변경 여부
                if(isImageChanged){
                    if (mediaPath != null) {
                        File file = new File(mediaPath);
                        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                        RequestBody idToUpload = RequestBody.create(MediaType.parse("text/plain"), id);
                        api.setProfile(fileToUpload, idToUpload).enqueue(new Callback<ProfileImage>() {
                            @Override
                            public void onResponse(Call<ProfileImage> call, Response<ProfileImage> response) {
                                if (response.isSuccessful()) {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onFailure(Call<ProfileImage> call, Throwable t) {
                                Toast.makeText(getApplicationContext(), "통신 실패", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                finish();
            }
        });

        //로그아웃 버튼
        btnSignout = (TextView) findViewById(R.id.btn_signout);
        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                api.signout(id).enqueue(new Callback<Signout>() {
                    @Override
                    public void onResponse(Call<Signout> call, Response<Signout> response) {
                        if (response.isSuccessful()) {}
                        else {
                            Toast.makeText(getApplicationContext(), "서버 에러", Toast.LENGTH_SHORT);
                        }
                    }
                    @Override
                    public void onFailure(Call<Signout> call, Throwable t) {
                        System.out.println("error "+t.toString());
                    }
                });
                editor.remove("id");
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getProfile(String id){
        api.getProfile(id).enqueue(new Callback<BasicProfile>() {
            @Override
            public void onResponse(Call<BasicProfile> call, Response<BasicProfile> response) {
                if(response.isSuccessful()){
                    String id = response.body().getId();
                    String nickname = response.body().getNickname();
                    textId.setText(id);
                    editNickname.setText(nickname);
                }else{
                    editNickname.setText("알 수 없는 오류");
                }
            }
            @Override
            public void onFailure(Call<BasicProfile> call, Throwable t) {
                editNickname.setText("알 수 없는 오류");
            }
        });
    }

    @SuppressLint("Range")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data != null) {
                Uri selectedImage = data.getData();

                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                ivProfileImage.setImageBitmap(bitmap);

                Cursor cursor = getContentResolver().query(Uri.parse(selectedImage.toString()), null, null, null, null);
                assert cursor != null;
                cursor.moveToFirst();
                mediaPath = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            }
        else{
            Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_SHORT);
        }
    }
}