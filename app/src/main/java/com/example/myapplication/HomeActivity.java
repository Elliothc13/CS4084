package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
    private Button settingsButton, mapButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivitySettings();
            }
        });
        mapButton = findViewById(R.id.mapButton);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openActivityMap(); }
        });

    }

    public void openActivityMap() {
        Intent intent = new Intent(this, MapActivity.class);
        startActivity(intent);
    }

    public void openActivitySettings(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}