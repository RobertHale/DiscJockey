package com.hale.robert.discjockey;

import android.content.Context;
import android.graphics.Color;
import android.util.SparseIntArray;
import android.widget.Toast;

import com.amazonaws.models.nosql.CoursesDO;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;
import java.util.HashMap;

public class StatComputer {

    private DBConnector dbc;
    private ArrayList<HistoryItem> scores;
    private ArrayList<String> names;

    public StatComputer(DBConnector dbc){
        this.dbc = dbc;
        this.scores = new ArrayList<>();
        this.names = new ArrayList<>();
    }

    public void setScores(ArrayList<HistoryItem> scores){
    	this.scores = scores;
    }

    public LineGraphSeries<DataPoint> computeAveragePerHole(){
    	LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
    	SparseIntArray holeScores = new SparseIntArray();
        for(HistoryItem item : scores){
            for(int score : item.getScores()){
                holeScores.put(score, holeScores.get(score) + 1);
            }
        }
        for (int i = 0; i < holeScores.size(); i++){
            series.appendData(new DataPoint(holeScores.keyAt(i), holeScores.valueAt(i)), true, 36);
        }
        series.setDrawDataPoints(true);
        series.setAnimated(true);
    	return series;
    }

    public BarGraphSeries<DataPoint> computeAveragePerCourse(){
    	BarGraphSeries<DataPoint> series = new BarGraphSeries<>();
    	SparseIntArray courseScores = new SparseIntArray();
        for(HistoryItem item : scores){
            courseScores.put(item.getTotalScore(), courseScores.get(item.getTotalScore()) + 1);
        }
        for (int i = 0; i < courseScores.size(); i++){
            series.appendData(new DataPoint(courseScores.keyAt(i), courseScores.valueAt(i)), true, 36);
        }
        series.setSpacing(15);
        return series;
    }

    public ArrayList<LineGraphSeries<DataPoint>> computeComparison(String courseName){
    	CoursesDO course = dbc.getCourse(courseName);
    	ArrayList<LineGraphSeries<DataPoint>> seriesList = new ArrayList<>();
    	LineGraphSeries<DataPoint> parSeries = new LineGraphSeries<>();
    	parSeries.setDrawDataPoints(true);
        parSeries.setColor(Color.BLUE);
        parSeries.setTitle("Course Par");
        parSeries.setAnimated(true);
        parSeries.setBackgroundColor(Color.argb(100,0,0,205));
        parSeries.setDrawBackground(true);
        for(int i = 0; i < course.getPars().size(); i++){
            parSeries.appendData(new DataPoint(i+1, course.getPars().get(i)), true, 36);
        }
        seriesList.add(parSeries);
        for (HistoryItem item : scores){
            if(item.getCourseName().equals(course.getName())){
                LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
                for(int i = 0; i < item.getScores().size(); i++){
                    series.appendData(new DataPoint(i+1, item.getScore(i) + course.getPars().get(i)), true, 36);
                }
                series.setDrawDataPoints(true);
                series.setColor(Color.RED);
                series.setAnimated(true);
                series.setBackgroundColor(Color.argb(100,255,99,71));
                series.setDrawBackground(true);
                series.setTitle("User Score");
                seriesList.add(series);
            }
        }
        return seriesList;
    }

    public LineGraphSeries<DataPoint> computeDistPerCourse() {
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        HashMap<String, Integer> count = new HashMap<>();
        for (HistoryItem hi : scores) {
            if (count.containsKey(hi.getCourseName())) {
                count.put(hi.getCourseName(), count.get(hi.getCourseName()) + 1);
            } else {
                count.put(hi.getCourseName(), 1);
            }
        }
        ArrayList<Integer> totalDist = new ArrayList<>();
        for (String name : count.keySet()) {
            names.add(name);
            CoursesDO course = dbc.getCourse(name);
            int sum = 0;
            for (int x : course.getDistances()) sum += x;
            sum *= count.get(name);
            totalDist.add(sum);
        }
        for (int i = 0; i < totalDist.size(); i++) {
            series.appendData(new DataPoint(i, totalDist.get(i)), true, 36);
        }
        series.setDrawDataPoints(true);
        return series;
    }

    //should only be called after the above method is called
    public ArrayList<String> getNames(){
    	return this.names;
    }

    public int[] computeNumberStats(){
    	int[] stats = new int[3];
    	for(HistoryItem hi : scores){
            CoursesDO course = dbc.getCourse(hi.getCourseName());
            for (int i = 0; i < course.getPars().size(); i++){
                stats[0] += course.getPars().get(i) + hi.getScore(i);
                stats[1] += course.getDistances().get(i);
                stats[2] += hi.getScore(i);
            }
        }
        stats[1] = stats[1] / 5280;//convert from feet to miles
        return stats;
    }
}
