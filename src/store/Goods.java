/**
 * @author Timotej Ponek xponek00
 * @author Timotej Kamensky xkamen24
 * @copyright Brno university of technology, faculty of computer science, Czechia.
 * @brief assignment of java application for basic warehouse management system
 */
package store;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

public class Goods {

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goods that = (Goods) o;
        return Objects.equals(name, that.name);
    }



    public int hashCode() {
        return Objects.hash(name);
    }

    private String name;
    ArrayList<Item> list = new ArrayList<>();

    public Goods(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    // Only adds new item to Box

    public boolean addItem(Item Item) {
        if(list.contains(Item))
            return false;
        else
            return list.add(Item);
        //return false;
    }

    // Creates new item and adds it to box

    public Item newItem(LocalDate localDate) {
        Item newlyCreated =  new Item(this, localDate, true);

        return newlyCreated;
    }


    public Item getItemAndRemove(){
        return list.size() > 0 ? list.remove(0) : null;
    }


    public boolean remove(Item Item) {
        if(list.contains(Item)){
            return list.remove(Item);
        }
        else
            return false;
    }


    public boolean empty() {

        return list.isEmpty();
    }


    public int size() {

        return list.size();
    }
}
