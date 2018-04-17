package com.hale.robert.discjockey;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import static android.content.ContentValues.TAG;

public class HistoryAdapter extends ArrayAdapter<HistoryItem> {
    private LayoutInflater theInflater = null;

    public HistoryAdapter(Context context){
        super(context, R.layout.history_row);

        theInflater = LayoutInflater.from(getContext());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = theInflater.inflate(R.layout.history_row, parent, false);
        }
        Log.d(TAG, "getView: start");
        convertView = BindView(position, convertView);
        Log.d(TAG, "getView: end");
        return convertView;
    }

    private View BindView(int position, View theView) {
        // We retrieve the text from the array
        final HistoryItem hi = getItem(position);
        TextView name = (TextView) theView.findViewById(R.id.history_name);
        TextView score = (TextView) theView.findViewById(R.id.history_score);
        assert hi != null;
        assert name != null;
        Log.d("history", "BindView: " + name.getText());
        Log.d("history", "BindView: " + hi.getCourseName() + " " + hi.getTotalScore());
        name.setText(hi.getCourseName());
        score.setText(String.valueOf(hi.getTotalScore()));
        return theView;
    }
}
