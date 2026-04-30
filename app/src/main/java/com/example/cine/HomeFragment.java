package com.example.cine;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;

public class HomeFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private static final String PREF_NAME = "cinefast_session_pref_v3";
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        ImageView btnDots = view.findViewById(R.id.btnDots);

        viewPager.setAdapter(new HomePagerAdapter(this));

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(position == 0 ? "Now Showing" : "Coming Soon");
        }).attach();

        if (btnDots != null) {
            btnDots.setOnClickListener(this::showPopupMenu);
        }

        return view;
    }

    private void showPopupMenu(View view) {
        PopupMenu popup = new PopupMenu(requireContext(), view);
        popup.getMenu().add("View Last Booking");
        popup.getMenu().add("Logout");
        popup.setOnMenuItemClickListener(item -> {
            if (item.getTitle().equals("View Last Booking")) {
                showLastBookingDialog();
                return true;
            } else if (item.getTitle().equals("Logout")) {
                logoutUser();
                return true;
            }
            return false;
        });
        popup.show();
    }

    private void logoutUser() {
        // Clear Firebase session
        FirebaseAuth.getInstance().signOut();

        // Clear SharedPreferences session
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Toast.makeText(getContext(), "Logged out successfully", Toast.LENGTH_SHORT).show();

        // Redirect to LoginActivity
        Intent intent = new Intent(requireActivity(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finish();
    }

    private void showLastBookingDialog() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("CinePrefs", Context.MODE_PRIVATE);
        String movie = prefs.getString("last_movie", null);
        int seats = prefs.getInt("last_seats", 0);
        float price = prefs.getFloat("last_price", 0);

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Last Booking");

        if (movie == null) {
            builder.setMessage("No previous booking found.");
        } else {
            builder.setMessage("Movie: " + movie + "\nSeats: " + seats + "\nTotal Price: $" + (int)price);
        }

        builder.setPositiveButton("OK", null);
        builder.show();
    }

    private static class HomePagerAdapter extends FragmentStateAdapter {
        public HomePagerAdapter(@NonNull Fragment fragment) { super(fragment); }
        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return position == 0 ? new NowShowingFragment() : new ComingSoonFragment();
        }
        @Override
        public int getItemCount() { return 2; }
    }
}