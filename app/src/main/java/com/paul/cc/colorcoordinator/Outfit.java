package com.paul.cc.colorcoordinator;


import android.graphics.Bitmap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

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

    /**
     * Updates all flags based on matching flags in clothing objects
     */
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

    //Repopulates colors array with primary hues of clothing
    private void refreshColors(){
        for(Clothing cl : clothes){
            cl.findPrimaryHues();
            ArrayList<HSLColor> cs = cl.getPrimaryHues();
            for(HSLColor c: cs) colors.add(c);
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
        primaryHues.clear();
        for(HSLColor color : colors){
            boolean add = true;
            for(HSLColor c : primaryHues){
                    /*if(color.getLuminance()>= 75 && 10 < color.getHue() && color.getHue() < 50){
                        brown = true;
                    }
                    else if(color.getLuminance() >=75 && 220 < color.getHue() && color.getHue() < 255){
                        navy = true;
                    }
					if(color.getLuminance()<= 10){
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

    /**
     * findMatchRating attempts to quantify how well an outfit matches by guessing its scheme and seeing how close
     * the outfit is to the perfect version of that scheme
     * @return
     */
    public int findMatchRating(){
        updateFlags();
        //Get rid of duplicates
        Set<HSLColor> hs = new HashSet<HSLColor>();
        hs.addAll(colors);
        colors.clear();
        colors.addAll(hs);

        scheme = ColorScheme.findScheme(primaryHues);
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
                if(deg < 15){break;}
                score -= Math.min(Math.abs(150 - deg), Math.abs(70 - deg));
            }
        }
        else if(schemeName == "Tetrad"){
            for(int i = 0; i < colors.size()-1; i++){
                int deg = (int) colors.get(i).getDegreesBetween(colors.get(i+1));
                if(deg < 15){break;}
                score -= Math.min(Math.abs(base - deg),Math.abs(base*2 - deg));
            }
        }
        else{
            for(int i = 0; i < colors.size()-1; i++){
                int deg = (int) colors.get(i).getDegreesBetween(colors.get(i+1));
                if(deg < 15){continue;}
                score -= Math.abs(base - deg);
            }
        }

        if(black & navy){score -= 50;}
        if(black & brown){score -= 50;}
        boolean flag = false;
        for(Clothing c : clothes){
            if(c.pattern){
                if(flag){
                    score -= 50;
                }
                flag = true;
            }}
        if(score < 0){score = 0;}
        matchRating = score;
        return matchRating;
    }

}