package com.example.unilink.Services;

import android.util.Log;
import android.util.StateSet;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.unilink.Activities.FeaturePage.LoadingDialogBar;
import com.example.unilink.Models.UnilinkUser;
import com.example.unilink.UnilinkApplication;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * User service will be used for all things that
 * a Unilink User requires; such as Authentication,
 * Retrieving Information and Session Activity
 */
public class UserService {
    private final static String TAG = "UserService";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public UserService() {
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Check connection value, if null means error
        if (mAuth == null || db == null) {
            Log.e(TAG, "Error connecting to Firebase");
            throw new UserException("Firebase Connection Error", new Throwable(UserExceptionType.ConnectionError.name()));
        }
    }

    /**
     * Creates a user by authenticating and adding into the
     * firestore database. It will create user unless it fails.
     * adding into the database. (Does not add to SharedPreferences)
     * @param email Email information of the new user
     * @param password Password details of the new user
     * @param firstName First Name details of the new user
     * @param lastName Last Name details of the new user
     * @param pNumber Phone Number details of the new user
     * @return A Result object that holds the UnilinkUser resulting data
     */
    public void Register(String email,
                         String password,
                         String firstName, String lastName, String pNumber,
                         UserCallback callback) {
        UnilinkUser uUser = new UnilinkUser(null, firstName,
                lastName, pNumber, email);
        // Authenticate a new user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        uUser.setAuthId(task.getResult().getUser().getUid());
                        // Adding information to Database
                        db.collection("user_information")
                                .add(uUser)
                                .addOnSuccessListener(tasked->{
                                    Log.d(TAG, "Successfully added User Information for {"+email+"} to the database");
                                    callback.onCallback(uUser);
                                })
                                .addOnFailureListener(tasked->{
                                    Log.w(TAG, "Error adding User Information for {"+email+"} to the database");
                                    throw new UserException("Account Creation Failed - User Creation Failed", new Throwable(UserExceptionType.AccountCreationFailed.name()));
                                });

                        Log.d(TAG, "Successful User and Account Creation for " + email);
                    } else {
                        Log.w(TAG, "Account Authentication Failed.");
                        Toast.makeText(UnilinkApplication.getContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        throw new UserException("Account Authentication Failed - User Creation Failed", new Throwable(UserExceptionType.AccountRegistrationFailed.name()));
                    }
                });
    }
    /**
     * A Function that logs in the User by Email and Password,
     * and uses the UserCallback to let them know of completion
     * @param email Email as input for authentication
     * @param password Password used as input for authentication
     * @param callback Callback used for completion
     */
    public void Login(String email, String password, UserCallback callback) {
        Log.d(TAG, "Logged in called for user " + email);
        // Authenticate User
        UnilinkUser uUser = new UnilinkUser();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Successful login into Firebase Authentication");
                            String auth_id = task.getResult().getUser().getUid();
                            uUser.setAuthId(auth_id);
                            // Retrieving information from Database
                            db.collection("user_information")
                                    .whereEqualTo("authId", uUser.getAuthId())
                                    .get()
                                    .addOnCompleteListener(tasked -> {
                                        if (tasked.isSuccessful()) {
                                            UnilinkUser user = null;
                                            Log.d(TAG,"Number of Documents Found :" + tasked.getResult().size());
                                            for (QueryDocumentSnapshot doc : tasked.getResult()) {
                                                user = doc.toObject(UnilinkUser.class);
                                                Log.d(TAG, user.toString());
                                            }
                                            if (user == null)
                                                throw new UserException("Account is null.", new Throwable(UserExceptionType.AccountInformationError.name()));
                                            uUser.getDataFrom(user);
                                            Log.d(TAG, "Successfully retrieved information for " + email);
                                            callback.onCallback(uUser);
                                        } else {
                                            Log.w(TAG, "Error retrieving User Information for " + email);
                                            throw new UserException("Account Retrieval Failed - User Login Failed - Query Result:"
                                                    + tasked.getException().getMessage(), new Throwable(UserExceptionType.RetrievingInformationFailed.name()));
                                        }
                                    });
                        } else {
                            Log.w(TAG, "Account Authentication Failed.");
                            Toast.makeText(UnilinkApplication.getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            throw new UserException("Account Authentication Failed - User Login Failed", new Throwable(UserExceptionType.LoginFailed.name()));
                        }
                    }
                });
    }

    public interface UserCallback {
        void onCallback(UnilinkUser uUser);
    }

    private class UserException extends RuntimeException {
        public UserException(String message, Throwable cause)
        {
            super(message, cause);
        }
    }

    // Causes of UserExceptions
    public enum UserExceptionType {
        AccountRegistrationFailed,
        AccountInformationError,
        AccountCreationFailed,
        LoginFailed,
        RetrievingInformationFailed,
        ConnectionError
    }
}
