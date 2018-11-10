package com.example.ankush.activity1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


    }
    public void OnViewAllButtonClick(View v)
    {
        Intent intent1 =new Intent(getApplicationContext(),ViewActivity.class);
        startActivity(intent1);
    }

    public void OnAddNewButtonClick(View v)
    {
            Intent intent =new Intent(getApplicationContext(),AddActivity.class);
            startActivity(intent);

    }

    public void OnDeleteButtonClick(View v)
    {
        //
    }

    public void OnScanButtonClick(View v)
    {
        Intent intent4 =new Intent(getApplicationContext(),Scanner.class);
        startActivity(intent4);
    }
}
