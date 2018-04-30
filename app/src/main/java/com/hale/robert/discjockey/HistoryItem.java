package com.hale.robert.discjockey;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

class HistoryItem implements Comparable<HistoryItem>{
    private String courseName;
    private List<Integer> scores;
    private String userName;

    public HistoryItem(){

    }
    public HistoryItem(String courseName, List<Integer> scores){
        this.courseName = courseName;
        this.scores = scores;
        this.userName = "";
    }

    public HistoryItem(String courseName, List<Integer> scores, String userName){
        this.courseName = courseName;
        this.scores = scores;
        this.userName = userName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getScore(int hole){
        return scores.get(hole);
    }

    public List<Integer> getScores(){
        return scores;
    }

    public int getTotalScore() {
        int sum = 0;
        for(int score : scores){
            sum += score;
        }
        return sum;
    }

    @Override
    public String toString() {
        return this.userName + " " + this.getTotalScore() + " " + this.getCourseName();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public int compareTo(@NonNull HistoryItem o) {
        return this.getTotalScore() - o.getTotalScore();
    }
}
