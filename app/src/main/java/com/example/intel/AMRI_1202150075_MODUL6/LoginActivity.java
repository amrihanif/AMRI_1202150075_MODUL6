package com.example.intel.AMRI_1202150075_MODUL6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
/**
 * Created by Amri hanif on 01/04/2018.
 */

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    EditText loginEmail, loginPassword;
    Button btnLogin;
    TextView linkSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // get instance firebase Auth
        auth = FirebaseAuth.getInstance();

        // nge cek apakah user sudah login atau belum
        if (auth.getCurrentUser() != null) {
            // kalau sudah login langsung ke main activity
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        // referencing
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        linkSignUp = findViewById(R.id.linkSignUp);
        btnLogin = findViewById(R.id.btnLogin);

        // link ke sign up
        linkSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        // button login on click
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginFirebase();
            }
        });
    }

    // proses login firebase
    private void loginFirebase() {
        // ngambil email dan password
        String email = loginEmail.getText().toString();
        final String password = loginPassword.getText().toString();

        // validasi email
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Your email is still empty",
                    Toast.LENGTH_SHORT).show();
        }

        // validasi password
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Your password is still empty",
                    Toast.LENGTH_SHORT).show();
        }

        // proses login user with email and password
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // if login not success
                        if (!task.isSuccessful()) {
                            if (password.length() < 6) { // validasi panjang password
                                loginPassword.setError(getString(R.string.minimum_password));
                            } else {
                                Toast.makeText(LoginActivity.this,
                                        "Authentication Failed!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                        // if login success
                        else {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
}
