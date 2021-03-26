package com.example.terrariastats;

import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RCViewHolder extends RecyclerView.ViewHolder {
    public TextView userName;
    public TextView userID;
    public TextView dateLastLogin;
    public ImageView dummyPicture;

    public RCViewHolder(View view) {
        super(view);
        this.userID = view.findViewById(R.id.textViewUserID);
        this.userName = view.findViewById(R.id.textViewUserName);
        this.dateLastLogin = view.findViewById(R.id.textViewDateLastLogin);
        this.dummyPicture = view.findViewById(R.id.imageViewDummyPic);

        // Set the onclick listener when card clicked
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the stat activity
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setViewData(RCUserCardView cardView) {
        this.userID.setText(String.valueOf(cardView.getUserID()));
        this.userName.setText(cardView.getUserName());
        this.dateLastLogin.setText(formatDate(cardView.getLastLogin()));
        this.dummyPicture.setImageResource(R.drawable.mannequin_45px);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String formatDate(LocalDateTime lastLoginDate) {
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("dd MMM yyy | hh:mm a");
        return lastLoginDate.format(customFormatter);
    }
}
