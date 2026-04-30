package com.example.cine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class NowShowingFragment extends Fragment {

    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private ArrayList<Movie> nowShowing;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        TextView tvTitle = view.findViewById(R.id.tvSectionTitle);
        if (tvTitle != null) tvTitle.setText("Now Showing");

        // Load movies
        nowShowing = new ArrayList<>();
        ArrayList<Movie> allMovies = MovieRepository.getMovies(requireContext());
        for (Movie m : allMovies) {
            if (m.isNowPlaying()) nowShowing.add(m);
        }

        adapter = new MovieAdapter(nowShowing, (movie, selectedTime) -> {
            SeatSelectionFragment fragment = new SeatSelectionFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("MOVIE", movie);
            bundle.putString("SELECTED_TIME", selectedTime);
            
            RadioButton rbToday = view.findViewById(R.id.rbToday);
            String dateToPass = rbToday.isChecked() ? getFormattedDate(0) : getFormattedDate(1);
            bundle.putString("SELECTED_DATE", dateToPass);
            
            fragment.setArguments(bundle);
            ((MainActivity) requireActivity()).replaceFragment(fragment);
        });
        recyclerView.setAdapter(adapter);

        // Date selection logic
        RadioGroup rgDate = view.findViewById(R.id.rgDate);
        if (rgDate != null) {
            rgDate.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId == R.id.rbToday) {
                    adapter.updateDate("today");
                } else {
                    adapter.updateDate("tomorrow");
                }
            });
        }

        // Back button logic for Home
        View btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
        }

        return view;
    }

    private String getFormattedDate(int daysLater) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, daysLater);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }
}