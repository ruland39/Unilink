package com.example.unilink.Models.Interests;

import com.example.unilink.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Define the Music class that inherits from Category
public class Music extends InterestsCategory{
    public List<Interest> chosenInterests = new ArrayList<>();
    public static Map<Integer, Interest> Map = new HashMap<Integer, Interest>(){};

    public enum Interest {
        Pop,
        Hiphop,
        Rock,
        Jazz,
        Classical,
        Country,
    }

    public Music(int priorityLevel) {
        super(priorityLevel);
        Map.put(R.id.musichiphopchip, Interest.Hiphop);
        Map.put(R.id.musicclassicalchip, Interest.Classical);
        Map.put(R.id.musiccountrychip, Interest.Country);
        Map.put(R.id.musicjazzchip, Interest.Jazz);
        Map.put(R.id.musicpopchip, Interest.Pop);
        Map.put(R.id.musicrockchip, Interest.Rock);
    }

    public void addMusicInterest(Music.Interest interest) {
        chosenInterests.add(interest);
    }
}
