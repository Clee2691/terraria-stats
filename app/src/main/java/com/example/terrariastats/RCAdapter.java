package com.example.terrariastats;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.List;

public class RCAdapter extends RecyclerView.Adapter<RCViewHolder> {
    private List<RCUserCardView> userList;

    public RCAdapter(List<RCUserCardView> userData) {
        this.userList =  userData;
    }

    @NonNull
    @Override
    public RCViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Creates and inflates the viewholder
        View v = LayoutInflater.from(parent.getContext()) // Get the view
                .inflate(R.layout.user_card_layout, // Inflate the layout
                parent, false);

        return new RCViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull RCViewHolder holder, int position) {
        // RCViewHolder class contains method to set data for card view
        // No need to do it here.
        holder.setViewData(userList.get(position));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
