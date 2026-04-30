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
import java.util.ArrayList;

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
        tvTitle.setText("Now Showing");

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
            // We'll get the date from the RadioGroup later
            RadioButton rbToday = view.findViewById(R.id.rbToday);
            bundle.putString("SELECTED_DATE", rbToday.isChecked() ? "Today" : "Tomorrow");
            fragment.setArguments(bundle);
            ((MainActivity) requireActivity()).replaceFragment(fragment);
        });
        recyclerView.setAdapter(adapter);

        // Date selection logic
        RadioGroup rgDate = view.findViewById(R.id.rgDate);
        rgDate.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbToday) {
                adapter.updateDate("today");
            } else {
                adapter.updateDate("tomorrow");
            }
        });

        return view;
    }
}