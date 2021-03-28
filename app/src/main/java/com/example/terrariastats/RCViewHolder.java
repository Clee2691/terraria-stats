package com.example.terrariastats;

import android.content.Context;
import android.content.Intent;
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
    public TextView dateLastLogin;
    public ImageView dummyPicture;
    private int userID;
    private double playTime;
    private int numLogins;

    public RCViewHolder(View view) {
        super(view);
        this.userName = view.findViewById(R.id.textViewUserName);
        this.dateLastLogin = view.findViewById(R.id.textViewDateLastLogin);
        this.dummyPicture = view.findViewById(R.id.imageViewDummyPic);

        // Set the onclick listener when card clicked
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context mainContext = v.getContext();

                // Start the stat activity
                Intent individualUser = new Intent(mainContext, IndividualUserStats.class);
                individualUser.putExtra("uID", userID);
                individualUser.putExtra("userName", userName.getText().toString());
                individualUser.putExtra("lastLogin", dateLastLogin.getText().toString());
                individualUser.putExtra("playTime", playTime);
                individualUser.putExtra("numLogins", numLogins);
                mainContext.startActivity(individualUser);
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setViewData(RCUserCardView cardView) {
        //this.userID.setText(String.valueOf(cardView.getUserID()));
        this.userID = cardView.getUserID();
        this.playTime = cardView.getPlayTime();
        this.numLogins = cardView.getNumLogins();
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
