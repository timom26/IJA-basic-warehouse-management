/**
 * @author Timotej Ponek xponek00
 * @author Timotej Kamensky xkamen24
 * @copyright Brno university of technology, faculty of computer science, Czechia.
 * @brief implementation of a type of goods
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

    /**
     * Only adds new item to Box
     * @param Item
     * @return
     */
    public boolean addItem(Item Item) {
        if(list.contains(Item))
            return false;
        else
            return list.add(Item);
    }

    /**
     * Creates new item and adds it to box
     * @param localDate
     * @return
     */
    public Item newItem(LocalDate localDate) {
        Item newlyCreated =  new Item(this, localDate, true);

        return newlyCreated;
    }

    /**
     *
     * @return removed item, null if not found
     */
    public Item getItemAndRemove(){
        return list.size() > 0 ? list.remove(0) : null;
    }

    /**
     * Remove item if Goods contains it
     * @param Item
     * @return
     */
    public boolean remove(Item Item) {
        if(list.contains(Item)){
            return list.remove(Item);
        }
        else
            return false;
    }

    /**
     * Answers to whether Goods is empty
     * @return true if Goods is empty
     */
    public boolean empty() {
        return list.isEmpty();
    }

    /**
     * Returns size of Goods
     * @return
     */
    public int size() {
        return list.size();
    }
}
