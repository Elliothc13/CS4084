package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HomeActivity extends AppCompatActivity {
    private Button  mapButton, uploadButton, stopwatchButton;
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    ProfileFragment profileFragment = new ProfileFragment();
    SettingsFragment settingsFragment = new SettingsFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        mapButton = findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openActivityMap(); }
        });
        uploadButton = findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openActivityUpload(); }
        });
        stopwatchButton = findViewById(R.id.stopwatchButton);
        stopwatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openActivityStopwatch(); }
        });

        bottomNavigationView = findViewById(R.id.bottom_navagation);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Button mapButton = (Button) findViewById(R.id.mapButton);
                Button uploadButton = (Button) findViewById(R.id.uploadButton);
                Button stopwatchButton = (Button) findViewById(R.id.stopwatchButton);
                switch (item.getItemId()){
                    case R.id.nav_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                        mapButton.setVisibility(View.VISIBLE);
                        uploadButton.setVisibility(View.VISIBLE);
                        stopwatchButton.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.nav_profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, profileFragment).commit();
                        mapButton.setVisibility(View.GONE);
                        uploadButton.setVisibility(View.GONE);
                        stopwatchButton.setVisibility(View.GONE);
                        return true;
                    case R.id.nav_settings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, settingsFragment).commit();
                        mapButton.setVisibility(View.GONE);
                        uploadButton.setVisibility(View.GONE);
                        stopwatchButton.setVisibility(View.GONE);
                        return true;
                }
                return false;
            }
        });
    }

    public void openActivityMap() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }
    public void openActivityUpload() {
        Intent intent = new Intent(this, UploadActivity.class);
        startActivity(intent);
    }
    public void openActivityStopwatch() {
        Intent intent = new Intent(this, StopwatchAndTimer.class);
        startActivity(intent);
    }

}