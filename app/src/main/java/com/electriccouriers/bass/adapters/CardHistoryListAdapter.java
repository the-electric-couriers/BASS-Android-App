package com.electriccouriers.bass.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.electriccouriers.bass.R;
import com.electriccouriers.bass.models.History;

import java.util.ArrayList;

public class CardHistoryListAdapter extends ArrayAdapter<History> {

    private ArrayList<History> dataSet;
    private Context context;

    public CardHistoryListAdapter(ArrayList<History> data, Context context) {
        super(context, R.layout.card_list_row, data);

        this.dataSet = data;
        this.context = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(R.layout.card_list_row, parent, false);
        }

        History historyItem = dataSet.get(position);

        TextView routePointTitle = convertView.findViewById(R.id.Card_StationName);
        routePointTitle.setText(historyItem.getRoutePointName());

        return convertView;
    }

}
