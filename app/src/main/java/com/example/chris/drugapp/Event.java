package com.example.chris.drugapp;

import java.util.Date;

/**
 * Created by Chris on 01/02/2016.
 */
public class Event {
    public String time;
    public String msg;
    public Date date;

    public Event(String time, String msg, Date date){
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
}
