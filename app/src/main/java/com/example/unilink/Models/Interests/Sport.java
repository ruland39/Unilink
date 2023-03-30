package com.example.unilink.Models.Interests;

import com.example.unilink.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Define the Sports class that inherits from Category
public class Sport extends InterestsCategory{
    public List<Interest> chosenInterests = new ArrayList<>();
    public static java.util.Map<Integer, Sport.Interest> Map = new HashMap<Integer, Interest>(){};
    public enum Interest {
        Soccer,
        Basketball,
        Tennis,
        Badminton,
        Volleyball,
        Swimming,
        Running
    }

    public Sport(int priorityLevel) {
        super(priorityLevel);
        Map.put(R.id.sportssoccer, Interest.Soccer);
        Map.put(R.id.sportsbasketball, Interest.Basketball);
        Map.put(R.id.sportstennis, Interest.Tennis);
        Map.put(R.id.sportsbadminton, Interest.Badminton);
        Map.put(R.id.sportsvolleyball, Interest.Volleyball);
        Map.put(R.id.sportsswimming, Interest.Swimming);
        Map.put(R.id.sportsrunning, Interest.Running);
    }

    public void addSportInterest(Sport.Interest interest) {
        chosenInterests.add(interest);
    }
}
