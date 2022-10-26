package com.example.unilink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FeaturePage3Activity extends AppCompatActivity {
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feature_page3);

        button = (Button) findViewById(R.id.getstartedbutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openLoginorRegisterPage();
            }
        });
    }
    public void openLoginorRegisterPage(){
        Intent intent = new Intent(this,LoginorregisterActivity.class);
        startActivity(intent);
    }
}