package com.roundG0929.hibike;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.google.gson.Gson;

public class HibikeUtils {
    Context context;

    public HibikeUtils(){}

    public HibikeUtils(Context context) {
        this.context = context;
    }

    public void setTextViewFromAnotherActivity(String text, int viewId) {
        TextView tv = ((Activity) context).findViewById(viewId);
        tv.setText(text);
    }

    public static String objectToJson(Object object) {
        return new Gson().toJson(object);
    }


}
