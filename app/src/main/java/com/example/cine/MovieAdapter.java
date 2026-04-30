package com.example.cine;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private ArrayList<Movie> movies;
    private OnMovieClickListener listener;
    private String selectedDate = "today"; // default

    public interface OnMovieClickListener {
        void onBookSeats(Movie movie, String selectedTime);
    }

    public MovieAdapter(ArrayList<Movie> movies, OnMovieClickListener listener) {
        this.movies = movies;
        this.listener = listener;
    }

    public void updateDate(String date) {
        this.selectedDate = date.toLowerCase();
        notifyDataSetChanged();
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

        // Load poster by name
        int resId = holder.itemView.getContext().getResources().getIdentifier(
                movie.getPoster(), "drawable", holder.itemView.getContext().getPackageName());
        holder.moviePoster.setImageResource(resId != 0 ? resId : R.drawable.logo);

        // Handle Coming Soon state
        if (!movie.isNowPlaying()) {
            holder.btnBook.setEnabled(false);
            holder.btnBook.setAlpha(0.5f);
            holder.layoutShowtimes.setVisibility(View.GONE);
        } else {
            holder.btnBook.setEnabled(true);
            holder.btnBook.setAlpha(1.0f);
            holder.layoutShowtimes.setVisibility(View.VISIBLE);
            setupShowtimes(holder, movie);
        }

        holder.btnBook.setOnClickListener(v -> {
            if (listener != null) {
                String time = null;
                if (movie.getShowtimes().containsKey(selectedDate)) {
                    ArrayList<String> times = movie.getShowtimes().get(selectedDate);
                    if (!times.isEmpty()) time = times.get(0);
                }
                listener.onBookSeats(movie, time);
            }
        });

        holder.btnTrailer.setOnClickListener(v -> {
            String url = movie.getTrailerUrl();
            if (url != null && !url.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                holder.itemView.getContext().startActivity(intent);
            } else {
                // Fallback if URL is missing
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://www.youtube.com/results?search_query=" + movie.getTitle() + " trailer"));
                holder.itemView.getContext().startActivity(intent);
            }
        });
    }

    private void setupShowtimes(MovieViewHolder holder, Movie movie) {
        holder.layoutShowtimes.removeAllViews();
        ArrayList<String> times = movie.getShowtimes().get(selectedDate);

        if (times != null) {
            for (String time : times) {
                View timeView = LayoutInflater.from(holder.itemView.getContext())
                        .inflate(R.layout.item_showtime, holder.layoutShowtimes, false);
                TextView tvTime = timeView.findViewById(R.id.tvShowtime);
                tvTime.setText(time);
                
                tvTime.setOnClickListener(v -> {
                    // When a specific time is clicked, move to booking immediately
                    if (listener != null) {
                        listener.onBookSeats(movie, time);
                    }
                });

                holder.layoutShowtimes.addView(timeView);
            }
        }
    }

    @Override
    public int getItemCount() { return movies.size(); }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView moviePoster;
        TextView movieTitle, movieDetails;
        Button btnTrailer, btnBook;
        LinearLayout layoutShowtimes;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            moviePoster = itemView.findViewById(R.id.moviePoster);
            movieTitle = itemView.findViewById(R.id.movieTitle);
            movieDetails = itemView.findViewById(R.id.movieDetails);
            btnTrailer = itemView.findViewById(R.id.btnTrailer);
            btnBook = itemView.findViewById(R.id.btnBook);
            layoutShowtimes = itemView.findViewById(R.id.layoutShowtimes);
        }
    }
}
