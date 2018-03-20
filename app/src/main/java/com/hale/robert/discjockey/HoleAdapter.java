package com.hale.robert.discjockey;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

/**
 * Created by robert on 3/19/2018.
 */

public class HoleAdapter extends ArrayAdapter<Hole.UserScore> {
    private LayoutInflater theInflater = null;

    public HoleAdapter(Context context){
        super(context, R.layout.hole_row);

        theInflater = LayoutInflater.from(getContext());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = theInflater.inflate(R.layout.hole_row, parent, false);
        }
        Log.d(TAG, "getView: start");
        convertView = BindView(position, convertView);
        Log.d(TAG, "getView: end");
        return convertView;
    }

    private View BindView(int position, View theView) {
        // We retrieve the text from the array
        final Hole.UserScore us = getItem(position);
        TextView name = (TextView) theView.findViewById(R.id.hole_row_name);
        final TextView score = (TextView) theView.findViewById(R.id.hole_row_score);
        ImageButton scoreDown = (ImageButton) theView.findViewById(R.id.hole_row_down);
        ImageButton scoreUp = (ImageButton) theView.findViewById(R.id.hole_row_up);
        name.setText(us.user);
        score.setText(String.valueOf(us.score));
        scoreDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(us.score > 0) {
                    us.score -= 1;
                    score.setText(String.valueOf(us.score));
                }
            }
        });
        scoreUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                us.score += 1;
                score.setText(String.valueOf(us.score));
            }
        });
        return theView;
    }

}
