package com.hale.robert.discjockey;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import static android.content.ContentValues.TAG;

/**
 * Created by robert on 3/7/2018.
 */

public class HoleFragment extends Fragment {

    public Hole holeInfo;

    public static HoleFragment newInstance(Hole holeInfo){
        HoleFragment holeFragment = new HoleFragment();
        Bundle b = new Bundle();
        b.putParcelable("holeInfo", holeInfo);
        holeFragment.setArguments(b);
        return holeFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the root view and cache references to vital UI elements
        View v = inflater.inflate(R.layout.hole_frag, container, false);
        final TextView hole = v.findViewById(R.id.hole_num);
        final TextView par = v.findViewById(R.id.par_num);
        final TextView dist = v.findViewById(R.id.distance_num);
        final ListView scores = v.findViewById(R.id.hole_list);
        Bundle b = getArguments();
        Log.d(TAG, "onCreateView: start");
        if(b != null){
            Log.d(TAG, "onCreateView: getting parcel");
            holeInfo = (Hole) b.getParcelable("holeInfo");
        }
        if(holeInfo != null){
            hole.setText(String.valueOf(holeInfo.getNumber()));
            par.setText(String.valueOf(holeInfo.getPar()));
            dist.setText(String.valueOf(holeInfo.getDistance()));
            HoleAdapter ha = new HoleAdapter(getContext());
            Log.d(TAG, "onCreateView: " + holeInfo.getUserScores().get(0).user);
            ha.addAll(holeInfo.getUserScores());
            scores.setAdapter(ha);
        }
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
