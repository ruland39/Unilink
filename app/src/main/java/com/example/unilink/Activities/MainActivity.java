package com.example.unilink.Activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.unilink.Activities.FeaturePage.FeaturePageActivity;
import com.example.unilink.R;
import com.example.unilink.Services.AccountService;
import com.example.unilink.Services.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;
import com.onesignal.OneSignal;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = "MainActivity";
    private final static String WAVE_NOTIFCHANNEL_ID = "69420";
    // onCreate refers to a method that fires when the app is *created*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // forces light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        // Check for bluetooth availability
//        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        // Checks for bluetooth support; if unavailable, end the application.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "Bluetooth not supported. Application requires bluetooth to run", Toast.LENGTH_SHORT).show();
            finish();
        }
        // configuring window to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // set first page
        setContentView(R.layout.activity_main);
    }

    // onStart method is called when the activity enters the Started state
    @Override
    public void onStart() {
        super.onStart();
        new Handler().postDelayed(()->{
            OneSignal.promptForPushNotifications();
            AccountService accountService = new AccountService();
            UserService userService = new UserService();
            // in session
            if (accountService.isInSession()) {
                Log.d(TAG, "User session found!");
                accountService.getAccountByAuthID(accountService.getCurrentUserSessionID(), uAcc -> {
                    Log.d(TAG, "Retrieved current inSession User: " + uAcc);
                    if (uAcc != null) {
                        userService.getUserByUid(uAcc.getUid(), uUser -> {
                            if (uUser != null) {
                                Intent i = new Intent(MainActivity.this, HomescreenActivity.class);
                                i.putExtra("AuthenticatedUser", (Parcelable) uAcc);
                                i.putExtra("CreatedUser", uUser);
                                if (getIntent().getAction() != null)
                                    if (getIntent().getAction().equals("OPEN_WAVER_PROFILE_APPLICATION")){
                                        i.setAction("OPEN_WAVER_PROFILE");
                                        i.putExtra("SENDER_USERID", getIntent().getStringExtra("SENDER_USERID"));
                                    }
                                startActivity(i);
                                finish();
                            }
                        });
                    } else {
                        Toast.makeText(this,
                                "No User Information Found but is in session! Contact Developer!",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

            } else {
                Log.d(TAG, "User session not found!");
                // Get Boolean on first start
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                boolean firstTime = prefs.getBoolean(getString(R.string.firstTime), true);
                Intent i = new Intent(this, LoginorregisterActivity.class);
                if (firstTime) {
                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putBoolean(getString(R.string.firstTime),false).commit();
                    i = new Intent(this, FeaturePageActivity.class);
                }
                startActivity(i);
                finish();
            }

            // Creating the notification channel for friend requests
            CharSequence name = getString(R.string.wave_notification_channel);
            String description = getString(R.string.wave_notification_channel_desc);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(WAVE_NOTIFCHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }, 1500);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
