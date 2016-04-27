package com.paul.cc.colorcoordinator;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONStringer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Change on 4/2/2016.
 */
public class OutfitActivity extends AppCompatActivity {
    private int imageNumber;
    SharedPreferences imagePrefs;

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri fileUri;
    private File imageFile;
    private ClothingArrayAdapter adapter;
    ArrayList<clothingItem> stuff;
    private SharedPreferences stuffPref;
    private int stuffInt;
    private int itemCount;
    private Outfit outfit;
    private int matchRating;
    SharedPreferences matchRatingPrefs;
/*
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

            stuff=savedInstanceState.getParcelableArrayList("clothingList");
            itemCount=savedInstanceState.getInt("itmCount");

    }*/




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Remove the top bar of the app
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfit);



        imagePrefs = getSharedPreferences("labelImageNum", 0);
        imageNumber  = imagePrefs.getInt("imageNumCount", 0); //the 0 is the default value if nothing found
        //Toast.makeText(this, imageNumber + "hey", Toast.LENGTH_SHORT).show();



        if(savedInstanceState!=null){

            //stuff=savedInstanceState.getParcelableArrayList("clothingList");
           // itemCount=savedInstanceState.getInt("itmCount");
            //Toast.makeText(OutfitActivity.this, "true " + itemCount, Toast.LENGTH_SHORT).show();
        }
        else{

            //stuff = new ArrayList<clothingItem>();
            //itemCount=1;
            //Toast.makeText(OutfitActivity.this, "added " + itemCount, Toast.LENGTH_SHORT).show();
        }

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        //SharedPreferences.Editor mEditor = stuffPref.edit();
        try {
            //Toast.makeText(OutfitActivity.this, prefs.getString("please",""), Toast.LENGTH_SHORT).show();
            String holdStuff = prefs.getString("please","");
            if(holdStuff.equals("")){
                stuff = new ArrayList<clothingItem>();
            }
            else{
                stuff = (ArrayList<clothingItem>)fromString(holdStuff);
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        JSONStringer ji = new JSONStringer();


        adapter = new ClothingArrayAdapter(this, stuff);
        ListView listView  = (ListView) findViewById(R.id.listView);
        //listView.setChoiceMode(listView.CHOICE_MODE_MULTIPLE);
        listView.setAdapter(adapter);

        Button buttonAdd = (Button) findViewById(R.id.btnAdd);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // create Intent to take a picture and return control to the calling application
                /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                fileUri = getOutputMediaFileUri(); // create a file to save the image
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

                // start the image capture Intent
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                */

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                imageFile = new File(getExternalFilesDir(null),
                        "CC_" + timeStamp + ".jpg");

                fileUri = Uri.fromFile(imageFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(intent, 0);


            }
        });

        matchRatingPrefs = getSharedPreferences("labelMatchRating", 0);
        matchRating  = imagePrefs.getInt("matchNum", 100);
        TextView textView = (TextView) findViewById(R.id.matchRating);
        textView.setText(String.valueOf(matchRating));

        outfit = new Outfit();
        for(clothingItem cloth : stuff){
            addClothing(cloth.filepath);
        }
    /*
        clothingItem it1 = new clothingItem();
        it1.description= "Ben";

        clothingItem it2 = new clothingItem();
        it2.description= "Roll Tide";
        it1.filepath="";*/
        //adapter.add(it1);
       /* adapter.add(it2);
        adapter.add(it2);
        adapter.add(it2);
        adapter.add(it2);
        adapter.add(it1);
        adapter.add(it1);
        adapter.add(it2);
        adapter.add(it2);
        adapter.add(it2);
        adapter.add(it2);
        adapter.add(it1);
        adapter.add(it1);
        adapter.add(it2);
        adapter.add(it2);
        adapter.add(it2);
        adapter.add(it2);
        adapter.add(it1);*/

    }
    /* Set object to Base64 String */
    private  String toString( Serializable o ) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(o);
        oos.close();
        return Base64.encodeToString(baos.toByteArray(),0);
    }
    /** Read the object from Base64 string. */
    private static Object fromString( String s ) throws IOException ,
            ClassNotFoundException {
        byte [] data = Base64.decode(s, 0);
        ObjectInputStream ois = new ObjectInputStream(
                new ByteArrayInputStream(  data ) );
        Object o  = ois.readObject();
        ois.close();
        return o;
    }


    @Override
    protected void onStop() {
        super.onStop();
        //SharedPreferences.Editor mEditor = stuffPref.edit();
        //mEditor.putArray("imageNumCount", stuff).commit();
        //save the task list to preference
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor mEditor = prefs.edit();
        //mEditor.putInt("imageNumCount", 0).commit();
        //SharedPreferences.Editor editor = prefs.edit();
       // Gson gson = new Gson();
        try {
            String test = toString(stuff);
            mEditor.putString("please",test);
            mEditor.commit();
            //Toast.makeText(OutfitActivity.this, test, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    private Uri getOutputMediaFileUri(){
        return Uri.fromFile(getOutputMediaFile());
    }

    private  File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File file = new File(getFilesDir(), "TestFile1.jpeg");
        if (!file.exists()) try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                , "CCTestDIR");*/
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        /*if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("CCTestDIR", "failed to create directory");
                return null;
            }
        }*/

        // Create a media file name
       /* String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;

            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "TEST_"+ timeStamp + ".jpg");*/

        return file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Toast.makeText(this,"ACTIVATED",Toast.LENGTH_LONG).show();
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(this,"TAKEN",Toast.LENGTH_LONG).show();
                clothingItem test = new clothingItem();
                ///////new
                imageNumber++;
                SharedPreferences.Editor mEditor = imagePrefs.edit();
                mEditor.putInt("imageNumCount", imageNumber).commit();
                //////new
                test.description ="Item " + imageNumber;
                //itemCount++;
                test.filepath = fileUri.getPath();
                addClothing(test.filepath);
                adapter.add(test);
                /*Toast.makeText(this, "Image saved to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();*/
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
                Toast.makeText(this,"CANCELED",Toast.LENGTH_LONG).show();
            } else {
                // Image capture failed, advise user
                Toast.makeText(this,"NO",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        // savedInstanceState.putInt(STATE_SCORE, mCurrentScore);
        //savedInstanceState.putInt(STATE_LEVEL, mCurrentLevel);
        //savedInstanceState.putParcelableArrayList("clothingList", stuff);
        savedInstanceState.putInt("itmCount", itemCount);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    private void addClothing(String mCurrentPhotoPath) {

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        //int scaleFactor = Math.min(photoW/targetW, photoH/targetH);
        int scaleFactor = Math.min(photoW/50, photoH/50);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;


        Bitmap bitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        outfit.addClothing(bitmap);
        matchRating = outfit.findMatchRating();
        SharedPreferences.Editor mEditor = matchRatingPrefs.edit();
        mEditor.putInt("matchNum", matchRating).commit();
        Log.v("MATCHRATING!!!!!", String.valueOf(matchRating));
        TextView textView = (TextView) findViewById(R.id.matchRating);
        textView.setText(String.valueOf(matchRating));

    }
/*
    public void removeClothing(){

    }
    public void addClothing(View view){

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        imageFile=new File(getExternalFilesDir(null),
                "colorWilly3.jpg");

        Uri tempuri = Uri.fromFile(imageFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempuri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, 0);

    }

    //sets image to the image id location
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0){
            Log.d("ColorCoordinator", "THIS WORK?");
            switch(resultCode){
                case Activity.RESULT_OK:
                    if(imageFile.exists()){
                        Toast.makeText(this, "File saved at "+imageFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                        imageNumber++;
                        SharedPreferences.Editor mEditor = imagePrefs.edit();
                        mEditor.putInt("imageNumCount", imageNumber).commit();//maybe use apply() not commit
                        //Toast.makeText(this, imageNumber + " hey", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    break;
                default:
                    break;
            }
        }
        if(requestCode==1){

        }
    }
*/
    public void goBackToMain(View v) {
        Intent backToMain = new Intent(v.getContext(), MainActivity.class);
        startActivityForResult(backToMain, 0);
    }
}
