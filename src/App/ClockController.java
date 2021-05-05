package App;

import Reader.CartStruct;
import Reader.AStarNode;
import Reader.WarehouseStruct;
import javafx.application.Platform;
import javafx.event.Event;
import store.ShoppingCart;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static App.TrolleyController.*;

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
    private int _cartId;
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
    }


    public static void DispatchAll(){
        if(_allExecutors != null){
            for (ScheduledExecutorService toDestroy: _allExecutors) {
                toDestroy.shutdown();
            }
        }
        _allExecutors = new ArrayList<>();
    }

    public static void Pause(){
        _pause = !_pause;
    }

    public ClockController(CartStruct.Trolley trolley, WarehouseStruct workplace, int id){
        _trolley = trolley;
        _workplace = workplace;

        _cart = new ShoppingCart(workplace,-1,10);
        _cartId = id;
        _orderIndex = 0;

        //This will be moved to Controller class in production
        WarehouseController.BoundCartToTrolley(id, _cart);
        WarehouseController.AddTrolleyToolTip();

        Boolean inMotion = false;
        _currentShelfToGo = trolley.allWaypoints.get(_orderIndex).GetFirstPoint();

        // These will be used later
        _cart.goal_x = _currentShelfToGo.getY(); _cart.goal_y = _currentShelfToGo.getX();
        _cart.planRoute(_cart.goal_x, _cart.goal_y); /** shelfs are generated in reverse so we have to flip values*/


        //_coordList = _cart.coordList;
        _atWaypoint = 0;
        _WaypointSize = _cart.coordList.size() - 1; // we do this because we skip the first point

        _defaultExecutor = Executors.newSingleThreadScheduledExecutor();


        //helpers
        //https://stackoverflow.com/questions/28620806/how-do-i-change-the-rate-or-period-of-a-repeating-task-using-scheduledexecutorse
        //https://stackoverflow.com/questions/1519091/scheduledexecutorservice-with-variable-delay
        // And the bellow code is based on https://stackoverflow.com/a/52745658 (best answer)

        //TODO placeholder, delete
        if(_cartId == 0)
            MoveTrolley(_cartId, +20, -20);
        else
            PlaceTrolley(_cartId, 5, 320);


        futureTask = _defaultExecutor.scheduleAtFixedRate(this::TrolleyRoutine, (_cartId - 1) *100, _delay, TimeUnit.MILLISECONDS);

        _allExecutors.add(_defaultExecutor);

    }

    private void TrolleyRoutine(){
        /** JavaFX things cannot be updated outside JavaFX thread */
        try {
            Platform.runLater(new Runnable() {

                @Override
                public void run() {
                    if (!_pause && !_defaultExecutor.isTerminated()) {
                        if (_atWaypoint < _WaypointSize) {
                            if (_cart.warehouse.closedPaths.contains(_cart.coordList.get(_atWaypoint))) {
                                //if during walking found the next tile to be blocked,
                                //recalculate route
                                _cart.coordList.clear();
                                _cart.coordIndex = 0;
                                _cart.planRoute(_cart.goal_x, _cart.goal_y);

                                //reset these
                                _atWaypoint = 0;
                                _WaypointSize = _cart.coordList.size() - 1;
                                return;
                            }

                            //first coordinate is the current position of trolley, skip it
                            _atWaypoint += 1;

                            _cart.coordIndex++;

                            //int toGoX, toGoY

                            int toGoX = _cart.coordList.get(_atWaypoint).x;
                            int toGoY = _cart.coordList.get(_atWaypoint).y;

                            //convenient method
                            MoveTrolleyFromTo(_cartId, _cart.coord_x, _cart.coord_y, toGoX, toGoY);

                            _cart.coord_x = _cart.coordList.get(_atWaypoint).x;
                            _cart.coord_y = _cart.coordList.get(_atWaypoint).y;

                        } else {
                            System.out.println(_coordList);
                            //private
                            _orderIndex += 1;
                            if (_orderIndex < _trolley.allWaypoints.size()) {
                                /** TODO naložiť, ak je plný, poslať vyložiť a ptm poslať späť sa pohybovať po sklade */
                                _currentShelfToGo = _trolley.allWaypoints.get(_orderIndex).GetFirstPoint();
                                _cart.coordList = _cart.getCoords(_currentShelfToGo.getY(), _currentShelfToGo.getX()); // shelfs are generated in reverse so we have to flip values
                                _cart.coordIndex = 0;
                                _atWaypoint = 0;
                                _WaypointSize = _cart.coordList.size() - 1;

                                TrolleyController.ActualizeRoute(_cartId);


                            }
                            /** TODO move trolley to vyložiť náklad */
                            else {
//                            if(futureTask != null){
//                                futureTask.cancel(true);
//                            }
                                _defaultExecutor.shutdownNow();
                                //_executor.isTerminated()
                                ;//exit procedure
                            }
                        }
                    }
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
            e.getMessage();
            return;

        }
    }

    public static boolean get_pause() {
        return _pause;
    }

    public void set_pause(boolean _pause) {
        this._pause = _pause;
    }

}

