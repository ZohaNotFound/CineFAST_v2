package com.example.cine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TicketSummaryFragment extends Fragment {

    private TextView tvMovie, tvSeats, tvTicketsPrice, tvSnacksPrice, tvSnacksDetails, tvGrandTotal;
    private Button btnFinish, btnSendTicket;

    private Movie movie;
    private int seatCount;
    private float ticketTotal;
    private float snacksTotal;
    private String snacksDetails;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket_summary, container, false);

        if (getArguments() != null) {
            movie = (Movie) getArguments().getSerializable("MOVIE");
            seatCount = getArguments().getInt("SEAT_COUNT");
            ticketTotal = getArguments().getFloat("TICKET_TOTAL");
            snacksTotal = getArguments().getFloat("SNACKS_TOTAL");
            snacksDetails = getArguments().getString("SELECTED_SNACKS");
        }

        tvMovie = view.findViewById(R.id.tvSummaryMovie);
        tvSeats = view.findViewById(R.id.tvSummarySeats);
        tvTicketsPrice = view.findViewById(R.id.tvSummaryTicketsPrice);
        tvSnacksPrice = view.findViewById(R.id.tvSummarySnacksPrice);
        tvSnacksDetails = view.findViewById(R.id.tvSummarySnacksDetails);
        tvGrandTotal = view.findViewById(R.id.tvSummaryGrandTotal);
        btnFinish = view.findViewById(R.id.btnFinish);
        btnSendTicket = view.findViewById(R.id.btnSendTicket);

        if (movie != null) {
            tvMovie.setText("Movie: " + movie.getTitle());
            tvSeats.setText("Seats: " + seatCount);
            tvTicketsPrice.setText(String.format("Tickets: $%.2f", ticketTotal));
            tvSnacksPrice.setText(String.format("Snacks: $%.2f", snacksTotal));
            tvSnacksDetails.setText("Details: " + (snacksDetails != null ? snacksDetails : "None"));
            
            float grandTotal = ticketTotal + snacksTotal;
            tvGrandTotal.setText(String.format("Grand Total: $%.2f", grandTotal));

            saveBookingToPrefs(movie.getTitle(), seatCount, grandTotal);
        }

        btnFinish.setOnClickListener(v -> {
            ((MainActivity) requireActivity()).replaceFragment(new HomeFragment());
        });

        btnSendTicket.setOnClickListener(v -> {
            String shareText = "Movie: " + movie.getTitle() + "\n" +
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
        
        Toast.makeText(getContext(), "Booking Saved Locally", Toast.LENGTH_SHORT).show();
    }
}