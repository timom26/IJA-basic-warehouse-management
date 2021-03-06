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
    }


    public static void main(String[] args) {
        launch(args);
    }
}
