package com.example.ankush.activity1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.example.ankush.activity1.models.PointOfInterest;
import com.example.ankush.activity1.models.User;
import com.example.ankush.activity1.utils.DbUtil;
import com.example.ankush.activity1.utils.JSONUtil;

import org.json.JSONObject;

import java.io.InputStream;

public class AddActivity extends AppCompatActivity {

    private EditText TitleEditText;

    private EditText ContentEditText;

    private PointOfInterest poi;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TitleEditText = findViewById(R.id.titleEditText);
        ContentEditText = findViewById(R.id.contentEditText);

        user = getIntent().getParcelableExtra(getString(R.string.user_object));

        poi = getIntent().getParcelableExtra(getString(R.string.poi_object));

        if(poi != null) {
            TitleEditText.setText(poi.getTitle());
            ContentEditText.setText(poi.getDescription());
        }

        setSupportActionBar(toolbar);
    }

    public void OnAddButtonClick(View v) {
        String title, description;
        title = TitleEditText.getText().toString();
        description = ContentEditText.getText().toString();

        PointOfInterest poi = new PointOfInterest(1, title, description);

        // Send the request to the api
        String url = getString(R.string.local_host_url) + getString(R.string.api_poi_new);
        System.out.println("URL:" + url);
        InputStream inputStream = DbUtil.SendPostRequest(url, JSONUtil.GetPoiJsonObject(poi));
        if(inputStream != null) {
            JSONObject poiJson = JSONUtil.ParseJSONObject(inputStream);

            if(poiJson != null) {
                poi = JSONUtil.GetPoiObject(poiJson);

                if(poi == null) {
                    TitleEditText.setText("Couldn't be added to the database");
                }
                else{
                    TitleEditText.setText("Successfully added to the database");
                    Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                    intent.putExtra(getString(R.string.user_object), user);
                    startActivity(intent);
                }
            }
        }
    }
}
