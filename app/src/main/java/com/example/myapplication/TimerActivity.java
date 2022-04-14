package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class TimerActivity extends AppCompatActivity {
    private EditText editTextInput;
    private TextView textViewCountDown;
    private Button startButton, pauseButton, resetButton, setButton, stopwatchButton;
    private CountDownTimer countDownTimer;
    private boolean running;
    private long startTimeInMillis;
    private long timeLeftInMillis = startTimeInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);

        editTextInput = findViewById(R.id.edit_text_input);
        textViewCountDown = findViewById(R.id.text_view_countdown);

        setButton = findViewById(R.id.button_set);
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = editTextInput.getText().toString();
                if (input.length() == 0){
                    Toast.makeText(TimerActivity.this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                long millisInput = Long.parseLong(input) * 60000;
                if (millisInput == 0){
                    Toast.makeText(TimerActivity.this, "Please enter a positive number", Toast.LENGTH_SHORT).show();
                    return;
                }
                setTime(millisInput);
                editTextInput.setText("");
            }
        });
        startButton = findViewById(R.id.button_start);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!running){
                    startTimer();
                }
            }
        });
        pauseButton = findViewById(R.id.button_pause);
        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (running){
                    pauseTimer();
                }
            }
        });
        resetButton = findViewById(R.id.button_reset);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });
        stopwatchButton = findViewById(R.id.stopwatchButton);
        stopwatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { openActivityStopwatch(); }
        });

        updateCountdownText();
    }

    public void openActivityStopwatch() {
        Intent intent = new Intent(this, StopwatchAndTimer.class);
        startActivity(intent);
    }

    private void setTime(long milliseconds){
        startTimeInMillis = milliseconds;
        resetTimer();
        closeKeyboard();
    }

    private void startTimer(){
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMillis = l;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                running = false;
            }
        }.start();
        running = true;
    }
    private void pauseTimer(){
        if (countDownTimer != null){
            countDownTimer.cancel();
        }
        running = false;
    }
    private void resetTimer(){
        pauseTimer();
        timeLeftInMillis = startTimeInMillis;
        updateCountdownText();
    }
    private void closeKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private void updateCountdownText(){
        int hours = (int) (timeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((timeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted;
        if (hours > 0){
            timeLeftFormatted = String.format(Locale.getDefault(),"%d:%02d:%02d",hours, minutes, seconds);
        }
        else{
            timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        }
        textViewCountDown.setText(timeLeftFormatted);
    }
}