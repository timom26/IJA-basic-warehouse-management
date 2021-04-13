/**
 * @author Timotej Ponek
 * @author Timotej Kamensky
 * @copyright Brno university of technology, faculty of computer science, Czechia.
 * @brief assignment of java application for basic warehouse management system
 */
package store;
import java.util.ArrayList;


public class ShoppingCart {


    //  #####################  vars  ###############
    private ArrayList<Item> content = new ArrayList<>();
    private int maximumCount = 10;//prepared for future dev with variable max count.


    //  ###################  constructor  #################
    public ShoppingCart(){

    }
    //#############  class functions ###########################

    /**
     * Fills the cart by any item of a given goods-type from a given shelf
     * @param shelf shelf to take item from
     * @param goods goodstype to take
     * @return true if successful, false if full cart or no item of type in shelf
     */
    public boolean Fill(Shelf shelf,Goods goods){
        if (this.size() >= maximumCount){
            return false;
        }
        Item takenItem = shelf.removeAny(goods);
        if (takenItem == null){
            return false;
        }
        content.add(takenItem);
        return true;
    }

    /**
     * Takes out item of a given type to the given shelf
     * @param shelf shelf to pace item onto
     * @param goods goodstype of item to be placed
     * @return true if successful,false if no item of the given type to move to shelf
     */
    public boolean Empty(Shelf shelf, Goods goods){
        Item takenItem = content.stream().filter(o -> o.goods().equals(goods)).findFirst().orElse(null);
        if (takenItem == null){
            return false;
        }
        content.remove(takenItem);
        shelf.put(takenItem);
        return true;
    }

    /**
     * Function Empties all of its items into the given shelf. Useful when retrieving items from warehouse
     * @param shelf shelf where it needs to be emptied
     * @return true if sucessful
     */
    public void EmptyAll(Shelf shelf){
        for (Item item: content){
            content.remove(item);
            shelf.put(item);
        }
    }
    /**
     * Checks if the cart contains any item with given goodstype
     * @param goods goodstype to match
     * @return true if found, false if not
     */
    public boolean containsGoods(Goods goods){
        //https://stackoverflow.com/questions/18852059/java-list-containsobject-with-field-value-equal-to-x
        return content.stream().anyMatch(o -> o.goods().equals(goods));
    }
    /**
     * Function to retrieve count of items in the shopping cart
     * @return count of all items in the shopping cart
     */
    public int size(){
        return content.size();
    }
}
