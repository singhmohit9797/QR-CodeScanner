package com.example.ankush.activity1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ankush.activity1.models.PointOfInterest;
import com.example.ankush.activity1.models.User;
import com.example.ankush.activity1.utils.DbUtil;
import com.example.ankush.activity1.utils.JSONUtil;

import org.json.JSONObject;

import java.io.InputStream;

public class LandingPage extends AppCompatActivity {

    private User user;

    private PointOfInterest poi;

    private TextView resultTitleView;

    private TextView resultDescView;

    private ImageView EditButton;

    private ImageView DeleteButton;

    private DelPOITask task;

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

        EditButton = findViewById(R.id.editButton);
        DeleteButton = findViewById(R.id.deleteButton);

        if(user.getIsAdmin() != 1) {
            System.out.println("User is not an Admin");
            EditButton.setVisibility(View.INVISIBLE);
            DeleteButton.setVisibility(View.INVISIBLE);
        }
    }

    public void OnScanNewClick(View v) {
        Intent intent = new Intent(getApplicationContext(), Scanner.class);
        intent.putExtra(getString(R.string.user_object), user);

        System.out.println("Switching to Scanner Activity");
        startActivity(intent);
    }

    public void OnEditButtonClick(View v) {
        Intent intent = new Intent(getApplicationContext(), AddActivity.class);
        intent.putExtra(getString(R.string.user_object), user);
        intent.putExtra(getString(R.string.poi_object), poi);

        System.out.println("Switching to Add New Activity to edit this point of interest");
        startActivity(intent);
    }

    public void OnDeleteButtonClick(View v) {
        // Confirm if the user wants to delete the poi
        System.out.println("Confirming delete action");
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Confirm Delete");
        dialog.setMessage("Are you sure you want to delete this poi? Changes will be irreversible.");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("Received confirmation to delete this POI");
                dialog.dismiss();

                // Call the API
                CallAPI();
            }
        });
        dialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("POI Deletion canceled");
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    // Call the API to delete the poi
    private void CallAPI() {
        task = new DelPOITask(poi);
        try{
            task.execute((Void) null).get();
        } catch(Exception e) {
            e.printStackTrace();
        }

        final boolean success = (task.getPoi() == null);
        String result;
        if(success) {
            System.out.println("Success");
            result = "Successfully deleted the Point of Interest";
        }
        else {
            System.out.println("Failure");
            result = "Couldn't delete the Point of Interest. Try again.";
        }

        AlertDialog.Builder resultDialog = new AlertDialog.Builder(this);
        resultDialog.setTitle("Delete Result");
        resultDialog.setMessage(result);
        resultDialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if(success) {
                    Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                    intent.putExtra(getString(R.string.user_object), user);
                    poi = null;

                    System.out.println("Switching to the admin activity");
                    // Finish the current Activity
                    finish();
                    startActivity(intent);
                }
            }
        });
        resultDialog.show();
    }

    private class DelPOITask extends AsyncTask<Void, Void, PointOfInterest> {

        private PointOfInterest pointOfInterest;

        DelPOITask(PointOfInterest pointOfInterest) {
            this.pointOfInterest = pointOfInterest;
        }

        @Override
        protected PointOfInterest doInBackground(Void... params) {

            try {

                // Send the request to the api
                String url = getString(R.string.local_host_url) + getString(R.string.api_poi_del);
                System.out.println("URL:" + url);
                InputStream inputStream = DbUtil.SendPostRequest(url, JSONUtil.GetPoiJsonObject(pointOfInterest));

                if(inputStream != null) {
                    System.out.println("DELETE POI: Got the response from the API");
                    JSONObject poiJson = JSONUtil.ParseJSONObject(inputStream);

                    if(poiJson != null){
                        System.out.println("DELETE POI: Delete Unsuccessful");
                        pointOfInterest = JSONUtil.GetPoiObject(poiJson);
                    }
                    else {
                        System.out.println("DELETE POI: Empty response from the API i.e. POI deleted correctly");
                        pointOfInterest = null;
                    }
                    return pointOfInterest;
                }
            }catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(final PointOfInterest pointOfInterest) {
            task = null;

            System.out.println("Post Execute");

            if (pointOfInterest == null) {
                System.out.println("null");
            } else {
                resultDescView.setError(getString(R.string.error_connection_timeout));
                resultDescView.requestFocus();
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
