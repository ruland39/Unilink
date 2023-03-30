package com.example.unilink.Models.Interests;

import com.example.unilink.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Books extends InterestsCategory{
    public List<Interest> chosenInterests = new ArrayList<>();
    public static java.util.Map<Integer, Books.Interest> Map = new HashMap<Integer, Interest>(){};
    public enum Interest {
        Fiction,
        Non_Fiction,
        Romance,
        Mystery,
        Thriller,
        Science_Fiction,
    }

    public Books(int priorityLevel) {
        super(priorityLevel);
        Map.put(R.id.booksfiction, Interest.Fiction);
        Map.put(R.id.booksnonfiction, Interest.Non_Fiction);
        Map.put(R.id.booksromance, Interest.Romance);
        Map.put(R.id.booksmystery, Interest.Mystery);
        Map.put(R.id.booksthriller, Interest.Thriller);
        Map.put(R.id.bookssciencefiction, Interest.Science_Fiction);
    }

    public void addBookInterest(Books.Interest interest) {
        chosenInterests.add(interest);
    }
}
