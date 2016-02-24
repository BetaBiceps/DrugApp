package com.example.chris.drugapp;

/**
 * Events class is responsible for fetching and storing all
 * user requested events.
 *
 * Created by Chris on 01/02/2016.
 */


import android.content.Context;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;


public class Events {

    ArrayList<Event> eventslist = new ArrayList<Event>();
    String saveFileName = "calendarEvents.data";
    Context context;


    public Events(Context ctx) {
        super();
        context = ctx;
        //eventslist = readFile(saveFileName,context);
    }



    // Getting Cursor to read data from table
    public ArrayList<Event> readData(Date event_date) {
        ArrayList<Event> dayEvents = new ArrayList<Event>();

        for (Event entry : eventslist) {
            Date d = entry.getDate();

            if(d.compareTo(event_date) == 0){
                dayEvents.add(entry);
            }
        }
        return dayEvents;
    }


    // Getting Cursor to read data from table
    public ArrayList<Event> readAllEvents() {
        return eventslist;
    }

    // Inserting Data into table
    public int insertData(Date date, String time, String drug, int dose) {
        Event e = new Event(date, time, drug, dose);

        try {
            eventslist.add(e);
            e.save(saveFileName, e, context.getApplicationContext());
        } catch (Exception ex){
            ex.printStackTrace();
            return -1;
        }
        return 1;
    }





}