package com.example.chris.drugapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;


/**
 * This adapter is for the overview page, it controls the
 * custom row layout for each substance on the page.
 *
 * Created by Chris on 22/02/2016.
 */
public class OverviewAdapter extends BaseAdapter{

    private static class ViewHolder{
        private TextView itemView1;
        private TextView itemView2;
    }
    ViewHolder viewHolder;

    private final ArrayList mData;

    public OverviewAdapter(Map<String, String> map) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Map.Entry<String, String> getItem(int position) {
        return (Map.Entry) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO implement you own logic with ID
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.overview_row_layout,parent,false);

            viewHolder = new ViewHolder();
            viewHolder.itemView1 = (TextView) convertView.findViewById(R.id.drugName);
            viewHolder.itemView2 = (TextView) convertView.findViewById(R.id.drugTotal);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Map.Entry<String, String> item = getItem(position);


        if(item != null){
            viewHolder.itemView1.setText(String.format("%s", item.getKey()));
            viewHolder.itemView2.setText(String.format("%s", item.getValue()));
        }

        return convertView;

    }
}