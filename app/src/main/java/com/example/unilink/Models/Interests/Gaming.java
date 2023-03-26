package com.example.unilink.Models.Interests;

import java.util.ArrayList;
import java.util.List;

// Define the Books class that inherits from Category
public class Gaming extends InterestsCategory{
    public List<Interest> chosenInterests = new ArrayList<>();
    public enum Interest {
        Console,
        PC,
        Mobile,
        FirstPerson_shooter,
        Strategy,
        Sports,
        RPG
    }

    public Gaming(int priorityLevel) {
        super(priorityLevel);
    }

    public void addGamingInterest(Gaming.Interest interest) {
        chosenInterests.add(interest);
    }
}
