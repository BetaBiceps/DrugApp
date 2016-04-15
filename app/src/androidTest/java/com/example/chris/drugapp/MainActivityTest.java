package com.example.chris.drugapp;

import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;
import android.widget.TextView;

/**
 * Created by Chris on 14/04/2016.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<CalendarPage> {
    CalendarPage activity;

    public MainActivityTest(){
        super(CalendarPage.class);
        setName("CalendarPage");
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
    }

   /* @SmallTest
    public void testViewNotNull(){
        TextView textView = (TextView) activity.findViewById(R.id.textView);
        assertNotNull(textView);
    }
    */
}
