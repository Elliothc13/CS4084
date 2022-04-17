package com.example.myapplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Routine {
    private String workoutName;
    private int lengthInMins;
    private String bodyProfile;
    private List<Exercise> exerciseList;

    Routine(String workoutName, String bodyProfile) {
        this.workoutName = workoutName;
        this.bodyProfile = bodyProfile;
        this.lengthInMins = 0;
        this.exerciseList = new ArrayList<Exercise>();
    }

    public void addExercise(Exercise exercise) {
        exerciseList.add(exercise);
        lengthInMins += exercise.getDurationMins();
    }

    public void removeExercise(String exerciseName) {
        for (Exercise ex : exerciseList) {
            if (ex.getName().equals(exerciseName)) {
                exerciseList.remove(ex);
                updateRoutineLength(ex.getDurationMins() * -1);
                break;
            }
        }
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String updatedName) {
        workoutName = updatedName;
    }

    public int getLengthInMins() {
        return lengthInMins;
    }

    public String getBodyProfile() {
        return bodyProfile;
    }

    public void setBodyProfile(String updatedBodyProfile) {
        this.bodyProfile = updatedBodyProfile;
    }

    private void updateRoutineLength(int change) {
        lengthInMins += change;
    }
}
