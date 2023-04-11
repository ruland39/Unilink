package com.example.unilink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.unilink.Activities.HomescreenActivity;

public class othersProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_profile);


        ImageButton backbutton = findViewById(R.id.backbutton);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToHomeScreenActivity();
            }
        });

    }

    private void backToHomeScreenActivity() {
        Intent i = new Intent(this, HomescreenActivity.class);
        startActivity(i);
        finish();
    }
}