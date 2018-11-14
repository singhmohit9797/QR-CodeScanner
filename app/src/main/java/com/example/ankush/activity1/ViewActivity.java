package com.example.ankush.activity1;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ankush.activity1.models.PointOfInterest;
import com.example.ankush.activity1.models.User;
import com.example.ankush.activity1.utils.DbUtil;
import com.example.ankush.activity1.utils.JSONUtil;
import com.example.ankush.activity1.utils.POIAdapter;

import org.json.JSONArray;

import java.io.InputStream;
import java.util.ArrayList;

public class ViewActivity extends AppCompatActivity {

    ListView listView;

    ArrayList<PointOfInterest> data;

    private static POIAdapter adapter;

    private GetAllPOITask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        listView = findViewById(R.id.listView);

        // send the api request and retrieve the poi list'
        GetAll();

        // Must not be null
        data = task.getPoiList();

        adapter = new POIAdapter(this, R.layout.content_list_item, data);

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getApplicationContext(), LandingPage.class);
                    User user = getIntent().getParcelableExtra(getString(R.string.user_object));

                    PointOfInterest poi = (PointOfInterest) parent.getItemAtPosition(position);
                    intent.putExtra(getString(R.string.user_object), user);
                    intent.putExtra(getString(R.string.poi_object), poi);
                    startActivity(intent);
                 }
            });
    }

    public void GetAll() {
        if(task != null)
            return;

        task = new GetAllPOITask();

        try{
            task.execute((Void) null).get();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class GetAllPOITask extends AsyncTask<Void, Void, ArrayList<PointOfInterest>> {

        ArrayList<PointOfInterest> list;

        GetAllPOITask() {

        }

        @Override
        protected ArrayList<PointOfInterest> doInBackground(Void... params) {

            try {
                // Send the request to the api
                String url = getString(R.string.local_host_url) + getString(R.string.api_poi_all);
                System.out.println("URL:" + url);
                InputStream inputStream = DbUtil.SendGetRequest(url);

                if(inputStream != null) {
                    JSONArray jsonList = JSONUtil.ParseJSONListObject(inputStream);
                    if(jsonList != null) {
                        list = JSONUtil.GetPoiList(jsonList);
                    }

                    return list;
                }
            }catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(final ArrayList<PointOfInterest> list) {
            task = null;
        }

        @Override
        protected void onCancelled() {
            task = null;
        }

        public ArrayList<PointOfInterest> getPoiList() {
            return list;
        }
    }
}
