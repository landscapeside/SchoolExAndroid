package com.utils.datahelper;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.lang.reflect.Type;

public class JSONS {

    private static Gson sGson = null;
    private static JsonParser sParser = new JsonParser();

    static {
        sGson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
    }

    public static <T> T parseObject(String json, Type type) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        try {
            return (T) sGson.fromJson(json, type);
        } catch (Exception e) {
        }
        return null;
    }

    public static String parseJson(Object o) {
        if (o == null) {
            return null;
        }
        try {
            return sGson.toJson(o);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * convert string to JsonObject
     *
     * @param content
     * @return
     */
    public static JsonObject parseJsonObject(String content) {
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        return sParser.parse(content).getAsJsonObject();
    }

    public static JsonArray parseJsonArray(String content){
        if (TextUtils.isEmpty(content)) {
            return null;
        }
        return sParser.parse(content).getAsJsonArray();
    }
}
