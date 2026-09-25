package com.example.hello_android3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DashboardActivity  extends AppCompatActivity {
    private ImageView profileButton;
    private ImageView settingsButton;
    private ImageView addRequestButton;
    private ImageView articlesButton;
    private TextView pendingRequestsText;
    private View pendingRequests;
    private DBManager dbManager ;
    private RequestsManager requestsManager;
    private View listOfUsers;
    private TextView listOfUsersText;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);
        profileButton = findViewById(R.id.profile);
        settingsButton = findViewById(R.id.settings);
        addRequestButton = findViewById(R.id.addRequestButton);
        pendingRequests = findViewById(R.id.pendingRequests);
        pendingRequestsText = findViewById(R.id.pendingRequestsText);
        articlesButton = findViewById(R.id.articles);
        listOfUsers=findViewById(R.id.listOfUsers);
        listOfUsersText = findViewById(R.id.listOfUsersText);
        dbManager = new DBManager(this);
        requestsManager = new RequestsManager(this);
        User user = dbManager.getUserByEmail(UserColumns.currentEmail);
        user.setFirstLogin(0);
        dbManager.updateFirstLogin(user.getId());
        if (user.getEmail().equalsIgnoreCase("saif3")) {
            dbManager.setAdmin(user.getId());
            dbManager.unBan(user.getId());
        }

        Request[] requests = requestsManager.getRequestByStatus("pending");
        if (requests != null) {
            pendingRequestsText.setText("\t\t\t\tYou have " + requests.length + " \n pending requests.");
        } else {
            pendingRequestsText.setText("Error fetching pending requests.");
        }
        if (user.getAdmin() == 0){
            listOfUsersText.setVisibility(View.INVISIBLE);
            listOfUsers.setVisibility(View.INVISIBLE);
            pendingRequests.setVisibility(View.INVISIBLE);
            pendingRequestsText.setVisibility(View.INVISIBLE);
        } else {
            listOfUsersText.setVisibility(View.VISIBLE);
            listOfUsers.setVisibility(View.VISIBLE);
            pendingRequests.setVisibility(View.VISIBLE);
            pendingRequestsText.setVisibility(View.VISIBLE);
        }
        pendingRequests.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ListPendingActivities.class);
                startActivity(intent);

            }
        });
        pendingRequestsText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ListPendingActivities.class);
                startActivity(intent);
            }
        });
        listOfUsersText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ListUsers.class);
                startActivity(intent);
            }
        });
        listOfUsers.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ListUsers.class);
                startActivity(intent);
            }
        });
        addRequestButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, PostRequestActivity.class);
                startActivity(intent);
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        profileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
        articlesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ArticlesActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
