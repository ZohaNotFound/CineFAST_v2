package com.example.cine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

public class MyBookingsFragment extends Fragment {

    private RecyclerView recyclerView;
    private BookingAdapter adapter;
    private ArrayList<Booking> bookings = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_bookings, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewBookings);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        
        adapter = new BookingAdapter(bookings);
        recyclerView.setAdapter(adapter);

        // Header and back button
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
        FirebaseFirestore.getInstance().collection("bookings")
                .whereEqualTo("userId", uid)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    bookings.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        bookings.add(doc.toObject(Booking.class));
                    }
                    adapter.notifyDataSetChanged();
                });
    }

    private static class BookingAdapter extends RecyclerView.Adapter<BookingAdapter.ViewHolder> {
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
            holder.tvMovie.setText(b.getMovieTitle());
            holder.tvDateTime.setText(b.getDate() + " | " + b.getTime());
            holder.tvSeats.setText("Seats: " + b.getSeats());
            holder.tvAmount.setText(String.format("Total: $%.2f", b.getTotalAmount()));
        }

        @Override
        public int getItemCount() { return list.size(); }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvMovie, tvDateTime, tvSeats, tvAmount;
            public ViewHolder(View v) {
                super(v);
                tvMovie = v.findViewById(R.id.tvBookingMovie);
                tvDateTime = v.findViewById(R.id.tvBookingDateTime);
                tvSeats = v.findViewById(R.id.tvBookingSeats);
                tvAmount = v.findViewById(R.id.tvBookingAmount);
            }
        }
    }
}
