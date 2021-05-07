/**
 * @author Timotej Ponek xponek00
 * @author Timotej Kamensky xkamen24
 * @copyright Brno university of technology, faculty of computer science, Czechia.
 * @brief implementation of a single item
 */
package store;

import java.time.LocalDate;

public class Item {

    private String name;
    private LocalDate date;
    private Goods ref;

    /**
     * Constructor method
     * @param variable Goodstype
     * @param date timestamp
     * @param explicit
     */
    public Item(Goods variable, LocalDate date, boolean explicit) {
        name = variable.getName();
        this.date = date;
        ref = variable;
        if(explicit)
            variable.addItem(this);
    }

    /**
     * Returns goodstype of the item
     * @return goodstype of the item
     */
    public Goods goods() {
        return ref;
    }

    /**
     * Sell item
     * @return
     */
    public boolean sell() {
        if(ref != null){
            return ref.remove(this);
        }
        return false;
    }

    public String getName(){
        return this.name;
    }
}
