package com.example.cine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class SeatSelectionActivity extends AppCompatActivity {

    private TextView tvMovieName, tvSelectedCount, tvTotalPrice;
    private Button btnProceedSnacks, btnBookSeats;
    private GridLayout gridSeats;

    private String movieName;
    private String selectedDate, displayDate, displayTime;
    private List<Seat> seatList = new ArrayList<>();
    private List<Seat> selectedSeats = new ArrayList<>();
    private final int PRICE_PER_SEAT = 10;

    private static class Seat {
        String id;
        int state; // 
        Button button;

        Seat(String id, int state) {
            this.id = id;
            this.state = state;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        movieName = getIntent().getStringExtra("MOVIE_NAME");
        selectedDate = getIntent().getStringExtra("SELECTED_DATE");
        displayDate = getIntent().getStringExtra("DISPLAY_DATE");
        displayTime = getIntent().getStringExtra("DISPLAY_TIME");

        if (selectedDate == null) selectedDate = "Today";
        if (displayDate == null) displayDate = "13.04.2025";
        if (displayTime == null) displayTime = "22:15";

        tvMovieName = findViewById(R.id.tvMovieName);
        tvSelectedCount = findViewById(R.id.tvSelectedCount);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        btnProceedSnacks = findViewById(R.id.btnProceedSnacks);
        btnBookSeats = findViewById(R.id.btnBookSeats);
        gridSeats = findViewById(R.id.gridSeats);

        tvMovieName.setText("Movie: " + movieName);

        createSeats();

        if (savedInstanceState != null) {
            ArrayList<String> selectedIds = savedInstanceState.getStringArrayList("selectedSeats");
            if (selectedIds != null) {
                selectedSeats.clear();
                for (Seat seat : seatList) {
                    if (selectedIds.contains(seat.id)) {
                        seat.state = 1;
                        selectedSeats.add(seat);
                    } else if (seat.state != 2) {
                        seat.state = 0;
                    }
                    updateSeatButton(seat.button, seat);
                }
                updateSelectionSummary();
            }
        }

        btnProceedSnacks.setEnabled(false);

        btnProceedSnacks.setOnClickListener(v -> {
            ArrayList<String> selectedIds = new ArrayList<>();
            for (Seat s : selectedSeats) {
                selectedIds.add(s.id);
            }

            int ticketTotal = selectedSeats.size() * PRICE_PER_SEAT;

            Intent snacksIntent = new Intent(SeatSelectionActivity.this, SnacksActivity.class);
            snacksIntent.putExtra("MOVIE_NAME", movieName);
            snacksIntent.putExtra("SEAT_COUNT", selectedSeats.size());
            snacksIntent.putExtra("TICKET_TOTAL", ticketTotal);
            snacksIntent.putStringArrayListExtra("SELECTED_SEATS", selectedIds);
            snacksIntent.putExtra("SELECTED_DATE", selectedDate);
            snacksIntent.putExtra("DISPLAY_DATE", displayDate);
            snacksIntent.putExtra("DISPLAY_TIME", displayTime);
            startActivity(snacksIntent);
        });

        btnBookSeats.setOnClickListener(v -> {
            ArrayList<String> selectedIds = new ArrayList<>();
            for (Seat s : selectedSeats) {
                selectedIds.add(s.id);
            }

            int ticketTotal = selectedSeats.size() * PRICE_PER_SEAT;

            Intent summaryIntent = new Intent(SeatSelectionActivity.this, TicketSummaryActivity.class);
            summaryIntent.putExtra("MOVIE_NAME", movieName);
            summaryIntent.putExtra("SEAT_COUNT", selectedSeats.size());
            summaryIntent.putExtra("TICKET_TOTAL", ticketTotal);
            summaryIntent.putExtra("SNACKS_TOTAL", 0);
            summaryIntent.putStringArrayListExtra("SELECTED_SEATS", selectedIds);
            summaryIntent.putExtra("SELECTED_DATE", selectedDate);
            summaryIntent.putExtra("DISPLAY_DATE", displayDate);
            summaryIntent.putExtra("DISPLAY_TIME", displayTime);
            startActivity(summaryIntent);
        });
    }

    private void createSeats() {
        String[] rows = {"A", "B", "C", "D", "E"};
        int cols = 8;
        List<String> bookedSeats = List.of("A1", "B3", "C5", "D2", "E7");

        gridSeats.setColumnCount(cols);
        gridSeats.setRowCount(rows.length);

        for (int r = 0; r < rows.length; r++) {
            for (int c = 1; c <= cols; c++) {
                String seatId = rows[r] + c;
                int state = bookedSeats.contains(seatId) ? 2 : 0;
                Seat seat = new Seat(seatId, state);
                seatList.add(seat);

                Button btn = new Button(this);
                btn.setText(seatId);
                btn.setTag(seat);
                updateSeatButton(btn, seat);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = GridLayout.LayoutParams.WRAP_CONTENT;
                params.height = GridLayout.LayoutParams.WRAP_CONTENT;
                params.setMargins(4, 4, 4, 4);
                btn.setLayoutParams(params);

                btn.setOnClickListener(v -> {
                    Seat clickedSeat = (Seat) v.getTag();
                    if (clickedSeat.state == 2) {
                        Toast.makeText(this, "Seat " + clickedSeat.id + " is already booked", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (clickedSeat.state == 0) {
                        clickedSeat.state = 1;
                        selectedSeats.add(clickedSeat);
                    } else if (clickedSeat.state == 1) {
                        clickedSeat.state = 0;
                        selectedSeats.remove(clickedSeat);
                    }
                    updateSeatButton((Button) v, clickedSeat);
                    updateSelectionSummary();
                });

                gridSeats.addView(btn);
                seat.button = btn;
            }
        }
    }

    private void updateSeatButton(Button btn, Seat seat) {
        switch (seat.state) {
            case 0:
                btn.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                btn.setTextColor(getResources().getColor(android.R.color.white));
                btn.setEnabled(true);
                break;
            case 1:
                btn.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                btn.setTextColor(getResources().getColor(android.R.color.white));
                btn.setEnabled(true);
                break;
            case 2:
                btn.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                btn.setTextColor(getResources().getColor(android.R.color.white));
                btn.setEnabled(false);
                break;
        }
    }

    private void updateSelectionSummary() {
        int count = selectedSeats.size();
        tvSelectedCount.setText("Selected: " + count);
        int total = count * PRICE_PER_SEAT;
        tvTotalPrice.setText("Total: $" + total);
        btnProceedSnacks.setEnabled(count > 0);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<String> selectedIds = new ArrayList<>();
        for (Seat s : selectedSeats) {
            selectedIds.add(s.id);
        }
        outState.putStringArrayList("selectedSeats", selectedIds);
    }
}