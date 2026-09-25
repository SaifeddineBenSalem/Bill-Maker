package com.example.hello_android3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.ArrayList;
import java.util.List;

public class ListUsers extends AppCompatActivity {
    private ImageView profileButton;
    private ImageView settingsButton;
    private ImageView articlesButton;
    private ImageView dashboardButton;
    private DBManager dbManager;
    private TableLayout tableLayout;
    private EditText searchEditText;
    private User user[];
    private int totalPages;
    private static final int ITEMS_PER_PAGE = 5;
    private int currentPage = 0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_list);
        profileButton = findViewById(R.id.profile);
        settingsButton = findViewById(R.id.settings);
        articlesButton = findViewById(R.id.articles);
        dashboardButton = findViewById(R.id.dashboard);
        tableLayout = findViewById(R.id.tableLayout);
        dbManager = new DBManager(this);
        searchEditText = findViewById(R.id.searchEditText);
        user = dbManager.getAllUsers();
        totalPages = (int) Math.ceil((double) user.length / ITEMS_PER_PAGE);
        addPaginationButtons();
        loadNextPage(currentPage);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                user = dbManager.getAllUsers();
                totalPages = (int) Math.ceil((double) user.length / ITEMS_PER_PAGE);
                filter(s.toString());
                addPaginationButtons();
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ListUsers.this, SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        profileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ListUsers.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
        articlesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ListUsers.this, ArticlesActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dashboardButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ListUsers.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void loadNextPage(int page) {
        tableLayout.removeAllViews();
        int start = currentPage * ITEMS_PER_PAGE;
        int end = Math.min((currentPage + 1) * ITEMS_PER_PAGE, user.length);
        for (int i = start; i < end; i++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            ImageView imageView = new ImageView(this);
            if (user[i].getProfilePhoto() == null )
                imageView.setImageResource(R.drawable.male);
            else {
                if (user[i].getProfilePhoto().equalsIgnoreCase("male.png")) {
                    imageView.setImageResource(R.drawable.male);
                } else if (user[i].getProfilePhoto().equalsIgnoreCase("female.png")) {
                    imageView.setImageResource(R.drawable.female);
                } else {
                    String imagePath = user[i].getProfilePhoto();
                    Glide.with(this)
                            .asBitmap()
                            .load(imagePath)
                            .skipMemoryCache(true)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .placeholder(R.drawable.imageplaceholder)
                            .error(R.drawable.male)
                            .override(300, 300) // Resize to 300x300 pixels
                            .centerCrop()
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    imageView.setImageBitmap(resource);
                                }
                            });
                }
            }
            imageView.setLayoutParams(new TableRow.LayoutParams(400, TableRow.LayoutParams.MATCH_PARENT));

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setGravity(Gravity.START);
            String[] listOfData = {
                    user[i].getFirstName()+" "+user[i].getLastName(),
                    user[i].getCountry(),
                    user[i].getAdmin() == 0 ? "User" : " Admin"
            };
            for (int j = 0; j < 3; j++) {
                TextView textView = new TextView(this);
                textView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                textView.setPadding(8, 8, 8, 8);
                textView.setText(listOfData[j]);
                textView.setTextColor(Color.BLACK);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                textView.setTypeface(null, Typeface.NORMAL);
                linearLayout.addView(textView);
            }
            Button button = new Button(this);
            button.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            button.setText("View more");
            int finalI = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    displayProfile(user[finalI]);
                }
            });

            linearLayout.addView(button);

            tableRow.addView(imageView);
            tableRow.addView(linearLayout);
            tableLayout.addView(tableRow);
            if (i < user.length) {
                View separator = new View(this);
                separator.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        1 // Height of the separator
                ));
                separator.setBackgroundColor(Color.parseColor("#CCCCCC"));
                tableLayout.addView(separator);
            }
        }
        currentPage = page;
    }
    private void addPaginationButtons() {
        LinearLayout paginationLayout = findViewById(R.id.paginationLayout);
        paginationLayout.removeAllViews();
        for (int i = 0; i < totalPages; i++) {
            Button button = new Button(this);
            button.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            button.setText(String.valueOf(i + 1));
            final int page = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadNextPage(page);
                }
            });
            paginationLayout.addView(button);
        }
    }
    private void displayProfile(User user){
        Intent intent = new Intent(ListUsers.this, ProfileActivity.class);
        intent.putExtra("userId",user.getId());
        startActivity(intent);
    }
    private void filter(String searchText) {
        List<User> filteredList = new ArrayList<>();
        for (User item : user) {
            String fullName=item.getFirstName()+" "+item.getLastName();
            if (fullName.toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(item);
            }
        }
        user = filteredList.toArray(new User[0]);
        totalPages = (int) Math.ceil((double) filteredList.size() / ITEMS_PER_PAGE);
        loadNextPage(currentPage);
    }
}
