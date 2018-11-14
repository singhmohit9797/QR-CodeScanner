package com.example.ankush.activity1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.ankush.activity1.models.User;
import com.example.ankush.activity1.utils.DbUtil;
import com.example.ankush.activity1.utils.JSONUtil;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.InputStream;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditView;
    private EditText passwordEditView;

    private User user;
    private UserLoginTask authTask;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditView = findViewById(R.id.emailEditText);
        passwordEditView = findViewById(R.id.passwordEditText);
    }

    public void OnLoginButtonClick(View v)
    {
        boolean successfulLogin = attemptLogin();

        if(successfulLogin) {
            Intent intent = new Intent (getApplicationContext(),LoadingScreen.class);
            intent.putExtra(getString(R.string.user_object), user);

            // Finish the current activity
            finish();
            startActivityForResult(intent,0);
        }
    }

    public void OnSignupButtonClick(View v) {
            Intent intent = new Intent (getApplicationContext(),SignupActivity.class);

            // Finish the current activity
            finish();
            startActivity(intent);
    }
    
    private boolean attemptLogin() {
        if(authTask != null)
            return false;

        emailEditView.setError(null);
        emailEditView.setError(null);

        // Store values at the time of the login attempt.
        String email = emailEditView.getText().toString();
        String password = passwordEditView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            focusView = passwordEditView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            emailEditView.setError(getString(R.string.error_required_field));
            focusView = emailEditView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            emailEditView.setError(getString(R.string.error_invalid_email));
            focusView = emailEditView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
            return false;
        } else {
            authTask = new UserLoginTask(email, password);
            try {
                authTask.execute((Void) null).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        user = authTask.getUser();
        return (user != null);
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        if(password.length() < 4) {
            passwordEditView.setError(getString(R.string.error_insufficient_length_password));
            return false;
        }

        if(!checkCaps(password)) {
            passwordEditView.setError(getString(R.string.error_caps_character_password));
            return false;
        }

        return true;
    }

    private boolean checkCaps(String str) {
        int size = str.length();
        for(int i=0; i<size; i++) {
            if(str.charAt(i) >= 65 && str.charAt(i) <= 90)
                return true;
        }

        return false;
    }

    public class UserLoginTask extends AsyncTask<Void, Void, User> {

        private User user;

        private final String email;
        private final String password;

        UserLoginTask(String email, String password) {
            this.password = password;
            this.email = email;
        }

        @Override
        protected User doInBackground(Void... params) {

            try {
                //Call the API
                String url = getString(R.string.local_host_url) + getString(R.string.api_login);
               InputStream inputStream = DbUtil.SendPostRequest(url, JSONUtil.GetUserJsonObject(email, password));

               if(inputStream != null) {
                   JSONObject userJson = JSONUtil.ParseJSONObject(inputStream);

                   if(userJson != null)
                       user = JSONUtil.GetUserObject(userJson);
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
                finish();
            } else {
                passwordEditView.setError(getString(R.string.error_connection_timeout));
                passwordEditView.requestFocus();
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
