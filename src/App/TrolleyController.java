/**
 * @author Timotej Ponek xponek00
 * @author Timotej Kamensky xkamen24
 * @copyright Brno university of technology, faculty of computer science, Czechia.
 * @brief implementation of trolley (that is: guiding struct for a single Shopping Cart)
 */
package App;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.List;


public class TrolleyController {

    /**
     * @brief change position of a trolley
     * @param trolleyId id of given trolley
     * @param x x coordinate to move by
     * @param y y coordinate to move by
     */
    public static void MoveTrolley(int trolleyId, int x, int y){
        if(WarehouseController.Trolleys.size() < trolleyId)
            return;
        Rectangle movable = WarehouseController.Trolleys.get(trolleyId);
        movable.setX(movable.getX()+x);
        movable.setY(movable.getY()+y);
    }

    /**
     * @brief first initialisation of position of the given trolley
     * @param trolleyId id of given trolley
     * @param x x coordinate of Pane
     * @param y y coordinate of Pane
     */
    public static void PlaceTrolley(int trolleyId, int x, int y){
        if(WarehouseController.Trolleys.size() < trolleyId)
            return;
        Rectangle movable = WarehouseController.Trolleys.get(trolleyId);
        movable.setX(x);
        movable.setY(y);
    }

    /**
     * @brief change position of a trolley to a given direction by a single tile size
     * @param trolley id of trolley to be shifted
     * @param unit graphical shift
     * @param where direction of travel
     */
    public static void MoveTrolleyByUnit(Rectangle trolley, WarehouseController.UnitOfShift unit, WarehouseController.Direction where){
        switch (where){
            case left:
                trolley.setX(trolley.getX()-unit.measure);
                break;
            case right:
                trolley.setX(trolley.getX()+unit.measure);
                break;
            case up:
                trolley.setY(trolley.getY()-unit.measure);
                break;
            case down:
                trolley.setY(trolley.getY()+unit.measure);
                break;
        }
    };

    /**
     * @brief help function to calculate graphical shift of a given trolley
     * @param xFrom
     * @param yFrom
     * @param xTo
     * @param yTo
     * @return
     */
    public static WarehouseController.UnitOfShift GetMovementUnit(int xFrom, int yFrom, int xTo, int yTo){
        int moduloFromX = (xFrom+1)%4;
        int moduloToX = (xTo+1)%4;

        int moduloFromY = (yFrom+1)%(WarehouseController._controlledWarehouse.getRows()+1);
        int moduloToY = (yTo+1)%(WarehouseController._controlledWarehouse.getRows()+1);
        boolean Parking = yTo == WarehouseController._controlledWarehouse.getRows()+1 ||
                        yFrom == WarehouseController._controlledWarehouse.getRows()+1;

        if(yFrom == yTo){
            switch (moduloFromX+moduloToX){
                case 1:
                case 5:
                    return WarehouseController.UnitOfShift.shelfGridWidth;
                case 3:
                    if (moduloFromX == 0 || moduloFromX == 3)
                        return WarehouseController.UnitOfShift.gridSize;
                    else
                        return WarehouseController.UnitOfShift.shelfWidth;
                default:
                    return WarehouseController.UnitOfShift.gridSize;
            }
        }
        else {
            if(Parking)
                return WarehouseController.UnitOfShift.gridSize;
            else if(moduloToY == 0 || moduloFromY == 0)
                return WarehouseController.UnitOfShift.shelfGridHeight;
            else
                return WarehouseController.UnitOfShift.shelfHeight;
        }
    }


    /**
     * Move trolley up, down, left, right based on the differences of indexes
     * @param trolleyId
     * @param xFrom
     * @param yFrom
     * @param xTo
     * @param yTo
     */
    public static void MoveTrolleyFromTo(int trolleyId, int xFrom, int yFrom, int xTo, int yTo){
        /** JavaFX things cannot be updated outside JavaFX thread */
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if(WarehouseController.Trolleys.size() < trolleyId)
                    return;
                Rectangle movable = WarehouseController.Trolleys.get(trolleyId);

                WarehouseController.Direction where;
                if(xFrom > xTo){
                    where = WarehouseController.Direction.left;
                }
                else if(xFrom < xTo){
                    where = WarehouseController.Direction.right;
                }
                else if(yFrom > yTo){
                    where = WarehouseController.Direction.up;
                }
                else {
                    where = WarehouseController.Direction.down;
                }
                MoveTrolleyByUnit(movable,
                        GetMovementUnit(xFrom, yFrom, xTo, yTo),
                        where);

            }
        });
    }

    /**
     * @brief show route graphically as a line
     * @param coordList contains route to print
     * @param warehousePane Pane
     * @param trolley Rectangle
     */
    public static void PrintRoute(List<Point> coordList, Pane warehousePane, WarehouseController.TrolleyGrid trolley, int startIndex){
        if (coordList == null)
            return;

        int previous_x = coordList.get(startIndex).x;
        int previous_y = coordList.get(startIndex).y;

        double LineStartX = trolley.getX()+5;
        double LineStartY = trolley.getY()+5;
        WarehouseController.UnitOfShift length;

        int end = coordList.size();


        for (Point p; startIndex < end; startIndex++){
            p = coordList.get(startIndex);

            if(previous_x == p.x && previous_y == p.y)
                continue;

            length = TrolleyController.GetMovementUnit(previous_x, previous_y, p.x, p.y);
            Line RouteChunk;

            if(previous_x > p.x){
                RouteChunk = new Line(LineStartX, LineStartY, LineStartX-length.measure, LineStartY);
                LineStartX = LineStartX - length.measure;
            }
            else if(previous_x < p.x){
                RouteChunk = new Line(LineStartX, LineStartY, LineStartX+length.measure, LineStartY);
                LineStartX = LineStartX + length.measure;
            }
            else if(previous_y > p.y){
                RouteChunk = new Line(LineStartX, LineStartY, LineStartX, LineStartY - length.measure);
                LineStartY = LineStartY - length.measure;
            }
            else {
                RouteChunk = new Line(LineStartX, LineStartY, LineStartX, LineStartY + length.measure);
                LineStartY = LineStartY + length.measure;
            }

            RouteChunk.setStroke(Color.BLUE);
            RouteChunk.setStrokeWidth(5);
            warehousePane.getChildren().add(RouteChunk);
            RouteChunk.toFront();
            trolley._route.add(RouteChunk);

            previous_x = p.x;
            previous_y = p.y;
        }
    }

    /** @brief function is called to redraw a graphical route, when the route was changed
     * @param trolleyId
     */
    public static void ActualizeRoute(int trolleyId){
        /** JavaFX things cannot be updated outside JavaFX thread */
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                WarehouseController.TrolleyGrid tmp;

                if(!(tmp = WarehouseController.Trolleys.get(trolleyId)).isRoutePrinted)
                    return;
                WarehouseController._warehousePane.getChildren().removeAll(tmp._route);

                PrintRoute(tmp.boundedCart.coordList,
                        WarehouseController._warehousePane,
                        tmp,
                        tmp.boundedCart.coordIndex);
                tmp.toFront();
                return;
            }
        });

    }

}
