package com.example.unilink.Activities;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.unilink.Activities.FeaturePage.FeaturePageActivity;
import com.example.unilink.Models.UnilinkUser;
import com.example.unilink.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

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
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
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
        // basically, hold the layout main page for 1.5 seconds
        new Handler().postDelayed(() -> {
            FirebaseUser currentUsr = mAuth.getCurrentUser();
            // in session
            if (currentUsr != null) {
                String userId = currentUsr.getUid();
                getSharedPreferences("UserPrefs", MODE_PRIVATE).edit().putString("firebasekey", userId).commit();
                getUserInfo(user -> {
                    Gson gson = new Gson();
                    String objString = gson.toJson(user);
                    getSharedPreferences("UserPrefs",MODE_PRIVATE).edit().putString("userJson", objString).commit();
                    Log.d("com.example.unilink", "Succesfully added User JSON to SharedPref: " + objString);
                    if (user != null)
                        openHomeScreen();
                    else
                        finish();
                });
            } else
                openFp();
        }, 1500);
    }

    // Get the user information from firestore
    private void getUserInfo(GetUserCallback myCallback) {
        // Log.d("com.example.unilink", "Getting User Information from Firestore for: " + user.getEmail());
        db.collection("user_information")
                .whereEqualTo("authId", getSharedPreferences("UserPrefs",MODE_PRIVATE).getString("firebasekey", ""))
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            UnilinkUser uUser = doc.toObject(UnilinkUser.class);
                            myCallback.onCallback(uUser);
                        }
                    } else {
                        Log.w("com.example.unilink", "Error getting document: ", task.getException());
                        Toast.makeText(getApplicationContext(), "Unable to get User Information",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private void openFp() {
        Intent i = new Intent(MainActivity.this, FeaturePageActivity.class);
        startActivity(i);
        finish();
    }

    private void openHomeScreen() {
        Intent i = new Intent(MainActivity.this, HomescreenActivity.class);
        startActivity(i);
        finish();
    }

    private interface GetUserCallback {
        void onCallback(UnilinkUser user);
    }
}
