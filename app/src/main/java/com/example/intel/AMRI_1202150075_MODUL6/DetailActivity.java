package com.example.intel.AMRI_1202150075_MODUL6;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Amri hanif on 01/04/2018.
 */

public class DetailActivity extends AppCompatActivity {

    TextView detailUsername, detailTitle, detailDesc;
    ImageView detailPhoto;
    EditText detailComment;
    Button buttonComment;

    RecyclerView recyclerComment;
    List<Comment> comments;

    DatabaseReference database;
    String username = "", postId = "";
    String message = "";

    FirebaseUser user;
    // dari firebase
    String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // get instance firebase database dan langsung bikin child comments
        database = FirebaseDatabase.getInstance().getReference("comments");

        detailUsername = findViewById(R.id.detailUsername);
        detailTitle = findViewById(R.id.detailTitle);
        detailDesc = findViewById(R.id.detailDesc);
        detailPhoto = findViewById(R.id.detailPhoto);
        detailComment = findViewById(R.id.detailComment);
        buttonComment = findViewById(R.id.buttonPostComment);
        recyclerComment = findViewById(R.id.recyclerComment);

        // get instance firebase auth
        user = FirebaseAuth.getInstance().getCurrentUser(); // get user yang lagi aktif

        // ngambil emai user aktif
        userEmail = user.getEmail();

        // username
        username = userEmail.substring(0, userEmail.indexOf("@"));

        // cek get intent
        if (getIntent() != null) {
            detailUsername.setText(username);
            detailTitle.setText(getIntent().getStringExtra("title"));
            detailDesc.setText(getIntent().getStringExtra("desc"));
            postId = getIntent().getStringExtra("id");
            Picasso.get().load(getIntent().getStringExtra("photo")).into(detailPhoto);
        }
        // bikin array list untuk komentar
        comments = new ArrayList<>();
        // setting layout manager untuk recycler comment (Linear Layout)
        recyclerComment.setLayoutManager(new LinearLayoutManager(DetailActivity.this));
    }
}
