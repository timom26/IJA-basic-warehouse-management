package App;
import Reader.Read;
import Reader.WarehouseStruct;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;

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

    /** POGU method // handle data once the fields are injected */
    public void initialize() {
        //https://stackoverflow.com/a/37447039
        ObservableList<String> options = FXCollections.observableArrayList(
                "Trolley 1", "Trolley 2", "Trolley 3", "Trolley 4", "Trolley 5");
        _chooseTrolley.setValue("Trolley 1");
        _chooseTrolley.setItems(options);
        //_reset.setTooltip(new Tooltip("If checked, previously isued orders will be cancelled"));

        if(WarehouseController._controlledWarehouse != null){
            this._namesOfGoods = FXCollections.observableArrayList(WarehouseController._controlledWarehouse.GetGoodsNames());
            if(_allGoods.getValueFactory() == null){
                _allGoods.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory(this._namesOfGoods));
            }
        }

    }

//    public void SetGoods(WarehouseStruct warehouse){
//        //this._namesOfGoods = new ArrayList<>();
//        this._namesOfGoods = warehouse.GetGoodsNames();
//    }


    public void LoadRequests(ActionEvent event){
        /** char implicitly casts itself to int */
        if(WarehouseController._controlledWarehouse == null)
            return;
        Read.RequestViaGUI(_loadedRequests.getText(),
                Character.getNumericValue(_chooseTrolley.getValue().toString().charAt(8)),
                WarehouseController._controlledWarehouse.shelves,
                WarehouseController._controlledWarehouse.goods,
                WarehouseController._controlledWarehouse.getRows(),
                WarehouseController._controlledWarehouse.getCols(),
                _reset.isSelected()
        );
        //menu.close();
        //close action
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
        ClockController.Pause();
    }

}

