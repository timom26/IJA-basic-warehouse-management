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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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
    private static WarehouseStruct _workplace;
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
        this._workplace = depot;

        boolean loadResponse = Read.ReadWarehousePlan(depot.shelves);
        depot.setRows(depot.shelves.size());
        depot.setCols(((ArrayList) depot.shelves.get(0)).size());
        if(loadResponse){
            if(Read.ReadStock(depot.shelves, depot.goods)){
                WarehouseController.PaneDraw(depot.getRows(), depot.getCols(), warehousePane);
                WarehouseController.DrawGrid(depot.getRows(), depot.getCols(), warehousePane);
                WarehouseController.AddShelfToolTip(depot);
                WarehouseController.SetWarehouse(_workplace);

                ZoomController.SetProperties(_scrollPane);
                Read.ReadRequests(depot.shelves, depot.goods, null, depot.getRows(), depot.getCols());

            }
        }

    }

    public void buttonSimulate() {
        if(_inMotion != null){
            //return, you have to click load first
            return;
            //ClockController.DispatchAll();
        }
        _inMotion = new ArrayList<ClockController>();

        int id = 0;
        for (CartStruct.Trolley trolley : CartStruct.allOrders) {
            ClockController tmp = new ClockController(trolley, _workplace, id++);
            _inMotion.add(tmp);
        }
        WarehouseController.AddTrolleyToolTip();

    }

    public List GetNamesOfGoodsForSpinner(){
        return _workplace.GetGoodsNames();
    }

    public void launchMenu() throws IOException {
        if(!ClockController.get_pause())
            ClockController.Pause();
        Stage menu = new Stage();
        menu.initModality(Modality.APPLICATION_MODAL);
        FXMLLoader fxmlLoader = new FXMLLoader();

        //TODO menu Controller
        //fxmlLoader.setController(new Controller());
        Parent root = fxmlLoader.load(getClass().getClassLoader().getResource("menu.fxml"));
        menu.setTitle("Menu");
        menu.setScene(new Scene(root, 500, 270));
        menu.show();
        //if(_workplace != null)
            //((MenuController) fxmlLoader.getController()).SetGoods(_workplace);
    }

    public void ButtonPause(){
        ClockController.Pause();
    }

    /**
     * Speed change functions
     */

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
