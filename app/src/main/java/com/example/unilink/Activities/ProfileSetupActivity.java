package com.example.unilink.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.unilink.Fragments.Registration.ProfileSetupListener;
import com.example.unilink.Fragments.Registration.addBioFragment;
import com.example.unilink.Fragments.Registration.addBirthdayFragment;
import com.example.unilink.Fragments.Registration.addProfileBannerFragment;
import com.example.unilink.Fragments.Registration.addProfileInterestFragment;
import com.example.unilink.Fragments.Registration.addProfilePictureFragment;
import com.example.unilink.Models.UnilinkAccount;
import com.example.unilink.Models.UnilinkUser;
import com.example.unilink.R;
import com.example.unilink.Services.AccountService;

import java.util.Date;
import java.util.List;

public class ProfileSetupActivity extends AppCompatActivity implements ProfileSetupListener {

    private FragmentManager fragmentManager;
    private int currentFragmentIndex = 0;

    private UnilinkAccount uAcc = null;
    private UnilinkUser uUser;
    private AccountService accountService;
    private Button proceedBtn;
    private String currentBio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_setup);
        accountService = new AccountService();
        Intent callerIntent = getIntent();
        uAcc = callerIntent.getParcelableExtra("AuthenticatedUser");
        uUser = new UnilinkUser(uAcc.getUid());

        ImageButton backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(v -> {
            // Create a dialog to let user know registration cancelation
            new AlertDialog.Builder(this)
                    .setMessage("Would you like to cancel account registration?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        accountService.deleteAccount(uAcc.getAuthId());
                        dialogInterface.cancel();
                        finish();
                    })
                    .setNegativeButton("No", (dialog, i) -> {
                        dialog.cancel();
                    })
                    .create()
                    .show();
        });

        fragmentManager = getSupportFragmentManager();

        addProfilePictureFragment profilePictureFragment = addProfilePictureFragment.newInstance(uAcc);
        addProfileBannerFragment profileBannerFragment = addProfileBannerFragment.newInstance(uAcc);
        addBirthdayFragment birthdayFragment = addBirthdayFragment.newInstance(uAcc);
        addBioFragment bioFragment = addBioFragment.newInstance();
        addProfileInterestFragment interestFragment = new addProfileInterestFragment();

        // Replace the initial fragment with addProfilePictureFragment
        fragmentManager.beginTransaction()
                .replace(R.id.frame_layout, profilePictureFragment)
                .commitNow();

        proceedBtn = findViewById(R.id.proceedbutton);
        proceedBtn.setEnabled(false);
        proceedBtn.setOnClickListener(v -> {
            currentFragmentIndex++;
            switch (currentFragmentIndex) {
                case 1:
                    // Replace addProfilePictureFragment with addProfileBannerFragment
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_layout, profileBannerFragment)
                            .commitNow();
                    break;
                case 2:
                    // Replace addProfileBannerFragment with addBirthdayFragment
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_layout, birthdayFragment)
                            .commitNow();
                    break;
                case 3:
                    // Replace addBirthdayFragment with addBioFragment
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_layout, bioFragment)
                            .commitNow();
                    break;
                case 4:
                    uUser.setBio(currentBio);
                    Toast.makeText(this, "Set Bio!: " + currentBio, Toast.LENGTH_SHORT).show();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_layout, interestFragment)
                            .commitNow();
                    break;
                case 5:
                    // Do something when the user reaches the last fragment
                    openHomeScreen();
                    break;
            }
            proceedBtn.setEnabled(false);
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }
    public void openHomeScreen() {
        Intent i = new Intent(this, HomescreenActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    @Override
    public void AddedProfileImage(String newProfileImageURL) {
        uUser.setProfilePicture(newProfileImageURL);
        Toast.makeText(this, "Added Profile Picture: " + newProfileImageURL,
                Toast.LENGTH_SHORT).show();
        proceedBtn.setEnabled(true);
    }

    @Override
    public void AddedProfileBanner(String newBannerImageURL) {
        uUser.setProfileBanner(newBannerImageURL);
        Toast.makeText(this, "Added Profile Banner: " + newBannerImageURL,
                Toast.LENGTH_SHORT).show();
        proceedBtn.setEnabled(true);
    }

    @Override
    public void AddedBirthdate(Date newBirthDate){
        uUser.setBirthdate(newBirthDate);
        Toast.makeText(this, "Birthdate set!:" + newBirthDate, Toast.LENGTH_SHORT).show();
        proceedBtn.setEnabled(true);
    }

    @Override
    public void AddedBio(String bio){
        currentBio = bio;
        proceedBtn.setEnabled(true);
    }

    @Override
    public void AddedInterest(List<Enum> interests) {

    }
}