package com.example.unilink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.unilink.Activities.HomescreenActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class othersProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_profile);
        showBottomSheetDialog();


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

    public void showBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_friend_request);

        LinearLayout accept = bottomSheetDialog.findViewById(R.id.acceptbutton);
        LinearLayout ignore = bottomSheetDialog.findViewById(R.id.ignorebutton);

        bottomSheetDialog.show();
        
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(othersProfileActivity.this, "Friend Request Accepted", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }
        });
        
        ignore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(othersProfileActivity.this, "Friend Request Ignored", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();
            }
        });

    }
}

