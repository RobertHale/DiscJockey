package com.hale.robert.discjockey;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class HistoryRecordActivity extends AppCompatActivity{

    private ArrayList<Integer> pars;
    private ArrayList<Integer> scores;
    private String courseName;
    private String userName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history_frag);
        ListView recordList = (ListView) findViewById(R.id.history_frag_list);
        Bundle b = getIntent().getExtras();
        assert b != null;
        pars = b.getIntegerArrayList("pars");
        scores = b.getIntegerArrayList("scores");
        courseName = b.getString("courseName");
        userName = b.getString("userName");
        ArrayList<ArrayList<Integer>> record = fuzeData(pars, scores);
        HistoryRecordAdapter historyRecordAdapter = new HistoryRecordAdapter(getApplicationContext());
        historyRecordAdapter.addAll(record);
        recordList.setAdapter(historyRecordAdapter);
        Snackbar.make(findViewById(android.R.id.content), "Green = good, Red = bad, Blue = meh", Snackbar.LENGTH_LONG).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.history_record_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.share_card) {
            String scoreText = makeSocialText();
            Intent textShareIntent = new Intent(Intent.ACTION_SEND);
            textShareIntent.putExtra(Intent.EXTRA_TEXT, scoreText);
            textShareIntent.setType("text/plain");
            startActivity(Intent.createChooser(textShareIntent, "Share score card with..."));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @NonNull
    private String makeSocialText() {
        StringBuilder buff = new StringBuilder();
        buff.append("Look at ")
                .append(userName)
                .append("'s scorecard for ")
                .append(courseName)
                .append("!\n")
                .append(scores)
                .append("\nFor a total score of: ");
        int sum = 0;
        for (int i : scores){
            sum += i;
        }
        buff.append(sum)
                .append("!");
        return buff.toString();
    }
}
