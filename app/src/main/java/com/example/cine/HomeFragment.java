package com.example.cine;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class HomeFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        Toolbar toolbar = view.findViewById(R.id.toolbar);

        viewPager.setAdapter(new HomePagerAdapter(this));

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(position == 0 ? "Now Showing" : "Coming Soon");
        }).attach();

        toolbar.inflateMenu(R.menu.home_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.view_last_booking) {
                showLastBooking();
                return true;
            }
            return false;
        });

        return view;
    }

    private void showLastBooking() {
        SharedPreferences prefs = requireActivity().getSharedPreferences("CinePrefs", Context.MODE_PRIVATE);
        String movie = prefs.getString("last_movie", "No previous booking found.");
        int seats = prefs.getInt("last_seats", 0);
        float price = prefs.getFloat("last_price", 0);

        if (seats == 0) {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Last Booking")
                    .setMessage("No previous booking found.")
                    .setPositiveButton("OK", null)
                    .show();
        } else {
            new AlertDialog.Builder(requireContext())
                    .setTitle("Last Booking")
                    .setMessage("Movie: " + movie + "\nSeats: " + seats + "\nTotal Price: $" + price)
                    .setPositiveButton("OK", null)
                    .show();
        }
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