package com.hale.robert.discjockey;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;

public class HistoryAdapter extends ArrayAdapter<HistoryItem> {
    private LayoutInflater theInflater = null;

    public HistoryAdapter(Context context){
        super(context, R.layout.history_row);

        theInflater = LayoutInflater.from(getContext());
    }
}
