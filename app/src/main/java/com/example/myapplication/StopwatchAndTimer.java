package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

public class StopwatchAndTimer extends AppCompatActivity {
    private Button startButton, pauseButton, resetButton, timerButton;
    private Chronometer chronometer;
    private long pauseOffset;
    private boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch_and_timer);

        chronometer = findViewById(R.id.chronometer);

        startButton = findViewById(R.id.start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!running){
                    chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                    chronometer.start();
                    running = true;
                }
            }
        });
        pauseButton = findViewById(R.id.pause);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (running){
                    chronometer.stop();
                    pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                    running = false;
                }
            }
        });
        resetButton = findViewById(R.id.reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                pauseOffset = 0;
            }
        });
        timerButton = findViewById(R.id.timerButton);
        timerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openActivityTimer(); }
        });
    }

    public void openActivityTimer() {
        Intent intent = new Intent(this, TimerActivity.class);
        startActivity(intent);
    }
}