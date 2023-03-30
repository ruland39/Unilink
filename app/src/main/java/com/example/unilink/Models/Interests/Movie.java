package com.example.unilink.Models.Interests;

import com.example.unilink.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Define the Movies class that inherits from Category
public class Movie extends InterestsCategory{
    public List<Interest> chosenInterests = new ArrayList<>();
    public static java.util.Map<Integer, Movie.Interest> Map = new HashMap<Integer, Interest>(){};
    public enum Interest {
        Action,
        Romance,
        Comedy,
        Horror,
        Thriller,
        Drama,
        Science_Fiction
    }

    public Movie(int priorityLevel) {
        super(priorityLevel);
        Map.put(R.id.moviesaction, Interest.Action);
        Map.put(R.id.moviescomedy, Interest.Comedy);
        Map.put(R.id.moviesdrama, Interest.Drama);
        Map.put(R.id.movieshorror, Interest.Horror);
        Map.put(R.id.moviesromance, Interest.Romance);
        Map.put(R.id.moviesthriller, Interest.Thriller);
        Map.put(R.id.moviessciencefiction, Interest.Science_Fiction);
    }

    public void addMovieInterest(Movie.Interest interest) {
        chosenInterests.add(interest);
    }
}
