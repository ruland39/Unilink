package com.example.unilink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;

import android.text.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterpageActivity extends AppCompatActivity {
    private ImageButton backbutton;
    private Button registerBtn;

    // EditText objects for the inputs
    private boolean validatedInput[];
    private EditText firstName;
    private EditText lastName;
    private EditText phoneNumber;
    private EditText email;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerpage);

        // Set back button clicking
        backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBacktoLoginorRegisterPage();
            }
        });
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
        registerBtn = findViewById(R.id.registerbuttonsubmit);
        validatedInput = new boolean[] { false, false, false, false, false }; // the validated input must all be true to
                                                                              // enable button

        // need to create a listener for each textview
        firstName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String currentText = firstName.getText().toString();            
                // if (!currentText.isEmpty())
                //     validatedInput[0] = true;
                // else
                //     validatedInput[0] = false;
                validatedInput[0] = (!currentText.isEmpty());
                buttonValidates();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        lastName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String currentText = lastName.getText().toString();
                // if (!currentText.isEmpty())
                //     validatedInput[1] = true;
                // else
                //     validatedInput[1] = false;
                validatedInput[1] = (!currentText.isEmpty());
                buttonValidates();
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
                // if (!currentText.isEmpty() && match.find())
                //     validatedInput[2] = true;
                // else
                //     validatedInput[2] = false;
                validatedInput[2] = (!currentText.isEmpty() && match.find());
                buttonValidates();
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
                // if (!currentText.isEmpty() && match.find())
                //     validatedInput[3] = true;
                // else
                //     validatedInput[3]= false;
                validatedInput[3]=(!currentText.isEmpty() && match.find());
                buttonValidates();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
                String currentText = password.getText().toString();
                // if (!currentText.isEmpty())
                //     validatedInput[4] = true;
                // else
                //     validatedInput[4] = false;
                validatedInput[4] = (!currentText.isEmpty());
                buttonValidates();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
        });

    }

    // you have to dynamically change the button when it's all filled hence this has to be called on every text change
    private void buttonValidates(){
        // enable button after all validated
        boolean validated =false;
        for (boolean b : validatedInput)
            if (!b)
                validated = false;
            else
                validated = true;
        registerBtn.setEnabled(validated);
    }

    public void openBacktoLoginorRegisterPage() {
        Intent intent = new Intent(this, LoginorregisterActivity.class);
        startActivity(intent);
    }
}