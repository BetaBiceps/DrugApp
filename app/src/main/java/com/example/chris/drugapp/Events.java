package com.example.chris.drugapp;

/**
 * Created by Chris on 01/02/2016.
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Events {

    HashMap<Date, Event> events = new HashMap<Date, Event>();

    // Getting Cursor to read data from table
    public ArrayList<Event> readData(Date event_date) {
        ArrayList<Event> dayEvents = new ArrayList<Event>();

        for (Map.Entry<Date, Event> entry : events.entrySet()) {
            Date d = entry.getKey();
            Event e = entry.getValue();

            if(d == event_date){
                dayEvents.add(e);
            }
        }
        return dayEvents;
    }


    // Getting Cursor to read data from table
    public ArrayList<Event> readAllEvents() {
        ArrayList<Event> list = new ArrayList<Event>();

        for (Map.Entry<Date, Event> entry : events.entrySet()) {
            Event e = entry.getValue();
            list.add(e);
        }
        return list;
    }

    // Inserting Data into table
    public int insertData(Date date, String time, String msg) {
        Event e = new Event(time,msg, date);

        if(events.put(date,e) != null){
            return 1;
        }
        return -1;
    }



}