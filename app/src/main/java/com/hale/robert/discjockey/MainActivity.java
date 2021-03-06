package com.hale.robert.discjockey;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.models.nosql.UsersDO;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int COURSE_FINDER_ACT = 999;
    private static final int ADD_REMOVE_ACT = 998;
    ArrayList<String> users;
    private ArrayList<String> clickedUsers;
    private ListView userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AWSMobileClient.getInstance().initialize(this).execute();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //get users from db
        users = new ArrayList<>();
        final DBConnector dbc = new DBConnector();
        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                List<UsersDO> userItems = dbc.getAllUsers();
                for (UsersDO user : userItems){
                    Log.d("database", "user: " + user.getName());
                    users.add(user.getName());
                }
            }
        });
        th.start();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        userList = (ListView) findViewById(R.id.main_users);
        final EditText numHoles = (EditText) findViewById(R.id.num_holes);
        final EditText courseName = (EditText) findViewById(R.id.course_name);
        final Button create = (Button) findViewById(R.id.create_card_button);
        clickedUsers = new ArrayList<>();
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numHoles.getText().length() == 0){
                    showSnack("Please specify number of holes");
                    return;
                }
                if (courseName.getText().toString().equals("")){
                    showSnack("please give a course name");
                    return;
                }
                if (clickedUsers.size() > 0) {
                    int holeCount = Integer.parseInt(numHoles.getText().toString());
                    String course = courseName.getText().toString();
                    ArrayList<Integer> dist = new ArrayList<>(holeCount);
                    ArrayList<Integer> par = new ArrayList<>(holeCount);
                    Intent intent = new Intent(MainActivity.this, ScoreCardActivity.class);
                    Bundle data = new Bundle();
                    data.putStringArrayList("users", clickedUsers);
                    data.putString("course", course);
                    data.putInt("numHoles", holeCount);
                    data.putIntegerArrayList("dist", dist);
                    data.putIntegerArrayList("par", par);
                    intent.putExtras(data);
                    startActivity(intent);
                }else{
                    showSnack("Please add users to create course.");
                }
            }
        });
        try {
            th.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.add_user_option){
            startActivityForResult(new Intent(MainActivity.this, AddRemovePlayerActivity.class), ADD_REMOVE_ACT);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_scorecard) {

        } else if (id == R.id.nav_a_course){
            Intent intent = new Intent(MainActivity.this, CourseFinderActivity.class);
            intent.putExtra("users", users);
            startActivityForResult(intent, COURSE_FINDER_ACT);
        } else if (id == R.id.nav_history) {
            Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
            intent.putStringArrayListExtra("users", users);
            startActivityForResult(intent, 500);
        } else if (id == R.id.nav_stats) {
            Intent intent = new Intent(MainActivity.this, StatsActivity.class);
            intent.putStringArrayListExtra("users", users);
            startActivityForResult(intent, 501);
        } else if (id == R.id.nav_friends) {
            Intent intent = new Intent(MainActivity.this, LeaderBoardActivity.class);
            startActivityForResult(intent, 502);
        } else if (id == R.id.nav_share) {
            Intent intent = new Intent(MainActivity.this, AboutActivity.class);
            startActivityForResult(intent, 503);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_REMOVE_ACT){
            users.clear();
            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    DBConnector dbc = new DBConnector();
                    List<UsersDO> userItems = dbc.getAllUsers();
                    for (UsersDO user : userItems){
                        Log.d("database", "user: " + user.getName());
                        users.add(user.getName());
                    }
                }
            });
            th.start();
            try {
                th.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked, users);
            userList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
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
}
