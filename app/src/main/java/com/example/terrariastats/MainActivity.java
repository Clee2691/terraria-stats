package com.example.terrariastats;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<RCUserCardView> userList;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        userList = new ArrayList<>();
        createUserRecyclerView();
        setDemoData();
    }

    // Creates the recycler view to display the users
    private void createUserRecyclerView() {
        RecyclerView rcvUser = findViewById(R.id.recyclerViewUserList);
        rcvUser.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.HORIZONTAL));
        rcvUser.setHasFixedSize(true);
        RCAdapter rcAdapter = new RCAdapter(userList);
        rcvUser.setAdapter(rcAdapter);
        rcvUser.setLayoutManager(new LinearLayoutManager(this));

    }



    // Set some demo data, will remove/ deprecate after
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setDemoData() {
        LocalDateTime lastLog = LocalDateTime.parse("2021-03-25T19:59:24.595Z",
                DateTimeFormatter.ISO_ZONED_DATE_TIME);

        RCUserCardView demoCard = new RCUserCardView(0,"Steeldrgn", lastLog);
        userList.add(demoCard);
        userList.add(demoCard);
        userList.add(demoCard);

    }
}