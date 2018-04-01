package com.example.intel.AMRI_1202150075_MODUL6;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Amri hanif on 01/04/2018.
 */

public class MyPhotoFragment extends Fragment {
    RecyclerView myRecycler;
    MyAdapter adapter;
    List<Post> posts;
    DatabaseReference databaseReference;
    FirebaseUser user;
    String userEmail, userName;
    public MyPhotoFragment() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_recent, container, false);
        user = FirebaseAuth.getInstance().getCurrentUser();
        userEmail = user.getEmail();
        userName = userEmail.substring(0, userEmail.indexOf("@"));
        posts = new ArrayList<>();
        myRecycler = view.findViewById(R.id.recentRecycler);
        myRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        databaseReference = FirebaseDatabase.getInstance().getReference("posts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    if ( post.getUsername().equals(userName) ) {
                        posts.add(post);
                    }
                }
                adapter = new MyAdapter(getActivity(), posts);
                myRecycler.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return view;
    }
}
