/**
 * @author Timotej Ponek xponek00
 * @author Timotej Kamensky xkamen24
 * @copyright Brno university of technology, faculty of computer science, Czechia.
 * @brief assignment of java application for basic warehouse management system
 */
package Reader;
import javafx.util.Pair;
import store.Shelf;

import javax.management.ValueExp;
import java.awt.*;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;


/**
 * represents warehouse as a grid of shelves
 * */
public class WarehouseStruct {
    public List shelves = new ArrayList();
    public List goods = new ArrayList();
    public List<Point> closedPaths = new ArrayList<>();
    private int rows;
    private int cols;


    /**
     * @brief returns list of goods from a given shelf
     * @param row coord of a shelf
     * @param coll coord of a shelf
     * @return string, containing all goods
     */
    public String PrintGoods(int row, int coll){
        return ((Shelf) ((ArrayList) shelves.get(row)).get(coll)).getAll();
    }

    /**
     * @brief returns max rows of the warehouse
     * @return rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * @brief set rows of a warehouse
     * @param rows - set this value
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * @brief returns max cols of the warehouse
     * @return cols
     */
    public int getCols() {
        return cols;
    }

    /**
     * @brief set cols of a warehouse
     * @param cols set this value
     */
    public void setCols(int cols) {
        this.cols = cols;
    }

    /**@brief add blockage of a given tile**/
    public void addBlockage(int x,int y){
        Point point = new Point(x,y);
        this.closedPaths.add(point);
    }

    public void addBlockage(Point point){
        this.closedPaths.add(point);
    }
    /**@brief remove a blockage of a given tile**/
    public void removeBlockage(int x,int y){
        Point point = new Point (x,y);
        this.closedPaths.remove(point);
    }
    public void removeBlockage(Point point){
        this.closedPaths.remove(point);
    }
    /** @brief find path conflicts with a proposed path. Fast and doesnt do many checks
     * closed paths are not widely expected, therefore a fast checkup.
     * @param wishlist a list of points that we want to go through
     * @return list of points that we wish to go through but are closed
     * **/
    public List<Point> findConflicts(List<Point> wishlist){
        List<Point> conflictList = new ArrayList<>();
        //the commented should be faster, since the closed path will generally
        //be smaller. But, i want the conflictList to be ordered. Bad Luck.
        /*for (Point p : this.closedPaths){// big O=closed*wishlist (but we have a small storage so we dont have to care)
            if (wishlist.contains(p)){
                conflictList.add(p);
            }
        }*/
        for (Point p : wishlist){
            if (this.closedPaths.contains(p)){
                conflictList.add(p);
            }
        }
        return conflictList;
    }

    /**side function to assess if both of the given points are blocked
     * @param point1
     * @param point2
     * @return true if both points are in the list of blocked
     */
    private boolean testTwoPoints(Point point1,Point point2){
        if (this.closedPaths.contains(point1) && this.closedPaths.contains(point2)){
            return true;
        }
        return false;
    }


    public List<Point> getAStarCoords(Point startPoint, Point endPoint){
        List<AStarNode> alreadyExpanded = new ArrayList<>();
        List<AStarNode> queue = new ArrayList<>();
        List<Point> coordsList = new ArrayList<>();//results
        AStarNode startNode = new AStarNode(startPoint,0.0,getFastDistance(startPoint,endPoint),null);
        queue.add(startNode);
        while(true){
            if (queue.isEmpty()){
                return coordsList;//empty one
            }
            // g e t   a   p o i n t   t o   e x p a n d
            AStarNode currentNode = queue.get(0);
            queue.remove(0);
            alreadyExpanded.add(currentNode);
            //   i f   f i n a l ,   g e t   r o u t e   a n d   r e t u r n
            if (currentNode.getPoint().equals(endPoint) ){
                //System.out.println("  f  o  u  n  d     e  n  d  p  o  i  n  t  ,     g   e  t  t  i  n  g  a  p  a  t  h  b  a  c  k    ");
                //System.out.println("found point " + currentNode.getPoint() + " on cycle " + count);
                AStarNode lastNode = currentNode;
                int count2 = 0;
                while(true){
                    //coordsList.add(lastNode.getPoint());
                    for (AStarNode n : alreadyExpanded){
                        if (n.getPoint().equals(lastNode.getExpandedBy())){
                            coordsList.add(n.getPoint());
                            lastNode = n;
                            continue;
                        }
                    }
                    if (lastNode.getPoint().equals(startPoint)){
                        System.out.println("returning ");
                        Collections.reverse(coordsList);//so it goes from cart to target
                        coordsList.add(endPoint);
                        return  coordsList;
                    }
                }
            }
            //e x p a n d i n g   a   p o i n t
            List<Point> bufferList= getNeighbors(currentNode.getPoint());
            //System.out.println("neighbours are: " + bufferList);
            //add the points into a queue, ignore already expanded, update existing
            boolean expanded = false;
            boolean updated = false;
            for (Point p:bufferList) {
                //System.out.println("processing a point" + p);
                for (AStarNode already : alreadyExpanded) {//if already expanded, skip
                    //System.out.println("comparing " + p + " with " + already.getPoint());
                    if (p.equals(already.getPoint())) {
                        //System.out.println("point was already expanded, ignore");
                        expanded = true;
                    }
                }
                if (expanded == false){//maybe in queue
                    for (AStarNode queued : queue) {
                        if (queued.getPoint().equals(p)) {
                            if ((getFastDistance(p,endPoint)+getFastDistance(startPoint,p)) < queued.getDistanceBegginingEnd() ) {
                                queued.setDistanceBeggining(getFastDistance(p,startPoint));
                                queued.setDistanceBegginingEnd(getFastDistance(p,startPoint) + getFastDistance(p,endPoint));
                                queued.setExpandedBy(currentNode.getPoint());
                            }
                            updated = true;
                        }
                    }
                }//if not in queue, add the point to a queue
                if (updated == false && expanded == false) {
                    AStarNode newNode = new AStarNode(p,getFastDistance(startPoint,p),getFastDistance(startPoint,p) + getFastDistance(p,endPoint),currentNode.getPoint());
                    queue.add(newNode);
                }
                updated = false;
                expanded = false;
            }
            Collections.sort(queue);//sort the queue by distance to end
        }
    }

    /**@brief get neighbours of the given point into a
     * list and return them, if they are walkable to(not blocked)**/
    public List<Point> getNeighbors(Point point){
        List<Point> returnList = new ArrayList<>();

        //nowhere more than three neighbours
        Point p1 = new Point();
        Point p2 = new Point();
        Point p3 = new Point();
        boolean p1_set = false;
        boolean p2_set = false;
        boolean p3_set = false;

        if (point.x > 0 && point.x < this.getCols() && point.y > -1 && point.y < this.getRows()){
            p1_set = true;
            p2_set = true;
            p3_set = true;
            if (point.x % 2 == 0){//eastern col
                //add a point to the west
                p1.x = point.x - 1;
                p1.y = point.y;

            }else{
                //add a point to the east
                p1.x = point.x + 1;
                p1.y = point.y;
            }
            //and add points up and down from it
            p2.x = point.x;
            p2.y = point.y-1;//up
            p3.x = point.x;
            p3.y = point.y+1;//down
        }
        if (point.x == 0 || point.x == this.getCols() && point.y > -1 && point.y < this.getRows()){
            p2_set = true;
            p3_set = true;
            p2.x = point.x;
            p2.y = point.y-1;//up
            p3.x = point.x;
            p3.y = point.y+1;//down
        }

        if((point.y == -1  ||  point.y == this.getRows())    && point.x != 0  && point.x != this.getCols()){
            //in a horizontal walkway, and can add a neighbour to the left
            p1.x = point.x - 1;
            p1.y = point.y;
            p1_set = true;
        }
        if((point.y == -1  ||  point.y == this.getRows())    && point.x != this.getCols()){
            //in a horizontal walkway, and can add a neighbour to the right
            p2.x = point.x + 1;
            p2.y = point.y;
            p2_set = true;
        }
        if (point.y == -1){
            //if in upper horizontal row
            p3.x = point.x;
            p3.y = point.y+1;
            p3_set = true;
        }
        if (point.y == getRows()){
            //if in lower horizontal row
            p3.x = point.x;
            p3.y = point.y-1;
            p3_set = true;
        }


        //check if not blocked
        if (p1_set){
            if (!this.closedPaths.contains(p1)){
                //System.out.print("added p1 with value: " + p1);
                returnList.add(p1);
            }
        }
        if (p2_set) {
            if (!this.closedPaths.contains(p2)) {
                //System.out.print("added p2 with value: " + p2);

                returnList.add(p2);
            }
        }
        if (p3_set){
            if (!this.closedPaths.contains(p3)){
                //System.out.print("added p3 with value: " + p3);

                returnList.add(p3);
            }
        }
        return returnList;
    }

    /**@brief implements a*a + b*b = c*c**/
    public Double getFastDistance(Point p1,Point p2){
        return sqrt(  abs(p1.x - p2.x)*abs(p1.x - p2.x)  +   abs(p1.y - p2.y)*abs(p1.y - p2.y)  );
    }
}

