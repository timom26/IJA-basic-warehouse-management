package App;

import Reader.CartStruct;
import Reader.WarehouseStruct;
import store.ShoppingCart;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ClockController {

    //static ScheduledExecutorService defaultExecService;

    private static List<ScheduledExecutorService> _allExecutors;

    // instance properties
    private CartStruct.Trolley _trolley;
    private WarehouseStruct _workplace;
    private ShoppingCart _cart;
    private int _orderIndex;
    private CartStruct.Trolley.targetIndex.OnePoint _currentShelfToGo;
    private List<java.awt.Point> _coordList;
    private int _atWaypoint;
    private int _WaypointSize;

    public static void SetTime(int wtf){

    }

    public ClockController(CartStruct.Trolley trolley, WarehouseStruct workplace){
        _trolley = trolley;
        _workplace = workplace;

        _cart = new ShoppingCart(workplace,0,11);
        int _orderIndex = 0;

        Boolean inMotion = false;
        _currentShelfToGo = trolley.allWaypoints.get(_orderIndex).GetFirstPoint();

        _coordList = _cart.getCoords(_currentShelfToGo.getY(), _currentShelfToGo.getX()); // shelfs are generated in reverse so we have to flip values
        int _atWaypoint = 0;
        int _WaypointSize = _coordList.size();

        final ScheduledExecutorService defaultExecService = Executors.newSingleThreadScheduledExecutor();

        defaultExecService.scheduleAtFixedRate(this::TrolleyRoutine, 0, 1, TimeUnit.SECONDS);

    }

    private void TrolleyRoutine(){
        if(_atWaypoint <= _WaypointSize){
            //go fuck someone
            WarehouseController.MoveTrolley(1,0,-20);
        }
        else {
            //private
            _orderIndex += 1;
            if(_orderIndex < _trolley.allWaypoints.size()) {
                _currentShelfToGo = _trolley.allWaypoints.get(_orderIndex).GetFirstPoint();
                _coordList = _cart.getCoords(_currentShelfToGo.getY(), _currentShelfToGo.getX()); // shelfs are generated in reverse so we have to flip values
            }
            else {
                ;//exit procedure
            }
        }


    }

//    public static void TrolleyMovement(CartStruct.Trolley trolley, WarehouseStruct workplace){
//        ScheduledExecutorService defaultExecService = Executors.newSingleThreadScheduledExecutor();
//
//        defaultExecService.scheduleAtFixedRate(ClockController.TrolleyRoutine(trolley,workplace), 0, 1, TimeUnit.SECONDS);
//    }


//    protected class TrolleyRoutine{
//        private int x;
//        private int y;
//
//        TrolleyRoutine(){
//
//
//        }
//
//    }



}

