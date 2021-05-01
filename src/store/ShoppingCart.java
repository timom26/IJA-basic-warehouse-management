/**
 * @author Timotej Ponek
 * @author Timotej Kamensky
 * @copyright Brno university of technology, faculty of computer science, Czechia.
 * @brief assignment of java application for basic warehouse management system
 */
package store;

import Reader.WarehouseStruct;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class ShoppingCart {


    //  #####################  vars  ###############
    private ArrayList<Item> content = new ArrayList<>();
    private int maximumCount = 10;//prepared for future dev with variable max count.
    private int coord_x;
    private int coord_y;
    private WarehouseStruct warehouse;
    private boolean engaged = false;//use for calls
    //  ###################  constructor  #################
    /** warehouse in which the shopping cart is generated,   and starting coords x, y**/
    public ShoppingCart(WarehouseStruct warehouse,int coord_x,int coord_y){
        this.coord_x = coord_x;
        this.coord_y = coord_y;
        this.warehouse = warehouse;
    }
    //#############  class functions ###########################
    //#####  item manipulation  ############
    /**
     * @brief Fills the cart by any item of a given goods-type from a given shelf
     * @param shelf shelf to take item from
     * @param goods goodstype to take
     * @return true if successful, false if full cart or no item of type in shelf
     */
    public boolean Fill(Shelf shelf,Goods goods){
        if (this.size() >= maximumCount){
            return false;
        }
        Item takenItem = shelf.removeAny(goods);
        if (takenItem == null){
            return false;
        }
        content.add(takenItem);
        return true;
    }

    /**
     * @brief Takes out item of a given type to the given shelf
     * @param shelf shelf to pace item onto
     * @param goods goodstype of item to be placed
     * @return true if successful,false if no item of the given type to move to shelf
     */
    public boolean Empty(Shelf shelf, Goods goods){
        Item takenItem = content.stream().filter(o -> o.goods().equals(goods)).findFirst().orElse(null);
        if (takenItem == null){
            return false;
        }
        content.remove(takenItem);
        shelf.put(takenItem);
        return true;
    }

    /**
     * @brief Function Empties all of its items into the given shelf. Useful when retrieving items from warehouse
     * @param shelf shelf where it needs to be emptied
     * @return true if sucessful
     */
    public void EmptyAll(Shelf shelf){
        for (Item item: content){
            content.remove(item);
            shelf.put(item);
        }
    }
    /**
     * @brief Checks if the cart contains any item with given goodstype
     * @param goods goodstype to match
     * @return true if found, false if not
     */
    public boolean containsGoods(Goods goods){
        //https://stackoverflow.com/questions/18852059/java-list-containsobject-with-field-value-equal-to-x
        return content.stream().anyMatch(o -> o.goods().equals(goods));
    }
    /**
     * Function to retrieve count of items in the shopping cart
     * @return count of all items in the shopping cart
     */
    public int size(){
        return content.size();
    }






    //#####  movement functions   #########






    /** @brief function returns a length of route to a given shelf from current position
     * @param end_x coord x of the target shelf
     * @param end_y coord y of the target shelf
     * @return integer - length of route to a given shelf
     */
    public int routeLength(int end_x,int end_y){
        int start_x = this.coord_x;
        int start_y = this.coord_y;
        int warehouse_x = this.warehouse.getCols();
        int warehouse_y = this.warehouse.getRows();

        //the same column
        //if on western side and the shelf is in same column (western or eastern)
        if (start_x % 2 == 1 && (end_x == start_x || end_x == start_x + 1)){
            return (java.lang.Math.max(start_y,end_y) - java.lang.Math.min(start_y,end_y));
        }
        //on the eastern side and the shelf is in the same column (western or eastern)
        else if (start_x % 2 == 0 && (end_x == start_x || end_x == start_x -1)){
            return (java.lang.Math.max(start_y,end_y) - java.lang.Math.min(start_y,end_y));
        }
        else{
            //not in same column
            //calculate whether you go through top or bottom
            //divided for readability into up/down part, and left/right part
            int verticalMove = java.lang.Math.min(start_y+end_y + 2,warehouse_y - start_y + warehouse_y - end_y);
            int horizontalMove = java.lang.Math.abs(end_x - start_x);
            return verticalMove + horizontalMove;
        }

    }
    /** @brief function generates the shortest route to a given shelf and returns
     * it as a list of 2d points from current position to ending position
     * @param end_x coord x of the target shelf
     * @param end_y coord y of the target shelf
     * @return Route to target shelf - List of 2d points
     */
    public List<java.awt.Point> getCoords(int end_x, int end_y){
        int start_x = this.coord_x;
        int start_y = this.coord_y;
        int warehouse_x = this.warehouse.getCols();
        int warehouse_y = this.warehouse.getRows();
        List<java.awt.Point> pointList = new ArrayList<>();
        //the division is the same as when calculating a length of route
        //when in doubt, consult there. Here, it has been shortened
        //traded the code readability for a shorter code

        //if in the same col
        if ((start_x % 2 == 1 && (end_x == start_x || end_x == start_x + 1)) || (start_x % 2 == 0 && (end_x == start_x || end_x == start_x -1)))
        {//the same col
            if (end_y > start_y) {
                for (int i = start_y; i < end_y + 1; i++) {
                    java.awt.Point newPoint = new Point(start_x, i);
                    pointList.add(newPoint);
                }
            } else {
                for (int i = start_y; i > end_y - 1; i--) {
                    java.awt.Point newPoint = new Point(start_x, i);
                    pointList.add(newPoint);
                }
            }
            if (end_x != start_x) {
                java.awt.Point newPoint = new Point(end_x, end_y);
                pointList.add(newPoint);
            }

        }
        else
        {//not in the same col
            //first up, then down
            if (start_y + end_y + 2   <   warehouse_y - start_y  + warehouse_y - end_y) {

                //up
                for (int i = start_y;i>-1;i--){
                    java.awt.Point newPoint = new Point(start_x,i);
                    pointList.add(newPoint);
                }
                //to the sides
                if (start_x < end_x){// to the right
                    for (int i = start_x;i<end_x+1;i++){
                        java.awt.Point newPoint = new Point(i,-1);
                        pointList.add(newPoint);
                    }
                }
                else{//to the left
                    for (int i = start_x;i>end_x-1;i--){
                        java.awt.Point newPoint = new Point(i,-1);
                        pointList.add(newPoint);
                    }
                }

                //back down
                for (int i = 0; i< end_y+1;i++){
                    java.awt.Point newPoint = new Point(end_x,i);
                    pointList.add(newPoint);
                }
            }
            //first down, then up
            else{
                //down
                for (int i = start_y;i < warehouse_y; i++){
                    java.awt.Point newPoint = new Point(start_x,i);
                    pointList.add(newPoint);
                }
                //sides
                if (start_x < end_x) {
                    //right
                    for (int i = start_x; i < end_x + 1; i++) {
                        java.awt.Point newPoint = new Point(i, warehouse_y);
                        pointList.add(newPoint);
                    }
                }
                else{                    //left
                    for (int i = warehouse_y - 1; i > end_y - 1; i--) {
                        java.awt.Point newPoint = new Point(i, warehouse_y);
                        pointList.add(newPoint);
                    }
                }


                //back up
                for (int i = warehouse_y - 1; i > end_y - 1;i--){
                    System.out.println("back up");
                    java.awt.Point newPoint = new Point(end_x,i);
                    pointList.add(newPoint);
                }
            }
        }
        return pointList;
    }

    /**@brief function gets route to a shelf, and then makes the cart go to it in real time
     * @param end_x coord x of the target shelf
     * @param end_y coord y of the target shelf
     * @return None
     */
    public boolean goTo(int end_x,int end_y){
        if (end_x == this.coord_x && end_y == this.coord_y){
            return true;
        }
        List<java.awt.Point> coordList = getCoords(end_x,end_y);

        List<java.awt.Point> conflictList = this.warehouse.findConflicts(coordList);
        //first, test blockage of new target
        if (!conflictList.isEmpty()){
            //the first two checks are doing checks that make it impossible to get to a target completely
            Point targetPoint = new Point(end_x,end_y);
            Point placePoint = new Point(this.coord_x,this.coord_y);
            if (this.warehouse.isColBlocked(targetPoint,"both") || this.warehouse.isColBlocked(placePoint,"both")){
                System.out.println("ERROR: goTo - either a col of origin or destination is blocked");
                return false;
            }
            if(this.warehouse.blockedTopPaths(targetPoint,placePoint)) {
                System.out.println("ERROR: goTo - a top or bottom row is blocked on the same spot");
                return false;
            }

            // a n d   r i g h t   he r e   i s   w h e r e   i   s a i d
            //  "f u c k   i t "   a n d   i m p l e m  e nt e d   A *
            //  a l g o r i t h m . .   . .   . .   i t   t o o k   m e
            //   s o m e   t i m  e   t o   r e a l i s e  t h i s
            //   n e e d ,   b u t   i   r e a l i s e d   i t
            //   n e v e r t h e l e s s
            Point p1 = new Point(this.coord_x,this.coord_y);
            Point p2 = new Point(end_x,end_y);
            coordList = warehouse.getAStarCoords(p1,p2);
            if (coordList.isEmpty()){
                System.out.println(" E M P T Y ");
            }
            for (int i = 0; i < coordList.size(); i++) {
                System.out.print(coordList.get(i).x + " " + coordList.get(i).y + " ");
                this.coord_x = coordList.get(i).x;
                this.coord_y = coordList.get(i).y;
            }
            System.out.println("");
        }
        else {

            //if solved problem or conflict list empty, walk there
            for (int i = 0; i < coordList.size(); i++) {
                System.out.print(coordList.get(i).x + " " + coordList.get(i).y + " ");
                this.coord_x = coordList.get(i).x;
                this.coord_y = coordList.get(i).y;
            }
            System.out.println("");
        }
        return true;
    }


}
