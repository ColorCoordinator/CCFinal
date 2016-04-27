package com.paul.cc.colorcoordinator;

public class Point {
    public int clusterIndex = -1; //-1 means not part of a cluster yet
    public double x, y, z; //For use in this project, these will be rgb values

    public Point(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void setIndex(int index){
        clusterIndex = index;
    }

    public int getIndex(){
        return clusterIndex;
    }

    public double getDistance(Point pt){
        double squarex = Math.pow((pt.x - x), 2.0);
        double squarey = Math.pow((pt.y - y), 2.0);
        double squarez = Math.pow((pt.z - z), 2.0);
        double dist = Math.sqrt((squarex + squarey + squarez));
        return dist;
    }
}