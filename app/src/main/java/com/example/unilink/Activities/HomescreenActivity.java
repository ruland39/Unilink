package com.example.unilink.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.DialogFragment;

import com.example.unilink.Fragments.ChatFragment;
import com.example.unilink.Fragments.HomeFragment;
import com.example.unilink.Fragments.NotificationFragment;
import com.example.unilink.Fragments.ProfileFragment;
import com.example.unilink.R;
import com.example.unilink.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.util.Log;
import android.Manifest;
import android.content.pm.PackageManager;
import android.app.Dialog;

import com.example.unilink.UnilinkApplication;
import com.example.unilink.Models.UnilinkUser;
import com.example.unilink.Dialogs.BluetoothHomeScreenDialog;

public class HomescreenActivity extends AppCompatActivity
        implements BluetoothHomeScreenDialog.BtHomeScreenDialogListener {

    // TODO: BIG TODO, FIX UP THE HOMESCREEN ACTIVITY and add all of its fragments
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

    @RequiresApi(api = Build.VERSION_CODES.S)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Validate permission
        String[] BtPerms = {
                Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_ADMIN
        };
        validatePermissions(BtPerms);

        setContentView(R.layout.activity_homescreen);

        navdrawerBtn = findViewById(R.id.navDrawerBtn);
        navdrawerBtn.setOnClickListener(v -> logout());

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, homeFragment).commit();

        // Get Authentication instance
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUsr = mAuth.getCurrentUser();
        this.currentUser = new UnilinkUser();
        if (currentUsr == null) {
            Intent i = new Intent(this, LoginorregisterActivity.class);
            startActivity(i);
            finish();
        } else {
            String userJson = getSharedPreferences("UserPrefs", MODE_PRIVATE).getString("userJson", null);
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
        bottomNavigationView.setOnItemSelectedListener(item -> {
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
        getSharedPreferences("UserPrefs", MODE_PRIVATE).edit().remove("firebasekey").commit();
        getSharedPreferences("UserPrefs", MODE_PRIVATE).edit().remove("userJson").commit();
        Log.d("com.example.unilink", "User Logout Successful");
        Intent i = new Intent(this, LoginorregisterActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

    private void validatePermissions(@NonNull String[] perms) {
        // Initial check of the permissions, comes out to be true or not if it has
        Boolean granted = true;
        for (String perm : perms) {
            if (ActivityCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                granted = false;
            }
        }

        if (!granted)
            // Request permissions
            ActivityCompat.requestPermissions(this, perms, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // Add in the code, that logs out and disables the app if Bluetooth isn't
        // allowed by the user.
        if (requestCode == 1) {
            System.out.println("Request Permission CALLED");
            for (int i = 0, len = permissions.length; i < len; i++) {
                String perm = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED && perm.equals(Manifest.permission.BLUETOOTH_ADMIN)) {
                    BluetoothHomeScreenDialog btDialog = new BluetoothHomeScreenDialog();
                    btDialog.show(getSupportFragmentManager(), "BluetoothHomeDialogFragment");
                    finish();
                }
            }
        }

        // if (grantResults.length > 0 && grantResults[0] ==
        // PackageManager.PERMISSION_DENIED) {
        // BluetoothHomeScreenDialog btDialog = new BluetoothHomeScreenDialog();
        // btDialog.show(getSupportFragmentManager(), "BluetoothHomeDialogFragment");
        // finish();
        // }
    }

    // Dialog function
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        String[] BtPerms = {
                Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.BLUETOOTH_ADMIN
        };
        validatePermissions(BtPerms);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        System.out.println("LOL");
        UnilinkApplication.ExitApplication();
    }

}
