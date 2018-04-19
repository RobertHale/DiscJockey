package com.hale.robert.discjockey;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.models.nosql.CoursesDO;
import com.amazonaws.models.nosql.UsersDO;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.BarGraphSeries;
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
    GraphView distPerCourse;
    TextView totalThrows;
    TextView distanceTraveled;
    TextView totalScore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_stats);
        Intent caller = getIntent();
        users = caller.getStringArrayListExtra("users");
        avgPerHole = (GraphView) findViewById(R.id.avg_per_hole_score);
        avgPerCourse = (GraphView) findViewById(R.id.avg_per_course_score);
        comparisonGraph = (GraphView) findViewById(R.id.course_comparison_graph);
        distPerCourse = (GraphView) findViewById(R.id.dist_per_course_graph);
        totalThrows = (TextView) findViewById(R.id.total_throws_number);
        distanceTraveled = (TextView) findViewById(R.id.total_dist_number);
        totalScore = (TextView) findViewById(R.id.total_score_number);
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
        comparisonGraph.setTitle("Score per hole compared to par");
        distPerCourse.setTitle("Total distance traveled per course");
        distPerCourse.getGridLabelRenderer().setVerticalAxisTitle("Feet");
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
                final BarGraphSeries<DataPoint> series = new BarGraphSeries<>();
                for (int i = 0; i < relScores.size(); i++){
                    series.appendData(new DataPoint(relScores.keyAt(i), relScores.valueAt(i)), true, 36);
                }
                series.setSpacing(15);
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
                series.setAnimated(true);
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
                final CoursesDO course = dbc.getCourse(comparisonAdapter.getItem(pos));
                final LineGraphSeries<DataPoint> parSeries = new LineGraphSeries<DataPoint>();
                parSeries.setDrawDataPoints(true);
                parSeries.setColor(Color.BLUE);
                parSeries.setTitle("Course Par");
                parSeries.setAnimated(true);
                parSeries.setBackgroundColor(Color.argb(100,0,0,205));
                parSeries.setDrawBackground(true);
                for(int i = 0; i < course.getPars().size(); i++){
                    parSeries.appendData(new DataPoint(i+1, course.getPars().get(i)), true, 36);
                }
                Log.d("new issue", "par: " + parSeries.getHighestValueX());
                final ArrayList<LineGraphSeries<DataPoint>> userSeries = new ArrayList<>();
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
                        userSeries.add(series);
                        Log.d("new issue", "user: " + series.getHighestValueX());
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
                        for(LineGraphSeries<DataPoint> series : userSeries){
                            comparisonGraph.addSeries(series);
                        }
                        comparisonGraph.addSeries(parSeries);
                        comparisonGraph.getLegendRenderer().setVisible(true);
                        comparisonGraph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
                        comparisonGraph.getViewport().setMinY(0);
                        comparisonGraph.getViewport().setXAxisBoundsManual(true);
                        comparisonGraph.getViewport().setMinX(1);
                        comparisonGraph.getViewport().setMaxX(course.getPars().size());
                    }
                });
            }
        }).start();
    }

    public void setDistPerCourse(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
                HashMap<String, Integer> count = new HashMap<>();
                for(HistoryItem hi : scores){
                    if (count.containsKey(hi.getCourseName())){
                        count.put(hi.getCourseName(), count.get(hi.getCourseName()) + 1);
                    }else{
                        count.put(hi.getCourseName(), 1);
                    }
                }
                final ArrayList<Integer> totalDist = new ArrayList<>();
                final ArrayList<String>  names      = new ArrayList<>();
                for(String name : count.keySet()){
                    names.add(name);
                    CoursesDO course = dbc.getCourse(name);
                    int sum = 0;
                    for(int x : course.getDistances()) sum += x;
                    sum *= count.get(name);
                    totalDist.add(sum);
                }
                for (int i = 0; i < totalDist.size(); i++){
                    series.appendData(new DataPoint(i, totalDist.get(i)), true, 36);
                }
                series.setDrawDataPoints(true);
                distPerCourse.getGridLabelRenderer().setLabelFormatter(new LabelFormatter() {
                    @Override
                    public String formatLabel(double value, boolean isValueX) {
                        if (isValueX) {
                            if(value % 1 == 0 && value >= 0 && value < names.size()) {
                                return names.get((int) value);
                            }else{
                                return "";
                            }
                        } else {
                            return Double.toString(value);
                        }
                    }

                    @Override
                    public void setViewport(Viewport viewport) {

                    }
                });
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        distPerCourse.removeAllSeries();
                        distPerCourse.addSeries(series);
                        distPerCourse.getViewport().setXAxisBoundsManual(true);
                        distPerCourse.getViewport().setMinX(0);
                        distPerCourse.getViewport().setMaxX(1);
                        distPerCourse.getViewport().setMinY(0);
                        distPerCourse.getViewport().setScrollable(true);
                        distPerCourse.getViewport().setScalable(true);
                    }
                });
            }
        }).start();
    }

    public void setNumberStats(){
        int throwSum = 0;
        int distSum = 0;
        int scoreSum = 0;
        for(HistoryItem hi : scores){
            CoursesDO course = dbc.getCourse(hi.getCourseName());
            for (int i = 0; i < course.getPars().size(); i++){
                throwSum += course.getPars().get(i) + hi.getScore(i);
                distSum += course.getDistances().get(i);
                scoreSum += hi.getScore(i);
            }
        }
        final int totalThrow = throwSum;
        final int totalDist = distSum / 5280;//convert from feet to miles
        final int totalScores = scoreSum;
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("stats", "run: added score");
                        totalThrows.setText(String.valueOf(totalThrow));
                        distanceTraveled.setText(String.valueOf(totalDist));
                        totalScore.setText(String.valueOf(totalScores));
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
        if(scores.size() > 0) {
            setAvgPerHoleScore();
            setAvgPerCourseScore();
            setCourseComparison(0);
            setDistPerCourse();
            setNumberStats();
        }else{
            avgPerHole.removeAllSeries();
            avgPerCourse.removeAllSeries();
            comparisonGraph.removeAllSeries();
            distPerCourse.removeAllSeries();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            totalThrows.setText(String.valueOf(0));
                            distanceTraveled.setText(String.valueOf(0));
                            totalScore.setText(String.valueOf(0));
                            Snackbar.make(findViewById(android.R.id.content), "No data for this user...", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    });
                }
            }).start();
        }
    }
}
