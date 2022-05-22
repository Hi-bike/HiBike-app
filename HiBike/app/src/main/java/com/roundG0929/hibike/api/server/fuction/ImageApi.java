package com.roundG0929.hibike.api.server.fuction;

import android.app.Activity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageApi extends Activity{
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

    public void setImageOnImageView(Activity activity, ImageView imageView, String imageUri) {
        Glide.with(activity).load(imageUri).into(imageView);
    }
}
