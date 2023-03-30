package com.example.unilink.Models.Interests;

import com.example.unilink.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Define the Food class that inherits from Category
public class Food extends InterestsCategory{
    public List<Interest> chosenInterests = new ArrayList<>();
    public static java.util.Map<Integer, Food.Interest> Map = new HashMap<Integer, Interest>(){};
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
        Map.put(R.id.fooditalian, Interest.Italian);
        Map.put(R.id.foodchinese, Interest.Chinese);
        Map.put(R.id.foodmexican, Interest.Mexican);
        Map.put(R.id.foodjapanese, Interest.Japanese);
        Map.put(R.id.foodvegan, Interest.Vegan);
        Map.put(R.id.foodfastfood, Interest.Fast_food);
    }

    public void addFoodInterest(Food.Interest interest) {
        chosenInterests.add(interest);
    }
}
