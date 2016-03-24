package com.example.chris.drugapp;

import android.app.AlertDialog;
import android.app.TimePickerDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


/**
 * This class controls the main functions of the calendar page.
 *
 * The calendar is responsible for recording all user entered information,
 * and displaying it accordingly.
 *
 * Created by Chris on 27/01/2016.
 */
public class CalendarPage extends FragmentActivity {

    private SimpleDateFormat date_format,time_format;
    private CaldroidFragment caldroidFragment;

    private ArrayList<Event> eventList;
    private EventAdapter myadapter = null;
    Events events;
    NotificationController controller;

    private ListView listView;
    private TextView output;
    private EditText dose;
    private Spinner event;


    /**
     * The function called when this activity is opened.
     * @param savedInstanceState if there is a saved state it is here
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);
        events = new Events(this);
        controller = new NotificationController(CalendarPage.this);


        date_format = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        time_format = new SimpleDateFormat("hh:mm a", Locale.US);
        caldroidFragment = new CaldroidFragment();


        listView=(ListView) findViewById(R.id.event_list);

        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.event_header, listView, false);
        listView.addHeaderView(header, null, false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Event eventSelected = (Event) listView.getItemAtPosition(position);
                openDeleteDialogue(eventSelected);
            }
        });

        initCalendarView();
        calendarListener();
        caldroidFragment.refreshView();
    }


    /**
     * Creation of the calendar for the user, controls the layout.
     */
    private void initCalendarView() {

        Bundle args = new Bundle();
        //Getting current date
        Calendar cal = Calendar.getInstance();
        Date currentDate = cal.getTime();

        //Calendar view optimized
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.SUNDAY);
        args.putBoolean(CaldroidFragment.SHOW_NAVIGATION_ARROWS, true);

        caldroidFragment.setArguments(args);
        caldroidFragment.setBackgroundResourceForDate(R.color.caldroid_sky_blue, currentDate);

        android.support.v4.app.FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.cal, caldroidFragment);
        t.commit();
    }


    /**
     * Listener for user events on the calendar.
     * Listens for date selection, long clicks, and refreshing the view.
     */
    private void calendarListener() {
        // TODO Auto-generated method stub
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                eventList = events.readData(date);

                myadapter = new EventAdapter(CalendarPage.this,0, eventList);
                listView.setAdapter(myadapter);

                caldroidFragment.refreshView();
            }

            @Override
            public void onLongClickDate(Date date, View view) {
                Toast.makeText(getApplicationContext(),
                        "Long click " + date_format.format(date),
                        Toast.LENGTH_SHORT).show();

                Calendar cal=Calendar.getInstance();
                cal.setTime(date);
                openEventDialogue(cal, date);
                caldroidFragment.refreshView();
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                    eventList = events.readAllEvents();
                    for (Event e : eventList) {
                        Date date = e.getDate();
                        caldroidFragment.setBackgroundResourceForDate(R.drawable.red_border, date);
                    }
                }
            }

        };
        caldroidFragment.setCaldroidListener(listener);
    }


    /**
     * Run when the user requests to save a new event.
     * @param date the date
     * @param time the time
     * @param drug the substance from a spinner
     * @param dose integer dose value
     */
  protected void saveEvent(String date, String time, String drug, int dose) {
      Date convertedDate = new Date();
      try {
          convertedDate = date_format.parse(date);
      } catch (ParseException e) {
          e.printStackTrace();
      }
      int rowid = events.insertData(convertedDate,time, drug, dose);
      if ( rowid == -1)
      {
          caldroidFragment.setBackgroundResourceForDate(R.color.caldroid_white, convertedDate);
          Toast.makeText(getBaseContext(), "Event not stored", Toast.LENGTH_SHORT).show();
          return;

      }
      eventList = events.readAllEvents(); // Update the events list
      controller.notifyCheck(eventList, drug, dose);
      caldroidFragment.setBackgroundResourceForDate(R.drawable.red_border, convertedDate);
      caldroidFragment.refreshView();
  }


    /**
     * Run when the user long clicks a date on the calendar
     * @param calendar the main calendar
     * @param date the date selected
     */
    protected void openEventDialogue(final Calendar calendar, final Date date) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.event_add_layout, null);
        dialogBuilder.setView(dialogView);
        output = (TextView) dialogView.findViewById(R.id.output);
        event =(Spinner) dialogView.findViewById(R.id.drug);
        Button btnClick = (Button) dialogView.findViewById(R.id.set_button);
        dose = (EditText) dialogView.findViewById(R.id.dose);

        btnClick.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(CalendarPage.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        output.setText(selectedHour + ":" + selectedMinute);
                        calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                        calendar.set(Calendar.MINUTE, selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        dialogBuilder.setPositiveButton("Event", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String date = date_format.format(calendar.getTime());
                String time = time_format.format(calendar.getTime());
                String event_ = event.getSelectedItem().toString();

                int dose_ = Integer.parseInt(dose.getText().toString());
                saveEvent(date, time, event_, dose_);

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                caldroidFragment.setBackgroundResourceForDate(R.color.caldroid_white, date);
                caldroidFragment.refreshView();
            }
        });

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }



    /**
     * Run when the user clicks on an event
     * @param event the event
     */
    protected void openDeleteDialogue(final Event event) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Delete");
        dialogBuilder.setMessage("Are you sure you want to delete this event?");


        dialogBuilder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                events.deleteEvent(event);

                Toast.makeText(CalendarPage.this, "Deleted Event.", Toast.LENGTH_SHORT).show();

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                caldroidFragment.refreshView();
            }
        });


        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }

}
