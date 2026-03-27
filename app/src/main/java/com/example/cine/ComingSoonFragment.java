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

public class ComingSoonFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ArrayList<Movie> movies = new ArrayList<>();
        movies.add(new Movie("Doremon Adventure", "Animation", "90 min", R.drawable.doremon, true));
        movies.add(new Movie("Dora Explorer", "Adventure", "85 min", R.drawable.dora, true));
        movies.add(new Movie("Shawshank", "Drama", "142 min", R.drawable.shawshank, true));

        recyclerView.setAdapter(new MovieAdapter(movies, movie -> {
            SeatSelectionFragment fragment = new SeatSelectionFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("MOVIE", movie);
            fragment.setArguments(bundle);
            ((MainActivity) requireActivity()).replaceFragment(fragment);
        }));

        return view;
    }
}