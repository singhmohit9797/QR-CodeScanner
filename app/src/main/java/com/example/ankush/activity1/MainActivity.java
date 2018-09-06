package com.example.ankush.activity1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onLoginButtonClick(View v) {
        Intent intent = new Intent(v.getContext(), loader.class);
        startActivity(intent);
    }
    public void ver(View v)
    {
        Intent intent = new Intent(v.getContext(), Main2Activity.class);
        startActivity(intent);
    }

}

