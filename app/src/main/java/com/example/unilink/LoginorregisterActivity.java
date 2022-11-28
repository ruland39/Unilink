package com.example.unilink;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginorregisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button button1; // login button
    private Button button2; // register button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginorregister);

        button1 = (Button) findViewById(R.id.loginbutton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginPage();
            }
        });

        button2 = findViewById(R.id.registerbutton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterPage();
            }
        });
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
            getPreferences(MODE_PRIVATE).edit().putString("firebasekey", userId).commit();
            openHomeScreen();
            finish();
        }
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