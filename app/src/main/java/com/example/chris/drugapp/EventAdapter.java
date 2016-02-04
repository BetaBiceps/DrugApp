package com.example.chris.drugapp;

/**
 * Created by Chris on 01/02/2016.
 */
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class EventAdapter extends ArrayAdapter<Event> {

    private static class ViewHolder{
        private TextView itemView1;
        private TextView itemView2;
        private TextView itemView3;
    }
    ViewHolder viewHolder;

    public EventAdapter(Context context, int id, ArrayList<Event> e) {
        super(context, R.layout.event_item, e);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.event_item,parent,false);

            viewHolder = new ViewHolder();
            viewHolder.itemView1 = (TextView) convertView.findViewById(R.id.txt_eventDate);
            viewHolder.itemView2 = (TextView) convertView.findViewById(R.id.txt_eventTime);
            viewHolder.itemView3 = (TextView) convertView.findViewById(R.id.txt_eventName);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Event item = getItem(position);
        if(item != null){
            viewHolder.itemView1.setText(item.dateTostring());
            viewHolder.itemView2.setText(String.format("%s", item.time));
            viewHolder.itemView3.setText(String.format("%s", item.msg));
        }

        return convertView;
    }

}