package com.example.cine;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class TicketSummaryActivity extends AppCompatActivity {

    private TextView tvMovieName, tvSelectedSeats, tvSeatCount, tvTicketTotal,
            tvSnacksDetails, tvSnacksTotal, tvGrandTotal, tvDate, tvTime, tvSelectedDateLabel;
    private Button btnSendTicket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_summary);

        String movieName = getIntent().getStringExtra("MOVIE_NAME");
        int seatCount = getIntent().getIntExtra("SEAT_COUNT", 0);
        int ticketTotal = getIntent().getIntExtra("TICKET_TOTAL", 0);
        int snacksTotal = getIntent().getIntExtra("SNACKS_TOTAL", 0);
        ArrayList<String> selectedSeats = getIntent().getStringArrayListExtra("SELECTED_SEATS");
        String snacksDetails = getIntent().getStringExtra("SELECTED_SNACKS");
        String selectedDate = getIntent().getStringExtra("SELECTED_DATE");
        String displayDate = getIntent().getStringExtra("DISPLAY_DATE");
        String displayTime = getIntent().getStringExtra("DISPLAY_TIME");
        int grandTotal = ticketTotal + snacksTotal;

        tvMovieName = findViewById(R.id.tvMovieName);
        tvSelectedSeats = findViewById(R.id.tvSelectedSeats);
        tvSeatCount = findViewById(R.id.tvSeatCount);
        tvTicketTotal = findViewById(R.id.tvTicketTotal);
        tvSnacksDetails = findViewById(R.id.tvSnacksDetails);
        tvSnacksTotal = findViewById(R.id.tvSnacksTotal);
        tvGrandTotal = findViewById(R.id.tvGrandTotal);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvSelectedDateLabel = findViewById(R.id.tvSelectedDateLabel);
        btnSendTicket = findViewById(R.id.btnSendTicket);

        tvMovieName.setText("Movie: " + movieName);
        tvSeatCount.setText("Seats: " + seatCount);
        tvTicketTotal.setText("Tickets: $" + ticketTotal);
        tvSnacksTotal.setText("Snacks Total: $" + snacksTotal);
        tvGrandTotal.setText("Total: $" + grandTotal);

        if (selectedDate != null) {
            tvSelectedDateLabel.setText("Show: " + selectedDate);
        } else {
            tvSelectedDateLabel.setText("Show: Today");
        }

        if (displayDate != null) {
            tvDate.setText("Date: " + displayDate);
        } else {
            tvDate.setText("Date: 13.04.2025");
        }

        if (displayTime != null) {
            tvTime.setText("Time: " + displayTime);
        } else {
            tvTime.setText("Time: 22:15");
        }

        if (selectedSeats != null && !selectedSeats.isEmpty()) {
            String seatsStr = android.text.TextUtils.join(", ", selectedSeats);
            tvSelectedSeats.setText("Selected Seats: " + seatsStr);
        } else {
            tvSelectedSeats.setText("Selected Seats: None");
        }

        if (snacksDetails != null && !snacksDetails.isEmpty() && !snacksDetails.equals("None")) {
            tvSnacksDetails.setText("Snacks: " + snacksDetails);
        } else {
            tvSnacksDetails.setText("Snacks: None");
        }

        btnSendTicket.setOnClickListener(v -> {
            String shareText = "Movie: " + movieName + "\n" +
                    "Show: " + selectedDate + "\n" +
                    "Date: " + displayDate + " " + displayTime + "\n" +
                    "Seats: " + seatCount + " (" + (selectedSeats != null ? android.text.TextUtils.join(", ", selectedSeats) : "") + ")\n" +
                    "Snacks: " + (snacksDetails != null ? snacksDetails : "None") + "\n" +
                    "Ticket Price: $" + ticketTotal + "\n" +
                    "Snacks Price: $" + snacksTotal + "\n" +
                    "Grand Total: $" + grandTotal;

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Your Ticket from CineFAST");
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });
    }
}