package com.example.cine;

import android.content.Context;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MovieRepository {

    public static ArrayList<Movie> getMovies(Context context) {
        ArrayList<Movie> movies = new ArrayList<>();
        try {
            InputStream is = context.getAssets().open("movies.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");

            JSONArray array = new JSONArray(json);
            for (int i = 0; i < array.length(); i++) {
                JSONObject obj = array.getJSONObject(i);
                String title = obj.getString("title");
                String genre = obj.getString("genre");
                String duration = obj.optString("duration", "N/A");
                int year = obj.getInt("year");
                double rating = obj.getDouble("rating");
                String poster = obj.getString("poster");
                String trailerUrl = obj.optString("trailerUrl", "");
                boolean nowPlaying = obj.getBoolean("nowPlaying");

                // Parse showtimes Map
                Map<String, ArrayList<String>> showtimesMap = new HashMap<>();
                if (obj.has("showtimes")) {
                    JSONObject stObj = obj.getJSONObject("showtimes");
                    if (stObj.has("today")) {
                        JSONArray todayArray = stObj.getJSONArray("today");
                        ArrayList<String> todayList = new ArrayList<>();
                        for (int j = 0; j < todayArray.length(); j++) {
                            todayList.add(todayArray.getString(j));
                        }
                        showtimesMap.put("today", todayList);
                    }
                    if (stObj.has("tomorrow")) {
                        JSONArray tomorrowArray = stObj.getJSONArray("tomorrow");
                        ArrayList<String> tomorrowList = new ArrayList<>();
                        for (int j = 0; j < tomorrowArray.length(); j++) {
                            tomorrowList.add(tomorrowArray.getString(j));
                        }
                        showtimesMap.put("tomorrow", tomorrowList);
                    }
                }

                movies.add(new Movie(title, genre, duration, year, rating, poster, trailerUrl, nowPlaying, showtimesMap));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movies;
    }
}
