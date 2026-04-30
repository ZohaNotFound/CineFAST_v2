package com.example.cine;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Requirement: Add a toast statement when app launches with tag "CineFAST"
        Toast.makeText(this, "CineFAST", Toast.LENGTH_SHORT).show();
        Log.d("CineFAST", "App launched and MainActivity created.");

        // HomeFragment is typically defined in XML or replaced here.
        // Assuming fragment_container exists in activity_main.xml
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
