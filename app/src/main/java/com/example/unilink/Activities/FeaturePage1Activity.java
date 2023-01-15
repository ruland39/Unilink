package com.example.unilink.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.unilink.R;

public class FeaturePage1Activity extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.feature_page1);

        button = (Button) findViewById(R.id.buttontofeaturepage2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFeaturePage2();    
                finish();            
            }
        });

    }

    public void openFeaturePage2() {
        Intent intent = new Intent(this, FeaturePage2Activity.class);
        startActivity(intent);        
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}