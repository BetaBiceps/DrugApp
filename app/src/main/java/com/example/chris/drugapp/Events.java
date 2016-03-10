package com.example.chris.drugapp;

/**
 * Events class is responsible for fetching and storing all
 * user requested events.
 *
 * Created by Chris on 01/02/2016.
 */


import android.content.Context;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;

public class Events{

    private ArrayList<Event> eventslist = new ArrayList<Event>();

    String saveFileName = "calendarEvents.data";
    Context context;


    /**
     * Constructor
     * @param ctx context to be used
     */
    public Events(Context ctx) {
        super();
        context = ctx;
       // eventslist = readFile(saveFileName,context);
    }



    /**
     * Returns events on one given day.
     * @param event_date the date to return
     * @return dayEvents
     */
    public ArrayList<Event> readData(Date event_date) {
        ArrayList<Event> dayEvents = new ArrayList<Event>();
        eventslist = readFile(saveFileName,context);

        for (Event entry : eventslist) {
            Date d = entry.getDate();

            if(d.compareTo(event_date) == 0){
                dayEvents.add(entry);
            }
        }
        return dayEvents;
    }


    /**
     * Returns the full list of events.
     * @return eventslist
     */
    public ArrayList<Event> readAllEvents() {
        eventslist = readFile(saveFileName,context);
        return eventslist;
    }


    /**
     * Function called to insert an event into the database
     * @param date day of use
     * @param time time of use
     * @param drug drug used
     * @param dose mg of drug used
     * @return fail or not
     */
    public int insertData(Date date, String time, String drug, int dose) {
        Event e = new Event(date, time, drug, dose);

        try {
            eventslist.add(e);
            save(saveFileName);
        } catch (Exception ex){
            ex.printStackTrace();
            return -1;
        }
        return 1;
    }


    /**
     * Saves the events to file.
     * @param filename file
     */
    public void save(String filename) {
        FileOutputStream fos;
        ObjectOutputStream oos;

        try {
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);

            for(Event e : eventslist){
                oos.writeObject(e);
            }

            oos.close();
            fos.close();
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Deleted the selected event.
     * @param event the event to delete
     */
    public void deleteEvent(Event event){
        eventslist.remove(event);
        save(saveFileName);
    }

    /**
     * Reads all events from the file
     * @param filename file
     * @param ctx the context
     * @return the array of events
     */
    public ArrayList<Event> readFile(String filename, Context ctx) {
        FileInputStream fis;
        ObjectInputStream ois;
        ArrayList<Event> ev = new ArrayList<Event>();

        try {
            fis = ctx.openFileInput(filename);
            ois = new ObjectInputStream(fis);

            while(true) {
                try{
                    ev.add((Event) ois.readObject());
                } catch (NullPointerException | EOFException e){
                    e.printStackTrace();
                    ois.close();
                    fis.close();
                    break;
                }
            }
            ois.close();
            fis.close();
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return ev;



    }

}