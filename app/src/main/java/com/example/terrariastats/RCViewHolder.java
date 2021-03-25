package com.example.terrariastats;

import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class RCViewHolder extends RecyclerView.ViewHolder {
    public TextView userName;
    public TextView userID;
    public TextView dateLastLogin;
    private int selectedUID;

    public RCViewHolder(View view) {
        super(view);
        this.userID = view.findViewById(R.id.textViewUserID);
        this.userName = view.findViewById(R.id.textViewUserName);
        this.dateLastLogin = view.findViewById(R.id.textViewDateLastLogin);

        // Set the onclick listener when card clicked
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedUID = Integer.parseInt(userID.getText().toString());
                Log.e("Selected ID",String.valueOf(selectedUID));
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void setViewData(RCUserCardView cardView) {
        this.userID.setText(String.valueOf(cardView.getUserID()));
        this.userName.setText(cardView.getUserName());
        String lastLogin = parseDate(cardView.getLastLogin());
        this.dateLastLogin.setText(lastLogin);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private String parseDate(LocalDateTime lastLoginDate) {
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("dd MMM yyy | hh:mm a");
        return lastLoginDate.format(customFormatter);
    }

    public int getSelectedUID() {
        return this.selectedUID;
    }
}
