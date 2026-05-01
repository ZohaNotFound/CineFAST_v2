package com.example.cine;

import com.google.firebase.firestore.DocumentId;
import java.io.Serializable;

public class Booking implements Serializable {
    @DocumentId
    public String documentId;
    public String userId;
    public String movieTitle;
    public String date;
    public String time;
    public int seats;
    public double totalAmount;
    public long timestamp;
    public String poster;

    public Booking() {}

    public Booking(String userId, String movieTitle, String date, String time, int seats, double totalAmount, long timestamp, String poster) {
        this.userId = userId;
        this.movieTitle = movieTitle;
        this.date = date;
        this.time = time;
        this.seats = seats;
        this.totalAmount = totalAmount;
        this.timestamp = timestamp;
        this.poster = poster;
    }
}
