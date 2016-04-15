package com.example.chris.drugapp;


import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Chris on 14/04/2016.
 */
public class NoitifcaionControllerTest extends TestCase {
    NotificationController x = new NotificationController(MyApplication.getAppContext());

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @SmallTest
    public void testHighDose(){
        boolean result = x.isDoseHigh("MDMA", 333);
        assertEquals(result, true);
    }
    @SmallTest
    public void testLowDose(){
        boolean result = x.isDoseHigh("MDMA", 200);
        assertEquals(result, false);
    }
    @SmallTest
    public void testHighFreq(){
        Date date = new Date(0);
        Date date2 = new Date(50);
        Event e = new Event(date,"","Cocaine", 50);
        Event e2 = new Event(date2,"","Cocaine", 50);
        ArrayList<Event> al = new ArrayList<Event>();
        al.add(e);al.add(e2);

        boolean result = x.isFreqHigh(al, "Cocaine");
        assertEquals(result,true);
    }
    @SmallTest
    public void testLowFreq(){
        Date date = new Date(0);
        Date date2 = new Date(500000000);
        Event e = new Event(date,"","Cocaine", 50);
        Event e2 = new Event(date2,"","Cocaine", 50);
        ArrayList<Event> al = new ArrayList<Event>();
        al.add(e);al.add(e2);

        boolean result = x.isFreqHigh(al,"Cocaine");
        assertEquals(result,false);
    }
    @SmallTest
    public void testDoseIncrease(){
        Date date = new Date(0);
        Event e = new Event(date,"","Cocaine", 50);
        Event e2 = new Event(date,"","Cocaine", 500);
        Event e3 = new Event(date,"","Cocaine", 5000);
        ArrayList<Event> al = new ArrayList<Event>();
        al.add(e);al.add(e2);al.add(e3);

        boolean result = x.isDoseIncrease(al, "Cocaine");
        assertEquals(result,true);
    }

    @SmallTest
    public void testDoseDecrease(){
        Date date = new Date(0);
        Event e = new Event(date,"","Cocaine", 5000);
        Event e2 = new Event(date,"","Cocaine", 500);
        Event e3 = new Event(date,"","Cocaine", 50);
        ArrayList<Event> al = new ArrayList<Event>();
        al.add(e);al.add(e2);al.add(e3);

        boolean result = x.isDoseIncrease(al,"Cocaine");
        assertEquals(result,false);
    }

    @SmallTest
    public void testFreqIncrease(){
        Date date = new Date(0);
        Date date2 = new Date(50000);
        Date date3 = new Date(50050);
        Event e = new Event(date,"","Cocaine", 50);
        Event e2 = new Event(date2,"","Cocaine", 50);
        Event e3 = new Event(date3,"","Cocaine", 50);
        ArrayList<Event> al = new ArrayList<Event>();
        al.add(e);al.add(e2);al.add(e3);

        boolean result = x.isDoseIncrease(al,"Cocaine");
        assertEquals(result,true);
    }
    @SmallTest
    public void testFreqDecrease(){
        Date date = new Date(0);
        Date date2 = new Date(0);
        Date date3 = new Date(0);
        Event e = new Event(date,"","Cocaine", 50);
        Event e2 = new Event(date2,"","Cocaine", 50);
        Event e3 = new Event(date3,"","Cocaine", 50);
        ArrayList<Event> al = new ArrayList<Event>();
        al.add(e);al.add(e2);al.add(e3);

        boolean result = x.isDoseIncrease(al,"Cocaine");
        assertEquals(result, false);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

}
