package com.example.unilink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FeaturePage2Activity extends AppCompatActivity {
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feature_page2);

        button = (Button) findViewById(R.id.buttontofeaturepage3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFeaturePage3();
            }
        });
    }

public void openFeaturePage3(){
        Intent intent = new Intent(this,FeaturePage3Activity.class);
        startActivity(intent);
}
}