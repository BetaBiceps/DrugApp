package com.example.chris.drugapp;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the main class for the overview page.
 *
 * The main function of this page is to display the total
 * usage of each substance, along with other user info.
 *
 * Created by Chris on 27/01/2016.
 */
public class Overview extends Activity {
    Resources res;
    Map<String, String> drugsArray = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overview_layout);
        res = getResources();
        String[] nameArray = res.getStringArray(R.array.drugs);

        for(String key : res.getStringArray(R.array.drugs)){
            //Should be populated with totals for value
            drugsArray.put(key, "TOTALHERE");
        }



        final ListAdapter listAdapter = new OverviewAdapter(drugsArray);
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
