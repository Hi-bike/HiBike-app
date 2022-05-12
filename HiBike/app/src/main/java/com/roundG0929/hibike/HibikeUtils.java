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

    public static String regionJsonToString(Object obj) {
        String json = new Gson().toJson(obj);
        Log.v("json", json);
        try {
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject = (JSONObject) jsonArray.opt(0);
            JSONObject regionObject = jsonObject.getJSONObject("region");
            String area1 = regionObject.getJSONObject("area1").getString("name");
            String area2 = regionObject.getJSONObject("area2").getString("name");
            String area3 = regionObject.getJSONObject("area3").getString("name");

            if (!jsonArray.isNull(2)) {
                JSONObject jsonObject2 = (JSONObject) jsonArray.opt(2);
                JSONObject regionObject2 = jsonObject2.getJSONObject("land");
                String number1 = regionObject2.getString("number1");
                String number2 = regionObject2.getString("number2");
                String name = regionObject2.getString("name");
                if (!number2.equals("")) {
                    return area1 + " " + area2 + " " + area3 + " " + name + " " + number1 + "-" + number2;
                }
                return area1 + " " + area2 + " " + area3 + " " + name + " " + number1;

            } else {
                String landNumber1 = jsonObject.getJSONObject("land").getString("number1");
                return area1 + " " + area2 + " " + area3 + " " + landNumber1;
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static ArrayList<String> regionJsonToArray(Object obj) {
        String json = new Gson().toJson(obj);
        ArrayList<String> result = new ArrayList<>();
        Log.v("json", json);
        try {
            JSONArray jsonArray = new JSONArray(json);
            JSONObject jsonObject = (JSONObject) jsonArray.opt(0);
            JSONObject regionObject = jsonObject.getJSONObject("region");
            String area1 = regionObject.getJSONObject("area1").getString("name");
            String area2 = regionObject.getJSONObject("area2").getString("name");
            String area3 = regionObject.getJSONObject("area3").getString("name");

            if (!jsonArray.isNull(2)) {
                JSONObject jsonObject2 = (JSONObject) jsonArray.opt(2);
                JSONObject regionObject2 = jsonObject2.getJSONObject("land");
                String number1 = regionObject2.getString("number1");
                String number2 = regionObject2.getString("number2");
                String name = regionObject2.getString("name");
                if (!number2.equals("")) {
                    result.add(area1);
                    result.add(area2);
                    result.add(area3);
                    result.add(name);
                    result.add(number1);
                    result.add(number2);
                    return result;
                }
                result.add(area1);
                result.add(area2);
                result.add(area3);
                result.add(name);
                result.add(number1);
                return result;

            } else {
                String landNumber1 = jsonObject.getJSONObject("land").getString("number1");
                result.add(area1);
                result.add(area2);
                result.add(area3);
                result.add(landNumber1);
                return result;
            }

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
