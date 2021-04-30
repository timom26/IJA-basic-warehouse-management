/**
 * @author Timotej Ponek xponek00
 * @author Timotej Kamensky xkamen24
 * @copyright Brno university of technology, faculty of computer science, Czechia.
 * @brief assignment of java application for basic warehouse management system
 */
package Reader;
import store.Shelf;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
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
        for (Point p : this.closedPaths){// big O=closed*wishlist (but we have a small storage so we dont have to care)
            if (wishlist.contains(p)){
                conflictList.add(p);
            }
        }
        return conflictList;
    }

    public boolean isColBlocked(java.awt.Point testedP){
        //first test
        if (this.closedPaths.contains(testedP)){
            System.out.println("COLBLOCK: the exact target tile is blocked");
            return true;
        }

        //prepare for a test of a whole col
        int leftcol;
        int rightcol;
        boolean blockedTop = false;
        boolean blockedBottom = false;

        if (testedP.x % 2 == 0){//at eastern shelf
            leftcol = testedP.x-1;
            rightcol = testedP.x;
        }
        else{//at western shelf
            leftcol = testedP.x;
            rightcol = testedP.x+1;
        }
        Point point0 = new Point();
        Point point1 = new Point();
        Point point2 = new Point();
        Point point3 = new Point();
        point0.x = leftcol;
        point1.x = rightcol;point2.x=rightcol;point3.x=rightcol;

        //fork from left col to the three tiles on right
        for (int i = testedP.y-1;i>=-1;i--){
            point0.y=i;point1.y = i+1;point2.y=i;point3.y=i-1;
            if (testTwoPoints(point0,point1) || testTwoPoints(point0,point2) || testTwoPoints(point0,point3)){
                blockedTop = true;
                break;//not strictly needed, is there for optimalisation
            }
        }
        //in two for loops, so i can
        //check if blocked from both top and bottom
        for (int i = testedP.y+1;i<=this.getRows();i++){
            point0.y=i;point1.y = i+1;point2.y=i;point3.y=i-1;
            if (testTwoPoints(point0,point1) || testTwoPoints(point0,point2) || testTwoPoints(point0,point3)){
                blockedBottom = true;
                break;//not strictly needed, is there for optimalisation
            }
        }
        if (blockedTop && blockedBottom){
            System.out.println("col blocked");
            return true;
        }

        return false;
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
}
