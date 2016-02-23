package com.example.chris.drugapp;

import android.app.AlertDialog;
import android.app.TimePickerDialog;

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

import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


/**
 * Created by Chris on 27/01/2016.
 */
public class CalendarPage extends FragmentActivity {

    private SimpleDateFormat date_format,time_format;
    private CaldroidFragment caldroidFragment ;
    private Bundle args;
    private ArrayList<Event> cursor;
    private EventAdapter myadapter = null;
    private ListView listView;
    private TextView output;
    private EditText dose;
    private Spinner event;
    private Button btnClick;
    Events events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);
        events = new Events(this);


        date_format = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        time_format = new SimpleDateFormat("hh:mm a", Locale.US);
        caldroidFragment = new CaldroidFragment();


        listView=(ListView) findViewById(R.id.event_list);

        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.event_header, listView, false);
        listView.addHeaderView(header, null, false);

        initCalendarView();
        calendarListener();
        caldroidFragment.refreshView();
    }


    private void initCalendarView() {

        args = new Bundle();
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


    private void calendarListener() {
        // TODO Auto-generated method stub
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                cursor = events.readData(date);

                myadapter = new EventAdapter(CalendarPage.this,0, cursor);
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
                    cursor = events.readAllEvents();
                    for (Event e : cursor) {
                        Date date = e.getDate();
                        caldroidFragment.setBackgroundResourceForDate(R.drawable.red_border, date);
                    }
                }
            }

        };
        caldroidFragment.setCaldroidListener(listener);
    }


  protected void saveEvent(String date, String time, String drug, int dose) {
      // TODO Auto-generated method stub
      Date convertedDate = new Date();
      try {
          convertedDate = date_format.parse(date);
      } catch (ParseException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }
      int rowid = events.insertData(convertedDate,time, drug, dose);
      if ( rowid == -1)
      {
          caldroidFragment.setBackgroundResourceForDate(R.color.caldroid_white, convertedDate);
          Toast.makeText(getBaseContext(), "Event not stored", Toast.LENGTH_SHORT).show();
          return;

      }
      caldroidFragment.setBackgroundResourceForDate(R.drawable.red_border, convertedDate);
      caldroidFragment.refreshView();
  }



    protected void openEventDialogue(final Calendar calendar, final Date date) {

        // TODO Auto-generated method stub
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.event_add_layout, null);
        dialogBuilder.setView(dialogView);
        output = (TextView) dialogView.findViewById(R.id.output);
        event =(Spinner) dialogView.findViewById(R.id.drug);
        btnClick = (Button) dialogView.findViewById(R.id.set_button);
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
                        output.setText( selectedHour + ":" + selectedMinute);
                        calendar.set(Calendar.HOUR_OF_DAY, selectedHour);
                        calendar.set(Calendar.MINUTE, selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        dialogBuilder.setPositiveButton("Event", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                String date = date_format.format(calendar.getTime());
                String time = time_format.format(calendar.getTime());
                String event_ = event.getSelectedItem().toString();

                int dose_ = Integer.parseInt(dose.getText().toString());
                saveEvent(date,time,event_, dose_);

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                caldroidFragment.setBackgroundResourceForDate(R.color.caldroid_white, date);
                caldroidFragment.refreshView();
            }
        });

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

    }

}
