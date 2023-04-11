package com.example.unilink.Models.Interests;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Interest implements Parcelable {
    private String name;
    private Category category;

    public Interest(String name, Category cat) {
        this.name = name;
        this.category = cat;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public Interest(Parcel in){
        name = in.readString();
        category = in.readParcelable(Category.class.getClassLoader());
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeParcelable(category, i);
    }
    public static final Creator<Interest> CREATOR = new Creator<Interest>() {
        @Override
        public Interest createFromParcel(Parcel in) {
            return new Interest(in);
        }

        @Override
        public Interest[] newArray(int size) {
            return new Interest[size];
        }
    };
}
