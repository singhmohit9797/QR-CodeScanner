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
        DeleteButton = findViewById(R.id.editButton);

        if(user.getIsAdmin() != 1) {
            EditButton.setVisibility(View.INVISIBLE);
            DeleteButton.setVisibility(View.INVISIBLE);
        }
    }

    public void OnScanNewClick(View v) {
        Intent intent = new Intent(getApplicationContext(), Scanner.class);
        intent.putExtra(getString(R.string.user_object), user);
        startActivity(intent);
    }

    public void OnEditButtonClick(View v) {
        Intent intent = new Intent(getApplicationContext(), AddActivity.class);
        intent.putExtra(getString(R.string.user_object), user);
        intent.putExtra(getString(R.string.poi_object), poi);
        startActivity(intent);
    }

    public void OnDeleteButtonClick(View v) {
        // Confirm if the user wants to delete the poi
        final boolean[] bWantsToDelete = new boolean[1];

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Confirm Delete");
        dialog.setMessage("Are you sure you want to delete this poi? Changes will be irreversible.");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                bWantsToDelete[0] = true;
                dialog.dismiss();
            }
        });
        dialog.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                bWantsToDelete[0] = true;
                dialog.dismiss();
            }
        });
        dialog.show();

        if(bWantsToDelete[0]){
            // Call the API to delete the poi
            task = new DelPOITask(poi);
            try{
                task.execute((Void) null).get();
            } catch(Exception e) {
                e.printStackTrace();
            }

            boolean success = task.getPoi() != null;
            String result = null;
            if(success) {
                result = "Successfully deleted the Point of Interest";
            }
            else {
                result = "Couldn't delete the Point of Interest. Try again.";
            }

            dialog = new AlertDialog.Builder(this);
            dialog.setTitle("Delete Result");
            dialog.setMessage(result);
            dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            if(success) {
                Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                intent.putExtra(getString(R.string.user_object), user);
                startActivity(intent);
            }
        }
    }

    public class DelPOITask extends AsyncTask<Void, Void, PointOfInterest> {

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
                    JSONObject poiJson = JSONUtil.ParseJSONObject(inputStream);

                    if(poiJson != null)
                        pointOfInterest = JSONUtil.GetPoiObject(poiJson);
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

            if (pointOfInterest == null) {
                ;
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
