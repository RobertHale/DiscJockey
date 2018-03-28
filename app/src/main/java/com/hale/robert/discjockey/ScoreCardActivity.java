package com.hale.robert.discjockey;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

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
        ArrayList<User> users = new ArrayList<>();
        for (int i = 0; i < userNames.size(); i++){
            users.add(new User(userNames.get(i), 0));
        }
        sc = new ScoreCard(numHoles, courseName);
        sc.setUsers(users);
        sc.setPars(pars);
        sc.setDistances(dist);
        rv.addItemDecoration(new DividerItemDecoration(ScoreCardActivity.this, LinearLayoutManager.HORIZONTAL));
        scAdapter = new ScoreCardAdapter(sc.getHoles(), getApplicationContext());
        final LinearLayoutManager HorzMan = new LinearLayoutManager(ScoreCardActivity.this, LinearLayoutManager.HORIZONTAL, false);
        rv.setLayoutManager(HorzMan);
        rv.setAdapter(scAdapter);
        final PagerSnapHelper helper = new PagerSnapHelper();
        helper.attachToRecyclerView(rv);
//        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                if (newState == rv.SCROLL_STATE_SETTLING){
//                    HorzMan.findLastVisibleItemPosition();
//                    rv.cen
//                }
//            }
//        });
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

    }
}
