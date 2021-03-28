package com.example.terrariastats;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class IndividualUserStats extends AppCompatActivity {
    private Intent thisIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hides the title bar
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_individual_user_stats);
        thisIntent = getIntent();
        setUserStats();
    }
    private void setUserStats() {
        // Get intent variables from previous activity
        int userID = thisIntent.getIntExtra("uID", -1);
        String userName = thisIntent.getStringExtra("userName");
        String lastLogin = thisIntent.getStringExtra("lastLogin");
        double totalPlayTime = thisIntent.getDoubleExtra("playTime", -1);
        int numLogins = thisIntent.getIntExtra("numLogins", -1);

        // Set the views
        TextView tvUser = findViewById(R.id.cVUserName);
        TextView tvLastLog = findViewById(R.id.textViewIndLastLogin);
        TextView tvNumLogins = findViewById(R.id.textViewIndNumLog);
        TextView tvPlayTime = findViewById(R.id.textViewIndPlayTime);

        tvUser.setText(userName);
        tvLastLog.setText(lastLogin);
        tvPlayTime.setText(String.valueOf(totalPlayTime));
        tvNumLogins.setText(String.valueOf(numLogins));
    }
}