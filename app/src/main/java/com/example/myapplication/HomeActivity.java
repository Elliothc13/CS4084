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
    private Button  mapButton, uploadButton;
    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment = new HomeFragment();
    UploadFragment UploadFragment = new UploadFragment();
    SearchFragment searchFragment = new SearchFragment();
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

        bottomNavigationView = findViewById(R.id.bottom_navagation);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Button mapButton = (Button) findViewById(R.id.mapButton);
                switch (item.getItemId()){
                    case R.id.nav_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                        mapButton.setVisibility(View.VISIBLE);
                        return true;
                    case R.id.nav_upload:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, UploadFragment).commit();
                        mapButton.setVisibility(View.GONE);
                        return true;
                    case R.id.nav_profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, searchFragment).commit();
                        mapButton.setVisibility(View.GONE);
                        return true;
                    case R.id.nav_settings:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, settingsFragment).commit();
                        mapButton.setVisibility(View.GONE);
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

}