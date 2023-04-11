package com.example.unilink.Services;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.unilink.Activities.FeaturePage.LoadingDialogBar;
import com.example.unilink.Models.UnilinkAccount;
import com.example.unilink.UnilinkApplication;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

/**
 * User service will be used for all things that
 * a Unilink User requires; such as Authentication,
 * Retrieving Information and Session Activity
 */
public class AccountService {
    private final static String TAG = "AccountService";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    public AccountService() {
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
    public void RegisterAccount(LoadingDialogBar bar,
                         String email,
                         String password,
                         String firstName, String lastName, String pNumber,
                         AccountCallback callback) throws UserException {
        UnilinkAccount uUser = new UnilinkAccount(null, firstName,
                lastName, pNumber, email,null);
        // Authenticate a new user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        uUser.setAuthId(task.getResult().getUser().getUid());
                        // Adding information to Database
                        db.collection("user_information")
                                .add(uUser)
                                .addOnSuccessListener(tasked->{
                                    Log.d(TAG, "Successfully added Account Information for {"+email+"} to the database");
                                    callback.onCallback(uUser);
                                })
                                .addOnFailureListener(tasked->{
                                    Log.w(TAG, "Error adding Account Information for {"+email+"} to the database");
                                    throw new UserException("Account Creation Failed - User Creation Failed", new Throwable(UserExceptionType.AccountCreationFailed.name()));
                                });

                        Log.d(TAG, "Successful User and Account Creation for " + email);
                    } else {
                        Log.w(TAG, "Account Registration Failed.");
                        Toast.makeText(UnilinkApplication.getContext(), "Registration failed." + (task.getException() instanceof FirebaseAuthWeakPasswordException ? "Weak Password." : ""),
                                Toast.LENGTH_SHORT).show();
                        bar.hideDialog();
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
    public void Login(LoadingDialogBar bar, String email, String password, AccountCallback callback)
    throws UserException {
        Log.d(TAG, "Logged in called for user " + email);
        // Authenticate User
        UnilinkAccount uUser = new UnilinkAccount();
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
                                            UnilinkAccount user = null;
                                            Log.d(TAG,"Number of Documents Found :" + tasked.getResult().size());
                                            for (QueryDocumentSnapshot doc : tasked.getResult()) {
                                                user = doc.toObject(UnilinkAccount.class);
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
                            bar.hideDialog();
                        }
                    }
                });
    }

    /**
     * Function that retrieves user information from the Firestore
     * database. Returns the information through a callback (async function)
     * @param AuthId used as parameter of User Information to be found
     * @param callback Callback used to return information; Returns null if NOT FOUND
     */
    public void getAccountByAuthID(String AuthId, AccountCallback callback) {
        Log.d(TAG, "Get User Information by Auth ID has been called");
        db.collection("user_information")
                .whereEqualTo("authId", AuthId)
                .get()
                .addOnCompleteListener(task->{
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            UnilinkAccount uUser = doc.toObject(UnilinkAccount.class);
                            callback.onCallback(uUser);
                        }
                    } else {
                        callback.onCallback(null);
                    }
                });
    }

    /**
     * Function that retrieves user information from the Firestore
     * database. Returns the information through a callback (async function)
     * @param Uid used as parameter of User Information to be found
     * @param callback Callback used to return information; Returns null if NOT FOUND
     */
    public void getAccountByUId(String Uid, AccountCallback callback) {
        Log.d(TAG, "Get User Information by UID has been called for UID: " + Uid);
        db.collection("user_information")
                .whereEqualTo("uid", Uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            UnilinkAccount uUser = doc.toObject(UnilinkAccount.class);
                            callback.onCallback(uUser);
                        }
                    } else {
                        callback.onCallback(null);
                    }
                });
    }

    /**
     * @return The current user session/auth id
     */
    public String getCurrentUserSessionID() {
        return mAuth.getUid();
    }

    /**
     * @return If Current User Session is authenticated
     */
    public boolean isInSession() {
        FirebaseUser usr = mAuth.getCurrentUser();
        return (usr != null);
    }

    public void signOut() {
       mAuth.signOut();
    }

    public interface AccountCallback {
        void onCallback(UnilinkAccount uAcc);
    }

    public static class UserException extends RuntimeException {
        public UserException(String message, Throwable cause)
        {
            super(message, cause);
        }
    }

    /**
     * Method to delete a User's account from the Firebase Authentication Database
     * @param auth_id Method can only be called during inSessions, hence Auth_id to check
     *                current user
     */
    public void deleteAccount(String auth_id) {
        Log.d(TAG, "Account Deletion Called;");
        FirebaseUser currentUsr = mAuth.getCurrentUser();
        if (currentUsr.getUid().equals(auth_id)) {
            currentUsr.delete()
                    .addOnCompleteListener(task -> {
                       if (task.isSuccessful()) {
                            db.collection("user_information")
                                    .whereEqualTo("authId", auth_id)
                                    .get()
                                    .addOnCompleteListener(task1 -> {
                                       if (task1.isSuccessful()){
                                           for (QueryDocumentSnapshot doc : task1.getResult()){
                                               doc.getReference().delete();
                                           }
                                           Log.d(TAG, "Account Deletion Successful for Email: " +currentUsr.getEmail());
                                       } else
                                           throw new RuntimeException("Account Deletion Error");
                                    });
                       } else
                           throw new RuntimeException("Account Deletion Error");
                    });
        } else
            throw new RuntimeException("Account Deletion called without Authentication");
        signOut();
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
