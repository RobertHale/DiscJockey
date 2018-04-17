package com.hale.robert.discjockey;

import java.util.ArrayList;
import java.util.List;

class HistoryItem {
    private String courseName;
    private List<Integer> scores;

    public HistoryItem(){

    }
    public HistoryItem(String courseName, List<Integer> scores){
        this.courseName = courseName;
        this.scores = scores;
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
        return this.getCourseName();
    }

    public void setScores(List<Integer> scores) {
        this.scores = scores;
    }
}
