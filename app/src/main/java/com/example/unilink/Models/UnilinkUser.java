package com.example.unilink.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.unilink.Models.Interests.Category;
import com.example.unilink.Models.Interests.Interest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.PriorityQueue;

public class UnilinkUser implements Parcelable {
    private final String userID;
    private LocalDateTime timeCreated;
    public List<String> connectedUIDs;
    private PriorityQueue<Category> categories;
    private String bio;
    private String pfpURL;
    private String pfbURL;
    private Date birthdate;

    public static DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    public static DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.ROOT);
    public UnilinkUser(String userID) {
        this.userID = userID;
        this.bio = null;
        this.pfpURL = null;
        this.pfbURL = null;
        this.categories = new PriorityQueue<>(Comparator.comparingInt(Category::getPriorityLevel).reversed());
        this.connectedUIDs = new ArrayList<>();
        this.timeCreated = LocalDateTime.now();
        this.birthdate = null;
    }

    public PriorityQueue<Category> getCategories() {
        return categories;
    }

    public String getBio() {
        return bio;
    }
    public String getUserID() {return userID;}
    public String getPfpURL() {return pfpURL;}
    public String getPfbURL() {return pfbURL;}
    public LocalDateTime getTimeCreated() {return timeCreated;}
    public Date getBirthdate() {return birthdate;}
    public List<String> getConnectedUIDs() {
        return connectedUIDs;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
    public void setProfilePicture(String url) {this.pfpURL = url;}
    public void setProfileBanner(String url) {this.pfbURL = url;}
    public void setBirthdate(Date date) {this.birthdate = date;}
    public void setTimeCreated(LocalDateTime timeCreated) {this.timeCreated = timeCreated;}
    public void setConnectedUIDs(List<String> uids) {this.connectedUIDs = uids;}


    public void addChosenInterest(Interest interest) {
        for (Category category : categories) {
            if (category.getName() == interest.getCategory().getName()) {
                category.addInterest(interest);
                return;
            }
        }
        Category newCategory = new Category(4, interest.getCategory().getName());
        newCategory.addInterest(interest);
        categories.add(newCategory);
    }

    public List<Interest> getChosenInterests() {
        List<Interest> chosenInterest = new ArrayList<>();
        for (Category category : categories) {
            chosenInterest.addAll(category.getInterests().values());
        }
        return chosenInterest;
    }

    public List<Interest> getTop3HighestInterest(){
        List<Interest> interests = new ArrayList<>();
        for (Category category : categories) {
            interests.addAll(category.getInterests().values());
        }
        return interests.subList(0, Math.min(3, interests.size()));
    }

    public Category getHighestPriorityCategory() {
        return this.categories.peek();
    }

    public void addConnectedUser(String userID) {
        connectedUIDs.add(userID);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int i) {
        dest.writeString(this.userID);
        dest.writeString(this.bio);
        dest.writeString(this.pfpURL);
        dest.writeString(this.pfbURL);
//        dest.writeList(Arrays.asList(this.categories.toArray()));
        // To send the interest list to the parcel; we send out as a list of interests
//        List<Interest> parcel_interests = new ArrayList<>();
//        for (Category c : this.categories){
//            parcel_interests.addAll(c.getInterests().values());
//        }
        dest.writeList(getChosenInterests());
        dest.writeStringList(this.connectedUIDs);
        dest.writeString(format.format(this.timeCreated));
        dest.writeString(df.format(this.birthdate));
    }

    public UnilinkUser(Parcel in){
        this.userID = in.readString();
        this.bio = in.readString();
        this.pfpURL = in.readString();
        this.pfbURL = in.readString();
        // Retrieve the interest (categories) by adding chosen interests once more
        List<Interest> parcel_interests = new ArrayList<>();
        in.readList(parcel_interests, Interest.class.getClassLoader());
        this.categories = new PriorityQueue<>(Comparator.comparingInt(Category::getPriorityLevel).reversed());
        for (Interest i : parcel_interests) {
            this.addChosenInterest(i);
        }
        this.connectedUIDs = new ArrayList<>();
        in.readStringList(this.connectedUIDs);
        this.timeCreated = LocalDateTime.parse(in.readString(), format);
        try {
            this.birthdate = df.parse(in.readString());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static final Parcelable.Creator<UnilinkUser> CREATOR = new Parcelable.Creator<UnilinkUser>() {
        @Override
        public UnilinkUser createFromParcel(Parcel source) {
            return new UnilinkUser(source);
        }

        @Override
        public UnilinkUser[] newArray(int size) {
            return new UnilinkUser[size];
        }
    };
}
