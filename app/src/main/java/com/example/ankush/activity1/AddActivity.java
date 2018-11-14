package com.example.ankush.activity1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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

    private AddPOITask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TitleEditText = findViewById(R.id.titleEditText);
        ContentEditText = findViewById(R.id.contentEditText);

        user = getIntent().getParcelableExtra(getString(R.string.user_object));

        // Get the poi object if this is activity is activated using the edit action button
        poi = getIntent().getParcelableExtra(getString(R.string.poi_object));

        // If the user is editing a poi, then change the buttons
        if(poi != null) {
            TitleEditText.setText(poi.getTitle());
            ContentEditText.setText(poi.getDescription());
            ((Button)(findViewById(R.id.addButton))).setText("Update");
        }

        setSupportActionBar(toolbar);
    }

    public void OnAddButtonClick(View v) {
        String title, description;
        title = TitleEditText.getText().toString();
        description = ContentEditText.getText().toString();

        if(title.equals("")) {
            TitleEditText.setError(getString(R.string.error_required_field));
            TitleEditText.requestFocus();
            return;
        }
        else if(description.equals("")) {
            ContentEditText.setError(getString(R.string.error_required_field));
            ContentEditText.requestFocus();
            return;
        }

        // Check if the user is updating the poi or adding a new one
        if(poi != null) {
            poi.setTitle(title);
            poi.setDescription(description);
            task = new AddPOITask(poi);
        }
        else
            task = new AddPOITask(new PointOfInterest(1, title, description));

        try{
            task.execute((Void) null).get();
        } catch(Exception e) {
            e.printStackTrace();
        }
        poi = task.getPoi();
        final boolean success = (poi != null);

        String result;
        if(success) {
            System.out.println("Edit/Add: Successfully completed");
            result = "User Registered Successfully. Taking you to the login page";
        }
        else{
            System.out.println("Edit/Add: Something went wrong");
            result = "Something went wrong. Please try again after some time.";
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Add Result");
        dialog.setMessage(result);
        dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if(success) {
                    Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                    intent.putExtra(getString(R.string.user_object), user);
                    finish();
                    startActivity(intent);
                }
            }
        });
        dialog.show();
    }

    private class AddPOITask extends AsyncTask<Void, Void, PointOfInterest> {

        private PointOfInterest pointOfInterest;

        AddPOITask(PointOfInterest pointOfInterest) {
            this.pointOfInterest = pointOfInterest;
        }

        @Override
        protected PointOfInterest doInBackground(Void... params) {

            try {
                // Send the request to the api
                String url = getString(R.string.local_host_url) + getString(R.string.api_poi_new);
                System.out.println("URL:" + url);
                InputStream inputStream = DbUtil.SendPostRequest(url, JSONUtil.GetPoiJsonObject(pointOfInterest));

                if(inputStream != null) {
                    System.out.println("Call went through");
                    JSONObject poiJson = JSONUtil.ParseJSONObject(inputStream);

                    if(poiJson != null)
                        pointOfInterest = JSONUtil.GetPoiObject(poiJson);
                    return pointOfInterest;
                }

                System.out.println("Couldn't parse the object");
            }catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(final PointOfInterest pointOfInterest) {
            task = null;

            if (pointOfInterest != null) {
                ;
            } else {
                ContentEditText.setError(getString(R.string.error_connection_timeout));
                ContentEditText.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            task = null;
        }

        public PointOfInterest getPoi() {
            return pointOfInterest;
        }
    }

}
