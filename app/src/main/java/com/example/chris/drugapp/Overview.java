package com.example.chris.drugapp;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import java.util.HashMap;


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

    final String[] Months = {"Jan", "Feb", "Mar", "Apr", "May", "June",
            "July", "Aug", "Sept", "Nov", "Dec"};
    final String[] stringsOfDrugs = {"Meth", "GHB", "Marijuana", "Cocaine",
            "MDMA", "Heroin", "Ketamine"};

    OverviewAdapter listAdapter;
    ListView listView;
    GraphView graphView;
    BarGraphSeries<DataPoint> series;
    LineGraphSeries<DataPoint> seriesLine;
    DataPoint[] dataPoints;

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

        //TODO Fix this to be the correct months without array out of bounds exception
        cal.setTime(date);
        month1.setText(Months[cal.get(Calendar.MONTH) -2]);
        month2.setText(Months[cal.get(Calendar.MONTH) -1]);
        month3.setText(Months[cal.get(Calendar.MONTH)]);


        graphView = (GraphView) findViewById(R.id.graph);
        fillPage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        populateTotals();
        fillPage();
    }

    /**
     * Fills the page with data/graphs
     */
    protected  void fillPage(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String drugPicked = stringsOfDrugs[position-1];

                cal.setTime(date);
                int val1 = getTotal(drugPicked, cal.get(Calendar.MONTH));
                int val2 = getTotal(drugPicked, cal.get(Calendar.MONTH) - 1);
                int val3 = getTotal(drugPicked, cal.get(Calendar.MONTH) - 2);

                graphView.removeAllSeries();

                //BAR GRAPH
                series = new BarGraphSeries<DataPoint>(new DataPoint[]{
                        new DataPoint(0, val3),
                        new DataPoint(1, val2),
                        new DataPoint(2, val1)
                });


                //LINE GRAPH
                dataPoints = getDataPoints(drugPicked, cal.get(Calendar.MONTH));
                seriesLine = new LineGraphSeries<DataPoint>(dataPoints);
                seriesLine.setColor(Color.RED);

                graphView.addSeries(series);
                graphView.addSeries(seriesLine);

                graphView.invalidate();
            }
        });
    }

    /**
     * Populates the totals for the overview page. Per month
     */
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

    /**
     * Returns the total of a given drug useage in a month
     * @param drug the drug to get totaled
     * @param month the month to confine to
     */
    public int getTotal(String drug, int month){
        int total = 0;
        for(Event e : list){
            cal.setTime(e.getDate());
            int m = cal.get(Calendar.MONTH);

            if(m == month){
                if(e.getDrug().equals(drug)){
                    total += e.getDose();
                }
            }
        }
        return total;
    }

    /**
     * returns the data points for the past 3 months for a line graph
     * @param drug the drug to find
     * @param month month to lookat
     * @return data the datapoints
     */
    public DataPoint[] getDataPoints(String drug, int month){
        ArrayList<DataPoint> data = new ArrayList<DataPoint>();
        int i=0;

        for(Event e : list){
            cal.setTime(e.getDate());
            int m = cal.get(Calendar.MONTH);

            if(m == month || m+1 == month || m+2 == month){
                if(e.getDrug().equals(drug)){
                    data.add(new DataPoint(i,e.getDose()));
                    i++;
                }
            }
        }

        DataPoint[] simpleArray = new DataPoint[data.size()];
        data.toArray( simpleArray );

        return simpleArray;
    }


}
