package com.example.unilink.Activities.User;

import java.util.ArrayList;
import java.util.List;

// Define the Movies class that inherits from Category
public class Movie extends InterestsCategory{
    public List<Interest> chosenInterests = new ArrayList<>();
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
    }

    public void addMovieInterest(Movie.Interest interest) {
        chosenInterests.add(interest);
    }
}
