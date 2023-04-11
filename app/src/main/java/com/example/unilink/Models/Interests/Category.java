package com.example.unilink.Models.Interests;

import java.util.HashMap;
import java.util.Map;

public class Category {
    private final CategoryName name;
    private int priorityLevel;
    private final Map<String, Interest> interests;

    public Category(int pLevel, CategoryName cName) {
        this.name = cName;
        this.priorityLevel = pLevel;
        this.interests = new HashMap<>();
    }

    public void addInterest(Interest interest){
        interests.put(interest.getName(), interest);
    }
    public CategoryName getName() {
        return name;
    }
    public void setPriorityLevel(int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }
    public int getPriorityLevel() {
        return priorityLevel;
    }
    public Map<String, Interest> getInterests() {
        return interests;
    }

    public enum CategoryName {
        BOOKS,
        FOOD,
        GAMING,
        MOVIES,
        MUSIC,
        SPORTS
    }
}
