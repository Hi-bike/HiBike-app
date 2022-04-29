package com.roundG0929.hibike.activities.auth;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

import com.bumptech.glide.Glide;
import com.roundG0929.hibike.MainActivity;
import com.roundG0929.hibike.R;
import com.roundG0929.hibike.api.server.ApiInterface;
import com.roundG0929.hibike.api.server.RetrofitClient;
import com.roundG0929.hibike.api.server.dto.BasicProfile;
import com.roundG0929.hibike.api.server.dto.ProfileImage;
import com.roundG0929.hibike.api.server.dto.Signout;
import com.roundG0929.hibike.api.server.fuction.ImageApi;

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
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ImageApi imageApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_basic_profile);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();
        id = pref.getString("id", "");

        textId = (TextView) findViewById(R.id.text_profile_id);
        editNickname = (EditText) findViewById(R.id.edit_profile_nickname);
        ivProfileImage = (ImageView) findViewById(R.id.iv_profile_image);

        //hibike server api
        api = RetrofitClient.getRetrofit().create(ApiInterface.class);

        //모달에 띄어줄 닉네임, 아이디 받아오기
        getProfile();

        //프로필 이미지 받아서 이미지뷰에 보여주기
        imageApi = new ImageApi();
        imageApi.getImage(ivProfileImage, imageApi.getProfileImageUrl(id));

        //프로필 이미지
        ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isImageChanged = true;
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });

        //닫기
        btnClose = (ImageButton)findViewById(R.id.btn_profile_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });

        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setProfile();
            }
        });

        //로그아웃 버튼
        btnSignout = (TextView) findViewById(R.id.btn_signout);
        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signout();
            }
        });
    }


    private void getProfile(){
        api.getProfile(id).enqueue(new Callback<BasicProfile>() {
            @Override
            public void onResponse(Call<BasicProfile> call, Response<BasicProfile> response) {
                if(response.isSuccessful()){
                    String user_id = response.body().getId();
                    textId.setText(user_id);
                    editNickname.setText(response.body().getNickname());
                }else{
                    Toast.makeText(getApplicationContext(), "서버 오류", Toast.LENGTH_SHORT);
                }
            }
            @Override
            public void onFailure(Call<BasicProfile> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "통신 실패", Toast.LENGTH_SHORT);
            }
        });
    }
    private void setProfile(){
        TextView btnSigninOrNickname = ((MainActivity)MainActivity.context_main).findViewById(R.id.btn_signin_or_nickname);
        oriNickname = btnSigninOrNickname.getText().toString();
        String newNickname = editNickname.getText().toString();
        //닉네임 변경 여부
        if(!(oriNickname.equals(newNickname) || newNickname.equals(""))){
            //닉네임 변경 api
            Log.v("profile_nickname","success");
            BasicProfile nicknameProfile = new BasicProfile();
            nicknameProfile.setId(id);
            nicknameProfile.setNickname(newNickname);
            api.setNickname(nicknameProfile).enqueue(new Callback<BasicProfile>() {
                @Override
                public void onResponse(Call<BasicProfile> call, Response<BasicProfile> response) {
                    if (response.isSuccessful()) {
                        if (!isImageChanged){
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), response.code()+"", Toast.LENGTH_SHORT);
                    }
                }
                @Override
                public void onFailure(Call<BasicProfile> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "통신 실패", Toast.LENGTH_SHORT);
                }
            });
        }
        if(isImageChanged){ //이미지 변경 여부
            if (mediaPath != null) {
                File file = new File(mediaPath);
                RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
                RequestBody idToUpload = RequestBody.create(MediaType.parse("text/plain"), id);
                api.setProfile(fileToUpload, idToUpload).enqueue(new Callback<ProfileImage>() {
                    @Override
                    public void onResponse(Call<ProfileImage> call, Response<ProfileImage> response) {
                        if (response.isSuccessful()) {
                            Log.v("profile_image","success");
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        } else {
                            Log.v("profile_image",response.code()+"");
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
    private void signout(){
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
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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
                Glide.with(this).load(selectedImage).into(ivProfileImage);

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