package com.example.intel.AMRI_1202150075_MODUL6;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Amri hanif on 01/04/2018.
 */

public class MainActivity extends AppCompatActivity {

    private TabLayout tabMainLayout;
    private ViewPager viewPagerMain;
    FloatingActionButton fabPost;

    FirebaseUser user;
    String userEmail, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get instance firebase Auth
        user = FirebaseAuth.getInstance().getCurrentUser(); // ngambil user yang sedang aktif

        // ngambil email dari user yang aktif
        userEmail = user.getEmail();

        // nyusun username
        userName = userEmail.substring(0, userEmail.indexOf("@"));

        // referencing
        viewPagerMain = findViewById(R.id.viewPagerMain);
        tabMainLayout = findViewById(R.id.tabMainLayout);
        fabPost = findViewById(R.id.fabPost);

        // on click floating action button
        fabPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // pindah ke halaman post
                Intent intent = new Intent(MainActivity.this, PostActivity.class);
                startActivity(intent);
            }
        });

        // setup view pager
        setupViewPager(viewPagerMain);

        // pasang view pager ke tab layout
        tabMainLayout.setupWithViewPager(viewPagerMain);
    }

    private void setupViewPager(ViewPager viewPager) {
        // membuat adapter view pager
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // nambahin fragment popular & my photo
        adapter.addFragment(new RecentFragment(), "Popular");
        adapter.addFragment(new MyPhotoFragment(), "My Photos");

        // set adapter ke view pager
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate menu option
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.optionLogout:
                // proses logout
                signOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void signOut() {
        // logout user menggunakan firebase auth
        FirebaseAuth.getInstance().signOut();

        // pindah lagi ke login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    // class view pager adapter berguna untuk menyimpan fragment-fragment yang akan digunakan
    private class ViewPagerAdapter extends FragmentPagerAdapter {
        // list fragment
        List<Fragment> fragmentList = new ArrayList<>();
        // list judul fragment
        List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        // nambahin fragment ke view pager
        public void addFragment(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
}
