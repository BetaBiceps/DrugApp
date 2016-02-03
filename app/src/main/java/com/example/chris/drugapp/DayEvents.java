package com.example.chris.drugapp;

/**
 * Created by Chris on 01/02/2016.
 */
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Date;





public class DayEvents {
    Context context;
    Date date;
    ArrayList<Event> events;


    public DayEvents(Context c,Date d, ArrayList<Event> e){
        context = c;
        date = d;
        events = e;
    }

    public void getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);



    }

}