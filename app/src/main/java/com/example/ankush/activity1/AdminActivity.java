package com.example.ankush.activity1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.ankush.activity1.models.User;

public class AdminActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        user = getIntent().getParcelableExtra(getString(R.string.user_object));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public void OnViewAllButtonClick(View v) {
        Intent intent = new Intent(getApplicationContext(),ViewActivity.class);
        intent.putExtra(getString(R.string.user_object), user);
        System.out.println("Switching to the view all activity");

        // Finish the current activity
        finish();
        startActivity(intent);
    }

    public void OnAddNewButtonClick(View v) {
        // Go to the add new activity
        Intent intent = new Intent(getApplicationContext(),AddActivity.class);
        intent.putExtra(getString(R.string.user_object), user);
        System.out.println("Switching to the Add New activity");

        // Finish the current activity
        finish();
        startActivity(intent);
    }

    public void OnScanButtonClick(View v) {
        // Go to the scanner
        Intent intent = new Intent(getApplicationContext(),Scanner.class);
        intent.putExtra(getString(R.string.user_object), user);
        System.out.println("Switching to the scanner activity");

        // Finish the current activity
        finish();
        startActivity(intent);
    }
}
