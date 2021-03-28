package com.example.terrariastats;

import java.time.LocalDateTime;

public class RCUserCardView {
    private int userID;
    private double playTime;
    private int numLogins;
    private String userName;
    private LocalDateTime lastLogin;

    public RCUserCardView(int uID, String uName, LocalDateTime userLastLogin, double playSecs, int logins) {
        this.userID = uID;
        this.userName = uName;
        this.lastLogin = userLastLogin;
        this.playTime = playSecs;
        this.numLogins = logins;
    }

    public int getUserID() {
        return this.userID;
    }

    public String getUserName() {
        return this.userName;
    }

    public LocalDateTime getLastLogin() {
         return this.lastLogin;
    }

    public double getPlayTime() {
        return this.playTime;
    }

    public int getNumLogins() {
        return this.numLogins;
    }
}
