package com.hale.robert.discjockey;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.amazonaws.models.nosql.CoursesDO;
import com.amazonaws.models.nosql.UsersDO;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.OnDataPointTapListener;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StatsActivity extends AppCompatActivity {

    ArrayList<String> users;
    ArrayList<HistoryItem> scores;
    DBConnector dbc;
    Spinner comparisonSpinner;
    ArrayAdapter<String> comparisonAdapter;
    GraphView avgPerHole;
    GraphView avgPerCourse;
    GraphView comparisonGraph;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_stats);
        Intent caller = getIntent();
        users = caller.getStringArrayListExtra("users");
        avgPerHole = (GraphView) findViewById(R.id.avg_per_hole_score);
        avgPerCourse = (GraphView) findViewById(R.id.avg_per_course_score);
        comparisonGraph = (GraphView) findViewById(R.id.course_comparison_graph);
        Spinner spinner = (Spinner) findViewById(R.id.stats_spinner);
        comparisonSpinner = (Spinner) findViewById(R.id.comparison_spinner);
        comparisonAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.user_spinner_item);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.user_spinner_item, users);
        comparisonAdapter.setDropDownViewResource(R.layout.user_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.user_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        dbc = new DBConnector();
        avgPerHole.setTitle("Average score per hole compared to par");
        avgPerCourse.setTitle("Average score per course compared to par");
        switchUsers(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switchUsers(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        comparisonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setCourseComparison(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setAvgPerCourseScore() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SparseIntArray relScores = new SparseIntArray();
                for(HistoryItem item : scores){
                        relScores.put(item.getTotalScore(), relScores.get(item.getTotalScore()) + 1);
                }
                final LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
                for (int i = 0; i < relScores.size(); i++){
                    series.appendData(new DataPoint(relScores.keyAt(i), relScores.valueAt(i)), true, 36);
                }
                series.setDrawDataPoints(true);
                series.setOnDataPointTapListener(new OnDataPointTapListener() {
                    @Override
                    public void onTap(Series series, DataPointInterface dataPoint) {
                        Toast.makeText(getApplicationContext(), "Score: " + dataPoint.getX() + "\nCount: " + dataPoint.getY(), Toast.LENGTH_SHORT).show();
                    }
                });
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        avgPerCourse.removeAllSeries();
                        avgPerCourse.addSeries(series);
                        avgPerCourse.getViewport().setMinY(0);
                    }
                });
            }
        }).start();
    }

    public void setAvgPerHoleScore(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                SparseIntArray relScores = new SparseIntArray();
                for(HistoryItem item : scores){
                    for(int score : item.getScores()){
                        relScores.put(score, relScores.get(score) + 1);
                    }
                }
                final LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>();
                for (int i = 0; i < relScores.size(); i++){
                    series.appendData(new DataPoint(relScores.keyAt(i), relScores.valueAt(i)), true, 36);
                }
                series.setDrawDataPoints(true);
                series.setOnDataPointTapListener(new OnDataPointTapListener() {
                    @Override
                    public void onTap(Series series, DataPointInterface dataPoint) {
                        Toast.makeText(getApplicationContext(), "Score: " + dataPoint.getX() + "\nCount: " + dataPoint.getY(), Toast.LENGTH_SHORT).show();
                    }
                });
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        avgPerHole.removeAllSeries();
                        avgPerHole.addSeries(series);
                    }
                });
            }
        }).start();
    }

    public void setCourseComparison(final int pos){
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Integer> coursePar = new ArrayList<>();
                CoursesDO course = dbc.getCourse(comparisonAdapter.getItem(pos));
                final LineGraphSeries<DataPoint> parSeries = new LineGraphSeries<DataPoint>();
                parSeries.setDrawDataPoints(true);
                parSeries.setColor(R.color.blue);
                parSeries.setTitle("Course Par");
                for(int i = 0; i < course.getPars().size(); i++){
                    Log.d("Stats", "parSeries: " + course.getPars().get(i));
                    parSeries.appendData(new DataPoint(i, course.getPars().get(i)), true, course.getPars().size());
                }
                final ArrayList<LineGraphSeries<DataPoint>> userSeries = new ArrayList<>();
                for (HistoryItem item : scores){
                    if(item.getCourseName().equals(course.getName())){
                        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
                        for(int i = 0; i < item.getScores().size(); i++){
                            Log.d("Stats", "userSeries: " + (item.getScores().get(i) + course.getPars().get(i)));
                            series.appendData(new DataPoint(i, item.getScores().get(i) + course.getPars().get(i)), true, item.getScores().size());
                        }
                        series.setDrawDataPoints(true);
                        series.setColor(R.color.red);
                        userSeries.add(series);
                    }
                }
                userSeries.get(0).setTitle("User Scores");
                parSeries.setOnDataPointTapListener(new OnDataPointTapListener() {
                    @Override
                    public void onTap(Series series, DataPointInterface dataPoint) {
                        Toast.makeText(getApplicationContext(), "Hole: " + dataPoint.getX() + "\nPar: " + dataPoint.getY(), Toast.LENGTH_SHORT).show();
                    }
                });
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        comparisonGraph.removeAllSeries();
                        comparisonGraph.addSeries(parSeries);
                        for(LineGraphSeries<DataPoint> series : userSeries){
                            comparisonGraph.addSeries(series);
                        }
                        comparisonGraph.getLegendRenderer().setVisible(true);
                        comparisonGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
                    }
                });
            }
        }).start();
    }

    public void setHistory(String name){
        UsersDO user = dbc.getItem(name);
        scores = new ArrayList<>();
        for(String course : user.getScorecards().keySet()){
            for(List<Integer> score : user.getScorecards().get(course)){
                scores.add(new HistoryItem(course, score));
            }
        }
    }

    public void switchUsers(int pos){
        setHistory(users.get(pos));
        ArrayList<String> itemNames = new ArrayList<>();
        for (int i = 0; i < scores.size(); i++) {
            if(!itemNames.contains(scores.get(i).getCourseName())) {
                itemNames.add(scores.get(i).getCourseName());
            }
        }
        comparisonAdapter.clear();
        comparisonAdapter.addAll(itemNames);
        comparisonSpinner.setAdapter(comparisonAdapter);
        setAvgPerHoleScore();
        setAvgPerCourseScore();
        setCourseComparison(0);
    }
}
