package com.example.unilink.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.DialogFragment;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.unilink.Activities.BLE.BeaconWorker;
import com.example.unilink.Fragments.ChatFragment;
import com.example.unilink.Fragments.HomeFragment;
import com.example.unilink.Fragments.NotificationFragment;
import com.example.unilink.Fragments.ProfileFragment;
import com.example.unilink.Models.UnilinkUser;
import com.example.unilink.R;
import com.example.unilink.Services.AccountService;
import com.example.unilink.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageButton;
import android.util.Log;
import android.Manifest;
import android.content.pm.PackageManager;

import com.example.unilink.Models.UnilinkAccount;
import com.example.unilink.Dialogs.BluetoothHomeScreenDialog;
import com.onesignal.OneSignal;

public class HomescreenActivity extends AppCompatActivity
        implements BluetoothHomeScreenDialog.BtHomeScreenDialogListener, GestureDetector.OnGestureListener {

    private static final String TAG = "HomescreenActivity";
    private UnilinkAccount currentUAcc;
    private UnilinkUser currentUUser;
    private AccountService accountService;

    ActivityMainBinding binding;
    BottomNavigationView bottomNavigationView;

    DrawerLayout drawerLayout;
    DrawerLayout drawNavView;
    ImageButton navDrawerBtn;
    NavigationView navigationView;

    private GestureDetector mDetector;
    private static final int MIN_DISTANCE = 200;
    private float x1=0;
    private float x2=0;

    HomeFragment homeFragment = new HomeFragment();
    ChatFragment chatFragment = new ChatFragment();
    NotificationFragment notificationFragment = new NotificationFragment();
    ProfileFragment profileFragment;

    @Override
    public void onStart() {
        super.onStart();
        this.currentUAcc = null;
        if (!accountService.isInSession()) {
            Intent i = new Intent(this, LoginorregisterActivity.class);
            startActivity(i);
            finish();
        } else {
            Intent i = getIntent();
            if (currentUAcc == null){
                currentUAcc = i.getParcelableExtra("AuthenticatedUser");
                OneSignal.setExternalUserId(currentUAcc.getUid());
            }
            if (currentUUser == null)
                currentUUser = i.getParcelableExtra("CreatedUser");
        }
        homeFragment = HomeFragment.newInstance(currentUAcc, currentUUser);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, homeFragment, "HOME_FRAGMENT")
                .commitNow();
    }

    @SuppressLint("MissingInflatedId")
    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mDetector = new GestureDetector(this, this);
        accountService = new AccountService();

        // Saved Instance state of activity to retrieve data
        if (savedInstanceState != null) {
            currentUAcc = savedInstanceState.getParcelable("CurrentAccount");
            currentUUser = savedInstanceState.getParcelable("CurrentUser");
        }

        // Validate permission
        String[] BtPerms = {
                Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_ADVERTISE, Manifest.permission.BLUETOOTH_CONNECT
        };
        validatePermissions(BtPerms);

        setContentView(R.layout.activity_homescreen);

        binding = ActivityMainBinding.inflate(getLayoutInflater());

        drawNavView=findViewById(R.id.nav_drawer_layout);
        navDrawerBtn =findViewById(R.id.navDrawerBtn);
        navDrawerBtn.setOnClickListener(v -> {
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
            UnilinkAccount user = getCurrentAccount();
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            // Get the fragment in the backstack
            Fragment home_frag = null;
            if (getSupportFragmentManager().getBackStackEntryCount() > 0){
                home_frag = getSupportFragmentManager().findFragmentByTag("HOME_FRAGMENT");
            }

            switch (item.getItemId()) {
                case R.id.home:
                    if (home_frag != null) {
                        getSupportFragmentManager().popBackStack("HOME_FRAGMENT", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        Log.d("HomescreenActivity", "Backstack popped");
                    }else {
                        homeFragment = HomeFragment.newInstance(currentUAcc, currentUUser );
                        trans.replace(R.id.frame_layout, homeFragment, "HOME_FRAGMENT")
                                .commitNow();
                    }
                    return true;
                case R.id.chat:
                    trans.replace(R.id.frame_layout, chatFragment, "CHAT_FRAGMENT")
                            .addToBackStack("HOME_FRAGMENT")
                            .commit();
                    return true;
                case R.id.notification:
                    trans.replace(R.id.frame_layout, notificationFragment, "NOTIF_FRAGMENT")
                            .addToBackStack("HOME_FRAGMENT")
                            .commit();
                    return true;
                case R.id.profile:
                    profileFragment = ProfileFragment.newInstance(currentUAcc, currentUUser);
                    trans.replace(R.id.frame_layout, profileFragment, "PROFILE_FRAGMENT")
                            .addToBackStack("HOME_FRAGMENT")
                            .commit();
                    return true;
            }
            return false;
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "Activity going to the background! Saving states..");
        outState.putParcelable("CurrentAccount", currentUAcc);
        outState.putParcelable("CurrentUser", currentUUser);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStop() {
        BeaconWorker.stopAdvertisement();
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        Fragment home_frag = getSupportFragmentManager().findFragmentByTag("HOME_FRAGMENT");
        if (home_frag != null) {
            Log.d(TAG, "Home Fragment detached, attaching it once more");
            getSupportFragmentManager().beginTransaction()
                    .attach(home_frag)
                    .commit();
        }

        if (checkSelfPermission(Manifest.permission.BLUETOOTH_ADVERTISE) == PackageManager.PERMISSION_GRANTED){
            System.out.println("Transmission started");
            startBeaconTransmission();
        } else {
            startBeaconTransmission();
            System.out.println("Transmission Failed; Bluetooth Ad not allowed");
        }
    }

    private void startBeaconTransmission() {
        if(currentUAcc == null)
            throw new RuntimeException("CURRENT USER IS NULL");
        OneTimeWorkRequest beaconWorkRequest =
                new OneTimeWorkRequest.Builder(BeaconWorker.class)
                        .setInputData(new Data.Builder()
                                .putString("CurrentUid", currentUAcc.getUid())
                                .build())
                        .build();
        WorkManager.getInstance(this).beginUniqueWork(
                "BeaconTransmission",
                ExistingWorkPolicy.REPLACE,
                beaconWorkRequest).enqueue();
    }

    private UnilinkAccount getCurrentAccount() {
        return this.currentUAcc;
    }

    // logout method
    public void logout() {
        Intent ix = getIntent();
        ix.removeExtra("AuthenticatedUser");
        accountService.signOut();
        Log.d(TAG, "User Logout Successful");
        BeaconWorker.stopAdvertisement();
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
        if (drawNavView.isDrawerOpen(GravityCompat.START)){
            drawNavView.closeDrawer(GravityCompat.START);
        }else
            super.onBackPressed();
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
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 2);
            }
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.d(TAG, "Action Down");
                x1 = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                x2 = event.getX();
                float valueX = x2 - x1;
                if (Math.abs(valueX) > MIN_DISTANCE) {
                    Log.d(TAG, "Action Up");
                    if (x2 > x1)
                        if (!drawNavView.isDrawerOpen(GravityCompat.START))
                            drawNavView.openDrawer(GravityCompat.START);
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onDown(@NonNull MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(@NonNull MotionEvent motionEvent, @NonNull MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

}
