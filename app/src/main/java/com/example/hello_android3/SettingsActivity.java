package com.example.hello_android3;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {
    private ImageView articlesButton;
    private ImageView dashboardButton;
    private ImageView profileButton;
    TableRow disconnectRow;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_page);
        articlesButton = findViewById(R.id.articles);
        profileButton = findViewById(R.id.profile);
        dashboardButton= findViewById(R.id.dashboard);
        disconnectRow = findViewById(R.id.disconnectRow);
        disconnectRow.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDisconnectConfirmationDialog();
            }
        });
        profileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
        articlesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, ArticlesActivity.class);
                startActivity(intent);
                finish();
            }
        });

        dashboardButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void showDisconnectConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Disconnect");
        builder.setMessage("Are you sure you want to disconnect?");

        // Set up buttons
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Perform disconnect operation
                performDisconnect();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void performDisconnect() {
        // Code to perform disconnect operation
        Toast.makeText(this, "Disconnecting...", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // This code will run after the specified delay
                // Add your actual disconnect logic here
                // For example, disconnect network connection, close sockets, etc.

                Toast.makeText(SettingsActivity.this, "Disconnected", Toast.LENGTH_SHORT).show();
                UserColumns.currentEmail=null;
                Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
