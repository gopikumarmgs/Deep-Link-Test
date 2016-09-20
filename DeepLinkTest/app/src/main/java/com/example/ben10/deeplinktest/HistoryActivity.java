package com.example.ben10.deeplinktest;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

/**
 * Created by Ben10 on 7/30/16.
 */

public class HistoryActivity extends AppCompatActivity {
    ListView listView ;
    private ArrayAdapter<String> listAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // Get ListView object from xml
        listView = (ListView) findViewById(R.id.list);
        SharedPreferences prefs = this.getSharedPreferences(
                MainActivity.DEEPLINK, Context.MODE_PRIVATE);

        // Defined Array values to show in ListView
        Set<String> list = prefs.getStringSet(MainActivity.DEEPLINK, null);

        // Create and populate a List of URL names.
        String[] urlList = list.toArray(new String[list.size()]);

        ArrayList<String> planetList = new ArrayList<String>();
        planetList.addAll(Arrays.asList(urlList) );

        // Create ArrayAdapter using the planet list.
        listAdapter = new MobileArrayAdapter(this,planetList);
        // Set the ArrayAdapter as the ListView's adapter.
        listView.setAdapter( listAdapter );

        // ListView Item Click Listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // ListView Clicked item value
                String  itemValue = (String) listView.getItemAtPosition(position);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(itemValue));
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    // Show Alert
                    Toast.makeText(getApplicationContext(),
                            "No Application Found", Toast.LENGTH_LONG)
                            .show();
                }


            }

        });
    }

    public class MobileArrayAdapter extends ArrayAdapter<String> {
        private final Context context;
        ArrayList<String> planetList = new ArrayList<String>();

        public MobileArrayAdapter(Context context, ArrayList<String> planetList) {
            super(context, R.layout.listview, planetList);
            this.context = context;
            this.planetList = planetList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View rowView = inflater.inflate(R.layout.listview, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.rowTextView);
            textView.setText(planetList.get(position));
            return rowView;
        }

    }

}