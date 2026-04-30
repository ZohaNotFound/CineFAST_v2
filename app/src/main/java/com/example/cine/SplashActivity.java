package com.example.cine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    private static final String PREF_NAME = "cinefast_session_pref_v3";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Firebase Firestore Connection Test
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> testData = new HashMap<>();
        testData.put("message", "Firebase connected!");
        testData.put("from",    "CineFast app");

        db.collection("connectionTest")
                .add(testData)
                .addOnSuccessListener(ref ->
                        Log.d("FIREBASE_TEST", "✅ SUCCESS — Doc ID: " + ref.getId())
                )
                .addOnFailureListener(e ->
                        Log.e("FIREBASE_TEST", "❌ FAILED: " + e.getMessage())
                );

        ImageView logo = findViewById(R.id.logo);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        logo.startAnimation(fadeIn);

        new Handler().postDelayed(() -> {
            // Session Management: If logged in, skip Login and go to MainActivity
            SharedPreferences sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            if (sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false)) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
            finish();
        }, 5000);
    }
}