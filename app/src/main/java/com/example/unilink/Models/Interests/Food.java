package com.example.unilink.Models.Interests;

import java.util.ArrayList;
import java.util.List;

// Define the Food class that inherits from Category
public class Food extends InterestsCategory{
    public List<Interest> chosenInterests = new ArrayList<>();
    public enum Interest {
        Italian,
        Chinese,
        Mexican,
        Japanese,
        Vegan,
        Fast_food
    }

    public Food(int priorityLevel) {
        super(priorityLevel);
    }

    public void addFoodInterest(Food.Interest interest) {
        chosenInterests.add(interest);
    }
}
