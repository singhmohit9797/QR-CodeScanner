package com.example.ankush.activity1.utils;

import android.util.JsonReader;

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

}
