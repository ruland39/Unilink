package com.example.unilink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.util.Log;

import com.example.unilink.databinding.ActivityMainBinding;
import com.example.unilink.UnilinkUser;

import javax.crypto.spec.GCMParameterSpec;

import org.checkerframework.checker.units.qual.A;
import org.checkerframework.checker.units.qual.C;

public class HomescreenActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private SharedPreferences sharedPref;
    private UnilinkUser currentUser;
    private Bundle bundle;

    ActivityMainBinding binding;
    BottomNavigationView bottomNavigationView;

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageButton navdrawerBtn;

    HomeFragment homeFragment = new HomeFragment();
    ChatFragment chatFragment = new ChatFragment();
    NotificationFragment notificationFragment = new NotificationFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    public void onStart() {
        super.onStart();        
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

        navdrawerBtn = findViewById(R.id.navDrawerBtn);
        navdrawerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, homeFragment).commit();

        // Get Atuhentication instance
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUsr = mAuth.getCurrentUser();        
        this.currentUser = new UnilinkUser();
        if (currentUsr == null) {
            Intent i = new Intent(this, LoginorregisterActivity.class);
            startActivity(i);
            finish();
        } else {
            String userJson = getSharedPreferences("UserPrefs",MODE_PRIVATE).getString("userJson", null);
            Log.d("com.example.unilink", "User JSON: " + userJson);
            Gson gson = new Gson();
            this.currentUser = gson.fromJson(userJson, UnilinkUser.class);
            if (this.currentUser != null)
                Log.d("com.example.unilink", "Succesfully taken object from userJson");
            else
                Log.w("com.example.unilink", "Error: current User is NULL");
        }

        bundle = new Bundle();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                UnilinkUser user = getCurrentUser();
                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, homeFragment)
                                .commit();
                        return true;
                    case R.id.chat:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, chatFragment)
                                .commit();
                        return true;
                    case R.id.notification:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, notificationFragment)
                                .commit();
                        return true;
                    case R.id.profile:
                        // Add Profile Fragment argument to hold UnilinkUser
                        // bundle.putParcelable("user", user);
                        // user = bundle.getParcelable("user");
                        // profileFragment.setArguments(bundle);
                        profileFragment = profileFragment.newInstance(user);
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, profileFragment)
                                .commit();
                        return true;
                }
                return false;
            }
        });

        // drawerLayout = findViewById(R.id.drawerLayout);
        // navigationView = findViewById(R.id.navigationView);
        // toolbar = findViewById(R.id.toolbar);
        //
        // setSupportActionBar(toolbar);
        //
        // ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout,
        // toolbar, R.string.opendrawer,
        // R.string.closedrawer);

        // drawerLayout.addDrawerListener(toggle);

        // toggle.syncState();

        // to open the options in the drawe
        // navigationView.setNavigationItemSelectedListener(new
        // NavigationView.OnNavigationItemSelectedListener() {
        // @Override
        // public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // int id = item.getItemId();
        // // Settings
        // // Logout
        // // Support
        // // Terms and Policies
        // // About
        //
        // drawerLayout.closeDrawer(GravityCompat.START);
        // return true;
        // }
        // });

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private UnilinkUser getCurrentUser() {
        return this.currentUser;
    }

    // logout method
    public void logout() {
        // drawerLayout.setVisibility(View.VISIBLE);
        // remove sharedpreferences
        // open loginorregister
        mAuth.signOut();
        getSharedPreferences("UserPrefs",MODE_PRIVATE).edit().remove("firebasekey").commit();
        getSharedPreferences("UserPrefs",MODE_PRIVATE).edit().remove("userJson").commit();
        Log.d("com.example.unilink", "User Logout Successful");
        Intent i = new Intent(this, LoginorregisterActivity.class);
        startActivity(i);
        finish();
    }

    // load fragment method
    public void loadfragment() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.frame_layout, homeFragment);
        ft.commit();
    }

    // back method
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    public void openNavdrawer() {
        drawerLayout.setVisibility(View.VISIBLE);
    }

}
