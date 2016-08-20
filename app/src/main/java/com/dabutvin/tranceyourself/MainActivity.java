package com.dabutvin.tranceyourself;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
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

    final int interval = 500; // millsecs
    final int rows = 7;
    final int cols = 5;
    int currentRow = 0;
    List<FloatingActionButton> on = new ArrayList<>();
    FloatingActionButton[][] grid = new FloatingActionButton[rows][cols];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LinearLayout outerSurface = (LinearLayout)findViewById(R.id.surface);
        for (int i = 0; i< rows; i++){
            LinearLayout surface = new LinearLayout(this);
            outerSurface.addView(surface);

            for(int j = 0; j < cols; j++) {
                FloatingActionButton button = new FloatingActionButton(this);

                button.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                button.setLayoutParams(new LinearLayoutCompat.LayoutParams(200, 200));

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toggleButton((FloatingActionButton) v);
                    }
                });

                grid[i][j] = button;
                surface.addView(button);
            }
        }

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currentRow++;
                        if (currentRow >= rows) {
                            currentRow = 0;
                        }
                        highlightCurrentRow();
                        playSound();
                    }
                });

            }
        }, 0, interval);
    }

    void playSound(){
        int sampleRate = 8000;
        int numSamples = (int)Math.round((interval / 1000.0) * sampleRate);
        double sample[] = new double[numSamples];
        double freqOfTone = 0; // hz

        for(int i = 0; i < cols; i++) {
            if(on.contains(grid[currentRow][i])) {
                freqOfTone = 293.66 * Math.pow(Math.pow(2, 1.0/12.0), (2 + i) * 2);
            }
        }

        final byte generatedSnd[] = new byte[2 * numSamples];

        // fill out the array
        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (final double dVal : sample) {
            // scale to maximum amplitude
            final short val = (short) ((dVal * 32767));
            // in 16 bit wav PCM, first byte is the low order byte
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);

        }
        final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
                AudioTrack.MODE_STATIC);
        audioTrack.write(generatedSnd, 0, generatedSnd.length);
        audioTrack.play();
    }

    private void highlightCurrentRow() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (on.contains(grid[i][j])) {
                    if (i == currentRow) {
                        grid[i][j].setBackgroundTintList(ColorStateList.valueOf(Color.YELLOW));
                    } else {
                        grid[i][j].setBackgroundTintList(ColorStateList.valueOf(Color.GREEN));
                    }
                } else {
                    if (i == currentRow) {
                        grid[i][j].setBackgroundTintList(ColorStateList.valueOf(Color.GRAY));
                    } else {
                        grid[i][j].setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                    }
                }
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
