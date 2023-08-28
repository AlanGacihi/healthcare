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

public class RegisterActivity extends AppCompatActivity {

    EditText[] editTextArray = new EditText[5];
    Button registerButton;
    TextView login;
    Boolean empty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextArray[0] = findViewById(R.id.editTextRegisterFirstName);
        editTextArray[1] = findViewById(R.id.editTextRegisterLastName);
        editTextArray[2] = findViewById(R.id.editTextRegisterUsername);
        editTextArray[3] = findViewById(R.id.editTextRegisterPassword);
        editTextArray[4] = findViewById(R.id.editTextConfirmPassword);
        registerButton = findViewById(R.id.RegisterButton);
        login = findViewById(R.id.textViewExistingUser);

        for (EditText editText : editTextArray) {
            reset(editText);
        }
        editTextArray[0].requestFocus();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();
            }
        });

        editTextArray[4].setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    register();
                    return true; // Return true to indicate that the action was handled
                }
                return false; // Return false if you didn't handle the action
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private void register() {
        for (EditText editText : editTextArray) {
            reset(editText);
        }
        empty = false;

        for (EditText editText : editTextArray) {
            String contents = editText.getText().toString();
            if (contents.length() == 0) {
                empty = true;
                showErrorToast("All fields are required!");
                break;
            }
        }

        if (!empty) {
            String firstname = editTextArray[0].getText().toString().trim();
            String lastname = editTextArray[1].getText().toString().trim();
            String username = editTextArray[2].getText().toString().trim();
            String password = editTextArray[3].getText().toString();
            String confirm = editTextArray[4].getText().toString();
            if (password.equals(confirm)) {
                if (isValidPassword(password)) {
                    LocalDatabase localdb = new LocalDatabase(getApplicationContext(), "healthcare", null, 1);
                    if (localdb.userIsNew(username)) {
                        localdb.register(firstname, lastname, username, password);
                        Toast.makeText(getApplicationContext(), "Account Registered. Proceed to login.", Toast.LENGTH_LONG).show();
                        //startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    } else {
                        showErrorToast("A user with that username / email already exists!");
                        showInRed(editTextArray[2]);
                        editTextArray[2].requestFocus();
                    }
                } else {
                    showErrorToast("The password should be at least six characters!");
                    showInRed(editTextArray[3]);
                    showInRed(editTextArray[4]);
                    editTextArray[3].requestFocus();
                }
            } else {
                showErrorToast("Passwords do not match!");
                showInRed(editTextArray[3]);
                showInRed(editTextArray[4]);
                editTextArray[3].requestFocus();
            }
        } else {
            for (EditText editText : editTextArray) {
                String contents = editText.getText().toString();
                if (contents.length() == 0) {
                    showInRed(editText);
                }
            }
        }
    }

    private boolean isValidPassword(String password) {
        return password.length() >= 6;
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
            //gradientDrawable.setColor(Color.RED);  // Change the fill color
            gradientDrawable.setStroke(2, Color.RED);  // Change the stroke color and width
        }
    }

    private void reset(final EditText field) {
        Drawable originalBackground = field.getBackground();
        if (originalBackground instanceof GradientDrawable) {
            GradientDrawable gradientDrawable = (GradientDrawable) originalBackground;
            //gradientDrawable.setColor(Color.RED);  // Change the fill color
            gradientDrawable.setStroke(2, Color.WHITE);  // Change the stroke color and width
        }
    }
}