package com.app.sample.insta;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
/**
 * Created by Wesley Wienand, Tiago Pinto, Daniel Dos Santos on 30,01,2020
 */
public class ActivityLogin extends AppCompatActivity {
    public static final String TAG = "ActivityLogin";
    private EditText input_username;
    private EditText input_password;
    private Button btnSignUp;
    private View parent_view;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        parent_view = findViewById(android.R.id.content);

        firebaseAuth = FirebaseAuth.getInstance();

        hideKeyboard();


        input_username = (EditText) findViewById(R.id.input_username);
        input_password = (EditText) findViewById(R.id.input_password);

        btnSignUp = (Button) findViewById(R.id.btn_signin);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //submitForm();
                String email = input_username.getText().toString();
                String password = input_password.getText().toString();
                signIn(email, password);
            }
        });

    }
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void signIn(String email, String password) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        // [START sign_in_with_email]
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            firebaseUser = firebaseAuth.getCurrentUser();
                            updateUI(firebaseUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(ActivityLogin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        if (!task.isSuccessful()) {
                            // TODO: add failure task
                        }
                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }
    /* Form validations */
    private boolean validateForm() {
        boolean valid = true;

        String email = input_username.getText().toString();
        if (TextUtils.isEmpty(email)) {
            input_username.setError("Required.");
            valid = false;
        } else {
            input_username.setError(null);
        }

        String password = input_password.getText().toString();
        if (TextUtils.isEmpty(password)) {
            input_password.setError("Required.");
            valid = false;
        } else {
            input_password.setError(null);
        }

        return valid;
    }

    //TODO: add create account
    /*private void createAccount(final String email, String password, final String name) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }

        // [START create_user_with_email]
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            firebaseUser = firebaseAuth.getCurrentUser();
                            String uID = firebaseUser.getUid();

                            updateUI(firebaseUser);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(ActivityLogin.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                    }
                });
        // [END create_user_with_email]
    }*/

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null){
            startActivity(new Intent(ActivityLogin.this, ActivityMain.class));
        }else {
            // TODO: add ui configurations
        }
    }
}
