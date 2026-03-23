package com.example.cine;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SnacksActivity extends AppCompatActivity {

    private TextView tvPopcornQty, tvNachosQty, tvDrinkQty, tvCandyQty, tvSnacksTotal;
    private ImageButton btnPopcornPlus, btnPopcornMinus, btnNachosPlus, btnNachosMinus,
            btnDrinkPlus, btnDrinkMinus, btnCandyPlus, btnCandyMinus;
    private Button btnConfirm;

    private int popcornQty = 0, nachosQty = 0, drinkQty = 0, candyQty = 0;
    private final int POPCORN_PRICE = 9, NACHOS_PRICE = 8, DRINK_PRICE = 6, CANDY_PRICE = 7;

    private String movieName;
    private int seatCount, ticketTotal;
    private String selectedDate, displayDate, displayTime;
    private ArrayList<String> selectedSeats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snacks);

        movieName = getIntent().getStringExtra("MOVIE_NAME");
        seatCount = getIntent().getIntExtra("SEAT_COUNT", 0);
        ticketTotal = getIntent().getIntExtra("TICKET_TOTAL", 0);
        selectedDate = getIntent().getStringExtra("SELECTED_DATE");
        displayDate = getIntent().getStringExtra("DISPLAY_DATE");
        displayTime = getIntent().getStringExtra("DISPLAY_TIME");
        selectedSeats = getIntent().getStringArrayListExtra("SELECTED_SEATS");
        if (selectedSeats == null) selectedSeats = new ArrayList<>();

        if (selectedDate == null) selectedDate = "Today";
        if (displayDate == null) displayDate = "13.04.2025";
        if (displayTime == null) displayTime = "22:15";

        initViews();
        setupListeners();
        updateTotal();
    }

    private void initViews() {
        tvPopcornQty = findViewById(R.id.tvPopcornQty);
        btnPopcornPlus = findViewById(R.id.btnPopcornPlus);
        btnPopcornMinus = findViewById(R.id.btnPopcornMinus);

        tvNachosQty = findViewById(R.id.tvNachosQty);
        btnNachosPlus = findViewById(R.id.btnNachosPlus);
        btnNachosMinus = findViewById(R.id.btnNachosMinus);

        tvDrinkQty = findViewById(R.id.tvDrinkQty);
        btnDrinkPlus = findViewById(R.id.btnDrinkPlus);
        btnDrinkMinus = findViewById(R.id.btnDrinkMinus);

        tvCandyQty = findViewById(R.id.tvCandyQty);
        btnCandyPlus = findViewById(R.id.btnCandyPlus);
        btnCandyMinus = findViewById(R.id.btnCandyMinus);

        tvSnacksTotal = findViewById(R.id.tvSnacksTotal);
        btnConfirm = findViewById(R.id.btnConfirmSnacks);
    }

    private void setupListeners() {
        btnPopcornPlus.setOnClickListener(v -> {
            popcornQty++;
            tvPopcornQty.setText(String.valueOf(popcornQty));
            updateTotal();
        });
        btnPopcornMinus.setOnClickListener(v -> {
            if (popcornQty > 0) {
                popcornQty--;
                tvPopcornQty.setText(String.valueOf(popcornQty));
                updateTotal();
            }
        });

        btnNachosPlus.setOnClickListener(v -> {
            nachosQty++;
            tvNachosQty.setText(String.valueOf(nachosQty));
            updateTotal();
        });
        btnNachosMinus.setOnClickListener(v -> {
            if (nachosQty > 0) {
                nachosQty--;
                tvNachosQty.setText(String.valueOf(nachosQty));
                updateTotal();
            }
        });

        btnDrinkPlus.setOnClickListener(v -> {
            drinkQty++;
            tvDrinkQty.setText(String.valueOf(drinkQty));
            updateTotal();
        });
        btnDrinkMinus.setOnClickListener(v -> {
            if (drinkQty > 0) {
                drinkQty--;
                tvDrinkQty.setText(String.valueOf(drinkQty));
                updateTotal();
            }
        });

        btnCandyPlus.setOnClickListener(v -> {
            candyQty++;
            tvCandyQty.setText(String.valueOf(candyQty));
            updateTotal();
        });
        btnCandyMinus.setOnClickListener(v -> {
            if (candyQty > 0) {
                candyQty--;
                tvCandyQty.setText(String.valueOf(candyQty));
                updateTotal();
            }
        });

        btnConfirm.setOnClickListener(v -> {
            int snacksTotal = popcornQty * POPCORN_PRICE +
                    nachosQty * NACHOS_PRICE +
                    drinkQty * DRINK_PRICE +
                    candyQty * CANDY_PRICE;

            StringBuilder snacksDetails = new StringBuilder();
            if (popcornQty > 0) snacksDetails.append("Popcorn x").append(popcornQty).append(", ");
            if (nachosQty > 0) snacksDetails.append("Nachos x").append(nachosQty).append(", ");
            if (drinkQty > 0) snacksDetails.append("Drink x").append(drinkQty).append(", ");
            if (candyQty > 0) snacksDetails.append("Candy x").append(candyQty).append(", ");

            if (snacksDetails.length() > 0) {
                snacksDetails.setLength(snacksDetails.length() - 2);
            } else {
                snacksDetails.append("None");
            }

            Intent intent = new Intent(SnacksActivity.this, TicketSummaryActivity.class);
            intent.putExtra("MOVIE_NAME", movieName);
            intent.putExtra("SEAT_COUNT", seatCount);
            intent.putExtra("TICKET_TOTAL", ticketTotal);
            intent.putExtra("SNACKS_TOTAL", snacksTotal);
            intent.putExtra("SELECTED_SNACKS", snacksDetails.toString());
            intent.putStringArrayListExtra("SELECTED_SEATS", selectedSeats);
            intent.putExtra("SELECTED_DATE", selectedDate);
            intent.putExtra("DISPLAY_DATE", displayDate);
            intent.putExtra("DISPLAY_TIME", displayTime);
            startActivity(intent);
        });
    }

    private void updateTotal() {
        int total = popcornQty * POPCORN_PRICE +
                nachosQty * NACHOS_PRICE +
                drinkQty * DRINK_PRICE +
                candyQty * CANDY_PRICE;
        tvSnacksTotal.setText("Total: $" + total);
    }
}