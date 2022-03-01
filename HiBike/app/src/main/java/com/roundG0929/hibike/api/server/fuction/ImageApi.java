package com.roundG0929.hibike.api.server.fuction;

import android.app.Activity;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

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
//    String profileImageUrl = "http://10.0.2.2:5000/api/auth/image/"; //로컬

    public String getProfileImageUrl(String id) {
        return this.profileImageUrl+id;
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
        } }


}
