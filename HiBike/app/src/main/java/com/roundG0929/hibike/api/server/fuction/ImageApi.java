package com.roundG0929.hibike.api.server.fuction;

import android.app.Activity;
import android.annotation.SuppressLint;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageApi extends Activity{
    /*
    **이미지 api 사용법**
    1. ImageApi에 이미지 url을 적는다 ex) String profileImageUrl = http://132.226.232.31/api/auth/image/eui
    2. 이미지가 필요한 엑티비티에서 getImage(이미지 뷰 객체, 이미지 url)호출해서 이미지 뷰에 사진을 보여줍니다.
    ex) imageApi.getImage(ivProfile, profileImageUrl)
     */
    Bitmap bitmap;
    String profileImageUrl = "http://132.226.232.31/api/auth/image/";
    String ridingImageUrl = "http://132.226.232.31/api/auth/rimage/";
    String dangerImageUrl = "http://132.226.232.31/api/board/dimage/";
//    String profileImageUrl = "http://10.0.2.2:5000/api/auth/image/"; //로컬

    public String getProfileImageUrl(String id) {
        return this.profileImageUrl+id;
    }
    public String getRidingImageUrl(String unqueId) {
        return this.ridingImageUrl+unqueId;
    }
    public String getDangerImageUrl(String filename) {
        return dangerImageUrl+filename;
    }

    public void getImage(ImageView imageView, String imageUrl){
        new Thread(new Runnable() {
            public void run() {
                try {
                    bitmap = getBitmap(imageUrl);
                }catch(Exception e) { }
                finally {
                    if(bitmap!=null) {
                        runOnUiThread(new Runnable() {
                            @SuppressLint("NewApi")
                            public void run() {
//                                Bitmap rotatedBitmap = RotateBitmap(bitmap, 90);
                                // 변환된 이미지 사용
                                imageView.setImageBitmap(bitmap);
                            }
                        });
                    }
                }
            }
        }).start();
    }

    private Bitmap getBitmap(String url) {
        HttpURLConnection connection = null;
        Bitmap retBitmap = null;
        try{
            URL imgUrl = new URL(url);
            connection = (HttpURLConnection) imgUrl.openConnection();
            connection.setDoInput(true);
            connection.connect(); //연결
            InputStream is = connection.getInputStream(); //
            retBitmap = BitmapFactory.decodeStream(is);
        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            if(connection!=null) {
                connection.disconnect();
            }
            return retBitmap;
        }
    }
    public Bitmap RotateBitmap(Bitmap source, float angle)
    {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}
