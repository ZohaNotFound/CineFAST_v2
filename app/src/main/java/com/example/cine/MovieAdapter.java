package com.example.cine;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private ArrayList<Movie> movies;
    private OnMovieClickListener listener;

    public interface OnMovieClickListener {
        void onBookSeats(Movie movie);
    }

    public MovieAdapter(ArrayList<Movie> movies, OnMovieClickListener listener) {
        this.movies = movies;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        holder.movieTitle.setText(movie.getTitle());
        holder.movieDetails.setText(movie.getGenre() + " / " + movie.getDuration());
        holder.moviePoster.setImageResource(movie.getImageResId());

        holder.btnBook.setOnClickListener(v -> {
            if (listener != null) {
                listener.onBookSeats(movie);
            }
        });

        holder.btnTrailer.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.youtube.com/results?search_query=" + movie.getTitle() + " trailer"));
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() { return movies.size(); }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView moviePoster;
        TextView movieTitle, movieDetails;
        Button btnTrailer, btnBook;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.moviePoster);
            movieTitle = itemView.findViewById(R.id.movieTitle);
            movieDetails = itemView.findViewById(R.id.movieDetails);
            btnTrailer = itemView.findViewById(R.id.btnTrailer);
            btnBook = itemView.findViewById(R.id.btnBook);
        }
    }
}