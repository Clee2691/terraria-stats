package com.example.terrariastats;

import java.time.LocalDateTime;

public class RCUserCardView {
    private int userID;
    private String userName;
    private LocalDateTime lastLogin;

    public RCUserCardView(int uID, String uName, LocalDateTime userLastLogin) {
        this.userID = uID;
        this.userName = uName;
        this.lastLogin = userLastLogin;

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
}
