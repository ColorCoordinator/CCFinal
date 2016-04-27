package com.paul.cc.colorcoordinator;
import java.util.ArrayList;

public class ColorScheme {
    public int complementary =0, analagous=0, triad=0, tetrad=0, split=0;
    public int numberColors;
    public int degreesBetweenHues;
    String scheme;

    public ColorScheme(){
    }

    public ColorScheme(ArrayList<String> schemes, ArrayList<HSLColor> colors){

        for(String s : schemes){
            if(s.equals("Complementary")){
                complementary++;
            }
            else if(s.equals("Analagous")){
                analagous++;
            }
            else if(s.equals("Triad")){
                triad++;
            }
            else if(s.equals("Tetrad")){
                tetrad++;
            }
            else if(s.equals("Split Complementary")){
                split++;
            }
        }

        if(complementary!= 0 & tetrad != 0){
            tetrad = tetrad + complementary;
            complementary = 0;
        }

        int max = Math.max(Math.max(Math.max(Math.max(complementary, analagous), triad), tetrad), split);


        if(max == complementary){
            scheme = "Complementary";
        }
        else if(max == tetrad){
            scheme = "Tetrad";
        }
        else if(max == triad){
            scheme = "Triad";
        }
        else if(max == split){
            scheme = "Split Complementary";
        }
        else if(max == analagous){
            scheme = "Analagous";
        }



    }

    public static ColorScheme findScheme(ArrayList<HSLColor> colors){
        HSLColor c1, c2;
        ArrayList<String> schemes = new ArrayList<String>();
        if(colors.size() == 1){
            schemes.add("Analagous");
        }
        else{
            for(int i = 0; i < colors.size(); i++){
                c1 = colors.get(i);
                for(int j = i + 1; j < colors.size(); j++){
                    c2 = colors.get(j);
                    float deg = c1.getDegreesBetween(c2);
                    if(deg >= 160){
                        schemes.add("Complementary");
                    }
                    else if(deg >= 135){
                        schemes.add("Split Complementary");
                    }
                    else if(deg >= 110){
                        schemes.add("Triad");
                    }
                    else if(deg >= 80){
                        schemes.add("Tetrad");
                    }
                    else if(deg > 50){
                        schemes.add("Split Complementary");
                    }
                    else schemes.add("Analagous");
                }
            }}

        return new ColorScheme(schemes, colors);
    }

    public String getName(){
        return scheme;
    }
}