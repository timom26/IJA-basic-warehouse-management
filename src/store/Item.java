/**
 * @author Timotej Ponek xponek00
 * @author Timotej Kamensky xkamen24
 * @copyright Brno university of technology, faculty of computer science, Czechia.
 * @brief assignment of java application for basic warehouse management system
 */
package store;

import java.time.LocalDate;

public class Item {

    private String name;
    private LocalDate date;
    // just a reference to Box where item belongs,
    // should be smth like pointer in C if I understand it
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
        //boolean hell = ija.ija2020.homework1.store.StoreGoods.class.equals(this);
        return ref;
        //return null;
    }

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
