package com.example.unilink;

import com.google.firebase.Timestamp;
import java.util.HashMap;
import java.util.Map;

public class UnilinkUser {
    private String auth_uid;
    private HashMap<String, String> user_fullName; // first name and last name
    private String user_phoneNum;
    private String user_email;
    private Timestamp user_lastUpdated;

    // private String user_bio; // not applied just yet
    // private String user_profilepic; // not applied just yet

    public UnilinkUser() {
    }

    public UnilinkUser(String auth_id, String firstName, String lastName, String user_phoneNum, String user_email) {
        // add user Id from Firebase
        this.auth_uid = auth_id;
        // creating first name and full name
        this.user_fullName = new HashMap<String, String>();
        user_fullName.put("firstName", firstName);
        user_fullName.put("lastName", lastName);

        // adding in phonenumber and email
        this.user_phoneNum = user_phoneNum;
        this.user_email = user_email;
        this.user_lastUpdated = Timestamp.now();

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
            return fN;
        else
            return null;
    }

    public String getLastName() {
        if (this.user_fullName.isEmpty())
            return null;
        String lN = this.user_fullName.get("lastName");
        if (lN != null)
            return lN;
        else
            return null;
    }
    /* #endregion */

    /* #region getOtherInfo*/
    public String getPhoneNum() {
        return this.user_phoneNum;
    }

    public String getEmail() {
        return this.user_email;
    }

    public String getAuthId() {
        return this.auth_uid;
    }
    /* #endregion */
}
