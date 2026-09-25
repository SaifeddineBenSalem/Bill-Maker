package com.example.hello_android3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class UploadProfilePhoto extends AppCompatActivity {
    private Intent intent;
    private int id ;
    private String location;
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView photoImageView;
    private Button uploadButton;
    private Button submitButton; // Added button reference
    private Button skipButton;
    private DBManager dbManager;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        intent = getIntent();
        id= intent.getIntExtra("id", -1);
        location = intent.getStringExtra("location");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.upload_profile_photo);
        dbManager = new DBManager(this);
        photoImageView = findViewById(R.id.photoImageView);
        uploadButton = findViewById(R.id.uploadButton);
        submitButton = findViewById(R.id.submitButton); // Initialize submit button
        skipButton = findViewById(R.id.skipButton);
        uploadButton.setOnClickListener(v -> openFileChooser());
        submitButton.setOnClickListener(v -> saveImageAndSubmit());
        skipButton.setOnClickListener(v -> skipProfilePhoto());
    }
    private void skipProfilePhoto(){
        Intent x;
        if (location.equalsIgnoreCase("register"))
            x= new Intent(UploadProfilePhoto.this, LoginActivity.class);
        else
            x= new Intent(UploadProfilePhoto.this, ProfileActivity.class);
        startActivity(x);
        finish();
    }
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            photoImageView.setImageURI(selectedImageUri);
        }
    }

    private void saveImageToInternalStorage(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            if (inputStream != null) {
                File directory = getFilesDir(); // Internal storage directory
                File parentDirectory = new File(directory, "images/profilephoto");
                parentDirectory.mkdirs();
                File imageFile = new File(parentDirectory, id+"profile_image.jpg"); // File name for the image
                FileOutputStream outputStream = new FileOutputStream(imageFile);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                outputStream.close();
                selectedImageUri = Uri.fromFile(imageFile); // Update URI after saving
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveImageAndSubmit() {
        if (selectedImageUri != null) {
            saveImageToInternalStorage(selectedImageUri);
            String imagePath = selectedImageUri.toString();
            boolean uploadingChecker = dbManager.updateProfilePhoto(id,imagePath);
            if (uploadingChecker) {
                Toast.makeText(this, "Image saved and submitted successfully", Toast.LENGTH_SHORT).show();
                skipProfilePhoto();
            } else {
                Toast.makeText(this, "Failed to save image and submit", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }
}
