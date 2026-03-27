package com.example.cine;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
        popup.setOnMenuItemClickListener(item -> {
            if (item.getTitle().equals("View Last Booking")) {
                showLastBookingDialog();
                return true;
            }
            return false;
        });
        popup.show();
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