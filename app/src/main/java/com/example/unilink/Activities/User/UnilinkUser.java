package com.example.unilink.Activities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class UnilinkUser {
    private PriorityQueue<InterestsCategory> categories;
    private String bio;
    private String userID;
    private LocalDateTime timeCreated;
    public List<String> userIDList;

    public UnilinkUser(String userID, String bio) {
        this.userID = userID;
        this.bio = bio;
        this.categories = new PriorityQueue<>();
        categories.add(new Food(0));
        categories.add(new Music(0));
        categories.add(new Movie(0));
        categories.add(new Sport(0));
        categories.add(new Gaming(0));
        this.userIDList = new ArrayList<>();
        this.timeCreated = LocalDateTime.now();
    }

    public PriorityQueue<InterestsCategory> getCategories() {
        return categories;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

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
        userIDList.add(userID);
    }

    public List<String> getUserIDList() {
        return userIDList;
    }
}
