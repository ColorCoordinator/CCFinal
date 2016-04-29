package com.paul.cc.colorcoordinator;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Clothing represents one article of clothing by finding all of its comprising colors, the primary
 * hues in the clothing, and any scheme that might be present
 */
public class Clothing {

    boolean pattern;
    private boolean black = false, white=false, gray=false, brown = false, navy = false;
    private ArrayList<HSLColor> colors = null;
    private ArrayList<HSLColor> primaryHues = new ArrayList<HSLColor>();
    boolean selected;
    private ColorScheme scheme;

    public Clothing(ArrayList<HSLColor> c){
        colors = c;
        pattern = false;
        findPrimaryHues();
    }

    /**
     * Create clothing object from image
     * @param img
     */
    public Clothing(Bitmap img){
        int k = 10;
        boolean flag = true;
        ColorFinder cf = null;
        while(flag){
            flag = false;
            cf = new ColorFinder(img, k);
            ArrayList<Cluster> clusters = cf.clusters;
            for(int i = 0; i < clusters.size(); i++){
                for(int j = i + 1; j < clusters.size(); j++){
                    if(clusters.get(i).getCenter().getDistance(clusters.get(j).getCenter()) < 50.0){
                        flag = true;
                    }
                }
            }
            k -= 1;
        }

        colors = cf.getColors();
    }

    public boolean hasBlack(){
        return black;
    }
    public boolean hasWhite(){
        return white;
    }
    public boolean hasBrown(){
        return brown;
    }
    public boolean hasGray(){
        return gray;
    }
    public boolean hasNavy(){
        return navy;
    }

    public ArrayList getColors(){
        return colors;
    }

    public ArrayList getPrimaryHues(){
        return primaryHues;
    }

    /**
     * Finds the primary hues in a clothing object by throwing out colors with similar hue
     * Also sets flags for colors on the ends of the saturation and lightness spectrums, as these could
     * imply hues that arent really present
     */
    public void findPrimaryHues(){
        for(HSLColor color : colors){
            boolean add = true;
            if(color.getLuminance()>= 75 && 10 < color.getHue() && color.getHue() < 50){
                brown = true;
            }
            else if(color.getLuminance() >=75 && 220 < color.getHue() && color.getHue() < 255){
                navy = true;
            }
            if(color.getLuminance()<= 10){
                add=false;
                white = true;
                continue;
            }
            else if(color.getLuminance() >= 90){
                add=false;
                black=true;
                continue;
            }
            else if(color.getSaturation() <= 7){
                add=false;
                gray=true;
                continue;
            }
            for(HSLColor c : primaryHues){
                if(color.getDegreesBetween(c) < 30){
                    add = false;
                    break;
                }

            }
            if(add){
                primaryHues.add(color);
            }
            else{
                add = true;
            }
        }
        int numcolors = primaryHues.size();
        if(black) numcolors++;
        if(white) numcolors++;
        if(gray) numcolors++;
        if(numcolors > 2){pattern = true;}
    }

    public void findScheme(){
        scheme =  ColorScheme.findScheme(primaryHues);
    }

    public ColorScheme getScheme(){
        return scheme;
    }
}