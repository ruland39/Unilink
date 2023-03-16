package com.example.unilink.Models;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class UnilinkUser implements Parcelable, Serializable {
    private String Uid;
    private String auth_uid;
    private HashMap<String, String> user_fullName; // first name and last name
    private String user_phoneNum;
    private String user_email;
    private Timestamp user_lastUpdated;

    // private String user_bio; // not applied just yet
    // private String user_profilepic; // not applied just yet

    public UnilinkUser() {
        this.user_email = null;
        this.user_fullName = new HashMap<String, String>();
        this.user_phoneNum = null;
    }

    public UnilinkUser(String auth_id, String firstName, String lastName, String user_phoneNum, String user_email, UUID uid) {
        // add user Id from Firebase
        this.auth_uid = auth_id;
        // creating first name and full name
        this.user_fullName = new HashMap<String, String>();
        user_fullName.put("firstName", firstName.substring(0,1).toUpperCase() + firstName.substring(1));
        user_fullName.put("lastName", lastName.substring(0,1).toUpperCase() + lastName.substring(1));

        // adding in phonenumber and email
        this.user_phoneNum = user_phoneNum;
        this.user_email = user_email;
        this.user_lastUpdated = Timestamp.now();
        this.Uid = uid != null ? uid.toString() : UUID.randomUUID().toString();
    }

    public UnilinkUser getDataFrom(UnilinkUser usr) {
        this.auth_uid = usr.auth_uid;
        this.user_fullName = usr.user_fullName;
        this.user_email = usr.user_email;
        this.user_phoneNum = usr.user_phoneNum;
        this.user_lastUpdated = usr.user_lastUpdated;
        return this;
    }

    @Override
    public String toString() {
        return "[Email: " + this.user_email + "; FullName: " + this.user_fullName + " ; Phone: " + this.user_phoneNum + "]";
    }

    /* #region getName */
    // get the full name in one string
    public String getFullName() {
        if (this.user_fullName.isEmpty())
            return null;

        String fN = this.user_fullName.get("firstName");
        String lN = this.user_fullName.get("lastName");

        if (fN != null && lN != null)
            return fN + " " + lN;
        else
            return null;
    }

    // Getting firstName information
    public String getFirstName() {
        if (this.user_fullName.isEmpty())
            return null;
        String fN = this.user_fullName.get("firstName");
        if (fN != null)
            return fN.substring(0,1).toUpperCase() + fN.substring(1);
        else
            return null;
    }

    public String getLastName() {
        if (this.user_fullName.isEmpty())
            return null;
        String lN = this.user_fullName.get("lastName");    
        if (lN != null)
            return lN.substring(0,1).toUpperCase() + lN.substring(1);
        else
            return null;
    }
    /* #endregion */

    /* #region getOtherInfo */
    public String getPhoneNum() {
        return this.user_phoneNum;
    }

    public String getEmail() {
        return this.user_email;
    }

    public String getAuthId() {
        return this.auth_uid;
    }

    public String getUid() {return this.Uid;}
    public UUID getUUid() {return UUID.fromString(this.Uid);}
    /* #endregion */

    /* #region setters */
    public void setFirstName(String fN) {
        this.user_fullName.put("firstName", fN);
    }

    public void setLastName(String lN) {
        this.user_fullName.put("lastName", lN);
    }

    public void setEmail(String email) {
        this.user_email = email;
    }

    public void setPhoneNum(String pN) {
        this.user_phoneNum = pN;
    }

    public void setAuthId(String uid) {
        this.auth_uid = uid;
    }

    public void setFullName(String ffN) {
        return;
    }

    public void setUid(String uid) {this.Uid = uid;}
    public void setUUid(UUID uid) {this.Uid = uid.toString();}
    /* #endregion */

    /* #region Parcel Code */
    @Override
    public int describeContents() {
        return 0;
    }

    // The implementation created by the Parcelabler site.
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.auth_uid);
        dest.writeString(this.user_phoneNum);
        dest.writeString(this.user_email);
        dest.writeSerializable(this.user_fullName);
        dest.writeString(this.Uid);
    }

    private UnilinkUser(Parcel in) {
        this.auth_uid = in.readString();
        this.user_phoneNum = in.readString();
        this.user_email = in.readString();
        this.user_fullName = (HashMap<String, String>) in.readSerializable();
        this.Uid = in.readString();
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
    /* #endregion */
}
