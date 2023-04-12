package com.example.unilink.Models.Interests;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Category implements Parcelable {
    private CategoryName name;
    private Integer priorityLevel;
    private Map<String, Interest> interests;

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

    @Override
    public int describeContents() {
        return 0;
    }

    public Category(Parcel in) {
        this.priorityLevel = in.readInt();
        this.name = CategoryName.valueOf(in.readString());
        List<Interest> received_interests = new ArrayList<>();
        in.readList(received_interests, Interest.class.getClassLoader());
        this.interests = new HashMap<>();
        for (Interest i : received_interests){
            this.addInterest(i);
        }
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(this.priorityLevel);
        parcel.writeString(this.name.name());
        List<Interest> interests = new ArrayList<>(this.interests.values());
        parcel.writeList(interests);
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public enum CategoryName {
        BOOKS,
        FOOD,
        GAMING,
        MOVIES,
        MUSIC,
        SPORTS
    }
}
