package com.example.cine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;

public class SnacksFragment extends Fragment {

    private ListView snacksListView;
    private TextView tvSnacksTotal;
    private Button btnGoToSummary;
    private ArrayList<Snack> snackList;
    private SnackAdapter adapter;

    private Movie movie;
    private int seatCount;
    private float ticketTotal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_snacks, container, false);

        if (getArguments() != null) {
            movie = (Movie) getArguments().getSerializable("MOVIE");
            seatCount = getArguments().getInt("SEAT_COUNT");
            ticketTotal = getArguments().getFloat("TICKET_TOTAL");
        }

        snacksListView = view.findViewById(R.id.snacksListView);
        tvSnacksTotal = view.findViewById(R.id.tvSnacksTotal);
        btnGoToSummary = view.findViewById(R.id.btnGoToSummary);

        prepareSnackData();

        adapter = new SnackAdapter(getContext(), snackList, this::updateTotal);
        snacksListView.setAdapter(adapter);

        btnGoToSummary.setOnClickListener(v -> {
            TicketSummaryFragment fragment = new TicketSummaryFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("MOVIE", movie);
            bundle.putInt("SEAT_COUNT", seatCount);
            bundle.putFloat("TICKET_TOTAL", ticketTotal);
            bundle.putFloat("SNACKS_TOTAL", calculateTotal());
            
            // Generate snacks description
            StringBuilder details = new StringBuilder();
            for (Snack s : snackList) {
                if (s.getQuantity() > 0) {
                    details.append(s.getName()).append(" x").append(s.getQuantity()).append(", ");
                }
            }
            if (details.length() > 2) details.setLength(details.length() - 2);
            else details.append("None");
            
            bundle.putString("SELECTED_SNACKS", details.toString());
            fragment.setArguments(bundle);
            ((MainActivity) requireActivity()).replaceFragment(fragment);
        });

        return view;
    }

    private void prepareSnackData() {
        snackList = new ArrayList<>();
        snackList.add(new Snack("Popcorn", 9.0f, R.drawable.popcorn));
        snackList.add(new Snack("Nachos", 8.0f, R.drawable.nachos));
        snackList.add(new Snack("Drink", 6.0f, R.drawable.drink));
        snackList.add(new Snack("Candy", 7.0f, R.drawable.candy));
    }

    private void updateTotal() {
        tvSnacksTotal.setText(String.format("Snacks Total: $%.2f", calculateTotal()));
    }

    private float calculateTotal() {
        float total = 0;
        for (Snack s : snackList) {
            total += s.getPrice() * s.getQuantity();
        }
        return total;
    }
}