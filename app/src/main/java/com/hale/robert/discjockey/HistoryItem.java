package com.hale.robert.discjockey;

class HistoryItem {
    private String courseName;
    private int score;

    public HistoryItem(){

    }
    public HistoryItem(String courseName, int score){
        this.courseName = courseName;
        this.score = score;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
