package com.example.unilink;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.annotation.NonNull;

import org.xml.sax.HandlerBase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import android.util.Log;

import com.example.unilink.UnilinkUser;
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUsr = mAuth.getCurrentUser();
                // in session
                if (currentUsr != null) {
                    String userId = currentUsr.getUid();
                    getPreferences(MODE_PRIVATE).edit().putString("firebasekey", userId).commit();
                    getUserInfo(currentUsr);
                    openHomeScreen();
                } else
                    openFp();
            }
        }, 1500);
    }

        // Get the user information from firestore 
        private void getUserInfo(FirebaseUser user) {
            Log.d("com.example.unilink", "Getting User Information from Firestore for: " + user.getEmail());
            db.collection("user_information")
                    .whereEqualTo("authId", user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>(){
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                     UnilinkUser uUser = doc.toObject(UnilinkUser.class);
                                     saveUserInfo(uUser);
                                }
                            } else {
                                Log.w("com.example.unilink", "Error getting document: ", task.getException());
                                Toast.makeText(getApplicationContext(), "Unable to get User Information", Toast.LENGTH_SHORT);                            
                                finish();
                            }
                        }
                    });
        }
    
        // Save the user info into sharedpreference json
        private void saveUserInfo(UnilinkUser user) {
            Gson gson = new Gson();
            String objString = gson.toJson(user);
            getPreferences(MODE_PRIVATE).edit().putString("userJson", objString).commit();
            Log.d("com.example.unilink", "Succesfully added User JSON to SharedPref: " + objString);
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