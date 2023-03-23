package com.example.unilink.Activities.User;

public abstract class InterestsCategory implements Comparable<InterestsCategory>{
    private int priorityLevel;
    private Enum interest;

    public InterestsCategory(int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public int getPriorityLevel() {
        return priorityLevel;
    }

    public void setPriorityLevel(int priorityLevel) {
        this.priorityLevel = priorityLevel;
    }

    public Enum getInterest() {
        return interest;
    }

    public void setInterest(Enum interest) {
        this.interest = interest;
    }

    @Override
    public int compareTo(InterestsCategory o) {
        return Integer.compare(this.priorityLevel, o.priorityLevel);
    }
}
