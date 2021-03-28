package com.example.terrariastats;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String ainrariaAPI = "https://ainraria.alvinlee.dev/ainraria/";
    private List<RCUserCardView> userList;
    private Handler uiHandler = new Handler();
    private PieChart serverPieChart;
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
        setPieChartStyle();
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

    private void setPieChartStyle () {
        serverPieChart = findViewById(R.id.serverPieChart);
        serverPieChart.setNoDataText("Press Get Server Info!");
        serverPieChart.setEntryLabelColor(Color.WHITE);
        serverPieChart.setEntryLabelTextSize(12f);
        Description serverChartDesc = serverPieChart.getDescription();
        serverChartDesc.setEnabled(false);

        Legend pieLegend = serverPieChart.getLegend();
        pieLegend.setTextColor(Color.WHITE);
        pieLegend.setWordWrapEnabled(true);
    }

    public void populateUserRecyclerView(View view) throws InterruptedException, IOException {
        Button currBtn = findViewById(view.getId());
        currBtn.setText("Wait...");
        currBtn.setEnabled(false);
        GetUsers gUserThread = new GetUsers();
        GetServerStats gServeThread = new GetServerStats();
        if((userList.size() != 0)) {
            userList.clear();
            rcAdapter.notifyDataSetChanged();
        }
        // Disable button and re-enable after 2 seconds.
        // Avoids spamming the thread
        currBtn.postDelayed(new Runnable() {
            @Override
            public void run() {
                currBtn.setText("Get Server Info");
                currBtn.setEnabled(true);
            }
        }, 2000);

        new Thread(gUserThread).start();
        new Thread(gServeThread).start();
    }

    // Get server stats and display them
    class GetServerStats implements Runnable {
        private int numBoss = 0;
        private int numMonsters = 0;
        private int numTiles = 0;
        private double totalSec = 0;
        @Override
        public void run() {
            try {
                URL serverURL = new URL(ainrariaAPI + "server/stats");
                getServerInputStream(serverURL);
                setServerViews();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        private void getServerInputStream(URL serverURL) {
            try {
                HttpURLConnection serverConn = (HttpURLConnection) serverURL.openConnection();
                serverConn.setRequestMethod("GET");
                serverConn.connect();
                parseInputStream(serverConn.getInputStream());
            } catch (IOException ie) {
                ie.printStackTrace();
            }
        }

        private void parseInputStream(InputStream serverStats) throws IOException {
            JsonReader serverRead = new JsonReader(new InputStreamReader(serverStats));
            serverRead.beginObject();
            while (serverRead.hasNext()) {
                String currObject = serverRead.nextName();
                if (currObject.equals("totalBosses")) {
                    numBoss = serverRead.nextInt();
                } else if (currObject.equals("totalMonsters")) {
                    numMonsters = serverRead.nextInt();
                } else if (currObject.equals("totalTiles")) {
                    numTiles = serverRead.nextInt();
                } else if (currObject.equals("totalSeconds")) {
                    totalSec = serverRead.nextDouble();
                } else {
                    serverRead.skipValue();
                }
            }
            serverRead.endObject();
        }

        private void setServerViews() {
            TextView totalSecView = findViewById(R.id.textViewTotalUserTime);
            final double secInDay = 24 * 3600;
            double minutes;
            double hours;
            double days;

            days = Math.floor(totalSec/secInDay);
            totalSec %= secInDay;
            hours = Math.floor(totalSec/3600);
            totalSec %= 3600;
            minutes = Math.floor(totalSec/60);
            totalSec %= 60;

            String totalPLayTime = String.format("Days: %.0f\nHrs: %.0f | Mins: %.0f | Secs: %.0f",
                    days, hours, minutes, totalSec);

            uiHandler.post(() -> {
                totalSecView.setText(totalPLayTime);
            });
            setPieChart();
        }

        private void setPieChart() {
            List<PieEntry> pieChartDataList = new ArrayList<>();
            pieChartDataList.add(new PieEntry(numBoss, "Bosses"));
            pieChartDataList.add(new PieEntry(numMonsters, "Monsters"));
            pieChartDataList.add(new PieEntry(numTiles, "Tiles"));

            PieDataSet pieDataSet = new PieDataSet(pieChartDataList, "Entities");
            ArrayList<Integer> colors = new ArrayList<>();

            colors.add(Color.rgb(248, 0, 113));
            colors.add(Color.rgb(24, 194, 233));
            colors.add(Color.rgb(77, 95, 209));

            pieDataSet.setColors(colors);
            pieDataSet.setSliceSpace(1.5f);
            pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
            pieDataSet.setValueLineColor(Color.WHITE);

            PieData pieData = new PieData(pieDataSet);
            pieData.setValueTextColor(Color.WHITE);
            pieData.setValueTextSize(12f);
            serverPieChart.setData(pieData);
            serverPieChart.setCenterText(String.format("%d total entities",
                    (numBoss+numMonsters+numTiles)));
            serverPieChart.invalidate();
        }
    }

    // Thread runnable for fetching user data from API
    class GetUsers implements Runnable {
        // Get users from an api call to https://ainraria.alvinlee.dev/ainraria/users
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void run() {
            try {
                URL userURL = new URL(ainrariaAPI + "users/");
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
            double playTime = 0.0;
            int numLogins = 0;

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
                    } else if (name.equals("playTime")) {
                        playTime = jReader.nextDouble();
                    } else if (name.equals("numLogins")) {
                        numLogins = jReader.nextInt();
                    } else {
                        jReader.skipValue();
                    }
                }
                jReader.endObject();
                createUserCards(userID, playerName, lastLogin, playTime, numLogins);
            }
            jReader.endArray();
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        private void createUserCards(int uID, String name, String loginTime,
                                     double playSecs, int logins) {
            LocalDateTime lastLog = LocalDateTime.parse(loginTime,
                    DateTimeFormatter.ISO_ZONED_DATE_TIME);
            RCUserCardView userCard = new RCUserCardView(uID, name, lastLog, playSecs, logins);
            uiHandler.post(()-> {
                userList.add(userCard);
                rcAdapter.notifyItemInserted(userList.size()-1);
            });
        }
    }
}