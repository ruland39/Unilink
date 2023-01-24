package com.example.unilink.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unilink.Models.UnilinkUser;
import com.example.unilink.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// The login page validates dynamically for the input
// but will only check the correct or authenticated value when user clicks the button
public class LoginpageActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private EditText email;
    private EditText password;
    private Button loginBtn;
    private CheckBox showHidePW;
    private boolean[] validatedInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageButton backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(v -> {
            finish(); // finishing the activity basically closing the pages
            // openBacktoLoginorRegisterPage();
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Get all the view objects during creation
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        showHidePW = findViewById(R.id.showHidePW);
        loginBtn = findViewById(R.id.loginbuttonsubmit);
        validatedInput = new boolean[] { false, false };
    }

    @Override
    public void onResume() {
        super.onResume();
        // Login dynamic validation works by setting a textChange listener
        // This listener updates a boolean array and then validates the button if all
        // true

        email.addTextChangedListener(new TextWatcher() { // this text watcher bothers me and it definitely is able to be
                                                         // a seperate class
            public void afterTextChanged(Editable s) {
                // Regex email validation
                String currentText = email.getText().toString();
                Pattern ptrn = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
                Matcher match = ptrn.matcher(currentText);
                validatedInput[0] = (!currentText.isEmpty() && match.find()); // sets the boolean (if not empty and
                                                                              // follows regex)
                buttonValidates();
                if (!validatedInput[0]) {
                    email.setError("Invalid Email Input");
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                // Simple password empty detection
                String currentText = password.getText().toString();
                validatedInput[1] = (!currentText.isEmpty());
                buttonValidates();
                if (!validatedInput[1]) {
                    password.setError("Empty password field");
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        });
    }

    private void buttonValidates() {
        boolean validated = false;
        for (boolean b : validatedInput)
            validated = b;
        loginBtn.setEnabled(validated);
        if (validated)
            loginBtn.setOnClickListener(v -> {
                // Authenticates on press, then opens the page and ends this activity.
                authenticate(email.getText().toString(), password.getText().toString());
            });

        showHidePW.setOnCheckedChangeListener((compoundButton, value) -> {
            if (value) {
                // Show Password
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                // Hide Password
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
    }

    // Authentication method to check credentials
    private void authenticate(String email, String password) {
        Log.d("com.example.unilink", "loginAccount:" + email);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("com.example.unilink", "signInWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userId = user.getUid();
                        getSharedPreferences("UserPrefs",MODE_PRIVATE).edit().putString("firebasekey", userId).commit();
                        Log.d("com.example.unilink", "UserIdOnSharedPref: success");
                        getUserInfo(user);
                        openHomeScreen();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("com.example.unilink", "signInWithEmail:failure", task.getException());
                        Toast.makeText(getApplicationContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }

                });
    }

    // Get the user information from firestore 
    private void getUserInfo(FirebaseUser user) {
        Log.d("com.example.unilink", "Getting User Information from Firestore for: " + user.getEmail());
        db.collection("user_information")
                .whereEqualTo("authId", user.getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                             UnilinkUser uUser = doc.toObject(UnilinkUser.class);
                             saveUserInfo(uUser);
                        }
                    } else {
                        Log.w("com.example.unilink", "Error getting document: ", task.getException());
                        Toast.makeText(getApplicationContext(), "Unable to get User Information", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    // Save the user info into sharedpreference json
    private void saveUserInfo(UnilinkUser user) {
        Gson gson = new Gson();
        String objString = gson.toJson(user);
        getSharedPreferences("UserPrefs",MODE_PRIVATE).edit().putString("userJson", objString).commit();
        Log.d("com.example.unilink", "Succesfully added User JSON to SharedPref: " + objString);
    }

    public void openHomeScreen() {
        Intent i = new Intent(this, HomescreenActivity.class);
        startActivity(i);
        finish();
    }

}
