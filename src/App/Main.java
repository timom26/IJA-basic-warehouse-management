/**
 * @author Timotej Ponek xponek00
 * @author Timotej Kamensky xkamen24
 * @copyright Brno university of technology, faculty of computer science, Czechia.
 * @brief assignment of java application for basic warehouse management system
 */

package App;

import com.sun.net.httpserver.Authenticator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import store.*;

import java.time.LocalDate;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        /*note - this code is usually used to start graphical interface.
        However, shopping cart was not yet implemented into GUI.
        Therefore, GUI launcher is commented out and Shopping cart showcase
        is added under it for demonstration of its yet created functionality*/

        /*FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setController(new Controller());
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("sample.fxml"));
        primaryStage.setTitle("Warehouse");
        primaryStage.setScene(new Scene(root, 640, 545));
        primaryStage.show();*/

        System.out.println("---------  start of the showcase  --------");
        System.out.println("t h i s   t e s t   i s   e q u i v a l e n t   r e p l a c e m e n t   f o r   t e s t   0 1");
        Goods typeofgood1 = new Goods("zidle");
        Item exemplar1 = new Item(typeofgood1, LocalDate.of(2021,12,12),false);
        Item exemplar2 = new Item(typeofgood1,LocalDate.of(2020,2,12),true);

        typeofgood1.addItem(exemplar1);//both are added, one automatically, one here

        if (!typeofgood1.empty() && typeofgood1.size() == 2){
            System.out.println("items were correctly added to the type of goods");
        }else{System.out.println("error has occured while adding items to its type.");}

        if (!typeofgood1.remove(exemplar2)){
            System.out.println("error has occurred while removing item from its type");
        }
        if(!typeofgood1.empty() && typeofgood1.size() == 1){
            System.out.println("correctly removed first item");
        }
        if (!typeofgood1.remove(exemplar1)){
            System.out.println("error has occurred while removing item from its type");
        }
        if(typeofgood1.empty() && typeofgood1.size() == 0){
            System.out.println("all items sucessfully removed from its type.");
        }
        typeofgood1.addItem(exemplar1);
        typeofgood1.addItem(exemplar2);
        System.out.println("t h i s   t e s t   i s   e q u i v a l e n t   r e p l a c e m e n t   f o r   s h e l f   t e s t");
        System.out.println("   --items were readded to their types");
        Shelf shelf1 = new Shelf();
        if (shelf1.size(typeofgood1) != 0){
            System.out.println("error newly created shelf is already filled by the type");
        }
        shelf1.put(exemplar1);
        shelf1.put(exemplar2);
        if (shelf1.size(typeofgood1) != 2){
            System.out.println("error - items were not correctly added to the shelf");
        }
        else{
            System.out.println("items sucessfully added to the shelf");
        }
        Item vybrane_zbozi = shelf1.removeAny(typeofgood1);
        if (shelf1.size(typeofgood1) != 1){
            System.out.println("error - item was not taken out as expected");
        }
        else{
            System.out.println("item sucessfully taken from shelf");
        }

        System.out.println("t h i s   t e s t   i s   s h o w c a s i n g   t h e   S h o p p i n g   C a r t");
        System.out.println("    --there, items will be taken from shelf to cart, and other way around");
        ShoppingCart cart1 = new ShoppingCart();
        cart1.Fill(shelf1,typeofgood1);
        if (shelf1.size(typeofgood1) != 0){
            System.out.println("ERROR - item was not taken into cart as expected");
        }
        else{
            System.out.println("item  taken from shelf to cart");
        }
        if (cart1.size() == 1){
            System.out.println("1 item is sucessfully in cart");
        }else{
            System.out.println("ERROR - item has not reached the cart");
        }
        System.out.println("    --put the item from cart to a different shelf");
        Shelf shelf2 = new Shelf();
        cart1.Empty(shelf2,typeofgood1);
        if (cart1.size() != 0 ){
            System.out.println("ERROR - item is stuck in the cart");
        }
        else{
            System.out.println("item has sucessfully left the cart");
        }
        if(shelf2.size(typeofgood1) == 1){
            System.out.println("item has sucessfully reached new shelf");
        }
        else{
            System.out.println("ERROR - item has not reached new shelf");
        }
        System.out.println("    --last partial test. Lets see if data of the item has survived all of the shifts from shelves to carts ");
        Item survivingItem = shelf2.removeAny(typeofgood1);
        if (survivingItem.goods() == typeofgood1){
            System.out.println("type of goods has survived.");
        }
        else{
            System.out.println("ERROR - type of goods has not survived");
        }
        if (survivingItem.getName() == "zidle"){
            System.out.println("item has preserved its name");
        }
        else{
            System.out.println("ERROR - item has lost its name");
        }

        if (survivingItem.getTimestamp().compareTo(LocalDate.of(2020,2,12)) == 0){
            System.out.println("item has preserved its timestamp");
        }
        else{
            System.out.println("ERROR - item has lost its timestamp");
            System.out.println("instead, timestamp is "+ survivingItem.getTimestamp());
        }
        System.out.println("---------  end of the showcase -----------");
        System.exit(0);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
