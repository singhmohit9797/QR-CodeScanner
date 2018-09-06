package com.example.ankush.activity1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.ankush.activity1.models.User;

public class LandingPage extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        user = intent.getParcelableExtra(getString(R.string.user_object));
    }

    public void OnScanButtonClick(View v) {
        Intent intent = new Intent(v.getContext(), Scanner.class);
        startActivityForResult(intent ,0);
    }
}
