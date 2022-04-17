package com.example.myapplication;

public class Exercise {
    private String name;
    private int durationMins;
    private int reps;

    Exercise(String name, int durationMins, int reps) {
        this.name = name;
        this.durationMins = durationMins;
        this.reps = reps;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDurationMins() {
        return durationMins;
    }

    public void setDurationMins(int durationMins) {
        this.durationMins = durationMins;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }
}
