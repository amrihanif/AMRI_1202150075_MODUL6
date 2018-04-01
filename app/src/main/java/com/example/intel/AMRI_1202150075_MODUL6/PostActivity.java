package com.example.intel.AMRI_1202150075_MODUL6;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
/**
 * Created by Amri hanif on 01/04/2018.
 */

public class PostActivity extends AppCompatActivity {

    //a constant to track the file chooser intent
    private static final int PICK_IMAGE_REQUEST = 234;

    EditText postDesc, postTitle;
    ImageView postPhoto;
    Button btnChoose;
    FloatingActionButton fabAddPost;

    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private Uri filePath;

    FirebaseUser user;
    // dari firebase
    String userEmail;

    // yang bakal dikirim
    String username, title, desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        // get instance dari firebase Database
        databaseReference = FirebaseDatabase.getInstance().getReference();
        // get instance dari firebase storage
        storageReference = FirebaseStorage.getInstance().getReference();
        // bikin child app_title dengan value Popotoan
        FirebaseDatabase.getInstance().getReference("app_title").setValue("Popotoan");
        // referencing
        postTitle = findViewById(R.id.postTitle);
        postDesc = findViewById(R.id.postDescription);
        postPhoto = findViewById(R.id.postPhoto);
        btnChoose = findViewById(R.id.buttonChoose);
        fabAddPost = findViewById(R.id.fabAddPost);
        // ngambil user yang udah login
        user = FirebaseAuth.getInstance().getCurrentUser();
        // ngambil email dari user yang udah login
        userEmail = user.getEmail();
        // ngambil username
        username = userEmail.substring(0, userEmail.indexOf("@"));
        // button on click
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                        PICK_IMAGE_REQUEST);
            }
        });
        // fab on click
        fabAddPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPost();
            }
        });
    }
    // proses upload post
    private void uploadPost() {
        if (filePath != null) {
            // nambahin progress dialog ke activity
            final ProgressDialog progressDialog = new ProgressDialog(PostActivity.this);
            progressDialog.setTitle("Upload Post");
            progressDialog.show();

            // ngambil title sama desc
            title = postTitle.getText().toString();
            desc = postDesc.getText().toString();

            // get reference database dari child posts
            final DatabaseReference database = FirebaseDatabase.getInstance().getReference("posts");

            // get reference storage dari child image
            StorageReference riversRef = storageReference.child("image").child(filePath.getLastPathSegment());
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // nyiapin link download file dari storage
                            final Uri downloadUri = taskSnapshot.getDownloadUrl();

                            // generate post id
                            String postId = database.push().getKey();

                            Post post = new Post();
                            post.setPostId(postId);
                            post.setUsername(username);
                            post.setPhoto(downloadUri.toString());
                            post.setPhotoTitle(title);
                            post.setPhotoDesc(desc);

                            // save data ke database dengan child post id dan value dari class Post
                            database.child(postId).setValue(post);

                            progressDialog.dismiss();

                            Toast.makeText(getApplicationContext(),
                                    "File uploaded!", Toast.LENGTH_SHORT).show();

                            // balik ke main activity
                            Intent intent = new Intent(PostActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();

                            Toast.makeText(getApplicationContext(),
                                    e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.setMessage("Uploading...");
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(),
                    "No files!", Toast.LENGTH_SHORT).show();
        }
    }
    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                postPhoto.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
