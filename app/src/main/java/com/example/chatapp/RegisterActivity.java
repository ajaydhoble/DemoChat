package com.example.chatapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends Activity {
    public static final String CHAT_PREFS = "ChatPrefs";
    public static final String DISPLAY_NAME_KEY = "Username";

    private TextInputEditText mEmailView;
    private TextInputEditText mUsernameView;
    private EditText mPasswordView;
    private EditText mConfirmPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mEmailView = findViewById(R.id.register_email);
        mPasswordView = findViewById(R.id.register_password);
        mConfirmPassword = findViewById(R.id.register_confirm_password);
        mUsernameView = findViewById(R.id.register_username);

        mConfirmPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.integer.register_form_finished || id == EditorInfo.IME_NULL) {
                    attemptRegistration();
                    return true;
                }

                return false;
            }
        });
        mAuth = FirebaseAuth.getInstance();
    }

    private void attemptRegistration() {
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) || !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // TODO: Call create FirebaseUser() here
            createFireBaseUser();
        }
    }

    // Executed when Sign Up button is pressed.

    private boolean isEmailValid(String email) {
        // You can add more checking logic here.
        return email.contains("@gmail.com");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Add own logic to check for a valid password (minimum 6 characters)
        String confirmPassword = mConfirmPassword.getText().toString();
        return password.equals(confirmPassword) && password.length() >= 6;
    }


    // TODO: Create a Firebase user
    private void createFireBaseUser() {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("Admin","CREATE USER :"+task.isSuccessful());
                if (!task.isSuccessful()){
                    Log.d("ADMIN","FAILED BRO");
                    showError("Registration Failed");
                }
                else{
                    saveDisplayName();
                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }

    public void signUp(View view) {
        attemptRegistration();
    }

    // TODO: Save the display name to Shared Preferences
    private void saveDisplayName(){
        String displayName = mUsernameView.getText().toString();
        SharedPreferences prefs = getSharedPreferences(CHAT_PREFS,0);
        prefs.edit().putString(DISPLAY_NAME_KEY,displayName).apply();
    }

    // TODO: Create an alert dialog to show in case registration failed
    private void  showError(String msg){
       new AlertDialog.Builder(this)
               .setTitle("Error")
               .setMessage(msg)
               .setPositiveButton(android.R.string.ok,null)
               .setIcon(android.R.drawable.ic_dialog_alert)
               .show();
    }

}
