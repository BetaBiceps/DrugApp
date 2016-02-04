package com.example.chris.drugapp;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Chris on 01/02/2016.
 */
public class Event {
    public String time;
    public String msg;
    public Date date;
    SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");

    public Event(Date date, String time, String msg){
        this.time = time;
        this.msg = msg;
        this.date = date;
    }

    public String getMsg(){
        return this.msg;
    }
    public Date getDate(){
        return this.date;
    }
    public String dateTostring(){
        String x = null;
        try {
            x = format.format(date);
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return x;
    }
}
