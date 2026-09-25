package com.example.hello_android3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class ProfileActivity extends AppCompatActivity {
    private ImageView settingsButton;
    private ImageView articlesButton;
    private ImageView dashboardButton;
    private DBManager dbManager;
    private TextView firstNameText;
    private TextView emailText;
    private TextView phoneText;
    private TextView countryText;
    private TextView birthdate;
    private TextView genderText;
    private ImageView profilePhoto;
    private Button editProfilePhotoButton;
    private Button editProfile;
    private ImageView dashboard;
    private int userId = -1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        Intent in = getIntent();
        userId= in.getIntExtra("userId", -1);
        articlesButton = findViewById(R.id.articles);
        settingsButton = findViewById(R.id.settings);
        dashboardButton= findViewById(R.id.dashboard);
        dbManager= new DBManager(this);
        firstNameText = findViewById(R.id.nameText);
        emailText = findViewById(R.id.countryText1);
        phoneText = findViewById(R.id.postingTime);
        countryText = findViewById(R.id.countryText);
        birthdate = findViewById(R.id.descriptionText);
        genderText = findViewById(R.id.typeText);
        profilePhoto = findViewById(R.id.requestPhoto);
        editProfilePhotoButton = findViewById(R.id.editProfilePhotoButton);
        editProfile = findViewById(R.id.Decline);
        dashboard = findViewById(R.id.dashboard);
        User user;
        if (userId == -1)
             user = dbManager.getUserByEmail(UserColumns.currentEmail);
        else
            user = dbManager.getUserById(userId);
        editProfile.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                if (userId != -1)
                    intent.putExtra("userId",userId);
                startActivity(intent);
                finish();
            }
        });
        dashboard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        editProfilePhotoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, UploadProfilePhoto.class);
                intent.putExtra("id",user.getId());
                intent.putExtra("location","profile");
                startActivity(intent);
                finish();
            }
        });
        firstNameText.setText(user.getFirstName() + " "+user.getLastName());
        emailText.setText(user.getEmail());
        phoneText.setText(user.getPhoneNumber());
        countryText.setText(user.getCountry());
        birthdate.setText(user.getDateOfBirth());
        genderText.setText(user.getGender());
        if (user.getProfilePhoto().equalsIgnoreCase("male.png"))
            profilePhoto.setImageResource(R.drawable.male);
        else if (user.getProfilePhoto().equalsIgnoreCase("female.png"))
            profilePhoto.setImageResource(R.drawable.female);
        else {
            String imagePath = user.getProfilePhoto();
            Glide.with(this)
                    .load(imagePath) // Load image from the file path
                    .skipMemoryCache(true) // Skip caching in memory
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // Skip caching on disk
                    .placeholder(R.drawable.imageplaceholder) // Placeholder image while loading
                    .error(R.drawable.male) // Image to display if loading fails
                    .into(profilePhoto);

        }
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        articlesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, ArticlesActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dashboardButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
