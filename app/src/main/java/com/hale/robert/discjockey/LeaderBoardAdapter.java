package com.hale.robert.discjockey;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class LeaderBoardAdapter extends ArrayAdapter<HistoryItem> {
    private LayoutInflater theInflater = null;

    public LeaderBoardAdapter(Context context){
        super(context, R.layout.leaderboard_row);
        theInflater = LayoutInflater.from(getContext());
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = theInflater.inflate(R.layout.leaderboard_row, parent, false);
        }
        convertView = BindView(position, convertView);
        return convertView;
    }

    private View BindView(int position, View theView) {
        Log.d("historyRecord", "BindView: adding score: " + getItem(position));
        final HistoryItem data = getItem(position);
        TextView rank = (TextView) theView.findViewById(R.id.leader_rank);
        TextView name = (TextView) theView.findViewById(R.id.leader_name);
        TextView score = (TextView) theView.findViewById(R.id.leader_score);
        assert data != null;
        rank.setText(String.valueOf(position + 1));
        name.setText(data.getUserName());
        score.setText(String.valueOf(data.getTotalScore()));
        if (position == 0){
            rank.setBackground(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.rounded_corner_gold, null));
        }else if (position == 1){
            rank.setBackground(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.rounded_corner_silver, null));
        }else if (position == 2){
            rank.setBackground(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.rounded_corner_bronze, null));
        }else{
            rank.setBackground(ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.rounded_corner_primary_dark, null));
        }
        return theView;
    }
}
