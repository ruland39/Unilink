package com.example.unilink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import android.text.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// The login page validates dynamically for the input
// but will only check the correct or authenticated value when user clicks the button
public class LoginpageActivity extends AppCompatActivity {
    private ImageButton backbutton;
    private EditText email;
    private EditText password;
    private Button loginBtn;
    private boolean[] validatedInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);

        backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // finishing the activity basically closing the pages
                // openBacktoLoginorRegisterPage();
            }
        });

        // Get all the view objects during creation
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
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
            if (!b)
                validated = false;
            else
                validated = true;
        loginBtn.setEnabled(validated);
        if (validated)
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Authenticates on press, then opens the page and ends this activity.
                    if (authenticate()){
                        openHomeScreen();
                        finish();
                    } else {
                        // Send in a toast notification on invalid input
                        Context context = getApplicationContext();
                        CharSequence text = "Invalid Email or Password";
                        int duration = Toast.LENGTH_SHORT;
                        Toast t = Toast.makeText(context, text, duration);
                        t.show();
                    }
                }
            });
    }

    // Authentication method to check credentials
    private boolean authenticate() {
        // for now just hard coding the thing
        // email: test@gmail.com pass:test1234
        String emailInput = email.getText().toString();
        String passwordInput = password.getText().toString();

        if (emailInput.equalsIgnoreCase("test@gmail.com") &&
                passwordInput.equalsIgnoreCase("test1234")) 
            return true;
        else
            return false;
    }

    // public void openBacktoLoginorRegisterPage() {
    // Intent intent = new Intent(this, LoginorregisterActivity.class);
    // startActivity(intent);
    // }

    public void openHomeScreen() {
        Intent i = new Intent(this, HomescreenActivity.class);
        startActivity(i);
    }

}
