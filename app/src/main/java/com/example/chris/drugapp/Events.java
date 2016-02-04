package com.example.chris.drugapp;

/**
 * Created by Chris on 01/02/2016.
 */

import java.util.ArrayList;
import java.util.Date;


public class Events {

    ArrayList<Event> events = new ArrayList<Event>();

    // Getting Cursor to read data from table
    public ArrayList<Event> readData(Date event_date) {
        ArrayList<Event> dayEvents = new ArrayList<Event>();

        for (Event entry : events) {
            Date d = entry.getDate();

            if(d.compareTo(event_date) == 0){
                dayEvents.add(entry);
            }
        }
        return dayEvents;
    }


    // Getting Cursor to read data from table
    public ArrayList<Event> readAllEvents() {
        ArrayList<Event> list = new ArrayList<Event>();

        for (Event entry : events) {
            list.add(entry);
        }
        return list;
    }

    // Inserting Data into table
    public int insertData(Date date, String time, String msg) {
        Event e = new Event(date, time, msg);

        try {
            events.add(e);
        } catch (Exception ex){
            ex.printStackTrace();
            return -1;
        }
        return 1;
    }
}