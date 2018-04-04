package com.hale.robert.discjockey;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class UserAdapter extends ArrayAdapter<String> {
    private LayoutInflater theInflater = null;
    private ArrayList<String> clickedUsers;

    public UserAdapter(Context context, ArrayList<String> clickedUsers){
        super(context, android.R.layout.simple_list_item_checked);
        theInflater = LayoutInflater.from(getContext());
        this.clickedUsers = clickedUsers;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = theInflater.inflate(android.R.layout.simple_list_item_checked, parent, false);
        }
        Log.d(TAG, "getView: start");
        convertView = BindView(position, convertView);
        Log.d(TAG, "getView: end");
        return convertView;
    }

    private View BindView(int position, View theView) {
        // We retrieve the text from the array
        final String item = getItem(position);
        CheckedTextView name = (CheckedTextView) theView.findViewById(android.R.id.text1);
        Log.d(TAG, "BindView: " + item);
        name.setText(item);
        name.setChecked(clickedUsers.contains(item));
        return theView;
    }
}
