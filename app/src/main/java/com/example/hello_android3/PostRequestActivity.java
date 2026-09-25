package com.example.hello_android3;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class PostRequestActivity extends AppCompatActivity {
    private ImageView profileButton;
    private ImageView settingsButton;
    private Spinner typeSpinner,countrySpinner;
    private EditText edittext_name;
    private EditText description;
    private Button submitButton;
    private Button uploadPhotoButton;
    private Uri selectedImageUri;
    private RequestsManager requestsManager;
    private DBManager dbManager;
    private ImageView photoImageView;
    private Request  request;
    private int requestId;
    private int photoChanging;
    public String[] type = {
            "Product",
            "Company"
    };
    public String[] countries = {"International","Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire", "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and Mc Donald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait", "Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia, The Former Yugoslav Republic of", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan, Province of China", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe", "Palestine"};
    private static final int PICK_IMAGE_REQUEST = 1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_request);
        photoChanging=0;
        profileButton = findViewById(R.id.profile);
        settingsButton = findViewById(R.id.settings);
        dbManager = new DBManager(this);
        typeSpinner = findViewById(R.id.type);
        submitButton = findViewById(R.id.submitButton);
        countrySpinner = findViewById(R.id.country);
        edittext_name = findViewById(R.id.edittext_name);
        description = findViewById(R.id.description);
        photoImageView = findViewById(R.id.photoImageView);
        uploadPhotoButton = findViewById(R.id.uploadButton);
        requestsManager = new RequestsManager(this);
        addType();
        addCountries();
        uploadPhotoButton.setOnClickListener(v -> openFileChooser());
        requestId = getIntent().getIntExtra("id", -1);
        if (requestId != -1){
            request = requestsManager.getRequestById(requestId);
            edittext_name.setText(request.getName());
            description.setText(request.getDescription());
            int index = Arrays.asList(countries).indexOf(request.getCountry());
            if (index != -1)
                countrySpinner.setSelection(index);
            int index2 = -1;
            if ("Product".equals(request.getType())) {
                index2 = 0;
            } else if ("Company".equals(request.getType())) {
                index2 = 1;
            }
            if (index2 != -1) {
                typeSpinner.setSelection(index2);
            }
            if (request.getPhoto().equalsIgnoreCase("company.png") ){
                photoImageView.setImageResource(R.drawable.company);
                selectedImageUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.company);

            }
            else if (request.getPhoto().equalsIgnoreCase("product.png")){
                photoImageView.setImageResource(R.drawable.product);
                selectedImageUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.drawable.product);
            }
            else {
                String imagePath = request.getPhoto();
                selectedImageUri = Uri.parse(imagePath);
                Glide.with(this)
                        .load(imagePath) // Load image from the file path
                        .skipMemoryCache(true) // Skip caching in memory
                        .diskCacheStrategy(DiskCacheStrategy.NONE) // Skip caching on disk
                        .placeholder(R.drawable.imageplaceholder) // Placeholder image while loading
                        .error(R.drawable.male) // Image to display if loading fails
                        .into(photoImageView);
            }

            Toast.makeText(PostRequestActivity.this, index2+ " "+ request.getType(), Toast.LENGTH_SHORT).show();
        }
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               String name = edittext_name.getText().toString().trim();
               String descriptionS = description.getText().toString().trim();
               String type = typeSpinner.getSelectedItem().toString();
               String country = countrySpinner.getSelectedItem().toString();
               User user = dbManager.getUserByEmail(UserColumns.currentEmail);
               if (name.isEmpty() || descriptionS.isEmpty())
                   Toast.makeText(PostRequestActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
               else {
                   String fileName;
                   if (requestId == -1)
                        fileName = type.equalsIgnoreCase("company") ? "company.png" : "product.png";
                   else
                       fileName = request.getPhoto();

                   String status = user.getAdmin() == 0 ? "pending" : "posted";
                   int id;
                   User currentUser = dbManager.getUserByEmail(UserColumns.currentEmail);
                   if (requestId == -1)
                    id = requestsManager.addRequest(name,descriptionS,country,type,fileName,user.getId(),status);
                   else {
                       requestsManager.updateARequest(requestId, name, descriptionS, country, type, fileName, user.getId(), status, currentUser.getId());
                   id=requestId;
                   }
                   if (selectedImageUri != null && photoChanging == 1) {
                       saveImageToInternalStorage(selectedImageUri,id);
                       String imagePath = selectedImageUri.toString();
                       boolean uploadingChecker = requestsManager.updatePhoto(id,imagePath);
                       if (uploadingChecker) {
                           Toast.makeText(PostRequestActivity.this, "Image saved and submitted successfully", Toast.LENGTH_SHORT).show();
                       } else {
                           Toast.makeText(PostRequestActivity.this, "Failed to save image and submit", Toast.LENGTH_SHORT).show();
                       }
                   }
                   Intent intent = new Intent(PostRequestActivity.this, DashboardActivity.class);
                   startActivity(intent);
                   finish();
                   }
               }

        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(PostRequestActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        profileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(PostRequestActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
    private void addType (){

        CustomSpinnerAdapter adapter =  new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, type, 0xFF000000, 0xFFFFFFFF);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        typeSpinner.setAdapter(adapter);
    }
    private void addCountries() {
        countrySpinner = findViewById(R.id.country);
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, countries, 0xFF000000, 0xFFFFFFFF);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(adapter);
    }
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            photoChanging=1;
            photoImageView.setImageURI(selectedImageUri); // Set selected image to ImageView
        }
    }

    private void saveImageToInternalStorage(Uri imageUri,int id) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);
            if (inputStream != null) {
                File directory = getFilesDir(); // Internal storage directory
                File parentDirectory = new File(directory, "images/requests");
                parentDirectory.mkdirs();
                File imageFile = new File(parentDirectory, id+"request_image.jpg"); // File name for the image
                FileOutputStream outputStream = new FileOutputStream(imageFile);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                outputStream.close();
                // Now you have saved the image to internal storage
                selectedImageUri = Uri.fromFile(imageFile); // Update URI after saving
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
