package com.hale.robert.discjockey;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.amazonaws.models.nosql.UsersDO;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_history);
        final ListView historyList = findViewById(R.id.history_list);
        DBConnector dbc = new DBConnector();
        UsersDO user = dbc.getItem("test");
        ArrayList<HistoryItem> items = new ArrayList<>();
        for(String course : user.getScorecards().keySet()){
            for(List<Integer> scores : user.getScorecards().get(course)){
                HistoryItem hi = new HistoryItem();
                hi.setCourseName(course);
                int sum = 0;
                for(int s : scores){
                    sum += s;
                }
                hi.setScore(sum);
                items.add(hi);
            }
        }
        HistoryAdapter historyAdapter = new HistoryAdapter(getApplicationContext());
        historyAdapter.addAll(items);
        historyList.setAdapter(historyAdapter);
    }
}
