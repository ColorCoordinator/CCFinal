package com.paul.cc.colorcoordinator;


import android.graphics.Bitmap;
import android.graphics.Color;

import java.util.ArrayList;
import java.util.Random;

public class ColorFinder {

    public int k = 8;
    public ArrayList<Cluster> clusters = new ArrayList<Cluster>();
    public ArrayList<Point> allPoints = new ArrayList<Point>();
    public ArrayList<HSLColor> colors = new ArrayList<HSLColor>();
    public ArrayList<HSLColor> primaryHues = new ArrayList<HSLColor>();
    public static Bitmap imageBitmap;

    public ArrayList getColors(){
        return colors;
    }

    public int getNearestClusterIndex(Point pt){
        double smallest = 442; //This is the maximum value between points (255,255,255) and (0,0,0)
        int clusterIndex = -1;
        for (Cluster cluster : clusters){
            double dist = pt.getDistance(cluster.getCenter());
            if (dist < smallest){
                smallest = dist;
                clusterIndex = clusters.indexOf(cluster);
            }
        }
        return clusterIndex;
    }

    /*	public void getPrimaryHues(){
            for(int i = 0; i < colors.size(); i++){
                boolean flag = true;
                for(int j = i+1; j<colors.size(); j++){
                    HSLColor c1 = colors.get(i);
                    HSLColor c2 = colors.get(j);
                    if(c1.getDegreesBetween(c2) < 10){
                        flag = false;
                    }
                }
                if(flag){primaryHues.add(colors.get(i));
                }
            }
        }*/
    public ColorFinder(Bitmap bmap, int k){


        //Open image and create all rgb points
        imageBitmap = bmap;
        this.k = k;

        //Image smallImg = image.getScaledInstance(200, 200, java.awt.Image.SCALE_DEFAULT);
        for (int y=0; y<imageBitmap.getHeight(); y++){
            for(int x=0; x<imageBitmap.getWidth(); x++){
                int rgb = imageBitmap.getPixel(x, y);
                double r = Color.red(rgb);
                double g = Color.green(rgb);
                double b = Color.blue(rgb);
                allPoints.add(new Point(r,g,b));
            }
        }
        for(int i = 0; i < k; i++){
            Random random = new Random();
            clusters.add(new Cluster(allPoints.get(random.nextInt(allPoints.size()))));

        }
        run();
    }

    public void run(){
        boolean flag = true;
        int iters = 0;
        while(flag){
            for (Point p : allPoints){
                int index = getNearestClusterIndex(p);
                clusters.get(index).addPoint(p);
            }
            flag = false;
            for (Cluster c : clusters){
                c.newCenter();
                c.points.clear();
                if(c.center.getDistance(c.oldCenter) > 1.0){
                    flag = true;
                }
            }
            iters++;
        }
        for (Point p : allPoints){
            int index = getNearestClusterIndex(p);
            clusters.get(index).addPoint(p);
        }
        for(Cluster c : clusters){
            Point center = c.getCenter();
            HSLColor color = new HSLColor(Color.rgb((int)center.x, (int)center.y, (int)center.z));
            colors.add(color);
        }
        //getPrimaryHues();
	/*for(HSLColor c : primaryHues){
		//System.out.println(c.toString());
		if(c.getLuminance() > 90){System.out.println("White");}
		else if(c.getLuminance() < 10){System.out.println("Black");}
		else if(c.getSaturation() < 10){System.out.println("Gray");}
		else{
			System.out.println(HSLColor.printHue((int) c.getHue()));
		}
	}*/
    }
}