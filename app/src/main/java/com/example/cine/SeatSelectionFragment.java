package com.example.cine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;

public class SeatSelectionFragment extends Fragment {

    private TextView tvMovieName, tvSelectedCount, tvTotalPrice;
    private Button btnProceedSnacks, btnBookSeats, btnWatchTrailer;
    private View layoutNowShowing, layoutComingSoon;
    private GridLayout gridSeats;

    private Movie movie;
    private List<Seat> seatList = new ArrayList<>();
    private List<Seat> selectedSeats = new ArrayList<>();
    private final int PRICE_PER_SEAT = 10;

    private static class Seat {
        String id;
        int state; // 0: available, 1: selected, 2: booked
        Button button;

        Seat(String id, int state) {
            this.id = id;
            this.state = state;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seat_selection, container, false);

        if (getArguments() != null) {
            movie = (Movie) getArguments().getSerializable("MOVIE");
        }

        tvMovieName = view.findViewById(R.id.tvMovieName);
        tvSelectedCount = view.findViewById(R.id.tvSelectedCount);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        btnProceedSnacks = view.findViewById(R.id.btnProceedSnacks);
        btnBookSeats = view.findViewById(R.id.btnBookSeats);
        btnWatchTrailer = view.findViewById(R.id.btnWatchTrailer);
        gridSeats = view.findViewById(R.id.gridSeats);
        layoutNowShowing = view.findViewById(R.id.layoutNowShowingButtons);
        layoutComingSoon = view.findViewById(R.id.layoutComingSoonButtons);

        if (movie != null) {
            tvMovieName.setText("Movie: " + movie.getTitle());
            if (movie.isComingSoon()) {
                layoutNowShowing.setVisibility(View.GONE);
                layoutComingSoon.setVisibility(View.VISIBLE);
            } else {
                layoutNowShowing.setVisibility(View.VISIBLE);
                layoutComingSoon.setVisibility(View.GONE);
            }
        }

        createSeats();

        btnProceedSnacks.setEnabled(false);
        btnProceedSnacks.setOnClickListener(v -> {
            SnacksFragment fragment = new SnacksFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("MOVIE", movie);
            bundle.putInt("SEAT_COUNT", selectedSeats.size());
            bundle.putFloat("TICKET_TOTAL", (float) (selectedSeats.size() * PRICE_PER_SEAT));
            fragment.setArguments(bundle);
            ((MainActivity) requireActivity()).replaceFragment(fragment);
        });

        btnBookSeats.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Booking Confirmed!", Toast.LENGTH_SHORT).show();
            TicketSummaryFragment fragment = new TicketSummaryFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("MOVIE", movie);
            bundle.putInt("SEAT_COUNT", selectedSeats.size());
            bundle.putFloat("TICKET_TOTAL", (float) (selectedSeats.size() * PRICE_PER_SEAT));
            bundle.putFloat("SNACKS_TOTAL", 0f);
            fragment.setArguments(bundle);
            ((MainActivity) requireActivity()).replaceFragment(fragment);
        });

        btnWatchTrailer.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/results?search_query=" + movie.getTitle() + " trailer"));
            startActivity(intent);
        });

        return view;
    }

    private void createSeats() {
        String[] rows = {"A", "B", "C", "D", "E"};
        int cols = 8;
        List<String> bookedSeats = List.of("A1", "B3", "C5", "D2", "E7");

        gridSeats.setColumnCount(cols);
        gridSeats.setRowCount(rows.length);

        int seatSize = (int) (40 * getResources().getDisplayMetrics().density);

        for (int r = 0; r < rows.length; r++) {
            for (int c = 1; c <= cols; c++) {
                String seatId = rows[r] + (c);
                int state = bookedSeats.contains(seatId) ? 2 : 0;
                Seat seat = new Seat(seatId, state);
                seatList.add(seat);

                Button btn = new Button(getContext());
                btn.setText(seatId);
                btn.setTag(seat);
                btn.setTextSize(10);
                btn.setPadding(0, 0, 0, 0);
                updateSeatButton(btn, seat);

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = seatSize;
                params.height = seatSize;
                params.setMargins(4, 4, 4, 4);
                btn.setLayoutParams(params);

                if (movie != null && !movie.isComingSoon()) {
                    btn.setOnClickListener(v -> {
                        Seat clickedSeat = (Seat) v.getTag();
                        if (clickedSeat.state == 2) return;

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
                } else {
                    btn.setEnabled(false);
                }

                gridSeats.addView(btn);
                seat.button = btn;
            }
        }
    }

    private void updateSeatButton(Button btn, Seat seat) {
        switch (seat.state) {
            case 0:
                btn.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                break;
            case 1:
                btn.setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
                break;
            case 2:
                btn.setBackgroundColor(getResources().getColor(android.R.color.holo_red_dark));
                break;
        }
    }

    private void updateSelectionSummary() {
        int count = selectedSeats.size();
        tvSelectedCount.setText("Selected: " + count);
        tvTotalPrice.setText("Total: $" + (count * PRICE_PER_SEAT));
        btnProceedSnacks.setEnabled(count > 0);
        btnBookSeats.setEnabled(count > 0);
    }
}