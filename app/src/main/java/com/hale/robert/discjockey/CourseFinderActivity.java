package com.hale.robert.discjockey;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by robert on 3/20/2018.
 */

public class CourseFinderActivity extends AppCompatActivity implements JsoupFetch.FetchCallback, CourseCreator.CreatorCallback {

    private EditText searchName;
    private ListView searchResults;
    private ArrayList<String> clickedUsers;
    private SearchResultCourse clikcedCourse;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_a_course);
        //initialize UI elements
        ListView userList = (ListView) findViewById(R.id.user_list);
        searchName = (EditText) findViewById(R.id.search_name);
        Button search = (Button) findViewById(R.id.find_a_button);
        searchResults = (ListView) findViewById(R.id.search_results);
        clickedUsers = new ArrayList<>();
        //set on click for button
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = searchName.getText().toString().toLowerCase();
                if(name.length() == 0){
                    showSnack("Please enter a name");
                    return;
                }
                new JsoupFetch(CourseFinderActivity.this, getApplicationContext(), "http://www.discasaurus.com/search/node/" + name);
            }
        });
        //add users to list
        Intent caller = getIntent();
        Bundle data = caller.getExtras();
        assert data != null;
        List<String> users = data.getStringArrayList("users");
        assert users != null;
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, users);
        userList.setAdapter(adapter);
        //set on click for users
        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String user = (String)adapterView.getItemAtPosition(i);
                CheckedTextView row = (CheckedTextView) view.findViewById(android.R.id.text1);
                if(row.isChecked()){
                    row.setChecked(false);
                    clickedUsers.remove(user);
                }else{
                    row.setChecked(true);
                    clickedUsers.add(user);
                }
            }
        });
        //set on click for search results
        searchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SearchResultCourse course = (SearchResultCourse) adapterView.getItemAtPosition(i);
                if(clickedUsers.size() > 0) {
                    Snackbar.make(view, "Sit back as we create your scorecard", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    clikcedCourse = course;
                    new CourseCreator(CourseFinderActivity.this, course.getUrl().toString());
                }else{
                    showSnack("Please select some Players to begin");
                }
            }
        });
    }

    private void setSearchResults(final ArrayList<SearchResultCourse> results) {
        final ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, results);
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        searchResults.setAdapter(adapter);
                    }
                });
            }
        });
        th.start();
    }

    private void showSnack(final String text) {
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(findViewById(android.R.id.content), text, Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
            }
        });
        th.start();
    }

    @Override
    public void fetchStart() {
    }

    @Override
    public void fetchComplete(Document d) {
        Elements tables = d.select("dl");
        if (tables.size() < 1){
            showSnack("Sorry, no results found...");
            return;
        }
        Element resultTable = tables.get(0);
        Elements results = resultTable.select("dt");
        ArrayList<SearchResultCourse> res = new ArrayList<>();
        for (Element result : results){
            Element link = result.selectFirst("a");
            String name = link.text();
            if (name.length() == 0 || name.contains("Hole") || name.contains("Score")){
                continue;
            }
            URL url;
            try {
                url = new URL(link.attr("abs:href"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
                continue;
            }
            res.add(new SearchResultCourse(name, url));
        }
        setSearchResults(res);
        for (SearchResultCourse result : res){
            Log.d("DiscJ", "fetchComplete: " + result.getName() + " : " + result.getUrl());
        }
    }

    @Override
    public void fetchFailed(IOException e) {
        e.printStackTrace();
    }

    @Override
    public void CreationComplete(ArrayList<Integer> pars, ArrayList<Integer> dist) {
        Log.d("DiscJ", "CreationComplete: Course");
        Intent intent = new Intent(CourseFinderActivity.this, ScoreCardActivity.class);
        Bundle data = new Bundle();
        data.putStringArrayList("users", clickedUsers);
        data.putString("course", clikcedCourse.getName());
        data.putInt("numHoles", pars.size());
        data.putIntegerArrayList("dist", dist);
        data.putIntegerArrayList("par", pars);
        intent.putExtras(data);
        startActivity(intent);
    }

    @Override
    public void CreationFailed() {

    }
}
