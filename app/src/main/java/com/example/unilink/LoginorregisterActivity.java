package com.example.unilink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginorregisterActivity extends AppCompatActivity {
    private Button button1; //login button
    private Button button2; //register button
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
    }

    public void openLoginPage(){
        Intent intent1 = new Intent(this, LoginpageActivity.class);
        startActivity(intent1);
    }

    public void openRegisterPage(){
        Intent intent2 = new Intent(this, RegisterpageActivity.class);
        startActivity(intent2);
    }

}