/**
 * @author Timotej Ponek xponek00
 * @author Timotej Kamensky xkamen24
 * @copyright Brno university of technology, faculty of computer science, Czechia.
 * @brief Menu controller
 */

package App;
import Reader.Read;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class MenuController {

    @FXML
    public ChoiceBox _chooseTrolley;
    @FXML
    public TextArea _loadedRequests;
    @FXML
    public Spinner<String> _allGoods;
    public CheckBox _reset;

    private ObservableList<String> _namesOfGoods;

    public MenuController(){
        super();
    }

    /**
     * @brief initialize fmxl field to default values
     */
    public void initialize() {
        //https://stackoverflow.com/a/37447039
        ObservableList<String> options = FXCollections.observableArrayList(
                "Trolley 1", "Trolley 2", "Trolley 3", "Trolley 4", "Trolley 5");
        _chooseTrolley.setValue("Trolley 1");
        _chooseTrolley.setItems(options);

        if(WarehouseController._controlledWarehouse != null){
            this._namesOfGoods = FXCollections.observableArrayList(WarehouseController._controlledWarehouse.GetGoodsNames());
            if(_allGoods.getValueFactory() == null){
                _allGoods.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory(this._namesOfGoods));
            }
        }

    }

    /**
     * @brief ask reader to load single Request ( + some checks )
     * @param event
     */
    public void LoadRequests(ActionEvent event){
        if(WarehouseController._controlledWarehouse == null)
            return;
        Read.RequestViaGUI(
                _loadedRequests.getText(),
                Character.getNumericValue(_chooseTrolley.getValue().toString().charAt(8)),
                WarehouseController._controlledWarehouse.shelves,
                WarehouseController._controlledWarehouse.goods,
                WarehouseController._controlledWarehouse.getRows(),
                WarehouseController._controlledWarehouse.getCols(),
                _reset.isSelected()
        );

        //close action
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        ClockController.Pause();
    }

}

