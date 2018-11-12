package com.example.ankush.activity1;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ankush.activity1.models.PointOfInterest;
import com.example.ankush.activity1.models.User;

public class ViewActivity extends AppCompatActivity {

    ListView listView;

    PointOfInterest data[];

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        listView= findViewById(R.id.listView);
        user = getIntent().getParcelableExtra(getString(R.string.user_object));
        data = null;

        // send the api request and retrieve the poi list

        ArrayAdapter<PointOfInterest> adapter= new ArrayAdapter<PointOfInterest>(this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ProcessItem(parent, view, position, id);
                 }
            });
    }

    public void ProcessItem(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getApplicationContext(), LandingPage.class);
        intent.putExtra(getString(R.string.user_object), user);

        PointOfInterest poi = (PointOfInterest) parent.getItemAtPosition(position);
        intent.putExtra(getString(R.string.poi_object), poi);
        startActivity(intent);
    }
}
