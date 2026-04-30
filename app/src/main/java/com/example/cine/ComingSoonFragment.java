package com.example.cine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ComingSoonFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        // Hide date selection for Coming Soon as it's not applicable
        View dateLabel = view.findViewById(R.id.tvSelectDateLabel);
        if (dateLabel != null) dateLabel.setVisibility(View.GONE);
        
        View dateGroup = view.findViewById(R.id.rgDate);
        if (dateGroup != null) dateGroup.setVisibility(View.GONE);

        TextView tvTitle = view.findViewById(R.id.tvSectionTitle);
        if (tvTitle != null) tvTitle.setText("Coming Soon");

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load ALL movies from JSON and filter
        ArrayList<Movie> comingSoon = new ArrayList<>();
        List<Movie> allMovies = MovieRepository.getMovies(requireContext());
        for (Movie m : allMovies) {
            if (!m.isNowPlaying()) {
                comingSoon.add(m);
            }
        }

        // Updated listener to match (movie, time) signature
        recyclerView.setAdapter(new MovieAdapter(comingSoon, (movie, selectedTime) -> {
            SeatSelectionFragment fragment = new SeatSelectionFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("MOVIE", movie);
            bundle.putString("SELECTED_TIME", selectedTime);
            fragment.setArguments(bundle);
            ((MainActivity) requireActivity()).replaceFragment(fragment);
        }));

        return view;
    }
}
