package com.example.myapplication;

import com.google.firebase.auth.FirebaseUser;

public class AppUser {
    private String name, idToken, email, businessName, businessType;
    private boolean businessOwner;

    AppUser() {

    }
    public String getName() {
        return name;
    }
    public String getIdToken() {
        return idToken;
    }
    public String getEmail() { return email; }
    public boolean hasBusiness() { return businessOwner; }

    public void setHasBusiness(boolean b) { businessOwner = b; }
}
