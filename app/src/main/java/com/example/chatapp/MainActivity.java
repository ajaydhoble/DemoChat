package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    //  MAIN ACTIVITY =============== LOGIN ACTIVITY
    private TextInputEditText mEmailView;
    private EditText mPasswordView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEmailView = findViewById(R.id.login_email);
        mPasswordView = findViewById(R.id.login_password);
        mAuth = FirebaseAuth.getInstance();
        mPasswordView.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == R.integer.login ||actionId == EditorInfo.IME_NULL){
                attemptLogin();
                return true;
            }
            return false;
        });
    }

    public void signInExistingUser(View v) {
        // TODO: Call attemptLogin() here
        attemptLogin();
    }

    public void registerNewUser(View v) {
        Intent intent = new Intent(this,com.example.chatapp.RegisterActivity.class);
        finish();
        startActivity(intent);
    }
    // TODO: Complete the attemptLogin() method
    private void attemptLogin() {

        // TODO: Use FirebaseAuth to sign in with email & password
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        if (email.isEmpty() || password.isEmpty()){
            return;
        }
        Toast.makeText(this,"Login in progres..",Toast.LENGTH_SHORT).show();

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("Admin","Sign In Successful: " + task.isSuccessful());
                if (!task.isSuccessful()){
                    Log.d("Admin","SIGN IN FAILED " + task.getException());
                    showError("Signing In Failed");
                }
                else{
                    Intent intent = new Intent(MainActivity.this,MainChatActivity.class);
                    finish();
                    startActivity(intent);
                }
            }
        });
    }

    private void  showError(String msg){
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok,null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}