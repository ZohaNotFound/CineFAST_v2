package com.example.cine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class HomeActivity extends AppCompatActivity {

    private Button btnToday, btnTomorrow;
    private LinearLayout todayMovies, tomorrowMovies;
    private String selectedDate = "Today";
    private String displayDate, displayTime = "22:15"; //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btnToday = findViewById(R.id.btnToday);
        btnTomorrow = findViewById(R.id.btnTomorrow);
        todayMovies = findViewById(R.id.todayMovies);
        tomorrowMovies = findViewById(R.id.tomorrowMovies);

        showToday();

        btnToday.setOnClickListener(v -> showToday());
        btnTomorrow.setOnClickListener(v -> showTomorrow());

        setupBookButtons();
        setupTrailerButtons();
    }

    private void showToday() {
        todayMovies.setVisibility(View.VISIBLE);
        tomorrowMovies.setVisibility(View.GONE);
        selectedDate = "Today";
        displayDate = getFormattedDate(0); //
        btnToday.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark));
        btnTomorrow.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
    }

    private void showTomorrow() {
        todayMovies.setVisibility(View.GONE);
        tomorrowMovies.setVisibility(View.VISIBLE);
        selectedDate = "Tomorrow";
        displayDate = getFormattedDate(1); // tomorrow
        btnTomorrow.setBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark));
        btnToday.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
    }

    private String getFormattedDate(int daysLater) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, daysLater);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    private void setupBookButtons() {
        findViewById(R.id.btnDarkKnightBook).setOnClickListener(v ->
                openSeatSelection("The Dark Knight"));
        findViewById(R.id.btnInceptionBook).setOnClickListener(v ->
                openSeatSelection("Inception"));
        findViewById(R.id.btnInterstellarBook).setOnClickListener(v ->
                openSeatSelection("Interstellar"));
        findViewById(R.id.btnShawshankBook).setOnClickListener(v ->
                openSeatSelection("The Shawshank Redemption"));

        findViewById(R.id.btnDoremonBook).setOnClickListener(v ->
                openSeatSelection("Doremon Adventure"));
        findViewById(R.id.btnDoraBook).setOnClickListener(v ->
                openSeatSelection("Dora Explorer"));
        findViewById(R.id.btnDarkKnightBookTomorrow).setOnClickListener(v ->
                openSeatSelection("The Dark Knight"));
        findViewById(R.id.btnInceptionBookTomorrow).setOnClickListener(v ->
                openSeatSelection("Inception"));
    }

    private void setupTrailerButtons() {
        findViewById(R.id.btnDarkKnightTrailer).setOnClickListener(v ->
                openTrailer("The Dark Knight"));
        findViewById(R.id.btnInceptionTrailer).setOnClickListener(v ->
                openTrailer("Inception"));
        findViewById(R.id.btnInterstellarTrailer).setOnClickListener(v ->
                openTrailer("Interstellar"));
        findViewById(R.id.btnShawshankTrailer).setOnClickListener(v ->
                openTrailer("The Shawshank Redemption"));

        findViewById(R.id.btnDoremonTrailer).setOnClickListener(v ->
                openTrailer("Doremon Adventure"));
        findViewById(R.id.btnDoraTrailer).setOnClickListener(v ->
                openTrailer("Dora Explorer"));
        findViewById(R.id.btnDarkKnightTrailerTomorrow).setOnClickListener(v ->
                openTrailer("The Dark Knight"));
        findViewById(R.id.btnInceptionTrailerTomorrow).setOnClickListener(v ->
                openTrailer("Inception"));
    }

    private void openSeatSelection(String movieName) {
        Intent intent = new Intent(HomeActivity.this, SeatSelectionActivity.class);
        intent.putExtra("MOVIE_NAME", movieName);
        intent.putExtra("SELECTED_DATE", selectedDate);
        intent.putExtra("DISPLAY_DATE", displayDate);
        intent.putExtra("DISPLAY_TIME", displayTime);
        startActivity(intent);
    }

    private void openTrailer(String movieName) {
        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://www.youtube.com/results?search_query=" + movieName + " trailer"));
        startActivity(intent);
    }
}