package com.example.ben10.deeplinktest;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    public static String DEEPLINK = "DeepLinks";
    public static String HELP_URL = "https://developer.android.com/training/app-indexing/deep-linking.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText url = (EditText) findViewById(R.id.enter_url);
        Button go = (Button) findViewById(R.id.btn_go);
        Button save = (Button) findViewById(R.id.btn_save);
        TextView help = (TextView) findViewById(R.id.help);

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(HELP_URL));
                try {
                    startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    Log.e("No Browser Found","");
                }
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startIntent(url);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(startIntent(url)) {
                    storeDeepLink(url.getText().toString());
                }
            }
        });
    }

    private  boolean startIntent(EditText url) {
        boolean success = true;
        if (url.getText() != null && !url.getText().toString().isEmpty()) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url.getText().toString()));
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                url.setError("No Application Found");
                success = false;
            }
        } else {
            success = false;
            url.setError("Please Enter the URL");
        }
        return success;
    }

    private void storeDeepLink(String url) {
        SharedPreferences prefs = this.getSharedPreferences(
                DEEPLINK, Context.MODE_PRIVATE);
        Set<String> set = new HashSet<>();
        set.add(url);
        if(prefs.getStringSet(DEEPLINK, null) != null) {
            set.addAll(prefs.getStringSet(DEEPLINK, null));
        }
        SharedPreferences.Editor scoreEditor = prefs.edit();
        scoreEditor.putStringSet(DEEPLINK, set);
        scoreEditor.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_list:
                SharedPreferences prefs = this.getSharedPreferences(
                        DEEPLINK, Context.MODE_PRIVATE);
                if(prefs.getStringSet(DEEPLINK, null) != null) {
                    startActivity(new Intent(this, HistoryActivity.class));
                } else {
                    Toast.makeText(this,"Please Save A URL",Toast.LENGTH_LONG).show();
                }
                break;
        }
        return true;
    }
}
