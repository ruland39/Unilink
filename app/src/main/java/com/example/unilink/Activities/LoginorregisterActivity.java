package com.example.unilink.Activities;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unilink.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginorregisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginorregister);

        // login button
        Button button1 = findViewById(R.id.loginbutton);
        button1.setOnClickListener(v -> openLoginPage());

        // register button
        Button button2 = findViewById(R.id.registerbutton);
        button2.setOnClickListener(v -> openRegisterPage());
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        // If user is currently logged in (inSession)
        if (currentUser != null) {
            String userId = currentUser.getUid();
            // add FirebaseKey into the SharedPreference
            getSharedPreferences("UserPrefs",MODE_PRIVATE).edit().putString("firebasekey", userId).commit();
            openHomeScreen();
            finish();
        }
    }

    @Override
    public void onBackPressed(){
        finish();
    }

    public void openLoginPage() {
        Intent intent1 = new Intent(this, LoginpageActivity.class);
        startActivity(intent1);
    }

    public void openRegisterPage() {
        Intent intent2 = new Intent(this, RegisterpageActivity.class);
        startActivity(intent2);
    }

    public void openHomeScreen(){
        Intent i = new Intent(this, HomescreenActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

}