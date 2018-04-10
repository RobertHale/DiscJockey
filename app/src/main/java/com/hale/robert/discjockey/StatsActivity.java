package com.hale.robert.discjockey;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;

public class StatsActivity extends AppCompatActivity {

    ArrayList<String> users;
    DBConnector dbc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_stats);
        Intent caller = getIntent();
        users = caller.getStringArrayListExtra("users");
        GraphView graph = (GraphView) findViewById(R.id.graph);
        Spinner spinner = (Spinner) findViewById(R.id.stats_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.user_spinner_item, users);
        adapter.setDropDownViewResource(R.layout.user_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        dbc = new DBConnector();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(new DataPoint[] {
                new DataPoint(-2, 1),
                new DataPoint(-1, 3),
                new DataPoint(0, 4),
                new DataPoint(1, 2),
                new DataPoint(2, 6)
        });
        graph.addSeries(series);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("score (compared to par)");
        graph.getGridLabelRenderer().setHighlightZeroLines(true);
        graph.setPadding(100, 100, 100, 100);
        graph.setTitle("Score distribution");
    }
}
