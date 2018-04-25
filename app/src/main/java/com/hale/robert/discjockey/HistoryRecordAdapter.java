package com.hale.robert.discjockey;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class HistoryRecordAdapter extends ArrayAdapter<ArrayList<Integer>> {
    private LayoutInflater theInflater = null;

    public HistoryRecordAdapter(Context context){
        super(context, R.layout.history_row);
        theInflater = LayoutInflater.from(getContext());
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = theInflater.inflate(R.layout.history_record_row, parent, false);
        }
        convertView = BindView(position, convertView);
        return convertView;
    }

    private View BindView(int position, View theView) {
        Log.d("historyRecord", "BindView: adding score: " + position);
        final ArrayList<Integer> data = getItem(position);
        TextView hole = (TextView) theView.findViewById(R.id.history_record_hole_text);
        TextView score = (TextView) theView.findViewById(R.id.history_record_score_text);
        TextView par = (TextView) theView.findViewById(R.id.history_record_par_text);
        assert data != null;
        hole.setText(String.valueOf(position+1));
        score.setText(String.valueOf(data.get(0)));
        par.setText(String.valueOf(data.get(1)));
        if (data.get(0) < 0){
            score.setBackgroundColor(ResourcesCompat.getColor(getContext().getResources(), R.color.green, null));
        }else if (data.get(0) > 0){
            score.setBackgroundColor(ResourcesCompat.getColor(getContext().getResources(), R.color.red, null));
        }else{
            score.setBackgroundColor(ResourcesCompat.getColor(getContext().getResources(), R.color.blue, null));
        }
        return theView;
    }
}
