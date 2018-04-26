package com.hale.robert.discjockey;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.amazonaws.models.nosql.CoursesDO;
import com.amazonaws.models.nosql.UsersDO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderBoardActivity extends AppCompatActivity {

    Spinner courseSelection;
    ListView theBoard;
    DBConnector dbc;
    ArrayList<HistoryItem> leaders;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_leaderboard);
        courseSelection = (Spinner) findViewById(R.id.leader_title);
        theBoard = (ListView) findViewById(R.id.leader_list);
        dbc = new DBConnector();
        final ArrayList<String> courseNames = new ArrayList<>();
        leaders = new ArrayList<>();
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
        setLeaders(courseNames.get(0));
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.user_spinner_item, courseNames);
        adapter.setDropDownViewResource(R.layout.user_spinner_dropdown_item);
        courseSelection.setAdapter(adapter);
        final LeaderBoardAdapter boardAdapter = new LeaderBoardAdapter(getApplicationContext());
        boardAdapter.addAll(leaders);
        theBoard.setAdapter(boardAdapter);
        theBoard.setBackgroundColor(Color.GRAY);
        courseSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setLeaders(courseNames.get(position));
                boardAdapter.clear();
                boardAdapter.addAll(leaders);
                boardAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setLeaders(final String s) {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                leaders.clear();
                List<UsersDO> users = dbc.getAllUsers();
                for(UsersDO user : users){
                    if (user.getScorecards().containsKey(s)){
                        for (ArrayList<Integer> score : user.getScorecards().get(s)){
                            leaders.add(new HistoryItem(s, score, user.getName()));
                        }
                    }
                }
                Collections.sort(leaders);
                Log.d("rolipoli", "run: " + leaders);
            }
        });
        th.start();
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
