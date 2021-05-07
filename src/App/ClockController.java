/**
 * @author Timotej Ponek xponek00
 * @author Timotej Kamensky xkamen24
 * @copyright Brno university of technology, faculty of computer science, Czechia.
 * @brief speed of simulation management (speed is given to trolleys), routine of a cart
 */

package App;

import Reader.CartStruct;
import Reader.WarehouseStruct;
import store.Shelf;
import store.ShoppingCart;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class ClockController {
    private static boolean _pause = false;
    private static int _delay = 1000;
    private static List<ScheduledExecutorService> _allExecutors;

    // instance properties
    private CartStruct.Trolley _trolley;
    private WarehouseStruct _workplace;
    private ShoppingCart _cart;
    private int _cartId;
    private int _orderIndex;
    private CartStruct.Trolley.targetIndex.OnePoint _currentShelfToGo;
    private int _atWaypoint;
    private int _WaypointSize;
    private boolean _isUnloading;
    private boolean _goEmpty;
    private boolean _blocked;
    private boolean _parked;
    private int _remainingToGet;
    private int _tryNext;
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
        _isUnloading = true;
        _goEmpty = false;
        _blocked = false;
        _parked = false;
        _trolley = trolley;
        _workplace = workplace;
        _tryNext = 0;
        _remainingToGet = _trolley.allWaypoints.get(_orderIndex).getIndexAmount();

        _cart = new ShoppingCart(workplace,-1,10);
        _cartId = id;
        _orderIndex = 0;

        //This will be moved to Controller class in production
        WarehouseController.BindCartToTrolley(id, _cart);

        Boolean inMotion = false;
        _currentShelfToGo = trolley.allWaypoints.get(_orderIndex).GetFirstPoint();

        // These will be used later
        _cart.goal_x = _currentShelfToGo.getY(); _cart.goal_y = _currentShelfToGo.getX();

        Point from = new Point(_cart.coord_x, _cart.coord_y);
        Point where = new Point(_cart.goal_x, _cart.goal_y);

        _cart.coordList = _cart.warehouse.getAStarCoords(from,where, "shelf");
        //_cart.planRoute(_cart.goal_x, _cart.goal_y); /** shelfs are generated in reverse so we have to flip values*/


        //_coordList = _cart.coordList;
        _atWaypoint = 0;
        _WaypointSize = _cart.coordList.size() - 1; // we do this because we skip the first point

        _defaultExecutor = Executors.newSingleThreadScheduledExecutor();


        //helpers
        //https://stackoverflow.com/questions/28620806/how-do-i-change-the-rate-or-period-of-a-repeating-task-using-scheduledexecutorse
        //https://stackoverflow.com/questions/1519091/scheduledexecutorservice-with-variable-delay
        // And the bellow code is based on https://stackoverflow.com/a/52745658 (best answer)

        //TODO placeholder, delete
//        if(_cartId == 0)
//            MoveTrolley(_cartId, +20, -20);
//        else
            TrolleyController.PlaceTrolley(_cartId, 5, 315);


        futureTask = _defaultExecutor.scheduleAtFixedRate(this::TrolleyRoutine, (_cartId - 1) *100, _delay, TimeUnit.MILLISECONDS);

        _allExecutors.add(_defaultExecutor);

    }

    /**
     * @brief function defines, what should each trolley tell the cart to do.
     *
     * Generally:
     *
     * For each goal: go to target, pickup, go to next target etc., and get back at the end.
     * If got full while doing so, go empty yourself.
     */
    private void TrolleyRoutine(){
        /** JavaFX things cannot be updated outside JavaFX thread */
                    if (!_pause && !_defaultExecutor.isTerminated()) {
                        if (_atWaypoint < _WaypointSize) {
                            _atWaypoint += 1;
                            _cart.coordIndex++;
                            if (_cart.warehouse.closedPaths.contains(_cart.coordList.get(_atWaypoint))) {
                                //if during walking found the next tile to be blocked,
                                //recalculate route
                                _cart.coordList.clear();
                                _cart.coordIndex = 0;
                                _cart.planRoute(_cart.goal_x, _cart.goal_y);

                                //reset parameters (we get a new route from recalculation,
                                // so it is basically a totally new route and we have to reset these indexes)
                                _atWaypoint = 0;
                                _WaypointSize = _cart.coordList.size() - 1;

                                //if we are blocked, we set these so sheduledController won't shut itself
                                if(_cart.coordList.size() == 0)
                                    _blocked = true;
                                else
                                    _blocked = false;

                                return;
                            }

                            //get the next tile to go to
                            int toGoX = _cart.coordList.get(_atWaypoint).x;
                            int toGoY = _cart.coordList.get(_atWaypoint).y;

                            //convenient method
                            //Note: this call will move it only by a single tile!
                            TrolleyController.MoveTrolleyFromTo(_cartId, _cart.coord_x, _cart.coord_y, toGoX, toGoY);

                            //save, where did you just move
                            _cart.coord_x = _cart.coordList.get(_atWaypoint).x;
                            _cart.coord_y = _cart.coordList.get(_atWaypoint).y;

                            _isUnloading = true;
                        }
                        /** we are blocked everywhere, keep looking for new routes*/
                        else if(_blocked){
                            _cart.coordList.clear();
                            _cart.coordIndex = 0;
                            _cart.planRoute(_cart.goal_x, _cart.goal_y);

                            if(_cart.coordList.size() > 0)
                                _blocked = false;
                            //reset these
                            _atWaypoint = 0;
                            _WaypointSize = _cart.coordList.size() - 1;
                            return;
                        }

                        else {
                            Point where, from;
                            //private
                            if (_isUnloading && !_goEmpty){

                                try{
                                    _remainingToGet = _cart.FillAmount(
                                            ((Shelf) ((ArrayList) _cart.warehouse.shelves.get(_cart.goal_y)).get(_cart.goal_x)),
                                            _trolley.allWaypoints.get(_orderIndex).getIndexName(),
                                            _remainingToGet
                                    );
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                    return;
                                }

                                WarehouseController.ActualizeTooltip(_cart.goal_y, _cart.goal_x);
                                WarehouseController.ActualizeTrolleyToolTip(_cartId);

                                if(_remainingToGet == 0 && !_cart.isFull()){
                                    /** we can continue to go for new goods */
                                    _isUnloading = false;
                                }
                                else if(_remainingToGet > 0 && !_cart.isFull()){
                                    _tryNext++;
                                    _currentShelfToGo = _trolley.allWaypoints.get(_orderIndex).GetPoint(_tryNext);


                                    /**skip order*/
                                    if(_currentShelfToGo == null) {
                                        _isUnloading = false;
                                        return;
                                    }
                                    _cart.goal_x = _currentShelfToGo.getY(); _cart.goal_y = _currentShelfToGo.getX();

                                    where = new Point(_currentShelfToGo.getY(),_currentShelfToGo.getX());
                                    from = new Point(_cart.coord_x, _cart.coord_y);
                                    _cart.coordList = _cart.warehouse.getAStarCoords(from, where, "shelf");

                                    _cart.coordIndex = 0;
                                    _atWaypoint = 0;
                                    _WaypointSize = _cart.coordList.size() - 1;

                                    TrolleyController.ActualizeRoute(_cartId);
                                    return;
                                }
                                else{
                                    GoEmptyCart();
                                }
                                return;
                            }
                            else if(_goEmpty){
                                _cart.Unload();
                                _goEmpty = false;
                                WarehouseController.ActualizeTrolleyToolTip(_cartId);
                                /** Get back to shelf*/
                                if(_remainingToGet != 0){
                                    _currentShelfToGo = _trolley.allWaypoints.get(_orderIndex).GetPoint(_tryNext);
                                    _cart.goal_x = _currentShelfToGo.getY(); _cart.goal_y = _currentShelfToGo.getX();

                                    Point tmp = new Point(_currentShelfToGo.getY(),_currentShelfToGo.getX());
                                    Point tmp2 = new Point(_cart.coord_x, _cart.coord_y);
                                    _cart.coordList = _cart.warehouse.getAStarCoords(tmp2,tmp, "shelf");

                                    _cart.coordIndex = 0;
                                    _atWaypoint = 0;
                                    _WaypointSize = _cart.coordList.size() - 1;

                                    TrolleyController.ActualizeRoute(_cartId);
                                    return;
                                }
                            }

                            _orderIndex += 1;
                            //if there is another target to go to:
                            if (_orderIndex < _trolley.allWaypoints.size()) {

                                _tryNext = 0; //reset index search
                                _remainingToGet = _trolley.allWaypoints.get(_orderIndex).getIndexAmount(); //new amount to get
                                _currentShelfToGo = _trolley.allWaypoints.get(_orderIndex).GetFirstPoint();
                                _cart.goal_x = _currentShelfToGo.getY(); _cart.goal_y = _currentShelfToGo.getX();

                                from = new Point(_cart.coord_x, _cart.coord_y);
                                where = new Point(_cart.goal_x, _cart.goal_y);
                                _cart.coordList = _cart.warehouse.getAStarCoords(from, where, "shelf");

                                _cart.coordIndex = 0;
                                _atWaypoint = 0;
                                _WaypointSize = _cart.coordList.size() - 1;

                                TrolleyController.ActualizeRoute(_cartId);
                            }
                            else if(!_cart.isEmpty()){
                                GoEmptyCart();
                            }
                            else {
//                            if(futureTask != null){
//                                futureTask.cancel(true);
//                            }
                                if(!_parked){
                                    goPark();
                                    _parked = true;
                                    return;
                                }
                                _defaultExecutor.shutdownNow();
                                //_executor.isTerminated()
                                ;//exit procedure
                            }
                        }
                    }
                }


    private void goPark() {
        Point where;
        Point from;
        _goEmpty = true;

        where = new Point(0,_workplace.getMax_y() - 1);
        from = new Point(_cart.coord_x, _cart.coord_y);
        _cart.coordList = _cart.warehouse.getAStarCoords(from,where, "tile");

        _cart.coordIndex = 0;
        _atWaypoint = 0;
        _WaypointSize = _cart.coordList.size() - 1;

        TrolleyController.ActualizeRoute(_cartId);
    }

    private void GoEmptyCart() {
        Point where;
        Point from;
        _goEmpty = true;

        where = new Point(_workplace.getMax_x() - 46,_workplace.getMax_y() - 1);
        from = new Point(_cart.coord_x, _cart.coord_y);
        _cart.coordList = _cart.warehouse.getAStarCoords(from,where, "tile");

        _cart.coordIndex = 0;
        _atWaypoint = 0;
        _WaypointSize = _cart.coordList.size() - 1;

        TrolleyController.ActualizeRoute(_cartId);
    }

    public static boolean get_pause() {
        return _pause;
    }

    public void set_pause(boolean _pause) {
        this._pause = _pause;
    }

}

