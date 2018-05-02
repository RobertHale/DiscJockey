package com.hale.robert.discjockey;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.amazonaws.models.nosql.UsersDO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddRemovePlayerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_remove_players);
        final EditText addPlayer = (EditText) findViewById(R.id.add_player_text);
        final EditText removePlayer = (EditText) findViewById(R.id.remove_player_text);
        final Button addButton = (Button) findViewById(R.id.add_player_button);
        final Button removeButton = (Button) findViewById(R.id.remove_player_button);
        final DBConnector dbc = new DBConnector();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = addPlayer.getText().toString();
                if(dbc.getItem(name) == null){
                    UsersDO newUser = new UsersDO();
                    newUser.setName(name);
                    newUser.setScorecards(new HashMap<String, ArrayList<ArrayList<Integer>>>());
                    dbc.saveUser(newUser);
                    showSnack("User Added!");
                }else{
                    showSnack("User already exists");
                }
            }
        });
        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = removePlayer.getText().toString();
                UsersDO user;
                if ((user = dbc.getItem(name)) != null){
                    dbc.removeUser(user);
                    showSnack(name + " deleted");
                }else{
                    showSnack("User does not exist");
                }
            }
        });
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
