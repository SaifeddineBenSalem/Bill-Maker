package com.example.hello_android3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.w3c.dom.Text;

public class RequestDescription extends AppCompatActivity {
    private ImageView profileButton;
    private ImageView settingsButton;
    private ImageView articlesButton;
    private TextView posterLabel;
    private TextView nameText;
    private TextView typeText;
    private  TextView countryText;
    private TextView descriptionText;
    private TextView  posterText;
    private TextView postingTime;
    private ImageView requestPhoto;
    private DBManager dbManager;
    private Button editProfilePhotoButton;
    private TextView postingTimeLabel;
    private RequestsManager requestsManager;
    private Button Decline;
    private Button Accept;
    private Button declineAndBan;
    private Button editRequest;
    private User user;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_description);
        profileButton = findViewById(R.id.profile);
        settingsButton = findViewById(R.id.settings);
        articlesButton = findViewById(R.id.articles);
        requestPhoto = findViewById(R.id.requestPhoto);
        nameText = findViewById(R.id.nameText);
        Decline=findViewById(R.id.Decline);
        Accept = findViewById(R.id.Accept);
        declineAndBan = findViewById(R.id.declineAndBan);
        editRequest = findViewById(R.id.editRequest);
        typeText = findViewById(R.id.typeText);
        countryText = findViewById(R.id.countryText1);
        descriptionText = findViewById(R.id.descriptionText);
        posterText = findViewById(R.id.posterText);
        postingTime = findViewById(R.id.postingTime);
        posterLabel= findViewById(R.id.posterLabel);
        editProfilePhotoButton = findViewById(R.id.editProfilePhotoButton);
        //x= findViewById(R.id.textView2);
        int id = getIntent().getIntExtra("id", -1);
        requestsManager= new RequestsManager(this);
        dbManager = new DBManager(this);
        Request request =requestsManager.getRequestById(id);
        user = dbManager.getUserById(request.getPoster());
        posterText.setText(user.getFirstName() + " "+ user.getLastName());
        countryText.setText(request.getCountry());
        descriptionText.setText(request.getDescription());
        postingTime.setText(request.getPostingDate());
        typeText.setText(request.getType());
        nameText.setText(request.getName());
        User currentUser = dbManager.getUserByEmail(UserColumns.currentEmail);
        if (currentUser.getAdmin() == 0){
            postingTime.setVisibility(View.INVISIBLE);
            posterText.setVisibility(View.INVISIBLE);
            Decline.setVisibility(View.INVISIBLE);
            declineAndBan.setVisibility(View.INVISIBLE);
            Accept.setVisibility(View.INVISIBLE);
            editRequest.setVisibility(View.INVISIBLE);
            posterLabel.setVisibility(View.INVISIBLE);
            editProfilePhotoButton.setVisibility(View.INVISIBLE);
            postingTimeLabel.setVisibility(View.INVISIBLE);
        }
        if (request.getPhoto().equalsIgnoreCase("company.png") )
            requestPhoto.setImageResource(R.drawable.company);
        else if (request.getPhoto().equalsIgnoreCase("product.png"))
            requestPhoto.setImageResource(R.drawable.product);
        else {
            String imagePath = request.getPhoto();
            Glide.with(this)
                    .load(imagePath) // Load image from the file path
                    .skipMemoryCache(true) // Skip caching in memory
                    .diskCacheStrategy(DiskCacheStrategy.NONE) // Skip caching on disk
                    .placeholder(R.drawable.imageplaceholder) // Placeholder image while loading
                    .error(R.drawable.male) // Image to display if loading fails
                    .into(requestPhoto);
        }

        //x.setText(String.valueOf(id));
        Decline.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               boolean checker = requestsManager.updateStatus(id,"rejected",currentUser.getId());
               if (checker){
                   Toast.makeText(RequestDescription.this, "Request has been rejected !", Toast.LENGTH_SHORT).show();
                   Intent intent = new Intent(RequestDescription.this, ListPendingActivities.class);
                   startActivity(intent);
                   finish();
               }
               else
                   Toast.makeText(RequestDescription.this, "Some error has occured !", Toast.LENGTH_SHORT).show();
            }
        });
        posterText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(RequestDescription.this, ProfileActivity.class);
                intent.putExtra("userId",user.getId());
                startActivity(intent);
                finish();
            }
        });
        Accept.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean checker = requestsManager.updateStatus(id,"posted",currentUser.getId());
                if (checker){
                    Toast.makeText(RequestDescription.this, "Request has been accepted !", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else
                    Toast.makeText(RequestDescription.this, "Some error has occured !", Toast.LENGTH_SHORT).show();
            }
        });
        declineAndBan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean checker = requestsManager.updateStatus(id,"rejectedAndBanned",currentUser.getId());
                if (checker){
                    boolean banChecker = dbManager.banAUser(user.getId());
                    if (banChecker){
                        Toast.makeText(RequestDescription.this, "Request has been rejected and user has been banned !", Toast.LENGTH_SHORT).show();
                        finish();
                    } else
                        Toast.makeText(RequestDescription.this, "Request rejected but could not ban the user !", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(RequestDescription.this, "Some error has occurred !", Toast.LENGTH_SHORT).show();
            }
        });
        editRequest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(RequestDescription.this, PostRequestActivity.class);
                intent.putExtra("id",request.getId());
                startActivity(intent);
                finish();
                }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(RequestDescription.this, SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        profileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(RequestDescription.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
        articlesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(RequestDescription.this, ArticlesActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
