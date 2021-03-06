/**
 * @author Timotej Ponek xponek00
 * @author Timotej Kamensky xkamen24
 * @copyright Brno university of technology, faculty of computer science, Czechia.
 * @brief implementation of a single warehouse
 */
package Reader;
import store.Goods;
import store.Shelf;

import java.awt.*;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;


/**
 * represents warehouse as a grid of shelves
 * */
public class WarehouseStruct {
    public List shelves = new ArrayList();
    public List<Goods> goods = new ArrayList();
    public List<Point> closedPaths = new ArrayList();
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

    public List GetGoodsNames(){
        return goods.stream().map(Goods::getName).sorted().collect(Collectors.toList());
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

    public int getMax_x() {
        return cols + cols - (cols%2 == 0 ? 2 : 1);
    }

    public int getMax_y() {
        return this.rows + 2;
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


    public List<Point> getAStarCoords(Point startPoint, Point endPoint, String operation){ ;
        //it is a point of a shelf,
        // and i want to change it
        // to a corresponding tile in front of that given shelf
        if (operation.equals("shelf")){
            if (endPoint.x % 2 == 0) {//western shelf
                endPoint.x = endPoint.x * 2 - 1 ;
            }
            else if (endPoint.x % 2 == 1) {//eastern shelf
                endPoint.x = endPoint.x * 2;
            }
            else{
                throw new InvalidParameterException("this should not happen. probably, point of a shelf was given instead of tile");
            }
        }
        else if (operation == "tile"){}
        else{
            throw new InvalidParameterException("unknown parameter in function getAStarCoords");
        }
        List<AStarNode> alreadyExpanded = new ArrayList<>();
        List<AStarNode> queue = new ArrayList<>();
        List<Point> coordsList = new ArrayList<>();//results
        AStarNode startNode = new AStarNode(startPoint,0.0,getFastDistance(startPoint,endPoint),null);
        queue.add(startNode);
        while(true){
            if (queue.isEmpty()){
                return coordsList;//empty one, did not find the way
            }
            // g e t   a   p o i n t   t o   e x p a n d
            AStarNode currentNode = queue.get(0);
            queue.remove(0);
            alreadyExpanded.add(currentNode);
            //   i f   f i n a l ,   g e t   r o u t e   a n d   r e t u r n

            if (currentNode.getPoint().x == endPoint.x && currentNode.getPoint().y == endPoint.y ){
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
                        Collections.reverse(coordsList);//so it goes from cart to target
                        coordsList.add(endPoint);
                        return  coordsList;
                    }
                }
            }
            //e x p a n d i n g   a   p o i n t
            List<Point> bufferList= getAStarNeighbors(currentNode.getPoint());
            //add the points into a queue, ignore already expanded, update existing
            boolean expanded = false;
            boolean updated = false;
            for (Point p:bufferList) {
                for (AStarNode already : alreadyExpanded) {//if already expanded, skip
                    if (p.equals(already.getPoint())) {
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
    public List<Point> getAStarNeighbors(Point point){

        if (point.x > this.getMax_x() || point.x < -1 || point.y < -1 || point.y > this.getRows() + 1){
            throw new InvalidParameterException("the point ot out of warehouse bounds");
        }


        List<Point> returnList = new ArrayList<>();

        //nowhere more than three neighbours
        Point p1 = new Point();
        Point p2 = new Point();
        Point p3 = new Point();
        Point p4 = new Point();
        boolean p1_set = false;
        boolean p2_set = false;
        boolean p3_set = false;
        boolean p4_set = false;

        //if in col && not on the edge, get a tile from the other side of col
        if (point.x > 0 && point.x < this.getMax_x() && point.y > -1 && point.y < this.getRows()){
            //System.out.print("standard col");
            p1_set = true;
            p2_set = true;
            p3_set = true;

            if (point.x % 4 == 3){//eastern col
                //add a point to the west
                p1.x = point.x - 1;
                p1.y = point.y;

            }
            else if (point.x % 4 == 2)//western col
            {
                //add a point to the east
                p1.x = point.x + 1;
                p1.y = point.y;
            }
            else{
                throw new InvalidParameterException("you are asking for neighbours of a shelf");
            }
            //and add points up and down from it
            p2.x = point.x;
            p2.y = point.y-1;//up
            p3.x = point.x;
            p3.y = point.y+1;//down
        }
        //if is in first or last col, add only up or down
        if ((point.x == -1 || point.x == this.getMax_x()) && point.y > -1 && point.y < this.getRows()){
            //System.out.print("   first or last col   ");

            p2_set = true;
            p3_set = true;
            p2.x = point.x;
            p2.y = point.y-1;//up
            p3.x = point.x;
            p3.y = point.y+1;//down
        }

        if((point.y == -1  ||  point.y == this.getRows() || point.y == this.getRows() + 1)    && point.x >= 0  && point.x <= this.getMax_x()){
            //in a horizontal walkway, and can add a neighbour to the left

            p1.x = point.x - 1;
            p1.y = point.y;
            p1_set = true;
        }
        if((point.y == -1  ||  point.y == this.getRows() || point.y == this.getRows() + 1)  && point.x >= -1   && point.x != this.getMax_x()){
            //in a horizontal walkway, and can add a neighbour to the right

            p2.x = point.x + 1;
            p2.y = point.y;
            p2_set = true;
        }
        if (point.y == -1){//upper row
            if (point.x % 4 == 2 || point.x % 4 ==3 || point.x == -1){
                //if in upper horizontal row
                p3.x = point.x;
                p3.y = point.y+1;
                p3_set = true;
            }


        }
        if (point.y == getRows()){//lower row
            if (point.x % 4 == 2 || point.x % 4 ==3 || point.x == -1) {
                //System.out.print(" and printed ");
                //if in lower horizontal row
                p3.x = point.x;
                p3.y = point.y - 1;
                p3_set = true;
            }
            p4_set = true;//always down
            p4.x = point.x;
            p4.y = point.y +1;
        }
        if(point.y == getRows() + 1){
            //in garages, go up (sideways it is standardised)
            p3_set = true;
            p3.x = point.x;
            p3.y = point.y -1;
        }

        //check all points if not blocked
        if (p1_set){
            if (!this.closedPaths.contains(p1)){
                returnList.add(p1);
            }
        }
        if (p2_set) {
            if (!this.closedPaths.contains(p2)) {
                returnList.add(p2);
            }
        }
        if (p3_set){
            if (!this.closedPaths.contains(p3)){
                returnList.add(p3);
            }
        }
        if (p4_set){
            if (!this.closedPaths.contains(p4)){
                returnList.add(p4);
            }
        }
        return returnList;
    }

    /**@brief implements a*a + b*b = c*c**/
    public Double getFastDistance(Point p1,Point p2){
        return sqrt(  abs(p1.x - p2.x)*abs(p1.x - p2.x)  +   abs(p1.y - p2.y)*abs(p1.y - p2.y)  );
    }

}