package com.dabutvin.tranceyourself;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    List<FloatingActionButton> on = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        LinearLayout outerSurface = (LinearLayout)findViewById(R.id.surface);
        for (int i = 0; i< 7; i++){
            LinearLayout surface = new LinearLayout(this);
            outerSurface.addView(surface);

            for(int j = 0; j < 5; j++) {
                FloatingActionButton button = new FloatingActionButton(this);

                button.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                button.setLayoutParams(new LinearLayoutCompat.LayoutParams(200, 200));

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggleButton((FloatingActionButton) v);
                    }
                });

                surface.addView(button);
            }
        }
    }

    private void toggleButton(FloatingActionButton button) {
        if(on.contains(button)){
            on.remove(button);
            button.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
        } else {
            on.add(button);
            button.setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
