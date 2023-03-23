package com.example.unilink.Activities.User;

import java.util.ArrayList;
import java.util.List;

// Define the Music class that inherits from Category
public class Music extends InterestsCategory{
    public List<Interest> chosenInterests = new ArrayList<>();
    public enum Interest {
        Pop,
        Hiphop,
        Rock,
        Jazz,
        Classical,
        Country,
        Blues
    }

    public Music(int priorityLevel) {
        super(priorityLevel);
    }

    public void addMusicInterest(Music.Interest interest) {
        chosenInterests.add(interest);
    }
}
