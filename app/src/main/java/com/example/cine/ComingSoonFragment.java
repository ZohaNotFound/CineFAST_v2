package com.example.cine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load ALL movies from JSON
        List<Movie> allMovies = MovieRepository.getMovies(requireContext());
        
        // Filter to coming soon only
        ArrayList<Movie> comingSoon = new ArrayList<>();
        for (Movie m : allMovies) {
            if (!m.isNowPlaying()) {
                comingSoon.add(m);
            }
        }

        recyclerView.setAdapter(new MovieAdapter(comingSoon, movie -> {
            SeatSelectionFragment fragment = new SeatSelectionFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("MOVIE", movie);
            fragment.setArguments(bundle);
            ((MainActivity) requireActivity()).replaceFragment(fragment);
        }));

        return view;
    }
}