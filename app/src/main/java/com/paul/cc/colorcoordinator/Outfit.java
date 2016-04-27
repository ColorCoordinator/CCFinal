package com.paul.cc.colorcoordinator;


import android.graphics.Bitmap;
import java.util.ArrayList;

public class Outfit {

    public ArrayList<HSLColor> colors = new ArrayList<HSLColor>();
    public ArrayList<HSLColor> primaryHues = new ArrayList<HSLColor>();
    public ArrayList<Clothing> clothes = new ArrayList<Clothing>();
    public ColorScheme scheme;
    public String schemeName;
    public int matchRating;
    public boolean white = false;
    public boolean black = false;
    public boolean gray = false;
    public boolean brown = false;
    public boolean navy = false;

    public Outfit(){

    }

    public void updateFlags(){
        black = false;
        brown = false;
        gray = false;
        white = false;
        navy = false;
        for(Clothing c: clothes){
            black = black | c.hasBlack();
            brown = brown | c.hasBrown();
            gray = gray | c.hasGray();
            white = white | c.hasWhite();
            navy = navy | c.hasNavy();
        }
    }

    private void refreshColors(){
        for(Clothing cl : clothes){
            cl.findPrimaryHues();
            ArrayList<HSLColor> hues = cl.getPrimaryHues();
            for(HSLColor c: hues){
                colors.add(c);
            }
        }
        findPrimaryHues();
    }

    public void addClothing(Bitmap img){
        Clothing clothing = new Clothing(img);
        clothes.add(clothing);
        refreshColors();
    }

    public void removeClothing(int index){
        clothes.remove(index);
        refreshColors();
    }

    public void findPrimaryHues(){
        for(HSLColor color : colors){
            boolean add = true;
            for(HSLColor c : primaryHues){
					/*if(color.getLuminance()<= 10){
						add=false;
						white = true;
						break;
					}
					else if(color.getLuminance() >= 90){
						add=false;
						black=true;
						break;
					}
					else if(color.getSaturation() <= 7){
						add=false;
						gray=true;
						break;
					}
					else */if(color.getDegreesBetween(c) < 30){
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
    }

    public int findMatchRating(){
        updateFlags();
        scheme = ColorScheme.findScheme(colors);
        schemeName = scheme.getName();
        int base = 180;
        int score = 100;
        if (schemeName.equals("Complementary")) {
            base = 180;
        } else if (schemeName.equals("Triad")) {
            base = 120;
        } else if (schemeName.equals("Tetrad")) {
            base = 90;
        }
        if(schemeName == "Analagous"){
            for(int i = 0; i < colors.size()-1; i++){
                int deg = (int) colors.get(i).getDegreesBetween(colors.get(i+1));
                if(deg > 60){
                    score -= deg - 60;
                }
            }
        }
        else if(schemeName == "Split Complementary"){
            for(int i = 0; i < colors.size()-1; i++){
                int deg = (int) colors.get(i).getDegreesBetween(colors.get(i+1));
                score -= Math.min(Math.abs(150 - deg), Math.abs(70 - deg));
            }
        }
        else if(schemeName == "Tetrad"){
            for(int i = 0; i < colors.size()-1; i++){
                int deg = (int) colors.get(i).getDegreesBetween(colors.get(i+1));
                score -= Math.min(Math.abs(base - deg),Math.abs(base*2 - deg));
            }
        }
        else{
            for(int i = 0; i < colors.size()-1; i++){
                int deg = (int) colors.get(i).getDegreesBetween(colors.get(i+1));
                score -= Math.abs(base - deg);
            }
        }

        if(black & navy){score -= 50;}
        if(black & brown){score -= 50;}
        if(score < 0){score = 0;}
        matchRating = score;
        return matchRating;
    }

}