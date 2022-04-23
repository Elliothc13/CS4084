package com.example.myapplication;

// import com.google.firebase.auth.FirebaseUser;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Map;

public class AppUser {
    private static String name, idToken, email, businessName, businessType;
    private static boolean businessOwner;
    private static Map<String, Object> userDetails, businessDetails;


    AppUser() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        idToken = user.getUid();
        assert(idToken != null);
        email = user.getEmail();

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
        Log.i("INFO", "===== Reached 10 ");



//        } catch (Exception ex) {
//            Log.e("ERROR", "===== Fetching user details failed, preparing to recover the Firestore");
//        }
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
