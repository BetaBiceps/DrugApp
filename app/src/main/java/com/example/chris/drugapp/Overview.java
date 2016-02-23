package com.example.chris.drugapp;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by Chris on 27/01/2016.
 */
public class Overview extends Activity {
    Resources res;
    HashMap<String, Integer> drugsArray = new HashMap<String, Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview_layout);
        res = getResources();

        for(String key : res.getStringArray(R.array.drugs)){
            //Should be populated with totals for value
            drugsArray.put(key, 5);
        }



        final ListAdapter listAdapter = new OverviewAdapter(this, drugsArray);
        final ListView listView = (ListView) findViewById(R.id.overviewListID);
        listView.setAdapter(listAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String drugPicked = String.valueOf(listView.getItemAtPosition(position));

                Toast.makeText(Overview.this, drugPicked, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
