package com.hale.robert.discjockey;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoryRecordActivity extends AppCompatActivity{

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_frag);
        ListView recordList = (ListView) findViewById(R.id.history_frag_list);
        Bundle b = getIntent().getExtras();
        assert b != null;
        ArrayList<Integer> pars = b.getIntegerArrayList("pars");
        ArrayList<Integer> scores = b.getIntegerArrayList("scores");
        ArrayList<ArrayList<Integer>> record = fuzeData(pars, scores);
        HistoryRecordAdapter historyRecordAdapter = new HistoryRecordAdapter(getApplicationContext());
        historyRecordAdapter.addAll(record);
        recordList.setAdapter(historyRecordAdapter);
    }

    private ArrayList<ArrayList<Integer>> fuzeData(ArrayList<Integer> pars, ArrayList<Integer> scores) {
        ArrayList<ArrayList<Integer>> res = new ArrayList<>();
        for (int i = 0; i < pars.size(); i++){
            ArrayList<Integer> part = new ArrayList<>();
            part.add(scores.get(i));
            part.add(pars.get(i));
            res.add(part);
        }
        return res;
    }
}
