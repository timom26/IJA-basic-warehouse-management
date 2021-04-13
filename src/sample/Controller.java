/**
 * @author Timotej Ponek xponek00
 * @author Timotej Kamensky xkamen24
 * @copyright Brno university of technology, faculty of computer science, Czechia.
 * @brief assignment of java application for basic warehouse management system
 */

package sample;

import Reader.Read;
import Reader.WarehouseStruct;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

/**
 * Basic GUI controller.
 */
public class Controller {
    /**
     * GUI panel
     */
    public Pane warehousePane;

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

                //warehousePane.setOnScroll();
            }
        }
    }

    /**
     * WIP - ignore for now
     */
    public void onMouseEntered(){

    }

}
