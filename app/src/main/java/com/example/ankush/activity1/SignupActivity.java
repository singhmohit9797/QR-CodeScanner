package com.example.ankush.activity1;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.example.ankush.activity1.models.User;
import com.example.ankush.activity1.utils.DbUtil;
import com.example.ankush.activity1.utils.JSONUtil;

import org.json.JSONObject;

import java.io.InputStream;

public class SignupActivity extends AppCompatActivity {

    private EditText UsernameView;

    private EditText PasswordView;

    private EditText ConfirmPasswordView;

    private UserSignupTask authTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        UsernameView = findViewById(R.id.usernameEditText);
        PasswordView = findViewById(R.id.passwordEditText);
        ConfirmPasswordView = findViewById(R.id.confirmEditText);
    }

    public void OnSignupButtonClick(View v) {
        if(authTask != null)
            return;

        String username;
        String password;
        String confirmPassword;

        username = UsernameView.getText().toString();
        password = PasswordView.getText().toString();
        confirmPassword = ConfirmPasswordView.getText().toString();

        if(!password.equals(confirmPassword)) {
            ConfirmPasswordView.setError(getString(R.string.error_password_mismatch));
            ConfirmPasswordView.requestFocus();
            return;
        }

        authTask = new UserSignupTask(0, username, password);
        // Register the user
        try {
            authTask.execute((Void) null).get();
        } catch(Exception e) {
            e.printStackTrace();
        }

        // Show the successful message
        final boolean success = authTask.getUser() != null;
        String result = null;
        if(success) {
           result = "User Registered Successfully. Taking you to the login page";
        }
        else{
            result = "Something went wrong. Please try again after some time.";
        }

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Signup Result");
        dialog.setMessage(result);
        dialog.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(success) {
                    // Go to login page
                    Intent intent = new Intent(getApplicationContext(),LoginActivity.class);

                    System.out.println("Switching to the Login Activity");
                    // Finish the current activity
                    finish();
                    startActivity(intent);
                }

            }
        });
        dialog.show();
    }
    private class UserSignupTask extends AsyncTask<Void, Void, User> {

        private User user;

        private final int id;
        private final String email;
        private final String password;

        UserSignupTask(int id, String email, String password) {
            this.id = id;
            this.password = password;
            this.email = email;
        }

        @Override
        protected User doInBackground(Void... params) {

            try {

                //Call the API
                String url = getString(R.string.local_host_url) + getString(R.string.api_signup);

                User userObj = new User(id, email, password, 0);

                InputStream inputStream = DbUtil.SendPostRequest(url, JSONUtil.GetUserJsonObject(userObj));

                if(inputStream != null) {
                    System.out.println("SIGNUP: Got the response from the API");
                    JSONObject userJson = JSONUtil.ParseJSONObject(inputStream);

                    if(userJson != null) {
                        System.out.println("SIGNUP: Parsed the json response");
                        user = JSONUtil.GetUserObject(userJson);
                    }
                    return user;
                }
            }catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(final User user) {
            authTask = null;

            if (user != null) {
                ;
            } else {
                ConfirmPasswordView.setError(getString(R.string.error_connection_timeout));
                ConfirmPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            authTask = null;
        }

        public User getUser() {
            return user;
        }
    }

}
