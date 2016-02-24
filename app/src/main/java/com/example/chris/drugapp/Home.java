package com.example.chris.drugapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

/**
 * The home page
 */

public class Home extends AppCompatActivity {

    private Button calendarButton, overviewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        final TextView titleTextView = (TextView) findViewById(R.id.textView);
        //Calendar Button
        calendarButton = (Button) findViewById(R.id.calendarButton);
        //Overview Button
        overviewButton = (Button) findViewById(R.id.overviewButton);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void calendarClick(View view) {
        Intent calendarIntent = new Intent(this,
                CalendarPage.class);

        startActivity(calendarIntent);
    }

    public void overviewClick(View view) {
        Intent overviewIntent = new Intent(this,
                Overview.class);

        startActivity(overviewIntent);
    }
}
