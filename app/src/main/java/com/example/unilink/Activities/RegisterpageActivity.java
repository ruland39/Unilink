package com.example.unilink.Activities;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unilink.Activities.FeaturePage.LoadingDialogBar;
import com.example.unilink.R;
import com.example.unilink.Services.AccountService;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.EditText;

import android.text.*;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterpageActivity extends AppCompatActivity {
    private static final String TAG = "RegisterPageActivity";
    private Button registerBtn;

    // EditText objects for the inputs
    private boolean[] validatedInput;
    private EditText firstName;
    private EditText lastName;
    private EditText phoneNumber;
    private EditText email;
    private EditText password;
    private CheckBox showHidePW;
    LoadingDialogBar loadingDialogBar;

    private AccountService accountService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Set back button clicking
        ImageButton backbutton = findViewById(R.id.backbutton);
        loadingDialogBar = new LoadingDialogBar(this);
        backbutton.setOnClickListener(v -> {
            finish();
        });

        loadingDialogBar = new LoadingDialogBar(this);
        accountService = new AccountService();
    }

    @Override
    protected void onResume() {
        super.onResume();

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
        boolean validated = true;
        for (boolean b : validatedInput) if (!b) validated = false;
        registerBtn.setEnabled(validated);
        registerBtn.setOnClickListener(v -> {
            loadingDialogBar.showDialog("Loading");
            RegisterUser();
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

    private void RegisterUser() {
        accountService.RegisterAccount(
                loadingDialogBar,
                email.getText().toString(),
                password.getText().toString(),
                firstName.getText().toString(),
                lastName.getText().toString(),
                phoneNumber.getText().toString(),
                authenticatedUser -> {
                    Log.d(TAG, "[UserService] Successful User Register for " + authenticatedUser);
                    if (authenticatedUser != null){
                        loadingDialogBar.hideDialog();
                        Intent i = new Intent(this, ProfileSetupActivity.class);
                        i.putExtra("AuthenticatedUser", (Parcelable) authenticatedUser);
                        this.startActivity(i);
                        this.finish();
                    }
                });

    }
}