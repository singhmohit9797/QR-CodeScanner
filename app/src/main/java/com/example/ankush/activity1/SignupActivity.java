package com.example.ankush.activity1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.example.ankush.activity1.models.User;

public class SignupActivity extends AppCompatActivity {

    private EditText UsernameView;

    private EditText PasswordView;

    private EditText ConfirmPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        UsernameView = findViewById(R.id.usernameEditText);
        PasswordView = findViewById(R.id.passwordEditText);
        ConfirmPasswordView = findViewById(R.id.confirmEditText);
    }

    public void OnSignupButtonClick(View v) {
        String username;
        String password;
        String confirmPassword;

        username = UsernameView.getText().toString();
        password = PasswordView.getText().toString();
        confirmPassword = ConfirmPasswordView.getText().toString();

        User user = null;

        if(password.equals(confirmPassword)) {
            user = new User(1, username, password, 0);
        }
        else {
            ConfirmPasswordView.setError(getString(R.string.error_password_mismatch));
            ConfirmPasswordView.requestFocus();
            return;
        }

        // Register the user

        // Show the successful message

        // Go to login page
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        startActivity(intent);
    }
}
