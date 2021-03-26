package com.example.terrariastats;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String ainrariaAPI = "https://ainraria.alvinlee.dev/ainraria/";
    private List<RCUserCardView> userList;
    private JSONObject userPayLoad;
    private int numUsers;
    RecyclerView rcvUser;
    RCAdapter rcAdapter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hides the title bar
        this.getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        userList = new ArrayList<>();
        numUsers = 0;
        createUserRecyclerView();
    }

    // Creates the recycler view to display the users
    private void createUserRecyclerView() {
        rcvUser = findViewById(R.id.recyclerViewUserList);
        rcvUser.setHasFixedSize(true);
        rcAdapter = new RCAdapter(userList);
        rcvUser.setAdapter(rcAdapter);
        rcvUser.setLayoutManager(new LinearLayoutManager(this));
    }

    public void populateUserRecyclerView(View view) {
        // TODO: Implement a better solution for rapid get requests for users
        // Need a way to get updated logins if needed
        if((numUsers == 0) || (numUsers != userList.size())) {
            GetUsers gUserThread = new GetUsers();
            new Thread(gUserThread).start();
        }
    }

    // Thread runnable for fetching user data from API
    class GetUsers implements Runnable {
        // Get users from an api call to https://ainraria.alvinlee.dev/ainraria/users
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void run() {
            try {
                URL userURL = new URL(ainrariaAPI + "users");
                apiUserCall(userURL);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private void apiUserCall(URL userURL) {
            try {
                HttpURLConnection connectURL = (HttpURLConnection) userURL.openConnection();
                connectURL.setRequestMethod("GET");
                connectURL.connect();
                // Creates user card views for recycler view
                // Need to read response, then create the cardview object
                readJSONResponse(connectURL.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private void readJSONResponse(InputStream apiResp) throws IOException {
            int userID = 0;
            String playerName = "";
            String lastLogin = "";

            // Json reader to start parsing of data
            JsonReader jReader = new JsonReader(new InputStreamReader(apiResp));

            // Begin json array
            jReader.beginArray();
            while(jReader.hasNext()) {
                // Begin json object
                jReader.beginObject();
                while(jReader.hasNext()) {
                    String name = jReader.nextName();

                    if (name.equals("playerId")) {
                        userID = jReader.nextInt();
                    } else if (name.equals("username")) {
                        playerName = jReader.nextString();
                    } else if (name.equals("lastLoginTime")) {
                        lastLogin = jReader.nextString();
                    } else {
                        jReader.skipValue();
                    }
                }
                jReader.endObject();
                createUserCards(userID, playerName, lastLogin);
                numUsers++;
            }
            jReader.endArray();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private void createUserCards(int uID, String name, String loginTime) {
            LocalDateTime lastLog = LocalDateTime.parse(loginTime,
                    DateTimeFormatter.ISO_ZONED_DATE_TIME);
            RCUserCardView userCard = new RCUserCardView(uID, name, lastLog);
            userList.add(userCard);
            rcAdapter.notifyItemInserted(userList.size()-1);
        }
    }
}