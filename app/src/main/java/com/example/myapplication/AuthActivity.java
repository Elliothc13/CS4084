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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class AuthActivity extends AppCompatActivity {
    public static final String CLASS_NAME = AuthActivity.class.getSimpleName();
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
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (result.getResultCode() == RESULT_OK && currentUser != null) {
            System.out.println("===== Sign-in successful");
            if (response.isNewUser()) {
                Activity currentActivity = this;
                createProfile(currentUser, new CallbackForAsync() {
                    @Override
                    public void onCallback(boolean s) {
                        if (s) {
                            openHomeActivity();
                        } else {
                            Log.e(CLASS_NAME, "===== Sign in failed, Firestore error");
                            Toast.makeText(getApplicationContext(),
                                    "Firestore error: please login again", Toast.LENGTH_LONG).show();
                            SystemClock.sleep(1000);
                            currentActivity.onBackPressed();
                        }
                    }
                });
            } else {
                openHomeActivity();
            }

        } else {
            Log.e(CLASS_NAME, "===== Sign in failed: error: " + response.getError());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build()
        );
        Intent signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build();
        signInLauncher.launch(signInIntent);
    }

    public void openHomeActivity(){
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void createProfile(FirebaseUser user, CallbackForAsync cb) {
        String idToken = user.getUid();
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        final List<String> defaultEmpty = new ArrayList<>();
        Map<String, Object> profileDefaults = new HashMap<>();
        profileDefaults.put("name", user.getDisplayName());
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
}