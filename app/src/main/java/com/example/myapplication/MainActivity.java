package com.example.myapplication;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;
public class MainActivity extends AppCompatActivity {
    private Button button;
    public static SharedPreferences sharedPref;

//    @Override
//    protected void onStart() {
//      super.onStart();
//
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPref = getApplicationContext().getSharedPreferences("userData", MODE_PRIVATE);
        if (! sharedPref.getBoolean("loggedIn", false)) {
            // if user not logged in
            setContentView(R.layout.activity_main);
        } else {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("idToken", sharedPref.getString("idToken", null));
            Log.i("INFO", "===== Starting home activity");
            startActivity(intent);
        }
    }

    public void launchAuthentication(View view) {
        Intent intent = new Intent(this, AuthActivity.class);
        startActivity(intent);
    }


}