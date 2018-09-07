package com.example.ankush.activity1;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.JsonReader;
import android.view.View;
import android.widget.EditText;

import com.example.ankush.activity1.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutionException;

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
            System.out.println("Successful login");
            Intent intent = new Intent (v.getContext(),LoadingScreen.class);
            //intent.putExtra(getString(R.string.user_object), authTask.getUser());
            startActivityForResult(intent,0);
        }
    }
    
    private boolean attemptLogin()
    {
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
            passwordEditView.setError(getString(R.string.error_invalid_password));
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
        } else {
            authTask = new UserLoginTask(email, password);
            try {
                authTask.execute((Void) null).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        user = authTask.getUser();
        return (user != null) ? true : false;
    }

    private boolean isEmailValid(String email) {
        return true;
    }

    private boolean isPasswordValid(String password) {
        return true;
    }

    private JSONObject getUserJSONObject(String email, String password) {
        JSONObject object = new JSONObject();

        try {
            object.put("email", email);
            object.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return object;
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
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
                return null;
            }

            //Call the API
            String url = /*getString(R.string.local_host_url) + */"http://192.168.43.189:8080/QRCodeScannerAPI/api/login";
           try{
               System.out.println("Making the call");
               HttpURLConnection connection = (HttpURLConnection) (new URL(url)).openConnection();
               connection.setRequestMethod("POST");
               connection.setRequestProperty("Content-Type", "application/json");
               connection.setDoOutput(true);

               BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream(), "UTF-8"));
               writer.write(getUserJSONObject(email, password).toString());
               writer.flush();
               writer.close();

               connection.connect();

               JsonReader jsonReader = new JsonReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));

               JSONObject userJson = null;

               if(jsonReader.hasNext())
               {
                   userJson = new JSONObject();
                   jsonReader.beginObject();
                   while(jsonReader.hasNext())
                   {
                       String key = jsonReader.nextName();
                       String value = jsonReader.nextString();
                       userJson.put(key, value);
                   }
               }

               if(userJson != null)
                   user = new User(userJson.getInt("id"), userJson.getString("email"), userJson.getString("password"));

               return user;
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
                passwordEditView.setError(getString(R.string.error_incorrect_password));
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
