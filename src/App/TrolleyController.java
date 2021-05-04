package App;

import javafx.scene.shape.Rectangle;


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

        //This just skippes rectangle in between
//        if(xFrom%2 != 0){
//            movable.setX(movable.getX()+10);
//        }
    }
}
