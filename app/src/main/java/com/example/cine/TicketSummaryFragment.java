package com.example.cine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class TicketSummaryFragment extends Fragment {

    private TextView tvMovie, tvSeats, tvTicketsPrice, tvSnacksPrice, tvSnacksDetails, tvGrandTotal, tvSummaryDateTime;
    private Button btnFinish, btnSendTicket;
    private ImageButton btnBack;

    private Movie movie;
    private int seatCount;
    private float ticketTotal;
    private float snacksTotal;
    private String snacksDetails, selectedDate, selectedTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket_summary, container, false);

        if (getArguments() != null) {
            movie = (Movie) getArguments().getSerializable("MOVIE");
            selectedDate = getArguments().getString("SELECTED_DATE");
            selectedTime = getArguments().getString("SELECTED_TIME");
            seatCount = getArguments().getInt("SEAT_COUNT");
            ticketTotal = getArguments().getFloat("TICKET_TOTAL");
            snacksTotal = getArguments().getFloat("SNACKS_TOTAL");
            snacksDetails = getArguments().getString("SELECTED_SNACKS");
        }

        tvMovie = view.findViewById(R.id.tvSummaryMovie);
        tvSummaryDateTime = view.findViewById(R.id.tvSummaryDateTime);
        tvSeats = view.findViewById(R.id.tvSummarySeats);
        tvTicketsPrice = view.findViewById(R.id.tvSummaryTicketsPrice);
        tvSnacksPrice = view.findViewById(R.id.tvSummarySnacksPrice);
        tvSnacksDetails = view.findViewById(R.id.tvSummarySnacksDetails);
        tvGrandTotal = view.findViewById(R.id.tvSummaryGrandTotal);
        btnFinish = view.findViewById(R.id.btnFinish);
        btnSendTicket = view.findViewById(R.id.btnSendTicket);
        btnBack = view.findViewById(R.id.btnBack);

        if (btnBack != null) {
            btnBack.setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
        }

        if (movie != null) {
            tvMovie.setText("Movie: " + movie.getTitle());
            if (tvSummaryDateTime != null) {
                tvSummaryDateTime.setText(selectedDate + " | " + (selectedTime != null ? selectedTime : "N/A"));
            }
            tvSeats.setText("Seats: " + seatCount);
            tvTicketsPrice.setText(String.format("Tickets: $%.2f", ticketTotal));
            tvSnacksPrice.setText(String.format("Snacks: $%.2f", snacksTotal));
            tvSnacksDetails.setText("Details: " + (snacksDetails != null ? snacksDetails : "None"));
            
            float grandTotal = ticketTotal + snacksTotal;
            tvGrandTotal.setText(String.format("Grand Total: $%.2f", grandTotal));

            saveBookingToPrefs(movie.getTitle(), seatCount, grandTotal);
            saveBookingToFirebase(movie.getTitle(), seatCount, grandTotal);
        }

        btnFinish.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).replaceFragment(new HomeFragment());
        });

        btnSendTicket.setOnClickListener(v -> {
            String shareText = "Movie: " + movie.getTitle() + "\n" +
                    "Date: " + selectedDate + " | Time: " + selectedTime + "\n" +
                    "Seats: " + seatCount + "\n" +
                    "Snacks: " + (snacksDetails != null ? snacksDetails : "None") + "\n" +
                    "Grand Total: $" + (ticketTotal + snacksTotal);

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Share via"));
        });

        return view;
    }

    private void saveBookingToPrefs(String movieName, int seats, float totalPrice) {
        SharedPreferences prefs = requireActivity().getSharedPreferences("CinePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("last_movie", movieName);
        editor.putInt("last_seats", seats);
        editor.putFloat("last_price", totalPrice);
        editor.apply();
    }

    private void saveBookingToFirebase(String movieName, int seats, float totalPrice) {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) return;

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> booking = new HashMap<>();
        booking.put("userId", uid);
        booking.put("movieTitle", movieName);
        booking.put("date", selectedDate);
        booking.put("time", selectedTime);
        booking.put("seats", seats);
        booking.put("totalAmount", (double) totalPrice);
        booking.put("timestamp", System.currentTimeMillis());
        booking.put("poster", movie.getPoster()); // Save poster for My Bookings

        db.collection("bookings")
                .add(booking)
                .addOnSuccessListener(documentReference -> {
                    Log.d("BOOKING", "Ticket saved to Firebase: " + documentReference.getId());
                    Toast.makeText(getContext(), "Booking Confirmed!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Log.e("BOOKING", "Error saving ticket", e);
                    Toast.makeText(getContext(), "Error saving to cloud", Toast.LENGTH_SHORT).show();
                });
    }
}
