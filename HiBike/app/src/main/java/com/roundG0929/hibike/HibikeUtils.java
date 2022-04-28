package com.roundG0929.hibike;

import com.google.gson.Gson;

public class HibikeUtils {
    public static String objectToJson(Object object) {
        return new Gson().toJson(object);
    }

}
