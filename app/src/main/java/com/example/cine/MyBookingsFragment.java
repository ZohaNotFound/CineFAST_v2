package com.example.cine;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class MyBookingsFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookingAdapter adapter;
    private ArrayList<Booking> bookings = new ArrayList<>();
    private TextView tvEmptyMessage;
    private static final String TAG = "BOOKINGS_FETCH";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_bookings, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewBookings);
        tvEmptyMessage = view.findViewById(R.id.tvEmptyMessage);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        adapter = new BookingAdapter(bookings);
        recyclerView.setAdapter(adapter);

        TextView tvTitle = view.findViewById(R.id.tvSectionTitle);
        if (tvTitle != null) tvTitle.setText("My Bookings");
        
        View btnBack = view.findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> requireActivity().getOnBackPressedDispatcher().onBackPressed());
        }

        fetchBookings();

        return view;
    }

    private void fetchBookings() {
        if (FirebaseAuth.getInstance().getCurrentUser() == null) return;
        
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d(TAG, "Fetching bookings for user: " + uid);
        
        FirebaseFirestore.getInstance().collection("bookings")
                .whereEqualTo("userId", uid)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    bookings.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Booking b = doc.toObject(Booking.class);
                        // @DocumentId might not populate automatically in some versions, manual set:
                        b.documentId = doc.getId(); 
                        bookings.add(b);
                        Log.d(TAG, "Found booking: " + b.movieTitle);
                    }
                    
                    // Sort locally to avoid index requirement (API 23 compatible)
                    Collections.sort(bookings, (b1, b2) -> Long.compare(b2.timestamp, b1.timestamp));
                    
                    adapter.notifyDataSetChanged();
                    updateEmptyState();
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error fetching bookings: " + e.getMessage());
                    Toast.makeText(getContext(), "Failed to load bookings", Toast.LENGTH_SHORT).show();
                });
    }

    private void updateEmptyState() {
        if (bookings.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            if (tvEmptyMessage != null) tvEmptyMessage.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            if (tvEmptyMessage != null) tvEmptyMessage.setVisibility(View.GONE);
        }
    }

    private class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {
        private ArrayList<Booking> list;
        public BookingAdapter(ArrayList<Booking> list) { this.list = list; }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_booking, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Booking b = list.get(position);
            holder.tvMovie.setText(b.movieTitle);
            holder.tvDateTime.setText(b.date + " | " + b.time);
            holder.tvSeats.setText("Tickets: " + b.seats);

            // Load poster
            if (b.poster != null) {
                int resId = holder.itemView.getContext().getResources().getIdentifier(
                        b.poster, "drawable", holder.itemView.getContext().getPackageName());
                holder.ivPoster.setImageResource(resId != 0 ? resId : R.drawable.logo);
            } else {
                holder.ivPoster.setImageResource(R.drawable.logo);
            }

            holder.btnCancel.setOnClickListener(v -> {
                if (isFutureBooking(b.date, b.time)) {
                    showCancelDialog(b, holder.getAdapterPosition());
                } else {
                    Toast.makeText(getContext(), "Cannot cancel past bookings", Toast.LENGTH_SHORT).show();
                }
            });
        }

        private boolean isFutureBooking(String dateStr, String timeStr) {
            if (dateStr == null || timeStr == null) return false;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy hh:mm a", Locale.getDefault());
                Date showDate = sdf.parse(dateStr + " " + timeStr);
                return showDate != null && showDate.after(new Date());
            } catch (ParseException e) {
                Log.e(TAG, "Date parse error: " + e.getMessage());
                return false;
            }
        }

        private void showCancelDialog(Booking booking, int position) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Cancel Booking")
                    .setMessage("Are you sure you want to cancel this booking?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        cancelBooking(booking, position);
                    })
                    .setNegativeButton("No", null)
                    .show();
        }

        private void cancelBooking(Booking booking, int position) {
            if (booking.documentId == null) {
                Log.e(TAG, "Cannot delete: documentId is null");
                return;
            }
            
            FirebaseFirestore.getInstance().collection("bookings")
                    .document(booking.documentId)
                    .delete()
                    .addOnSuccessListener(unused -> {
                        list.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, list.size());
                        updateEmptyState();
                        Toast.makeText(getContext(), "Booking Cancelled Successfully", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Log.e(TAG, "Delete failed: " + e.getMessage());
                        Toast.makeText(getContext(), "Error cancelling booking", Toast.LENGTH_SHORT).show();
                    });
        }

        @Override
        public int getItemCount() { return list.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvMovie, tvDateTime, tvSeats;
            ImageView ivPoster;
            ImageButton btnCancel;
            public ViewHolder(View v) {
                super(v);
                tvMovie = v.findViewById(R.id.tvBookingMovie);
                tvDateTime = v.findViewById(R.id.tvBookingDateTime);
                tvSeats = v.findViewById(R.id.tvBookingSeats);
                ivPoster = v.findViewById(R.id.ivBookingPoster);
                btnCancel = v.findViewById(R.id.btnCancelBooking);
            }
        }
    }
}
