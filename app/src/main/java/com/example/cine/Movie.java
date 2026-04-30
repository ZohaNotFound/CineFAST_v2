package com.example.cine;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Movie implements Serializable {
    public String title;
    public String genre;
    public String duration;
    public int year;
    public double rating;
    public String poster; // Name of the drawable resource
    public String trailerUrl;
    public boolean nowPlaying;
    public Map<String, ArrayList<String>> showtimes; // "today" / "tomorrow"

    public Movie() {
        showtimes = new HashMap<>();
    }

    public Movie(String title, String genre, String duration, int year, double rating, String poster, String trailerUrl, boolean nowPlaying, Map<String, ArrayList<String>> showtimes) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.year = year;
        this.rating = rating;
        this.poster = poster;
        this.trailerUrl = trailerUrl;
        this.nowPlaying = nowPlaying;
        this.showtimes = showtimes;
    }

    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getDuration() { return duration; }
    public int getYear() { return year; }
    public double getRating() { return rating; }
    public String getPoster() { return poster; }
    public String getTrailerUrl() { return trailerUrl; }
    public boolean isNowPlaying() { return nowPlaying; }
    public Map<String, ArrayList<String>> getShowtimes() { return showtimes; }
}
