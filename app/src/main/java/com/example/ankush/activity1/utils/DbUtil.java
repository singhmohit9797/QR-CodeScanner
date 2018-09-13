package com.example.ankush.activity1.utils;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class DbUtil {



    public static InputStream SendPostRequest(String url, JSONObject obj)
    {
        HttpURLConnection connection = InitiateConnection(url, "POST");
        if(connection == null)
            return null;

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
            writer.write(obj.toString());
            writer.flush();
            writer.close();
            connection.connect();
            return connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static InputStream SendGetRequest(String url)
    {
        HttpURLConnection connection = InitiateConnection(url, "GET");

        if(connection == null)
            return null;

        try {
            connection.connect();
            return connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static HttpURLConnection InitiateConnection(String url, String requestMethod)
    {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod(requestMethod);

            if(requestMethod.equals("POST"))
            {
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);
            }
            return connection;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
