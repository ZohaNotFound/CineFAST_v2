package com.example.cine;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class OnboardingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        Button getStartedButton = findViewById(R.id.buttonGetStarted);
        getStartedButton.setOnClickListener(v -> {
            //  HomeActivity
            Intent intent = new Intent(OnboardingActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });
    }
}