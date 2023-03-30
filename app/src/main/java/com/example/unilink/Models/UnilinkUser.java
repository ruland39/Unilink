package com.example.unilink.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.unilink.Models.Interests.Food;
import com.example.unilink.Models.Interests.Gaming;
import com.example.unilink.Models.Interests.InterestsCategory;
import com.example.unilink.Models.Interests.Movie;
import com.example.unilink.Models.Interests.Music;
import com.example.unilink.Models.Interests.Sport;
import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;
import java.time.LocalDateTime;

public class UnilinkUser implements Serializable {
    private final String userID;
    private final Timestamp timeCreated;
    public List<String> ConnectedUIDs;
    private PriorityQueue<InterestsCategory> categories;
    private String bio;
    private String pfpURL;
    private String pfbURL;
    private Date birthdate;
    public UnilinkUser(String userID) {
        this.userID = userID;
        this.bio = null;
        this.pfpURL = null;
        this.pfbURL = null;
        this.categories = new PriorityQueue<>();
        categories.add(new Food(0));
        categories.add(new Music(0));
        categories.add(new Movie(0));
        categories.add(new Sport(0));
        categories.add(new Gaming(0));
        this.ConnectedUIDs = new ArrayList<>();
        this.timeCreated = Timestamp.now();
        this.birthdate = null;
    }

    public PriorityQueue<InterestsCategory> getCategories() {
        return categories;
    }

    public String getBio() {
        return bio;
    }
    public String getUserID() {return userID;}
    public String getPfpURL() {return pfpURL;}
    public String getPfbURL() {return pfbURL;}

    public void setBio(String bio) {
        this.bio = bio;
    }
    public void setProfilePicture(String url) {this.pfpURL = url;}
    public void setProfileBanner(String url) {this.pfbURL = url;}
    public void setBirthdate(Date date) {this.birthdate = date;}

    public void addChosenInterest(Enum interest, InterestsCategory category) {
        if (category instanceof Food) {
            ((Food) category).addFoodInterest((Food.Interest) interest);
            categories.remove(category);
            category.setPriorityLevel(1);
            categories.add(category);
        } else if (category instanceof Music) {
            ((Music) category).addMusicInterest((Music.Interest) interest);
            categories.remove(category);
            category.setPriorityLevel(1);
            categories.add(category);
        } else if (category instanceof Movie) {
            ((Movie) category).addMovieInterest((Movie.Interest) interest);
            categories.remove(category);
            category.setPriorityLevel(1);
            categories.add(category);
        } else if (category instanceof Sport) {
            ((Sport) category).addSportInterest((Sport.Interest) interest);
            categories.remove(category);
            category.setPriorityLevel(1);
            categories.add(category);
        } else if (category instanceof Gaming) {
            ((Gaming) category).addGamingInterest((Gaming.Interest) interest);
            categories.remove(category);
            category.setPriorityLevel(1);
            categories.add(category);
        }
    }

    public void addConnectedUser(String userID) {
        ConnectedUIDs.add(userID);
    }

    public List<String> getConnectedUIDs() {
        return ConnectedUIDs;
    }
}
