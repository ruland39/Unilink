package com.example.unilink.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.strictmode.FragmentTagUsageViolation;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.unilink.R;
import com.example.unilink.Fragments.Registration.addBioFragment;
import com.example.unilink.Fragments.Registration.addBirthdayFragment;
import com.example.unilink.Fragments.Registration.addProfileBannerFragment;
import com.example.unilink.Fragments.Registration.addProfilePictureFragment;

public class profileSetupActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment fragment1, fragment2, fragment3, fragment4;
    private com.example.unilink.Fragments.Registration.addProfilePictureFragment addProfilePictureFragment;
    private com.example.unilink.Fragments.Registration.addProfileBannerFragment addProfileBannerFragment;
    private com.example.unilink.Fragments.Registration.addBirthdayFragment addBirthdayFragment;
    private com.example.unilink.Fragments.Registration.addBioFragment addBioFragment;
    private int currentFragmentIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);

        fragmentManager = getSupportFragmentManager();

        addProfilePictureFragment = new addProfilePictureFragment();
        addProfileBannerFragment = new addProfileBannerFragment();
        addBirthdayFragment = new addBirthdayFragment();
        addBioFragment = new addBioFragment();

        // Replace the initial fragment with addProfilePictureFragment
        fragmentManager.beginTransaction()
                .replace(R.id.frame_layout, addProfilePictureFragment)
                .commit();

        ImageButton backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button proceedbutton = findViewById(R.id.proceedbutton);
        proceedbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentFragmentIndex++;
                switch (currentFragmentIndex){

                    case 1:
                        // Replace addProfilePictureFragment with addProfileBannerFragment
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_layout, addProfileBannerFragment)
                                .commit();
                        currentFragmentIndex++;
                        break;
                    case 2:
                        // Replace addProfileBannerFragment with addBirthdayFragment
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_layout, addBirthdayFragment)
                                .commit();
                        currentFragmentIndex++;
                        break;
                    case 3:
                        // Replace addBirthdayFragment with addBioFragment
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_layout, addBioFragment)
                                .commit();
                        currentFragmentIndex++;
                        break;
                    case 4:
                        // Do something when the user reaches the last fragment
                        openHomeScreen();
                        break;

                }

            }
        });



    }

    public void openHomeScreen() {
        Intent i = new Intent(this, HomescreenActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }


}