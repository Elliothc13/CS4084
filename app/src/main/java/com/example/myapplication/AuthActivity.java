package com.example.myapplication;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AuthActivity extends AppCompatActivity {
    public static final String CLASS_NAME = AuthActivity.class.getSimpleName();
    private FirebaseUser currentUser = null;

    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (result.getResultCode() == RESULT_OK && currentUser != null) {
            System.out.println("===== Sign-in successful");
            if (response.isNewUser()) {
                Activity currentActivity = this;
                final FirebaseFirestore database = FirebaseFirestore.getInstance();
                final String idToken = currentUser.getUid();
                final String userName = currentUser.getDisplayName();
                createProfile(userName, idToken, database, new CallbackForBool() {
                    @Override
                    public void onCallback(boolean s) {
                        if (s) {
                            Log.i("INFO", "===== Profile creation ok");
                            createEntry(userName, idToken, database, new CallbackForBool() {
                                @Override
                                public void onCallback(boolean b) {
                                    if (b) {
                                        Log.i("INFO", "===== Search entry update ok, opening home");
                                        openHomeActivity();
                                    } else {
                                        Log.e("ERROR", "===== Search entry update failed, Firestore error");
                                        Toast.makeText(getApplicationContext(),
                                                "Firestore error: please login again", Toast.LENGTH_LONG)
                                                .show();
                                        SystemClock.sleep(1000);
                                        currentActivity.onBackPressed();
                                    }
                                }
                            });
                        } else {
                            Log.e("ERROR", "===== Profile creation failed, Firestore error");
                            Toast.makeText(getApplicationContext(),
                                    "Firestore error: please login again", Toast.LENGTH_LONG)
                                    .show();
                            SystemClock.sleep(1000);
                            currentActivity.onBackPressed();
                        }
                    }
                });
            } else {
                openHomeActivity();
            }

        } else {
            Log.e("ERROR", "===== Sign in failed: error: " + response.getError());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (currentUser == null) {
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build()
            );
            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build();
            signInLauncher.launch(signInIntent);
        } else {
            openHomeActivity();
        }

    }

    public void openHomeActivity(){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("idToken", currentUser.getUid());
        Log.i("INFO", "===== Starting home activity");
        startActivity(intent);
    }

    public static void createProfile(String userName, String idToken, FirebaseFirestore database,
                                     CallbackForBool cb) {
        final List<String> defaultEmpty = new ArrayList<>();
        Map<String, Object> profileDefaults = new HashMap<>();
        profileDefaults.put("name", userName);
        profileDefaults.put("businessOwner", false);
        profileDefaults.put("posts", defaultEmpty);
        profileDefaults.put("routines", defaultEmpty);
        database.collection("users").document(idToken)
                .set(profileDefaults, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        boolean comp = task.isComplete();
                        boolean success = task.isSuccessful();
                        cb.onCallback(comp && success);
                    }
                });
    }

    public static void createEntry(String userName, String idToken, FirebaseFirestore database,
                                   CallbackForBool cb) {
        Map<String, Object> searchEntry = new HashMap<>();
        searchEntry.put("name", userName);
        searchEntry.put("id", idToken);
        database.collection("usersearch").document("allusers")
                .update("allusers", FieldValue.arrayUnion(searchEntry))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        boolean comp = task.isComplete();
                        boolean success = task.isSuccessful();
                        cb.onCallback(comp && success);
                    }
                });
    }
}