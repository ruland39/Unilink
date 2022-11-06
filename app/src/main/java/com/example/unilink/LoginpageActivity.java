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

public class LoginpageActivity extends AppCompatActivity {
    private ImageButton backbutton;
    private EditText email;
    private EditText password;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginpage);

        backbutton = findViewById(R.id.backbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBacktoLoginorRegisterPage();
            }
        });


        setupUI();
        setupListeners();


    }

    private void setupListeners() {
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkUsername();
            }

            private void checkUsername() {
                boolean isValid = true;

                if(isEmpty(email)){
                    email.setError("You must enter a valid email to login!");
                    isValid = false;
                }
                else{
                    if(!isEmail(email)){
                        email.setError("Enter a valid email!");
                        isValid = false;
                    }
                }

                if(isValid){
                    String emailValue = email.getText().toString();
                    String passwordValue = password.getText().toString();
                    //dummy data for login
                    if(emailValue.equals("test@gmail.com") && passwordValue.equals("test1234")){
                        Intent i = new Intent(LoginpageActivity.this, HomescreenActivity.class);
                        startActivity(i);
                        finish();
                        loginBtn.setEnabled(true);
                        loginBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                openHomeScreen();
                            }
                        });
                    }
                    else{
                        Context context = getApplicationContext();
                        CharSequence text = "Wrong Email or Password";
                        int duration = Toast.LENGTH_SHORT;
                        Toast t = Toast.makeText(context, text, duration);
                        t.show();
                    }
                }
            }

        });
    }


    boolean isEmail(EditText text){
        CharSequence email = text.getText().toString();
        return(!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean isEmpty(EditText text){
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    private void setupUI() {
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn = findViewById(R.id.loginbuttonsubmit);
    }

    public void openBacktoLoginorRegisterPage() {
        Intent intent = new Intent(this, LoginorregisterActivity.class);
        startActivity(intent);
    }

    public void openHomeScreen(){
        Intent i = new Intent(this, HomescreenActivity.class);
        startActivity(i);
    }


}

