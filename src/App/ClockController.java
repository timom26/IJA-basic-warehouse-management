package App;

import Reader.CartStruct;
import Reader.AStarNode;
import Reader.WarehouseStruct;
import store.ShoppingCart;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ClockController {

    //static ScheduledExecutorService defaultExecService;

    private static boolean _pause = false;
    private static int _delay = 1000;
    //private static WarehouseStruct _warehouse; //TODO this will be removed in production
    private static List<ScheduledExecutorService> _allExecutors;
    private ScheduledExecutorService scheduledExecutorService;
    //private static Runnable Task;


    // instance properties
    private CartStruct.Trolley _trolley;
    private WarehouseStruct _workplace;
    private ShoppingCart _cart;
    private int _orderIndex;
    private CartStruct.Trolley.targetIndex.OnePoint _currentShelfToGo;
    private List<java.awt.Point> _coordList;
    private int _atWaypoint;
    private int _WaypointSize;
    private ScheduledExecutorService _defaultExecutor;
    private ScheduledFuture<?> futureTask;

    public static void SetTime(int delay){
        _delay = delay;
    }

    public static void UpdateTimer(List<ClockController> toUpdate){
        if (toUpdate == null)   return;

        for (ClockController tmp: toUpdate) {
            if(!tmp._defaultExecutor.isShutdown())
                tmp.changeTime();
        }
    }


    public void changeTime(){
        if(this.futureTask != null){
            futureTask.cancel(true);
        }

        futureTask = _defaultExecutor.scheduleAtFixedRate(this::TrolleyRoutine, 0, _delay, TimeUnit.MILLISECONDS);

//        if(_allExecutors != null){
//            for (ScheduledExecutorService modify: _allExecutors) {
//                if ( != null)
//                {
//                    futureTask.cancel(true);
//                }
//
//                futureTask = scheduledExecutorService.scheduleAtFixedRate(myTask, 0, time, TimeUnit.SECONDS);
//            }
//        }
    }


    public static void DispatchAll(){
        if(_allExecutors != null){
            for (ScheduledExecutorService toDestroy: _allExecutors) {
                toDestroy.shutdown();
            }
            _allExecutors = new ArrayList<>();
        }
        else {
            _allExecutors = new ArrayList<>();
        }
    }

    public static void Pause(){
        _pause = !_pause;
    }

    public ClockController(CartStruct.Trolley trolley, WarehouseStruct workplace){
        _trolley = trolley;
        _workplace = workplace;

        _cart = new ShoppingCart(workplace,-1,10);
        _orderIndex = 0;

        //This will be moved to Controller class in production
        WarehouseController.BoundCartToTrolley(0, _cart);
        WarehouseController.AddTrolleyToolTip();

        Boolean inMotion = false;
        _currentShelfToGo = trolley.allWaypoints.get(_orderIndex).GetFirstPoint();

        //_coordList = _cart.getAStarCords(_currentShelfToGo.getY(), _currentShelfToGo.getX());
        // These will be used later
        _cart.goal_x = _currentShelfToGo.getY(); _cart.goal_y = _currentShelfToGo.getX();
        _cart.planRoute(_cart.goal_x, _cart.goal_y); // shelfs are generated in reverse so we have to flip values
        //_cart.planRoute(_cart.goal_x-1, _cart.goal_y); // shelfs are generated in reverse so we have to flip values


        //_cart.planRoute(10, 16); // shelfs are generated in reverse so we have to flip values

        int endOfWarehouse = _cart.warehouse.getCols() + _cart.warehouse.getCols() /2 + (_cart.warehouse.getCols()%2 == 0 ? 1 : 2);
        //_cart.planRoute(_cart.warehouse.getCols(), _cart.goal_y);

        _coordList = _cart.coordList;
        _atWaypoint = 0;
        _WaypointSize = _coordList.size();

        _defaultExecutor = Executors.newSingleThreadScheduledExecutor();


        //helpers
        //https://stackoverflow.com/questions/28620806/how-do-i-change-the-rate-or-period-of-a-repeating-task-using-scheduledexecutorse
        //https://stackoverflow.com/questions/1519091/scheduledexecutorservice-with-variable-delay
        // And the bellow code is based on https://stackoverflow.com/a/52745658 (best answer)

        //TODO placeholder, delete
        TrolleyController.MoveTrolley(0, +20, -20);


        futureTask = _defaultExecutor.scheduleAtFixedRate(this::TrolleyRoutine, 0, _delay, TimeUnit.MILLISECONDS);

        _allExecutors.add(_defaultExecutor);

    }

    private void TrolleyRoutine(){
        if(!_pause){
            if(_atWaypoint <= _WaypointSize){
                if (_cart.warehouse.closedPaths.contains(_coordList.get(_atWaypoint))){
                    //if during walking found the next tile to be blocked,
                    //recalculate route
                    _cart.coordList.clear();
                    _cart.coordIndex = 0;
                    _cart.planRoute(_cart.goal_x, _cart.goal_y);

                    //reset these
                    _coordList = _cart.coordList;
                    _atWaypoint = 0;
                    _WaypointSize = _coordList.size();
                    return;
                }
                _atWaypoint += 1;

                _cart.coordIndex++;

                int toGoX = _cart.coordList.get(_atWaypoint).x;
                int toGoY = _cart.coordList.get(_atWaypoint).y;

                //Move trolley up, down, left, right based on the differences of indexes
//                if(_cart.coord_x > toGoX){
//                    WarehouseController.MoveTrolley(0,-20,0);
//                }
//                else if(_cart.coord_x < toGoX){
//                    WarehouseController.MoveTrolley(0,+20,0);
//                }
//                else if(_cart.coord_y > toGoY){
//                    WarehouseController.MoveTrolley(0,0,-20);
//                }
//                else {
//                    WarehouseController.MoveTrolley(0,0,20);
//                }

                //convenient method
                TrolleyController.MoveTrolleyFromTo(0, _cart.coord_x, _cart.coord_y, toGoX, toGoY);

                _cart.coord_x = _cart.coordList.get(_atWaypoint).x;
                _cart.coord_y = _cart.coordList.get(_atWaypoint).y;

            }
            else {
                //private
                _orderIndex += 1;
                if(_orderIndex < _trolley.allWaypoints.size()) {
                    _currentShelfToGo = _trolley.allWaypoints.get(_orderIndex).GetFirstPoint();
                    _coordList = _cart.getCoords(_currentShelfToGo.getY(), _currentShelfToGo.getX()); // shelfs are generated in reverse so we have to flip values
                }
                else {
                    _defaultExecutor.shutdown();
                    //_executor.isTerminated()
                    ;//exit procedure
                }
            }
        }
    }

    public boolean get_pause() {
        return _pause;
    }

    public void set_pause(boolean _pause) {
        this._pause = _pause;
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

