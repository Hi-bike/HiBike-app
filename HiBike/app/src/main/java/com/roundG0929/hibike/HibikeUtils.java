package com.roundG0929.hibike;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Random;

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

    public static ArrayList<String> regionJsonToArray(Object obj) {
        ArrayList<String> result = new ArrayList<>();

        String json = new Gson().toJson(obj);
        try {
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject = (JSONObject) jsonArray.opt(0);
            JSONObject regionObject = jsonObject.getJSONObject("region");
            String area1 = regionObject.getJSONObject("area1").getString("name");
            String area2 = regionObject.getJSONObject("area2").getString("name");
            String area3 = regionObject.getJSONObject("area3").getString("name");

            result.add(area1);
            result.add(area2);
            result.add(area3);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getRandomString(int length) {
        String result = "";
        Random rnd = new Random();
        for (int i = 0; i < length; i++) {
            int rIndex = rnd.nextInt(3);
            switch (rIndex) {
                case 0:
                    result += (char) ((int) (rnd.nextInt(26)) + 97);
                    break;
                case 1:
                    result += ((char) ((int) (rnd.nextInt(26)) + 65));
                    break;
                case 2:
                    result += ((rnd.nextInt(10)));
                    break;
            }
        }
        return result;
    }
}
