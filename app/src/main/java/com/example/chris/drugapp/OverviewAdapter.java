package com.example.chris.drugapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by Chris on 22/02/2016.
 */
public class OverviewAdapter extends ArrayAdapter<HashMap<String,Integer>>{

    private static class ViewHolder{
        private TextView itemView1;
        private TextView itemView2;
    }
    ViewHolder viewHolder;

    public OverviewAdapter(Context context, HashMap<String,Integer> values) {
        super(context, R.layout.overview_row_layout);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(this.getContext())
                    .inflate(R.layout.overview_row_layout,parent,false);

            viewHolder = new ViewHolder();
            viewHolder.itemView1 = (TextView) convertView.findViewById(R.id.drugName);
            viewHolder.itemView2 = (TextView) convertView.findViewById(R.id.drugTotal);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        HashMap<String, Integer> item = new HashMap<String, Integer>();
        item = getItem(position);

        Set s = item.entrySet();
        Collection c = item.values();
        Iterator itrC = c.iterator(), itrS = s.iterator();


        if(item != null){
            viewHolder.itemView1.setText(String.format("hello %s", itrS.next()));
            viewHolder.itemView2.setText(String.format("%s", itrC.next()));
        }

        return convertView;

    }
}
