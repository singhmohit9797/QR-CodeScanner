package com.example.ankush.activity1;

import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.SparseArray;

import com.example.ankush.activity1.models.PointOfInterest;
import com.example.ankush.activity1.models.User;
import  com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import info.androidhive.barcode.BarcodeReader;

public class Scanner extends AppCompatActivity implements BarcodeReader.BarcodeReaderListener {

    private String qrCodeValue;

    private PointOfInterest poi;

    private POIDescriptionTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public void onScanned(Barcode barcode) {
        // single barcode scanned
        if(barcode.format == Barcode.QR_CODE)
        {
            qrCodeValue = barcode.displayValue;
            System.out.println(qrCodeValue);

            task = new POIDescriptionTask(qrCodeValue);
            try {
                task.execute((Void) null).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            poi = task.getPoi();

            System.out.println(poi.getDescription());
        }
    }

    @Override
    public void onScannedMultiple(List<Barcode> list) {
        // multiple barcodes scanned
    }

    @Override
    public void onBitmapScanned(SparseArray<Barcode> sparseArray) {
        // barcode scanned from bitmap image
    }

    @Override
    public void onScanError(String s) {
        // scan error
    }

    @Override
    public void onCameraPermissionDenied() {
        // camera permission denied
    }

    public class POIDescriptionTask extends AsyncTask<Void, Void, PointOfInterest> {

        private PointOfInterest poi;

        private final String title;

        POIDescriptionTask(String title) {
            this.title = title;
        }

        @Override
        protected PointOfInterest doInBackground(Void... params) {

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }

            //Call the API
            String url = /*getString(R.string.local_host_url) + */"http://192.168.137.1:8080/QRCodeScannerAPI/api/get/" + title;
            System.out.println(url);
            try{
                System.out.println("Making the call");
                HttpURLConnection connection = (HttpURLConnection) (new URL(url)).openConnection();
                connection.setRequestMethod("GET");

                connection.connect();

                JsonReader jsonReader = new JsonReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

                JSONObject poiJson = null;

                if(jsonReader.hasNext())
                {
                    poiJson = new JSONObject();
                    jsonReader.beginObject();
                    while(jsonReader.hasNext())
                    {
                        String key = jsonReader.nextName();
                        String value = jsonReader.nextString();
                        poiJson.put(key, value);
                    }
                System.out.println("Making the call");
                }
                if(poiJson != null)
                    poi = new PointOfInterest(poiJson.getInt("id"), poiJson.getString("title"), poiJson.getString("description"));

                return poi;
            }catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(final PointOfInterest poi) {
            task = null;
            if (poi != null) {
                finish();
            }
        }

        @Override
        protected void onCancelled() {
            task = null;
        }

        public PointOfInterest getPoi() {
            return poi;
        }
    }
}
