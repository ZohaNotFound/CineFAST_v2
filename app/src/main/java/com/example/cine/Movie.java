package com.example.cine;

import java.io.Serializable;

public class Movie implements Serializable {
    public String title;
    public String genre;
    public String releaseYear;
    public String duration;
    public double rating;
    public int imageResId;
    public boolean comingSoon;

    public Movie() {}  // required empty constructor for Firebase

    // Constructor for HomeActivity / Firestore
    public Movie(String title, String genre, String releaseYear, double rating) {
        this.title = title;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.rating = rating;
    }

    // Constructor for NowShowingFragment / ComingSoonFragment
    public Movie(String title, String genre, String duration, int imageResId, boolean comingSoon) {
        this.title = title;
        this.genre = genre;
        this.duration = duration;
        this.imageResId = imageResId;
        this.comingSoon = comingSoon;
    }

    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getReleaseYear() { return releaseYear; }
    public String getDuration() { return duration; }
    public double getRating() { return rating; }
    public int getImageResId() { return imageResId; }
    public boolean isComingSoon() { return comingSoon; }
}
