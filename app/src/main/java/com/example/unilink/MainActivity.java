package com.example.unilink;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import org.xml.sax.HandlerBase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences sharedPref;
    private FirebaseAuth mAuth;

    // onCreate refers to a method that fires when the app is *created*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // forces light mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // configuring window to full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // set first page
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUsr = mAuth.getCurrentUser();
                // in session
                if (currentUsr != null) {
                    String userId = currentUsr.getUid();
                    sharedPref = getPreferences(MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("firebasekey", userId).commit();
                    openHomeScreen();
                } else
                    openFp();
            }
        }, 1500);
    }

    private void openFp() {
        Intent i = new Intent(MainActivity.this, FeaturePage1Activity.class);
        startActivity(i);
        finish();
    }

    private void openHomeScreen() {
        Intent i = new Intent(MainActivity.this, HomescreenActivity.class);
        startActivity(i);
        finish();
    }

}