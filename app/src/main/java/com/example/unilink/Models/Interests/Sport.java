package com.example.unilink.Models.Interests;

import java.util.ArrayList;
import java.util.List;

// Define the Sports class that inherits from Category
public class Sport extends InterestsCategory{
    public List<Interest> chosenInterests = new ArrayList<>();
    public enum Interest {
        Soccer,
        Basketball,
        Football,
        Tennis,
        Volleyball,
        Swimming,
        Running
    }

    public Sport(int priorityLevel) {
        super(priorityLevel);
    }

    public void addSportInterest(Sport.Interest interest) {
        chosenInterests.add(interest);
    }
}
