package com.paul.cc.colorcoordinator;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * Created by Change on 4/3/2016.
 */
public class OptionsActivity extends Activity{
    private int step = 1;
    private int max = 100;
    private int min = 0;
    private SharedPreferences soundPref;
    private int matchThreshold;
    private SharedPreferences matchThreshPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Remove the top bar of the app
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        matchThreshPref = getSharedPreferences("labelMatchThreshold", 0);
        matchThreshold = matchThreshPref.getInt("matchThresh", 100); //the 0 is the default value if nothing found

        TextView view = (TextView) findViewById(R.id.txtMatchThreshold);
        view.setText("Match Threshold: " + matchThreshold);

        //seek bar info for match threshold
        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setProgress(matchThreshold);
        seekBar.setMax((max - min) / step);
        seekBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        SharedPreferences.Editor mEditor = matchThreshPref.edit();
                        mEditor.putInt("matchThresh", matchThreshold).commit();//maybe use apply() not commit
                        TextView view = (TextView) findViewById(R.id.txtMatchThreshold);
                        view.setText("Match Threshold: " + matchThreshold);
                        //Toast.makeText(OptionsActivity.this, matchThreshold + " is the Match Thres", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        // if progress = 13 -> value = 3 + (13 * 0.1) = 4.3
                        matchThreshold = min + (progress * step);

                    }
                }
        );

        boolean soundOn;
        ToggleButton toggle = (ToggleButton)this.findViewById(R.id.tglSound);
        soundPref = getSharedPreferences("labelSound", 0);
        soundOn = soundPref.getBoolean("sound", true); //the 0 is the default value if nothing found
        toggle.setChecked(soundOn);
        sounds(soundOn); //call sounds initially
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Is the toggle on?
                boolean on = ((ToggleButton) v).isChecked();
                sounds(on); //toggle sounds
            }
        });
    }

    public void sounds(boolean off){
        AudioManager aManager;
        aManager = (AudioManager)getSystemService(AUDIO_SERVICE);

        // Is the toggle on?
        //boolean on = soundOn;
        if (!off) {
            //set saved sound
            SharedPreferences.Editor mEditor = soundPref.edit();
            mEditor.putBoolean("sound", off).commit();//maybe use apply() not commit
            Log.i("onToggleIsChecked", "ToggleClick Is On");

            //turn ringer silent
            aManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            Log.i("RINGER_MODE_SILENT", "Set to true");

            //turn off sound, disable notifications
            aManager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
            Log.i("STREAM_SYSTEM", "Set to true");
            //example if I wanna allow for updated versions
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                aManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_MUTE, 0);
            } else {
                aManager.setStreamMute(AudioManager.STREAM_SYSTEM, true);
            }*/

            //notifications
            aManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
            Log.i("STREAM_NOTIFICATION", "Set to true");
            //alarm
            aManager.setStreamMute(AudioManager.STREAM_ALARM, true);
            Log.i("STREAM_ALARM", "Set to true");
            //ringer
            aManager.setStreamMute(AudioManager.STREAM_RING, true);
            Log.i("STREAM_RING", "Set to true");
            //media
            aManager.setStreamMute(AudioManager.STREAM_MUSIC, true);
            Log.i("STREAM_MUSIC", "Set to true");

        } else {
            //set saved sound
            SharedPreferences.Editor mEditor = soundPref.edit();
            mEditor.putBoolean("sound", off).commit();//maybe use apply() not commit

            Log.i("onToggleIsChecked", "ToggleClick Is Off");

            //turn ringer silent
            aManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            Log.i(".RINGER_MODE_NORMAL", "Set to true");

            // turn on sound, enable notifications
            aManager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
            Log.i("STREAM_SYSTEM", "Set to False");
            //notifications
            aManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
            Log.i("STREAM_NOTIFICATION", "Set to False");
            //alarm
            aManager.setStreamMute(AudioManager.STREAM_ALARM, false);
            Log.i("STREAM_ALARM", "Set to False");
            //ringer
            aManager.setStreamMute(AudioManager.STREAM_RING, false);
            Log.i("STREAM_RING", "Set to False");
            //media
            aManager.setStreamMute(AudioManager.STREAM_MUSIC, false);
            Log.i("STREAM_MUSIC", "Set to False");
        }
        Log.i("onToggleClicked", "ToggleClick Event Ended");
    }

    public void goBackToMain(View v) {
        // Kabloey
        Intent backToMain = new Intent(v.getContext(), MainActivity.class);
        startActivityForResult(backToMain, 0);
        //Toast.makeText(v.getContext(), "Login Fail" + MainActivity.somevalue, 3).show();
    }
}
