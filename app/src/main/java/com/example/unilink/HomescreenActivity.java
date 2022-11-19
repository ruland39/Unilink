package com.example.unilink;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;

public class HomescreenActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    private FirebaseAuth mAuth;
    private SharedPreferences sharedPref;

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            Intent i = new Intent(this, LoginorregisterActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);

        mAuth = FirebaseAuth.getInstance();

        // drawerLayout = findViewById(R.id.drawer_layout);
        // navigationView = findViewById(R.id.collapseMenu);

        ImageButton collapsemenubtn = findViewById(R.id.collapseMenuButton);
        collapsemenubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // drawerLayout.openDrawer(GravityCompat.START);
                openCollapseMenu();
            }
        });

        ImageButton homebtn = findViewById(R.id.homeBtn);
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHomeScreen();

            }
        });

        ImageButton chatbtn = findViewById(R.id.chatBtn);
        chatbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openChatScreen();

            }
        });

        ImageButton notifbtn = findViewById(R.id.notifBtn);
        notifbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNotifScreen();

            }
        });

        ImageButton profilebtn = findViewById(R.id.profileBtn);
        profilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openProfileScreen();

            }
        });

    }

    public void openHomeScreen() {
        Intent i = new Intent(this, HomescreenActivity.class);
        startActivity(i);
    }

    public void openChatScreen() {
        Intent i = new Intent(this, ChatscreenActivity.class);
        startActivity(i);
    }

    public void openNotifScreen() {
        Intent i = new Intent(this, NotificationscreenActivity.class);
        startActivity(i);
    }

    public void openProfileScreen() {
        Intent i = new Intent(this, ProfilescreenActivity.class);
        startActivity(i);
    }

    public void openCollapseMenu() {
        // drawerLayout.setVisibility(View.VISIBLE);
        // remove sharedpreferences
        // open loginorregister
        mAuth.signOut();
        sharedPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove("firebasekey").commit();
        Intent i = new Intent(this, LoginorregisterActivity.class);
        startActivity(i);
        finish();
    }
}