package com.paul.cc.colorcoordinator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class MainActivity extends AppCompatActivity {
    public static String somevalue = "Hello from application singleton!";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove the top bar of the app
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    /*
    Intent to go to outfit activity
     */
    public void startNewOutfit(View v) {
        if(v.getId() == R.id.btnOutfit) {
            Intent outfitScreen = new Intent(v.getContext(), OutfitActivity.class);
            startActivityForResult(outfitScreen, 0);
        }
    }
    /*
    Intent to go to options activity
    */
    public void openOptionsMenu(View v) {
        if(v.getId() == R.id.btnOptions) {
            Intent optionsScreen = new Intent(v.getContext(), OptionsActivity.class);
            startActivityForResult(optionsScreen, 0);
        }
    }



}
