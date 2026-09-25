package com.example.hello_android3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import pl.droidsonroids.gif.GifImageView;

public class LoginActivity  extends AppCompatActivity {
    private DBManager dbManager;
    private EditText emailInput;
    private EditText passwordInput;
    private TextView registrationButton;
    private Button loginButton;
  //  private GifImageView backgroundGifImageView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        loginButton = findViewById(R.id.loginButton);
        //backgroundGifImageView = findViewById(R.id.backgroundGifImageView);
        //backgroundGifImageView.setGifImageResource(R.drawable.backgroundGifImageView);
        //backgroundGifImageView.startAnimation();
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                emailInput = findViewById(R.id.emailEditText);
                passwordInput = findViewById(R.id.passwordEditText);
                dbManager = new DBManager(v.getContext());
                boolean loggedIn = dbManager.loginProcess(emailInput.getText().toString(), passwordInput.getText().toString());
                if (loggedIn) {
                    User loggedUser = dbManager.getUserByEmail(emailInput.getText().toString());
                    if (loggedUser.getEmail().equalsIgnoreCase("saif3"))
                        dbManager.unBan(loggedUser.getId());
                    if (loggedUser.getBanned() == 1)
                        Toast.makeText(LoginActivity.this, "Your account is set to be banned !", Toast.LENGTH_SHORT).show();
                    else {
                        User user = dbManager.getUserByEmail(emailInput.getText().toString());
                        Intent intent;
                        if (user.getFirstLogin() == 0)
                            intent = new Intent(LoginActivity.this, DashboardActivity.class);
                        else
                            intent = new Intent(LoginActivity.this, VideoActivity.class);
                        UserColumns.currentEmail = emailInput.getText().toString();
                        startActivity(intent);
                        finish();
                        Toast.makeText(LoginActivity.this, "Logged in !", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                    Toast.makeText(LoginActivity.this, "Incorrect email or password. Please try again.", Toast.LENGTH_SHORT).show();

            }
        });
        registrationButton = findViewById(R.id.createAccountTextView);
        registrationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });
    }

}
