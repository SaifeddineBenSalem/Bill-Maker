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

public class ArticlesActivity extends AppCompatActivity {
    private ImageView profileButton;
    private ImageView settingsButton;
    private ImageView dashboardButton;
    private int totalPages;
    TableLayout tableLayout;
    RequestsManager requestsManager;
    DBManager dbManager;
    EditText searchEditText;
    private static final int ITEMS_PER_PAGE = 5;
    private int currentPage = 0;
    private Request[] request;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.articles_list);
        settingsButton = findViewById(R.id.settings);
        dashboardButton= findViewById(R.id.dashboard);
        profileButton = findViewById(R.id.profile);
        tableLayout= findViewById(R.id.tableLayout);
        requestsManager= new RequestsManager(this);
        dbManager = new DBManager(this);
        request = requestsManager.getRequestByStatus("posted");
        totalPages = (int) Math.ceil((double) request.length / ITEMS_PER_PAGE);
        addPaginationButtons();
        loadNextPage(currentPage);
        EditText searchEditText = findViewById(R.id.searchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                request = requestsManager.getRequestByStatus("posted");
                totalPages = (int) Math.ceil((double) request.length / ITEMS_PER_PAGE);
                filter(s.toString());
                addPaginationButtons();
            }
        });
        dashboardButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ArticlesActivity.this, DashboardActivity.class);
                startActivity(intent);
                finish();
            }
        });
        settingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ArticlesActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        profileButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(ArticlesActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void displayTheRequest(int id){
        Intent intent = new Intent(ArticlesActivity.this, RequestDescription.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }
    private void loadNextPage(int page) {
        tableLayout.removeAllViews();
        int start = currentPage * ITEMS_PER_PAGE;
        int end = Math.min((currentPage + 1) * ITEMS_PER_PAGE, request.length);
        for (int i = start; i < end; i++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
            ImageView imageView = new ImageView(this);
            if (request[i].getPhoto().equalsIgnoreCase("company.png")) {
                imageView.setImageResource(R.drawable.company);
            } else if (request[i].getPhoto().equalsIgnoreCase("product.png")) {
                imageView.setImageResource(R.drawable.product);
            } else {
                String imagePath = request[i].getPhoto();
                Glide.with(this)
                        .asBitmap()
                        .load(imagePath)
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.drawable.imageplaceholder)
                        .error(R.drawable.male)
                        .override(300, 300)
                        .centerCrop()
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                imageView.setImageBitmap(resource);
                            }
                        });
            }
            imageView.setLayoutParams(new TableRow.LayoutParams(400, TableRow.LayoutParams.MATCH_PARENT));

            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1));
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.setGravity(Gravity.START);
            String posterName = dbManager.getUserById(request[i].getPoster()).getFirstName() + " " + dbManager.getUserById(request[i].getPoster()).getLastName();
            String[] listOfData = {
                    request[i].getName(),
                    request[i].getType(),
                    request[i].getPostingDate()
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
                    displayTheRequest(request[finalI].getId());
                }
            });

            linearLayout.addView(button);

            tableRow.addView(imageView);
            tableRow.addView(linearLayout);
            tableLayout.addView(tableRow);
            if (i < request.length) {
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
    private void filter(String searchText) {
        List<Request> filteredList = new ArrayList<>();
        for (Request item : request) {
            if (item.getName().toLowerCase().contains(searchText.toLowerCase())) {
                filteredList.add(item);
            }
        }
        request = filteredList.toArray(new Request[0]);
        totalPages = (int) Math.ceil((double) filteredList.size() / ITEMS_PER_PAGE);
        loadNextPage(currentPage);
    }
}
