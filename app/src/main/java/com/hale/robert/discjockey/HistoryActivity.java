package com.hale.robert.discjockey;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.amazonaws.models.nosql.UsersDO;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    ArrayList<String> users;
    DBConnector dbc;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_history);
        Intent caller = getIntent();
        users = caller.getStringArrayListExtra("users");
        final ListView historyList = findViewById(R.id.history_list);
        Spinner spinner = (Spinner) findViewById(R.id.user_spinner);
        Log.d("history", "onCreate: " + users);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.user_spinner_item, users);
        adapter.setDropDownViewResource(R.layout.user_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        dbc = new DBConnector();
        ArrayList<HistoryItem> items = getHistory(users.get(0));
        final HistoryAdapter historyAdapter = new HistoryAdapter(getApplicationContext());
        historyAdapter.addAll(items);
        historyList.setAdapter(historyAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<HistoryItem> items = getHistory(users.get(position));
                historyAdapter.clear();
                historyAdapter.addAll(items);
                historyAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("history", "onItemClick: adding frag");
                HistoryItem hi = historyAdapter.getItem(position);
                assert hi != null;
                ArrayList<Integer> pars = (ArrayList<Integer>) dbc.getCourse(hi.getCourseName()).getPars();
                Intent intent = new Intent(HistoryActivity.this, HistoryRecordActivity.class);
                Bundle b = new Bundle();
                b.putIntegerArrayList("pars", pars);
                b.putIntegerArrayList("scores", (ArrayList<Integer>) hi.getScores());
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    public ArrayList<HistoryItem> getHistory(String name){
        UsersDO user = dbc.getItem(name);
        ArrayList<HistoryItem> items = new ArrayList<>();
        for(String course : user.getScorecards().keySet()){
            for(List<Integer> scores : user.getScorecards().get(course)){
                items.add(new HistoryItem(course, scores));
            }
        }
        return items;
    }
}
