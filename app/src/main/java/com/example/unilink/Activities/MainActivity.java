package com.example.unilink.Activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.unilink.Activities.FeaturePage.FeaturePageActivity;
import com.example.unilink.Models.UnilinkUser;
import com.example.unilink.R;
import com.example.unilink.Services.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    // onCreate refers to a method that fires when the app is *created*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // forces light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        // Check for bluetooth availability
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        // Checks for bluetooth support; if unavailable, end the application.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE) || adapter == null) {
            Toast.makeText(this, "Bluetooth not supported. Application requires bluetooth to run", Toast.LENGTH_SHORT).show();
            finish();
        }
        // configuring window to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // set first page
        setContentView(R.layout.activity_main);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    // onStart method is called when the activity enters the Started state
    @Override
    public void onStart() {
        super.onStart();
        UserService userService = new UserService();
            // in session
            if (userService.isInSession()) {
                Log.d(TAG,"User session found!");
                userService.getUserInfoByAuthId(userService.getCurrentUserSessionID(), user -> {
                    Log.d(TAG, "Retrieved current inSession User: " + user);
                    if (user != null) {
                        Intent i = new Intent(MainActivity.this, HomescreenActivity.class);
                        i.putExtra("AuthenticatedUser", (Parcelable) user);
                        startActivity(i);
                        finish();
                    }
                    else {
                        Toast.makeText(this,
                                "No User Information Found but is in session! Contact Developer!",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            } else {
                Log.d(TAG,"User session not found!");
                openFp();
            }
    }
    private void openFp() {
        Intent i = new Intent(MainActivity.this, FeaturePageActivity.class);
        startActivity(i);
        finish();
    }
}
