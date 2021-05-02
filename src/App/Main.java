/**
 * @author Timotej Ponek xponek00
 * @author Timotej Kamensky xkamen24
 * @copyright Brno university of technology, faculty of computer science, Czechia.
 * @brief assignment of java application for basic warehouse management system
 */

package App;

import Reader.Read;
import Reader.WarehouseStruct;
import javafx.application.Application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import store.ShoppingCart;

import java.util.ArrayList;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(new Controller());
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));
        primaryStage.setTitle("Warehouse");
        primaryStage.setScene(new Scene(root, 640, 545));
        primaryStage.show();

        //init warehouse
//        WarehouseStruct warehouse1 = new WarehouseStruct();
//        Read.ReadWarehousePlan(warehouse1.shelves);
//        warehouse1.setRows(warehouse1.shelves.size());
//        warehouse1.setCols(((ArrayList) warehouse1.shelves.get(0)).size());


//        ShoppingCart cart1 = new ShoppingCart(warehouse1,0,0);
//        warehouse1.addBlockage(0,1);
//        System.out.println("###");
//        cart1.goTo(0,9);
//        System.out.println("###");
//        cart1.goTo(0,0);
//        System.out.println("###");
    }


    public static void main(String[] args) {
        launch(args);
    }
}
