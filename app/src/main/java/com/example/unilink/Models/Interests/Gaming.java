package com.example.unilink.Models.Interests;

import com.example.unilink.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Define the Books class that inherits from Category
public class Gaming extends InterestsCategory{
    public List<Interest> chosenInterests = new ArrayList<>();
    public static java.util.Map<Integer, Gaming.Interest> Map = new HashMap<Integer, Interest>(){};
    public enum Interest {
        Console,
        PC,
        Mobile,
        FirstPerson_shooter,
        Strategy,
        Sports,
    }

    public Gaming(int priorityLevel) {
        super(priorityLevel);
        Map.put(R.id.gamingconsole, Interest.Console);
        Map.put(R.id.gamingpc, Interest.PC);
        Map.put(R.id.gamingfps, Interest.FirstPerson_shooter);
        Map.put(R.id.gamingmobile, Interest.Mobile);
        Map.put(R.id.gamingstrategy, Interest.Strategy);
        Map.put(R.id.gamingsports, Interest.Sports);
    }

    public void addGamingInterest(Gaming.Interest interest) {
        chosenInterests.add(interest);
    }
}
