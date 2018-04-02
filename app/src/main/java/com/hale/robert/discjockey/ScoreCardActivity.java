package com.hale.robert.discjockey;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.amazonaws.models.nosql.CoursesDO;
import com.amazonaws.models.nosql.UsersDO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by robert on 3/20/2018.
 */

public class ScoreCardActivity extends AppCompatActivity {

    private RecyclerView rv;
    private ScoreCard sc;
    private ScoreCardAdapter scAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_scorecard);
        rv = (RecyclerView) findViewById(R.id.score_recycler);
        Intent caller = getIntent();
        Bundle data = caller.getExtras();
        final ArrayList<String> userNames = data.getStringArrayList("users");
        final String courseName = data.getString("course");
        final int numHoles = data.getInt("numHoles");
        ArrayList<Integer> pars = data.getIntegerArrayList("par");
        ArrayList<Integer> dist = data.getIntegerArrayList("dist");
        sc = new ScoreCard(numHoles, courseName);
        sc.setUsers(userNames);
        sc.setPars(pars);
        sc.setDistances(dist);
        rv.addItemDecoration(new DividerItemDecoration(ScoreCardActivity.this, LinearLayoutManager.HORIZONTAL));
        scAdapter = new ScoreCardAdapter(sc.getHoles(), getApplicationContext());
        final LinearLayoutManager HorzMan = new LinearLayoutManager(ScoreCardActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(HorzMan);
        rv.setAdapter(scAdapter);
        final PagerSnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(rv);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.scorecard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.finish) {
            saveScoreCard();
            Toast.makeText(getApplicationContext(), "Scorecard saved!", Toast.LENGTH_LONG)
                    .show();
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveScoreCard(){
        List<Hole> holes = sc.getHoles();
        HashMap<String, ArrayList<Integer>> scores = sc.getScoreMap();
        Log.d("scorecard", "saveScoreCard: " + scores);
        DBConnector dbc = new DBConnector();
        for (String name : scores.keySet()){
            UsersDO user = dbc.getItem(name);
            if (!user.getScorecards().containsKey(sc.getCourseName())){
                user.getScorecards().put(sc.getCourseName(), new ArrayList<ArrayList<Integer>>());
            }
            user.getScorecards().get(sc.getCourseName()).add(scores.get(name));
            dbc.saveUser(user);
        }
        if(dbc.getCourse(sc.getCourseName()) == null){
            CoursesDO course = new CoursesDO();
            course.setName(sc.getCourseName());
            course.setDistances(sc.getDists());
            course.setPars(sc.getPars());
            dbc.saveCourse(course);
        }
    }
}
