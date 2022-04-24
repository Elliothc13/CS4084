package com.example.myapplication;

// import com.google.firebase.auth.FirebaseUser;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// singleton class for smaller footprint
public class AppUser {
    private String name, idToken, email, businessName, businessType;
    private boolean businessOwner;
    private Map<String, Object> userDetails, businessDetails;
    private List<Map<String, Object>> posts;
    private List<String> upvotedPosts;
    public byte[] currentImage;

    private AppUser() {
        try {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            assert user != null;
            idToken = user.getUid();
            assert (idToken != null);
            email = user.getEmail();
            refreshProfileDetails();
            Log.i("INFO", "===== Reached 10 ");
            posts = new ArrayList<>();
            upvotedPosts = new ArrayList<>();
            refreshGenericPosts();
            Log.i("INFO", "===== Reached 11 ");
            fetchOwnImage();
            Log.i("INFO", "===== Reached 12 ");
        } catch (Exception exc) {
            Log.e("ERROR", "===== Exception on local app user profile creation: "
                    + exc.getMessage());
        }
    }

    public String getName() { return name; }
    public String getIdToken() { return idToken; }
    public String getEmail() { return email; }
    public List<String> getUpvotedPosts() { return upvotedPosts; }
    public List<Map<String,Object>> getPosts() { return posts; }
    public byte[] getCurrentImage() { return currentImage; }
    public boolean hasBusiness() { return businessOwner; }
    public void setHasBusiness(boolean b) {
        businessOwner = b;
        // updateProfile
    }



    public void refreshProfileDetails() {
        DbManager.getProfileDetails(idToken, new CallbackForMap() {
            @Override
            public void onCallback(Map<String, Object> s) {
                Log.i("INFO", "===== Reached 1 ");
                userDetails = s;
                Log.i("INFO", "===== Reached 2 ");
                name = (String) userDetails.get("name");
                Log.i("INFO", "===== Reached 3 ");
                businessOwner = (boolean) userDetails.get("businessOwner");
                Log.i("INFO", "===== Reached 4 ");
                if (businessOwner) {
                    Log.i("INFO", "===== Reached 5 ");
                    DbManager.getOwnersBusiness(getIdToken(), new CallbackForMap() {
                        @Override
                        public void onCallback(Map<String, Object> s) {
                            Log.i("INFO", "===== Reached 6 ");
                            businessDetails = s;
                            Log.i("INFO", "===== Reached 7 ");
                            businessName = (String) businessDetails.get("name");
                            Log.i("INFO", "===== Reached 8 ");
                            businessType = (String) businessDetails.get("bType");
                        }
                    });
                }
                Log.i("INFO", "===== Reached 9 ");
            }
        });
    }

    public void fetchOwnImage() {
        DbManager.getReferencedImage("uploads/" + getIdToken() + "/pics/profile.jpg", new CallbackForBytes() {
            @Override
            public void onCallback(byte[] s) {
                currentImage = s;
            }
        });
    }

    public void refreshGenericPosts() {
        DbManager.getPostsForGenericFeed(new CallbackForList<Map<String, Object>>() {
            @Override
            public void onCallback(List<Map<String, Object>> s) {
                posts = s;
                System.out.println("===== Posts refreshed, current size: " + s.size());
            }
        });
    }

    private static class LazyLoadUser {
        static final AppUser APP_USER = new AppUser();
    }

    public static AppUser getInstance() {
        return LazyLoadUser.APP_USER;
    }
}
