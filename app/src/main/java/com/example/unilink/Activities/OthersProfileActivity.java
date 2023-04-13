package com.example.unilink.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.unilink.Models.Interests.Interest;
import com.example.unilink.Models.UnilinkAccount;
import com.example.unilink.Models.UnilinkUser;
import com.example.unilink.Services.UserService;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.example.unilink.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

public class OthersProfileActivity extends AppCompatActivity {
    private static final String TAG = "OthersProfileActivity";
    private UserService userService;
    private UnilinkAccount uAcc;
    private UnilinkUser uUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_profile);
        showBottomSheetDialog();

        ImageButton backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(v -> finish());
    }

    @Override
    public void onStart() {
        super.onStart();
        uAcc = getIntent().getParcelableExtra("Account");
        uUser = getIntent().getParcelableExtra("User");
        userService = new UserService();

        TextView username = findViewById(R.id.defaultusername);
        ImageView banner = findViewById(R.id.profilebanner);
        ImageView pfp = findViewById(R.id.profilepic);
        ChipGroup interestChips = findViewById(R.id.interestchips);
        TextView connectionsNumTV = findViewById(R.id.connectionnumber);
        TextView bioTV = findViewById(R.id.aboutsection);

        username.setText(uAcc.getFullName());
        userService.setImage2View(this, banner, uUser.getPfbURL());
        userService.setImage2View(this, pfp, uUser.getPfpURL());
        for (Interest i : uUser.getChosenInterests()){
            Chip iChip = new Chip(this);
            iChip.setText(i.getName());
            iChip.setChipBackgroundColorResource(R.color.light_purple);
            interestChips.addView(iChip);
        }
        String connectionText = uUser.getConnectedUIDs().size() + " Connections";
        connectionsNumTV.setText(connectionText);
        bioTV.setText(uUser.getBio());
    }

    public void showBottomSheetDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_dialog_friend_request);

        LinearLayout accept = bottomSheetDialog.findViewById(R.id.acceptbutton);
        LinearLayout ignore = bottomSheetDialog.findViewById(R.id.ignorebutton);

        bottomSheetDialog.show();

        accept.setOnClickListener(v -> {
            Toast.makeText(OthersProfileActivity.this, "Friend Request Accepted", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
        });

        ignore.setOnClickListener(v -> {
            Toast.makeText(OthersProfileActivity.this, "Friend Request Ignored", Toast.LENGTH_SHORT).show();
            bottomSheetDialog.dismiss();
        });

    }
}