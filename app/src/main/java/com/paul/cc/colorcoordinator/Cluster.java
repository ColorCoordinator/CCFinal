package com.paul.cc.colorcoordinator;

import java.util.ArrayList;


//Clusters used for K-means clustering algorithm
public class Cluster {
    public Point center;
    public Point oldCenter;
    public ArrayList<Point> points = new ArrayList<Point>();

    public Cluster(Point startPt){
        points.add(startPt);
        center = startPt;
    }
    //Calculate new center of cluster based on all points associated with it
    public void newCenter(){
        double x = 0,y = 0,z = 0;
        for(Point pt : points){
            x += pt.x;
            y += pt.y;
            z += pt.z;
        }
        oldCenter = center;
        center = new Point(x/points.size(), y/points.size(), z/points.size());
    }

    public void addPoint(Point p){
        points.add(p);
    }

    public Point getCenter(){
        return center;
    }
}