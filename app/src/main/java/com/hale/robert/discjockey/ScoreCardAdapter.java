package com.hale.robert.discjockey;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by robert on 3/20/2018.
 */

public class ScoreCardAdapter extends RecyclerView.Adapter<ScoreCardAdapter.HoleViewHolder> {
    private List<Hole> holes;
    public Context context;

    public ScoreCardAdapter(List<Hole> holes, Context context){
        this.holes = holes;
        this.context = context;
    }

    @Override
    public HoleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View HoleViewer = LayoutInflater.from(parent.getContext()).inflate(R.layout.hole_frag, parent, false);
        HoleViewHolder hvh = new HoleViewHolder(HoleViewer);
        return hvh;
    }

    @Override
    public void onBindViewHolder(HoleViewHolder holder, int position) {
        Hole h = holes.get(position);
        holder.holeView.setText(String.valueOf(h.getNumber()));
        holder.parView.setText(String.valueOf(h.getPar()));
        holder.distView.setText(String.valueOf(h.getDistance()));
        HoleAdapter ha = new HoleAdapter(context);
        ha.addAll(h.getUserScores());
        holder.scores.setAdapter(ha);
    }

    @Override
    public int getItemCount() {
        return holes.size();
    }

    public class HoleViewHolder extends RecyclerView.ViewHolder{
        TextView holeView;
        TextView parView;
        TextView distView;
        ListView scores;

        public HoleViewHolder(View itemView) {
            super(itemView);
            this.holeView = itemView.findViewById(R.id.hole_num);
            this.parView  = itemView.findViewById(R.id.par_num);
            this.distView = itemView.findViewById(R.id.distance_num);
            this.scores   = itemView.findViewById(R.id.hole_list);
        }
    }
}
