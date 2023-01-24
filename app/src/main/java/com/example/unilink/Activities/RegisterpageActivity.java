package com.example.unilink.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import com.example.unilink.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Toast;

import android.text.*;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.example.unilink.Models.UnilinkUser;

public class RegisterpageActivity extends AppCompatActivity {

    //TODO: Fix up unused variables
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private Button registerBtn;

    // EditText objects for the inputs
    private boolean[] validatedInput;
    private EditText firstName;
    private EditText lastName;
    private EditText phoneNumber;
    private EditText email;
    private EditText password;
    private CheckBox showHidePW;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Set back button clicking
        ImageButton backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(v -> {
            finish(); // finishing the activity basically closing the page
            // openBacktoLoginorRegisterPage();
        });

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check connection value
        if (mAuth == null || db == null) {
            Toast.makeText(getApplicationContext(), "Unable to connect to Firebase", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Getting input objects
        firstName = findViewById(R.id.firstname);
        lastName = findViewById(R.id.lastname);
        phoneNumber = findViewById(R.id.phonenumber);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        showHidePW = findViewById(R.id.showHidePW);
        registerBtn = findViewById(R.id.registerbuttonsubmit);
        // for now all the inputs are manadtory
        validatedInput = new boolean[] { false, false, false, false, false }; // the validated input must all be true to

        // need to create a listener for each textview
        firstName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String currentText = firstName.getText().toString();
                validatedInput[0] = (!currentText.isEmpty());
                buttonValidates();
                if (!validatedInput[0])
                    firstName.setError("Fill in your first name");
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        lastName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String currentText = lastName.getText().toString();
                validatedInput[1] = (!currentText.isEmpty());
                buttonValidates();
                if (!validatedInput[1])
                    lastName.setError("Fill in your last name");
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        phoneNumber.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String currentText = phoneNumber.getText().toString();
                Pattern ptrn = Pattern.compile("^[\\+]?[(]?[0-9]{3}[)]?[-\\s\\.]?[0-9]{3}[-\\s\\.]?[0-9]{4,6}$",
                        Pattern.CASE_INSENSITIVE);
                Matcher match = ptrn.matcher(currentText);
                validatedInput[2] = (!currentText.isEmpty() && match.find());
                buttonValidates();
                if (!validatedInput[2])
                    phoneNumber.setError("Invalid Phone Number");
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        email.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String currentText = email.getText().toString();
                Pattern ptrn = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
                Matcher match = ptrn.matcher(currentText);
                validatedInput[3] = (!currentText.isEmpty() && match.find());
                buttonValidates();
                if (!validatedInput[3])
                    email.setError("Invalid Email Input");
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String currentText = password.getText().toString();
                validatedInput[4] = (!currentText.isEmpty());
                buttonValidates();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

    }

    // you have to dynamically change the button when it's all filled hence this has
    // to be called on every text change
    private void buttonValidates() {
        // enable button after all validated
        boolean validated = false;
        for (boolean b : validatedInput)
            validated = b;
        registerBtn.setEnabled(validated);
        registerBtn.setOnClickListener(v -> {
            boolean success1 = createAccount(email.getText().toString(), password.getText().toString(),
                    firstName.getText().toString(),
                    lastName.getText().toString(),
                    phoneNumber.getText().toString());
            if (success1) {
                finish();
            } else
                Toast.makeText(getApplicationContext(), "Authentication Error", Toast.LENGTH_SHORT).show();
        });

        showHidePW.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean value) {
                if (value)
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                else
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
    }

    public void openHomeScreen() {
        Intent i = new Intent(this, HomescreenActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    // Create FirebaseAuthentication
    private boolean createAccount(String email, String password, String firstName, String lastName, String pNumber) {
        Log.d("com.example.unilink", "createAccount:" + email);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();
                            getSharedPreferences("UserPrefs",MODE_PRIVATE).edit().putString("firebasekey", userId).commit();
                            Log.d("com.example.unilink", "UserIdOnSharedPref: success");
                            // Add the user information into the database
                            addUserInfo(user);
                            openHomeScreen();
                        } else {
                            Log.w("com.example.unilink", "createAccountWithEmail: failed");
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return true;
    }

    // Adding user information into the database
    private void addUserInfo(FirebaseUser user) {
        Log.d("com.example.unilink", "createUser:" + user.getEmail());

        UnilinkUser uUser = new UnilinkUser(user.getUid(), firstName.getText().toString(),
                lastName.getText().toString(), phoneNumber.getText().toString(), email.getText().toString());
        db.collection("user_information")
                .add(uUser)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference docRef) {
                        Log.d("com.example.unilink",
                                "FirestoreDocument succesfully written with ID: " + docRef.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("com.example.unilink", "Error adding User Information Document on Firestore", e);
                    }
                });

        Gson gson = new Gson();
        String objString = gson.toJson(uUser);
        getSharedPreferences("UserPrefs",MODE_PRIVATE).edit().putString("userJson", objString).commit();
        Log.d("com.example.unilink", "Succesfully added User JSON to SharedPref: " + objString);
    }
}