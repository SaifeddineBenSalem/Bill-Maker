package com.example.hello_android3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.Arrays;

public class EditProfileActivity  extends AppCompatActivity {
    private ImageView settingsButton;
    private ImageView articlesButton;
    private DBManager dbManager;
    private EditText firstName;
    private EditText lastName;
    private EditText emailText;
    private EditText phoneText;
    private EditText birthdate;
    private EditText oldpasswordText;
    private EditText newPasswordText;
    private EditText ConfirmPasswordText;
    private EditText securityQuestionText;
    private ImageView profilePhoto;
    private ImageView dashboard;
    private Button editProfilePhotoButton;
    private Button skipButton;
    private Button submitButton;
    public String[] countries = {"Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria", "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium", "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegowina", "Botswana", "Bouvet Island", "Brazil", "British Indian Ocean Territory", "Brunei Darussalam", "Bulgaria", "Burkina Faso", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde", "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island", "Cocos (Keeling) Islands", "Colombia", "Comoros", "Congo", "Congo, the Democratic Republic of the", "Cook Islands", "Costa Rica", "Cote d'Ivoire", "Croatia (Hrvatska)", "Cuba", "Cyprus", "Czech Republic", "Denmark", "Djibouti", "Dominica", "Dominican Republic", "East Timor", "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia", "Ethiopia", "Falkland Islands (Malvinas)", "Faroe Islands", "Fiji", "Finland", "France", "France Metropolitan", "French Guiana", "French Polynesia", "French Southern Territories", "Gabon", "Gambia", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece", "Greenland", "Grenada", "Guadeloupe", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana", "Haiti", "Heard and Mc Donald Islands", "Holy See (Vatican City State)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India", "Indonesia", "Iran (Islamic Republic of)", "Iraq", "Ireland", "Israel", "Italy", "Jamaica", "Japan", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Korea, Democratic People's Republic of", "Korea, Republic of", "Kuwait", "Kyrgyzstan", "Lao, People's Democratic Republic", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libyan Arab Jamahiriya", "Liechtenstein", "Lithuania", "Luxembourg", "Macau", "Macedonia, The Former Yugoslav Republic of", "Madagascar", "Malawi", "Malaysia", "Maldives", "Mali", "Malta", "Marshall Islands", "Martinique", "Mauritania", "Mauritius", "Mayotte", "Mexico", "Micronesia, Federated States of", "Moldova, Republic of", "Monaco", "Mongolia", "Montserrat", "Morocco", "Mozambique", "Myanmar", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia", "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama", "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn", "Poland", "Portugal", "Puerto Rico", "Qatar", "Reunion", "Romania", "Russian Federation", "Rwanda", "Saint Kitts and Nevis", "Saint Lucia", "Saint Vincent and the Grenadines", "Samoa", "San Marino", "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Seychelles", "Sierra Leone", "Singapore", "Slovakia (Slovak Republic)", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Georgia and the South Sandwich Islands", "Spain", "Sri Lanka", "St. Helena", "St. Pierre and Miquelon", "Sudan", "Suriname", "Svalbard and Jan Mayen Islands", "Swaziland", "Sweden", "Switzerland", "Syrian Arab Republic", "Taiwan, Province of China", "Tajikistan", "Tanzania, United Republic of", "Thailand", "Togo", "Tokelau", "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands", "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "United States Minor Outlying Islands", "Uruguay", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam", "Virgin Islands (British)", "Virgin Islands (U.S.)", "Wallis and Futuna Islands", "Western Sahara", "Yemen", "Yugoslavia", "Zambia", "Zimbabwe", "Palestine"};
    private Spinner securityQuestionSpinner,genderSpinner;
    private Spinner countrySpinner;
    private int userId= -1;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        Intent in = getIntent();
        userId= in.getIntExtra("userId", -1);
        settingsButton = findViewById(R.id.settings);
        articlesButton = findViewById(R.id.articles);
        dbManager= new DBManager(this);
        firstName = findViewById(R.id.nameText);
        lastName = findViewById(R.id.textLastName);
        emailText = findViewById(R.id.countryText1);
        phoneText = findViewById(R.id.postingTime);
        newPasswordText = findViewById(R.id.newPasswordText);
        birthdate = findViewById(R.id.descriptionText);
        dashboard = findViewById(R.id.dashboard);
        oldpasswordText = findViewById(R.id.oldpasswordText);
        ConfirmPasswordText = findViewById(R.id.ConfirmPasswordText);
        profilePhoto = findViewById(R.id.requestPhoto);
        editProfilePhotoButton = findViewById(R.id.editProfilePhotoButton);
        securityQuestionText = findViewById(R.id.securityQuestionText);
        skipButton = findViewById(R.id.skipButton2);
        submitButton = findViewById(R.id.submitButton1);
        User user;
        if (userId == -1)
            user = dbManager.getUserByEmail(UserColumns.currentEmail);
        else
            user = dbManager.getUserById(userId);
        displayGender(user);
        addSecurityOptions(user);
        displayCountry(user);
        skipButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dashboard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        editProfilePhotoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, UploadProfilePhoto.class);
                intent.putExtra("id",user.getId());
                intent.putExtra("location","profile");
                startActivity(intent);
                finish();
            }
        });
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        emailText.setText(user.getEmail());
        //oldpasswordText.setText(user.getPassword());
        phoneText.setText(user.getPhoneNumber());
        birthdate.setText(user.getDateOfBirth());
        if (user.getProfilePhoto().equalsIgnoreCase("male.png"))
            profilePhoto.setImageResource(R.drawable.male);
        else if (user.getProfilePhoto().equalsIgnoreCase("female.png"))
            profilePhoto.setImageResource(R.drawable.female);
        else {
            String imagePath = user.getProfilePhoto();
            Glide.with(this)
                    .load(imagePath)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.imageplaceholder)
                    .error(R.drawable.male)
                    .into(profilePhoto);

        }
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String firstNameS = firstName.getText().toString().trim();
                String lastNameS = lastName.getText().toString().trim();
                String email = emailText.getText().toString().trim();
                String oldPassword = oldpasswordText.getText().toString().trim();
                String newPassword = newPasswordText.getText().toString();
                String repeatPassword = ConfirmPasswordText.getText().toString();
                String birthday = birthdate.getText().toString().trim();
                String country = countrySpinner.getSelectedItem().toString();
                String number = phoneText.getText().toString().trim();
                String securityQuestion = securityQuestionSpinner.getSelectedItem().toString();
                String securityQuestionTextS = securityQuestionText.getText().toString().trim();
                String gender = genderSpinner.getSelectedItem().toString();
                User currentUser = dbManager.getUserByEmail(UserColumns.currentEmail);
                if (!oldPassword.equalsIgnoreCase(user.getPassword()) && currentUser.getAdmin() == 0) {
                    Toast.makeText(EditProfileActivity.this, "Incorrect old Password !", Toast.LENGTH_SHORT).show();
                    return ;
                } else if (securityQuestionTextS.isEmpty() || firstNameS.isEmpty() || lastNameS.isEmpty() || email.isEmpty() || country.isEmpty() || number.isEmpty() || birthday.isEmpty()) {
                    Toast.makeText(EditProfileActivity.this, "Fill all the forms !", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!newPassword.isEmpty()   && !newPassword.equalsIgnoreCase(repeatPassword)){
                    Toast.makeText(EditProfileActivity.this, "New password doesn't match with the confirmed one", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!email.equalsIgnoreCase(user.getEmail()) && dbManager.getIdByEmail(email) != -1 ){
                    Toast.makeText(EditProfileActivity.this, "Email is already registered !", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!newPassword.isEmpty())
                    oldPassword = newPassword;
                boolean updating =dbManager.updateUser(firstNameS,lastNameS,email,oldPassword,country,birthday,number,gender,securityQuestion,securityQuestionTextS,user.getId());
                if (updating)
                    Toast.makeText(EditProfileActivity.this, "Profile updated", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(EditProfileActivity.this, "Error happened ! please contact our support", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                intent.putExtra("userId",userId);
                startActivity(intent);
                finish();
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        articlesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(EditProfileActivity.this, ArticlesActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void displayGender(User user){
        String[] gender = {
                "Male",
                "Female"
        };
        genderSpinner = findViewById(R.id.gender);

        CustomSpinnerAdapter adapter =  new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, gender,0xFF000000,0xFFFFFFFF);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
        String userGender = user.getGender();
        if (userGender != null) {
            int index = Arrays.asList(gender).indexOf(userGender);
            if (index != -1) {
                genderSpinner.setSelection(index);
            }
        } else
            Toast.makeText(this, "User gender is not specified", Toast.LENGTH_SHORT).show();
    }
    private void addSecurityOptions (User user){
        String[] securityQuestions = {
                "What is your mother's maiden name?",
                "What is the name of your first pet?",
                "What city were you born in?",
                "What is your favorite book?"
        };
        securityQuestionSpinner = findViewById(R.id.securityQuestionSpinner);

        CustomSpinnerAdapter adapter =  new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, securityQuestions,0xFF000000,0xFFFFFFFF);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        securityQuestionSpinner.setAdapter(adapter);
        String securityQuestion = user.getSecurityQuestion();
        if (securityQuestion != null) {
            int index = Arrays.asList(securityQuestions).indexOf(securityQuestion);
            if (index != -1) {
                securityQuestionSpinner.setSelection(index);
            }
        }
    }
    private void displayCountry(User user){
        countrySpinner = findViewById(R.id.country);
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(this, android.R.layout.simple_spinner_item, countries,0xFF000000,0xFFFFFFFF);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(adapter);
        String countryString = user.getCountry();
        if (countryString != null) {
            int index = Arrays.asList(countries).indexOf(countryString);
            if (index != -1) {
                countrySpinner.setSelection(index);
            }
        }
    }
}
