package store;

import java.time.LocalDate;

public class Item {

    private String name;
    private LocalDate date;
    // just a reference to Box where item belongs,
    // should be smth like pointer in C if I understand it
    private Goods ref;


    public Item(Goods variable, LocalDate date, boolean explicit) {
        name = variable.getName();
        this.date = date;
        ref = variable;
        if(explicit)
            variable.addItem(this);
    }

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
}
