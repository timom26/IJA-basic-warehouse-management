/**
 * @author Timotej Ponek xponek00
 * @author Timotej Kamensky xkamen24
 * @copyright Brno university of technology, faculty of computer science, Czechia.
 * @brief GUI buttons and their action implementation, speed changes
 */

package App;

import Reader.CartStruct;
import Reader.Read;
import Reader.WarehouseStruct;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic GUI controller.
 */
public class Controller {
    /**
     * GUI panel
     */
    public ScrollPane _scrollPane;
    public Pane warehousePane;
    public TextField _state;
    private static WarehouseStruct _workplace;
    private List<ClockController> _inMotion;
    private boolean paused = false;

    /**
     * @brief implements the left button "launch"
     */
    public void buttonLaunch() {
        //clear possible ongoing simulation
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

    /**
     * @brief implements the right button "Simulate"
     */
    public void buttonSimulate() {
        //we could click pause before Simulate
        _state.textProperty().set(ClockController.get_pause() ? "Paused" : "Playing");
        if(_inMotion != null){
            return;//you have to click load first
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

        Parent root = fxmlLoader.load(getClass().getClassLoader().getResource("menu.fxml"));
        menu.setTitle("Menu");
        menu.setScene(new Scene(root, 500, 270));
        menu.setMinWidth(500);
        menu.setMinHeight(270);
        menu.show();
    }

    /**
     * @brief implements the button "pause"
     */
    public void ButtonPause(){
        ClockController.Pause();
        _state.textProperty().set(ClockController.get_pause() ? "Paused" : "Playing");
    }

    /********* Speed change functions ***************/

    /**
     * @brief change the time of single tick to 1000ms
     */
    public void Speed1x(){
        ClockController.SetTime(1000);
        ClockController.UpdateTimer(_inMotion);
    }

    /**
     * @brief change the time of single tick to 500ms
     */
    public void Speed2x(){
        ClockController.SetTime(500);
        ClockController.UpdateTimer(_inMotion);
    }

    /**
     * @brief change the time of single tick to 250ms
     */
    public void Speed4x(){
        ClockController.SetTime(250);
        ClockController.UpdateTimer(_inMotion);
    }

    /**
     * @brief change the time of single tick to 125ms
     */
    public void Speed8x(){
        ClockController.SetTime(125);
        ClockController.UpdateTimer(_inMotion);
    }

    /**
     * @brief change the time of single tick to 63ms
     */
    public void Speed16x(){
        ClockController.SetTime(63);
        ClockController.UpdateTimer(_inMotion);
    }

}
