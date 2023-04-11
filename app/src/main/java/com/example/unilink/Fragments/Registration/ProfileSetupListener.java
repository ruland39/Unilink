package com.example.unilink.Fragments.Registration;

import com.example.unilink.Models.Interests.Interest;

import java.util.Date;
import java.util.List;

public interface ProfileSetupListener {
    void AddedProfileImage(String newProfileImageURL);
    void AddedProfileBanner(String newBannerImageURL);
    void AddedBirthdate(Date newBirthdate);
    void AddedBio(String bio);
    void AddedInterest(List<Interest> interests);
}
