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

import java.util.ArrayList;
import java.util.List;

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

    /**
     * serves as de-facto main in GUI.
     *
     * Asks for warehouse and its files (read from txt files)
     * After that it generates a graphical interface to see current status
     * of warehouse.
     */
    public void buttonLaunch() {
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
            ;
        }
        else
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


    public void ScrollAction(){
        _scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        _scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        _scrollPane.setScaleShape(true);
        _scrollPane.getContent().setScaleX(1.7);
        _scrollPane.getContent().setScaleY(1.7);
    }

    public void zoomAction(){

    }


    /**
     * WIP - ignore for now
     */
    public void onMouseEntered(){

    }

}
