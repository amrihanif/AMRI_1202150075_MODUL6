package com.example.intel.AMRI_1202150075_MODUL6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
/**
 * Created by Amri hanif on 01/04/2018.
 */

public class RegisterActivity extends AppCompatActivity {

    EditText registerEmail, registerPassword;
    Button btnRegister;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // get instance firebase Auth
        auth = FirebaseAuth.getInstance();
        // referencing
        registerEmail = findViewById(R.id.registerEmail);
        registerPassword = findViewById(R.id.registerPassword);
        btnRegister = findViewById(R.id.btnRegsiter);
        // on click button
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerFirebase();
            }
        });
    }

    // proses register dari firebase
    private void registerFirebase() {
        // ngambil email dan password
        String email = registerEmail.getText().toString();
        String password = registerPassword.getText().toString();
        // validasi inputan
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                    "Your email is still empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        // validasi password
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                    "Your password is still empty!", Toast.LENGTH_SHORT).show();
            return;
        }
        // validasi panjang password
        if (password.length() < 6) {
            Toast.makeText(getApplicationContext(),
                    "Your password is short!", Toast.LENGTH_SHORT).show();
            return;
        }
        // create user with email and password
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // if register user is not success
                        if (!task.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this,
                                    "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                        // if register success
                        else {
                            // langsung pindah ke main
                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}
