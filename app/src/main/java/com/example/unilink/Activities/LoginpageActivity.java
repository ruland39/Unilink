package com.example.unilink.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.unilink.Activities.FeaturePage.LoadingDialogBar;
import com.example.unilink.R;
import com.example.unilink.Services.AccountService;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

// The login page validates dynamically for the input
// but will only check the correct or authenticated value when user clicks the button
public class LoginpageActivity extends AppCompatActivity {
    private static final String TAG = "LoginPageActivity";
    private EditText email;
    private EditText password;
    private Button loginBtn;
    private CheckBox showHidePW;
    private boolean[] validatedInput;
    LoadingDialogBar loadingDialogBar;
    private AccountService accountService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ImageButton backbutton = findViewById(R.id.backbutton);
        loadingDialogBar = new LoadingDialogBar(this);
        accountService = new AccountService();

        backbutton.setOnClickListener(v -> {
            finish();
        });

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
        boolean validated = true;
        for (boolean b : validatedInput) if (!b) validated = false;
        loginBtn.setEnabled(validated);
        if (validated)
            loginBtn.setOnClickListener(v -> {
                loadingDialogBar.showDialog("Loading");
               LoginUser();
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

    private void LoginUser() {
            accountService.Login(loadingDialogBar,
                    email.getText().toString(),
                    password.getText().toString(),
                    authenticatedUser -> {
                        Log.d(TAG, "[UserService] Successful User Login for " + authenticatedUser);
                        if (authenticatedUser != null) {
                            loadingDialogBar.hideDialog();
                            Intent i = new Intent(this, HomescreenActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.putExtra("AuthenticatedUser", (Parcelable) authenticatedUser);
                            this.startActivity(i);
                            this.finish();
                        }
                    });

    }

}
