package com.example.unilink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.unilink.databinding.ActivityMainBinding;
import org.checkerframework.checker.units.qual.C;

public class HomescreenActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private SharedPreferences sharedPref;
    //DrawerLayout drawerLayout;
    //NavigationView navigationView;
    ActivityMainBinding binding;
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    ChatFragment chatFragment = new ChatFragment();
    NotificationFragment notificationFragment = new NotificationFragment();
    ProfileFragment profileFragment = new ProfileFragment();

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
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, homeFragment).commit();
        
        mAuth = FirebaseAuth.getInstance();

//     drawerLayout = findViewById(R.id.drawer_layout);
//     navigationView = findViewById(R.id.collapseMenu);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home: getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, homeFragment).commit();
                        return true;
                    case R.id.chat: getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, chatFragment).commit();
                        return true;
                    case R.id.notification: getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, notificationFragment).commit();
                        return true;
                    case R.id.profile: getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, profileFragment).commit();
                        return true;
                }
                return false;
            }
        });
        
        // for now collapsemenu signs you out for authentication purposes
        ImageButton collapsemenubtn = findViewById(R.id.collapseMenuButton);
        collapsemenubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCollapseMenu();
            }
        });
        
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

//        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomnavigationview);
//        bottomNavigationView.setBackground(null);
//        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                Fragment temp = null;
//
//                switch (item.getItemId()) {
//
//                    case R.id.homeBtn:
//                        temp = new HomeFragment();
//                        break;
//
//                    case R.id.chatBtn:
//                        temp = new ChatFragment();
//                        break;
//
//                    case R.id.notifBtn:
//                        temp = new NotificationFragment();
//                        break;
//
//                    case R.id.profileBtn:
//                        temp = new ProfileFragment();
//                        break;
//                }
//
//                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, temp).commit();
//                return true;
//
//            }
//
//
//        });


//        ImageButton collapsemenubtn = findViewById(R.id.collapseMenuButton);
//        collapsemenubtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //drawerLayout.openDrawer(GravityCompat.START);
//                openCollapseMenu();
//            }
//        });
//        ImageButton homebtn = findViewById(R.id.homeBtn);
//        homebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openHomeScreen();
//
//            }
//        });
//
//        ImageButton chatbtn = findViewById(R.id.chatBtn);
//        chatbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openChatScreen();
//
//            }
//        });
//
//        ImageButton notifbtn = findViewById(R.id.notifBtn);
//        notifbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openNotifScreen();
//
//            }
//        });
//
//        ImageButton profilebtn = findViewById(R.id.profileBtn);
//        profilebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openProfileScreen();
//
//            }
//        });



//    public void openHomeScreen(){
//        Intent i = new Intent(this, HomescreenActivity.class);
//        startActivity(i);
//    }
//
//    public void openChatScreen(){
//        Intent i = new Intent(this, ChatscreenActivity.class);
//        startActivity(i);
//    }
//
//    public void openNotifScreen(){
//        Intent i = new Intent(this, NotificationscreenActivity.class);
//        startActivity(i);
//    }
//
//    public void openProfileScreen(){
//        Intent i = new Intent(this, ProfilescreenActivity.class);
//        startActivity(i);
//    }
//
//    public void openCollapseMenu(){
//        drawerLayout.setVisibility(View.VISIBLE);
//    }
//}

