package com.hale.robert.discjockey;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.amazonaws.models.nosql.CoursesDO;

import java.util.ArrayList;
import java.util.List;

public class LeaderBoardActivity extends AppCompatActivity {

    Spinner courseSelection;
    ListView theBoard;
    DBConnector dbc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_leaderboard);
        courseSelection = (Spinner) findViewById(R.id.leader_title);
        theBoard = (ListView) findViewById(R.id.leader_list);
        dbc = new DBConnector();
        final ArrayList<String> courseNames = new ArrayList<>();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                for(CoursesDO course : dbc.getAllCourses()){
                    courseNames.add(course.getName());
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.user_spinner_item, courseNames);
        adapter.setDropDownViewResource(R.layout.user_spinner_dropdown_item);
        courseSelection.setAdapter(adapter);
        ArrayAdapter<String> boardAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, courseNames);
        theBoard.setAdapter(boardAdapter);
        theBoard.setBackgroundColor(Color.GRAY);
    }
}
