package com.example.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText edUsername, edPassword;
    TextView register, loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edUsername = findViewById(R.id.editTextLoginUsername);
        edPassword = findViewById(R.id.editTextLoginPassword);
        loginButton = findViewById(R.id.editTextLoginButton);
        register = findViewById(R.id.textViewRegister);

        reset(edUsername);
        reset(edPassword);
        edUsername.requestFocus();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        edPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login();
                    return true; // Return true to indicate that the action was handled
                }
                return false; // Return false if you didn't handle the action
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                finish();
            }
        });
    }

    private void login() {
        reset(edUsername);
        reset(edPassword);

        String username = edUsername.getText().toString().trim();
        String password = edPassword.getText().toString();

        if (password.length() == 0 && username.length() == 0) {
            showErrorToast("Please input username and password");
            showInRed(edUsername);
            showInRed(edPassword);
            edUsername.requestFocus();
        } else if (username.length() == 0) {
            showErrorToast("Username field is required!");
            showInRed(edUsername);
            edUsername.requestFocus();
        } else if (password.length() == 0) {
            showErrorToast("Password field is required!");
            showInRed(edPassword);
            edPassword.requestFocus();
        } else {
            LocalDatabase localdb = new LocalDatabase(getApplicationContext(), "healthcare", null, 1);
            if (localdb.areValidCredentials(username, password)) {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            } else if (username.equals("admin") && password.equals("4Thespine")) {
                startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                finish();
            } else {
                // Call the method with the error message
                showErrorToast("Incorrect Username/Password!");
                showInRed(edUsername);
                showInRed(edPassword);
                edUsername.requestFocus();
            }
        }
    }

    private void showErrorToast(String errorMessage) {
        // Inflate the custom layout
        LayoutInflater inflater = getLayoutInflater();
        View errorToastView = inflater.inflate(R.layout.error_toast_layout, null);

        // Set the error message
        TextView errorMessageTextView = errorToastView.findViewById(R.id.errorToastMessage);
        errorMessageTextView.setText(errorMessage);

        // Set the custom layout to the Toast
        Toast errorToast = new Toast(getApplicationContext());
        errorToast.setView(errorToastView);
        errorToast.setDuration(Toast.LENGTH_SHORT);
        errorToast.show();
    }

    private void showInRed(final EditText field) {

        Drawable originalBackground = field.getBackground();
        if (originalBackground instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (GradientDrawable) originalBackground;
            gradientDrawable.setStroke(3, Color.RED);  // Change the stroke color and width
        }
    }

    private void reset(final EditText field) {
        Drawable originalBackground = field.getBackground();
        if (originalBackground instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (GradientDrawable) originalBackground;
            gradientDrawable.setStroke(2, Color.BLACK);  // Change the stroke color and width
        }
    }
}