/**
 * @author Timotej Ponek xponek00
 * @author Timotej Kamensky xkamen24
 * @copyright Brno university of technology, faculty of computer science, Czechia.
 * @brief main
 */

package App;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(new Controller());
        Parent root = fxmlLoader.load(getClass().getClassLoader().getResource("sample.fxml"));
        primaryStage.setTitle("Warehouse");
        primaryStage.setScene(new Scene(root, 640, 540));
        primaryStage.setMinWidth(640);
        primaryStage.setMinHeight(540);
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
