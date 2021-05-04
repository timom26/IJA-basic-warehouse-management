package App;

import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.util.List;


public class TrolleyController {

    public static void MoveTrolley(int trolleyId, int x, int y){
        if(WarehouseController.Trolleys.size() < trolleyId)
            return;
        Rectangle movable = WarehouseController.Trolleys.get(trolleyId);
        movable.setX(movable.getX()+x);
        movable.setY(movable.getY()+y);
    }

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

    public static WarehouseController.UnitOfShift GetMovementUnit(int xFrom, int yFrom, int xTo, int yTo){
        int moduloFromX = xFrom%4;
        int moduloToX = xTo%4;

        //To discuss with teammate what indexes we allow
        //yFrom%(_controlledWarehouse.getRows()+1);
        int moduloFromY = yFrom%(WarehouseController._controlledWarehouse.getRows());
        int moduloToY = yTo%(WarehouseController._controlledWarehouse.getRows());

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
                    return WarehouseController.UnitOfShift.Err;
            }
        }
        else {
            if(moduloToY == 0 || moduloFromY == 0)
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

    /**
     *
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

            //Line RouteChunk = new Line(LineStartX, LineStartY, LineStartX+lenght.measure, LineStartY+lenght.measure);
            RouteChunk.setStroke(Color.BLUE);
            RouteChunk.setStrokeWidth(5);
            warehousePane.getChildren().add(RouteChunk);
            RouteChunk.toBack();
            trolley._route.add(RouteChunk);

            previous_x = p.x;
            previous_y = p.y;
        }
    }

    public static void ActualizeRoute(int trolleyId){
        WarehouseController.TrolleyGrid tmp;

        if(!(tmp = WarehouseController.Trolleys.get(trolleyId)).isRoutePrinted)
            return;

        //(WarehouseController._warehousePane.)getChildren().removeAll(tmp._route);
        try {
            WarehouseController._warehousePane.getChildren().removeAll(tmp._route);
            //Controller.WWW.getChildren().removeAll(tmp._route);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return;
        }
        //Controller.WWW.getChildren().removeAll(tmp._route);
        PrintRoute(tmp.boundedCart.coordList,
         //       Controller.WWW,
                WarehouseController._warehousePane,
                tmp,
                tmp.boundedCart.coordIndex);
        return;
    }

}
