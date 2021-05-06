package Reader;

import java.awt.*;
import java.util.Collections;

public class AStarNode implements Comparable<AStarNode> {
    private java.awt.Point point;
    private java.awt.Point expandedBy;
    private Double distanceBeggining;
    private Double distanceBegginingEnd;

    //constructors and value get/setters
    public AStarNode(){

    }
    public AStarNode(java.awt.Point point){
        this.point = point;
    }
    public AStarNode(java.awt.Point point,Double distanceBeggining, Double distanceBegginingEnd){
        this.point = point;
        this.distanceBeggining = distanceBeggining;
        this.distanceBegginingEnd = distanceBegginingEnd;
    }
    public AStarNode(java.awt.Point point, Double distanceBeggining, Double distanceBegginingEnd, java.awt.Point expandedBy){
        this.point = point;
        this.distanceBeggining = distanceBeggining;
        this.distanceBegginingEnd = distanceBegginingEnd;
        this.expandedBy = expandedBy;
    }

    public void setDistanceBeggining(Double distanceBeggining) {
        this.distanceBeggining = distanceBeggining;
    }
    public Double getDistanceBeggining(){
        return this.distanceBeggining;
    }
    public void setDistanceBegginingEnd(Double distanceBegginingEnd){
        this.distanceBegginingEnd = distanceBegginingEnd;
    }
    public  Double getDistanceBegginingEnd(){
        return this.distanceBegginingEnd;
    }
    public void setPoint(java.awt.Point point){
        this.point = point;
    }
    public Point getExpandedBy(){
        return this.expandedBy;
    }
    public void setExpandedBy(Point expandedBy){
        this.expandedBy = expandedBy;
    }
    public java.awt.Point getPoint(){
        return this.point;
    }
    public int compareTo(AStarNode a) {//allows sorting a list of AStarNodes, based on total distance
        if (this.distanceBegginingEnd > a.distanceBegginingEnd)
            return 1;
        else if (this.distanceBegginingEnd < a.distanceBegginingEnd)
            return -1;
        else {
            return 0;
        }
    }
}
