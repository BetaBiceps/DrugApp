package com.example.chris.drugapp;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import hirondelle.date4j.DateTime;

/**
 * This is the main class for the overview page.
 *
 * The main function of this page is to display the total
 * usage of each substance, along with other user info.
 *
 * Created by Chris on 27/01/2016.
 */
public class Overview extends Activity {
    Resources res;
    ArrayList<HashMap<String, String>> drugsArray = new ArrayList<HashMap<String, String>>();
    Events events;
    ArrayList<Event> list;
    Calendar cal = Calendar.getInstance();
    Date date= new Date();

    String[] Months = {"Jan", "Feb", "Mar", "Apr", "May", "June",
            "July", "Aug", "Sept", "Nov", "Dec"};

    OverviewAdapter listAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview_layout);
        res = getResources();
        events = new Events(this);
        list = events.readAllEvents();

        populateTotals();

        listAdapter = new OverviewAdapter(drugsArray);
        listView = (ListView) findViewById(R.id.overviewListID);
        listView.setAdapter(listAdapter);

        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.overview_header, listView, false);
        listView.addHeaderView(header, null, false);

        TextView month1 = (TextView) header.findViewById(R.id.month1);
        TextView month2 = (TextView) header.findViewById(R.id.month2);
        TextView month3 = (TextView) header.findViewById(R.id.month3);

        month1.setText(Months[cal.get(Calendar.MONTH)-2]);
        month2.setText(Months[cal.get(Calendar.MONTH)-1]);
        month3.setText(Months[cal.get(Calendar.MONTH)]);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String drugPicked = String.valueOf(listView.getItemAtPosition(position));

                Toast.makeText(Overview.this, drugPicked, Toast.LENGTH_SHORT).show();
            }
        });

    }

    protected void populateTotals(){
        int total,total2, total3;
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        HashMap<String,String> map = new HashMap<String, String>();
        HashMap<String,String> map2 = new HashMap<String, String>();
        HashMap<String,String> map3 = new HashMap<String, String>();


        //Populate totals
        for(String key : res.getStringArray(R.array.drugs)){
            total = 0;total2 = 0;total3 = 0;
            for(Event e : list){
                cal.setTime(e.getDate());
                int m = cal.get(Calendar.MONTH);

                if(m == month){
                    if(e.getDrug().equals(key)){
                        total3 += e.getDose();
                    }
                }
                else if(m == month-1){
                    if(e.getDrug().equals(key)){
                        total2 += e.getDose();
                    }
                }
                else if(m == month-2){
                    if(e.getDrug().equals(key)){
                        total += e.getDose();
                    }
                }
            }

            map.put(key, Integer.toString(total));
            map2.put(key, Integer.toString(total2));
            map3.put(key, Integer.toString(total3));

        }
        drugsArray.add(map);
        drugsArray.add(map2);
        drugsArray.add(map3);

    }


}
