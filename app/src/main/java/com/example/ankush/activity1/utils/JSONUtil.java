package com.example.ankush.activity1.utils;

import android.util.JsonReader;

import com.example.ankush.activity1.models.PointOfInterest;
import com.example.ankush.activity1.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;

public class JSONUtil {

    public static JSONObject ParseJSONObject(InputStream inputStream) {

        JSONObject obj  = null;

        try {
            JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
            if(jsonReader.hasNext())
            {
                obj = new JSONObject();
                jsonReader.beginObject();
                while(jsonReader.hasNext())
                {
                    String key = jsonReader.nextName();
                    String value = jsonReader.nextString();
                    obj.put(key, value);
                }
                System.out.println("Making the call");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return obj;

    }

    public static JSONObject GetUserJsonObject(String email, String password) {
        JSONObject object = new JSONObject();
        try {
            object.put("email", email);
            object.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
    }

    public static JSONObject GetPoiJsonObject(PointOfInterest poi) {
        JSONObject object = new JSONObject();
        try{
            object.put("id", poi.getId());
            object.put("title", poi.getTitle());
            object.put("description", poi.getDescription());
        } catch(JSONException e) {
            e.printStackTrace();;
        }

        return object;
    }

    public static JSONObject GetPoiJsonObject(String title, String description) {
        JSONObject object = new JSONObject();
        try{
            object.put("title", title);
            object.put("description", description);
        } catch(JSONException e) {
            e.printStackTrace();;
        }

        return object;
    }

    public static User GetUserObject(JSONObject obj) {
        User user = null;

        try{
            if(obj != null) {
                user = new User(obj.getInt("id"), obj.getString("email"), obj.getString("password"), obj.getInt("isAdmin"));
            }
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    public static PointOfInterest GetPoiObject(JSONObject obj) {
        PointOfInterest poi = null;

        try {
            if(obj != null)
                poi = new PointOfInterest(obj.getInt("id"), obj.getString("title"), obj.getString("description"));
        } catch(JSONException e) {
            e.printStackTrace();
        }

        return poi;
    }

}
