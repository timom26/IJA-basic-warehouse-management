/**
 * @author Timotej Ponek xponek00
 * @author Timotej Kamensky xkamen24
 * @copyright Brno university of technology, faculty of computer science, Czechia.
 * @brief assignment of java application for basic warehouse management system
 */

package App;

import Reader.CartStruct;
import Reader.Read;
import Reader.WarehouseStruct;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.control.ScrollPane;
import store.ShoppingCart;
import javafx.scene.layout.Pane;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Basic GUI controller.
 */
public class Controller {
    /**
     * GUI panel
     */
    //public ZoomablePane _zoomPane;
    public ScrollPane _scrollPane;
    public Pane warehousePane;
    private WarehouseStruct _workplace;
    private List<ClockController> _inMotion;
    private boolean paused = false;

    /**
     * serves as de-facto main in GUI.
     *
     * Asks for warehouse and its files (read from txt files)
     * After that it generates a graphical interface to see current status
     * of warehouse.
     */
    public void buttonLaunch() {
        // clear possible ongoing simulation
        ClockController.DispatchAll();
        _inMotion = null;

        WarehouseStruct depot = new WarehouseStruct();

        boolean loadResponse = Read.ReadWarehousePlan(depot.shelves);
        depot.setRows(depot.shelves.size());
        depot.setCols(((ArrayList) depot.shelves.get(0)).size());
        if(loadResponse){
            if(Read.ReadStock(depot.shelves, depot.goods)){
                WarehouseController.PaneDraw(depot.getRows(), depot.getCols(), warehousePane);
                WarehouseController.DrawGrid(depot.getRows(), depot.getCols(), warehousePane);
                WarehouseController.AddToolTip(depot);
                //_zoomPane.setContent(_scrollPane);
                //_zoomPane.prefWidth(610);
                ZoomController.SetProperties(_scrollPane);
                Read.ReadRequests(depot.shelves, depot.goods, null, depot.getRows(), depot.getCols());

                //warehousePane.setOnScroll();
            }
        }
        this._workplace = depot;
    }

    public void buttonSimulate() {
        //WarehouseStruct depot = new WarehouseStruct();
        //CartStruct allCarts = new CartStruct();


        if(_inMotion != null){
            ClockController.DispatchAll();
        }
        _inMotion = new ArrayList<ClockController>();

        CartStruct.Trolley tmpTrolley = CartStruct.allOrders.get(0);

        ClockController tmp = new ClockController(tmpTrolley, _workplace);
        _inMotion.add(tmp);

//        for (CartStruct.Trolley _trolley : CartStruct.allOrders) {
//
//            TrolleyRoutine(_trolley);
//
//        }

    }

    public void ButtonPause(){
        ClockController.Pause();

    }

    public void Speed1x(){
        ClockController.SetTime(1000);
        ClockController.UpdateTimer(_inMotion);
    }

    public void Speed2x(){
        ClockController.SetTime(500);
        ClockController.UpdateTimer(_inMotion);
    }

    public void Speed4x(){
        ClockController.SetTime(250);
        ClockController.UpdateTimer(_inMotion);
    }

    public void Speed8x(){
        ClockController.SetTime(125);
        ClockController.UpdateTimer(_inMotion);
    }

    /**
     * WIP - ignore for now
     */
    public void onMouseEntered(){

    }

}
