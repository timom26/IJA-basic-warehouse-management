/**
 * @author Timotej Ponek xponek00
 * @author Timotej Kamensky xkamen24
 * @copyright Brno university of technology, faculty of computer science, Czechia.
 * @brief assignment of java application for basic warehouse management system
 */
package store;
import java.util.ArrayList;

public class Shelf {

    private ArrayList<Goods> content = new ArrayList<>();

    public Shelf() {
    }

    public void put(Item itemToAdd) {
        Goods Adder = itemToAdd.goods();
        if(content.contains(Adder)){
            Goods toBeAdded = content.get(content.indexOf(Adder));
            toBeAdded.addItem(itemToAdd);
        }
        else{
            Goods toBeAdded = new Goods(Adder.getName());
            toBeAdded.addItem(itemToAdd);
            content.add(toBeAdded);
        }
    }

    public boolean containsGoods(Goods goods) {

        return content.contains(goods);
    }

    public Item removeAny(Goods goods) {
        if(containsGoods(goods)){
            Goods Remover = content.get(content.indexOf(goods));
            return Remover.getItemAndRemove();
//            int a = 99999;
//            int c = a + 222;
            //Remover.remove(g;
        }

        return null;
    }

    public String getAll(){
        if(content.isEmpty())
            return "Empty";
        StringBuilder All = new StringBuilder();
        int size = 0;
        for (Goods TypeofGoods:content) {
            All.append(TypeofGoods.getName()).append(' ').append(TypeofGoods.size()).append('\n');
            size += TypeofGoods.size();
        }
        // TODO
        return (size == 0) ? "Empty" : All.toString();
    }

    public int size(Goods goods) {
        return content.contains(goods) ? content.get(content.indexOf(goods)).size() : 0;
    }
}
