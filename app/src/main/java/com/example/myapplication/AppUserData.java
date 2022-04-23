package com.example.myapplication;

import java.util.Map;

public abstract class AppUserData {
    private static String name, idToken, email, businessName, businessType;
    private static boolean businessOwner;
    private static Map<String, Object> userDetails, businessDetails;

    public static String getName() {
        return name;
    }
    public static String getIdToken() {
        return idToken;
    }
    public static String getEmail() { return email; }
    public static boolean hasBusiness() { return businessOwner; }

    public static void setHasBusiness(boolean b) { businessOwner = b; }
}

