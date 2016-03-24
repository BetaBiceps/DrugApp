package com.example.chris.drugapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
        private TextView itemView3;
        private TextView itemView4;
    }
    ViewHolder viewHolder;

    private final ArrayList mData;
    private final ArrayList mData2;
    private final ArrayList mData3;

    public OverviewAdapter(ArrayList<HashMap<String, String>> list) {
        mData = new ArrayList();
        mData2 = new ArrayList();
        mData3 = new ArrayList();
        try {
            mData.addAll(list.get(0).entrySet());
            mData2.addAll(list.get(1).entrySet());
            mData3.addAll(list.get(2).entrySet());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Map.Entry<String, String> getItem(int position) {
        return (Map.Entry) mData.get(position);
    }
    public Map.Entry<String, String> getItem2(int position) {
        return (Map.Entry) mData2.get(position);
    }
    public Map.Entry<String, String> getItem3(int position) {
        return (Map.Entry) mData3.get(position);
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
            viewHolder.itemView3 = (TextView) convertView.findViewById(R.id.drugTotal2);
            viewHolder.itemView4 = (TextView) convertView.findViewById(R.id.drugTotal3);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Map.Entry<String, String> item = getItem(position);
        Map.Entry<String, String> item2 = getItem2(position);
        Map.Entry<String, String> item3 = getItem3(position);


        if(item != null){
            viewHolder.itemView1.setText(String.format("%s", item.getKey()));
            viewHolder.itemView2.setText(String.format("%s", item.getValue()));
        }
        if(item2 != null){
            viewHolder.itemView3.setText(String.format("%s", item2.getValue()));
        }
        if(item3 != null){
            viewHolder.itemView4.setText(String.format("%s", item3.getValue()));
        }

        return convertView;

    }
}