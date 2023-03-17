package com.example.unilink.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.fragment.app.DialogFragment;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.WorkRequest;
import androidx.core.view.GravityCompat;

import com.example.unilink.Activities.BLE.BeaconWorker;
import com.example.unilink.Fragments.ChatFragment;
import com.example.unilink.Fragments.HomeFragment;
import com.example.unilink.Fragments.NotificationFragment;
import com.example.unilink.Fragments.ProfileFragment;
import com.example.unilink.R;
import com.example.unilink.Services.UserService;
import com.example.unilink.UnilinkApplication;
import com.example.unilink.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.util.Log;
import android.Manifest;
import android.content.pm.PackageManager;

import com.example.unilink.Models.UnilinkUser;
import com.example.unilink.Dialogs.BluetoothHomeScreenDialog;

public class HomescreenActivity extends AppCompatActivity
        implements BluetoothHomeScreenDialog.BtHomeScreenDialogListener {

    private static final String TAG = "HomescreenActivity";
    private UnilinkUser currentUser;
    private UserService userService;

    ActivityMainBinding binding;
    BottomNavigationView bottomNavigationView;

    DrawerLayout drawerLayout;
    DrawerLayout drawNavView;
    ImageButton navdrawerBtn;
    NavigationView navigationView;

    HomeFragment homeFragment = new HomeFragment();
    ChatFragment chatFragment = new ChatFragment();
    NotificationFragment notificationFragment = new NotificationFragment();
    ProfileFragment profileFragment = new ProfileFragment();

    @Override
    public void onStart() {
        super.onStart();
        this.currentUser = null;
        if (!userService.isInSession()) {
            Intent i = new Intent(this, LoginorregisterActivity.class);
            startActivity(i);
            finish();
        } else {
            if (currentUser == null) {
                Intent i = getIntent();
                currentUser = i.getParcelableExtra("AuthenticatedUser");
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userService = new UserService();

        // Saved Instance state of activity to retrieve data
        if (savedInstanceState != null) {
            currentUser = savedInstanceState.getParcelable("CurrentUser");
        }

        // Validate permission
        String[] BtPerms = {
                Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_ADVERTISE
        };
        validatePermissions(BtPerms);

        setContentView(R.layout.activity_homescreen);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, homeFragment).commit();

        navdrawerBtn=findViewById(R.id.navDrawerBtn);
        navdrawerBtn.setOnClickListener(v -> {
            drawNavView=findViewById(R.id.nav_drawer_layout);
            if(!drawNavView.isDrawerOpen(GravityCompat.START)) drawNavView.openDrawer(GravityCompat.START);
            else drawNavView.closeDrawer(GravityCompat.END);
        });

        navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.logout:
                    logout();
                    return true;
            }
            return false;
        });

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
                    profileFragment = profileFragment.newInstance(user);
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, profileFragment)
                            .commit();
                    return true;
            }
            return false;
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "Activity going to the background! Saving states..");
        outState.putParcelable("CurrentUser", currentUser);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkSelfPermission(Manifest.permission.BLUETOOTH_ADVERTISE) == PackageManager.PERMISSION_GRANTED){
            System.out.println("Transmission started");
            startBeaconTransmission();
        } else {
            startBeaconTransmission();
            System.out.println("Transmission Failed; Bluetooth Ad not allowed");
//            requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 2);
        }
    }

    private void startBeaconTransmission() {
        if(currentUser == null)
            throw new RuntimeException("CURRENT USER IS NULL");
        WorkRequest beaconWorkRequest =
                new OneTimeWorkRequest.Builder(BeaconWorker.class)
                        .setInputData(new Data.Builder()
                                .putString("CurrentUid", currentUser.getUid())
                                .build())
                        .build();
        WorkManager.getInstance(this).enqueue(beaconWorkRequest);
    }

    private UnilinkUser getCurrentUser() {
        return this.currentUser;
    }

    // logout method
    public void logout() {
        Intent ix = getIntent();
        ix.removeExtra("AuthenticatedUser");
        userService.signOut();
        Log.d(TAG, "User Logout Successful");
        ix = new Intent(this, LoginorregisterActivity.class);
        ix.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(ix);
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

    private Boolean validatePermissions(@NonNull String[] perms) {
        System.out.println("Validating Permission");
        // Initial check of the permissions, comes out to be true or not if it has
        Boolean granted = true;
        for (String perm : perms) {
            if (ActivityCompat.checkSelfPermission(this, perm) != PackageManager.PERMISSION_GRANTED) {
                System.out.println("Requesting permissions for "+perm+" denied");
                granted = false;
            }
        }

        if (!granted) {
            System.out.println("Requesting permissions");
            // Request permissions
            requestPermissions(perms, 1);
        }
        return granted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // Add in the code, that logs out and disables the app if Bluetooth isn't
        // allowed by the user.
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        System.out.println("Requesting Permissions Code: "+requestCode + ";");
        if (requestCode == 1) {
            int denied = 0;
            System.out.println("Requesting Permissions CALLED");
            for (int i = 0, len = permissions.length; i < len; i++) {
                String perm = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    denied++;
                }
            }
            System.out.println("Requesting Permissions for " + denied + " denieds");
            if (denied > 0) {
                BluetoothHomeScreenDialog btDialog = new BluetoothHomeScreenDialog();
                btDialog.setCancelable(false);
                btDialog.show(getSupportFragmentManager(), "BluetoothHomeDialogFragment");
            } else
                startBeaconTransmission();
        } else {
            return;
        }
    }

    // Dialog function
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        dialog.getDialog().cancel();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        finishAndRemoveTask();
    }

}
