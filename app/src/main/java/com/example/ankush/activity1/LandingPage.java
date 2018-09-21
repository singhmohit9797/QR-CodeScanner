package com.example.ankush.activity1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.ankush.activity1.models.PointOfInterest;
import com.example.ankush.activity1.models.User;

public class LandingPage extends AppCompatActivity {

    private User user;

    private PointOfInterest poi;

    private TextView resultTitleView;

    private TextView resultDescView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        user = intent.getParcelableExtra(getString(R.string.user_object));
        poi = intent.getParcelableExtra(getString(R.string.poi_object));

        resultDescView = findViewById(R.id.resultData);
        resultTitleView = findViewById(R.id.resultTitle);

        resultTitleView.setText(poi.getTitle());
        resultDescView.setText(poi.getDescription());
    }

    public void OnScanNewClick(View v) {

        Intent intent = new Intent(getApplicationContext(), Scanner.class);
        intent.putExtra(getString(R.string.user_object), user);
        startActivity(intent);

    }

}
