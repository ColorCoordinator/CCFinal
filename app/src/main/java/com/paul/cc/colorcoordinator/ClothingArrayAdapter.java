package com.paul.cc.colorcoordinator;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Hampton on 4/25/2016.
 */
public class ClothingArrayAdapter extends ArrayAdapter<clothingItem> {

    public ClothingArrayAdapter(Context context,  ArrayList<clothingItem> clothes) {
        super(context, R.layout.clothing_item, clothes);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        clothingItem item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.clothing_item, parent, false);
        }

        TextView descLabel = (TextView) convertView.findViewById(R.id.label);
        ImageView image = (ImageView)  convertView.findViewById(R.id.clothing_pic);
        descLabel.setText(item.description);

        if(!(item.filepath.equals("")) && item!=null){

            // Bitmap myBitmap = BitmapFactory.decodeFile(item.filepath);
            setPic(image, item.filepath);

            //image.setImageBitmap(myBitmap);

        }


        return convertView;
    }

    private void setPic(ImageView imgView, String mCurrentPhotoPath) {
        // Get the dimensions of the View
        int targetW = imgView.getWidth();
        int targetH = imgView.getHeight();

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
        imgView.setImageBitmap(bitmap);
    }
}


class clothingItem implements Serializable {
    String description;
    String filepath;

    public clothingItem(){

    }

    public  clothingItem(Parcel in){
        description=in.readString();
        filepath =in.readString();
    }

}