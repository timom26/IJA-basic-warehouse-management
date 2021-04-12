package Reader;

import store.Goods;
import store.Item;
import store.Shelf;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Read {
    //private int rows = 0;
    //private int Scols = 0;

    /**
     * @brief Function reads 'warehouse.txt' to get dimensions of the warehouse.
     * Empties list added as a parameter, and adds required amount of shelves
     * @param wareHouse will be filled by shelves
     * @return True if successful, False if any exception occured
     */
    public static boolean ReadWarehousePlan(List wareHouse)
    {
        try
        {
            File file=new File("warehouse.txt");
            Scanner sc = new Scanner(file);     //file to be scanned
            // while (sc.hasNext())        //returns true if and only if scanner has another token
            int rows = Integer.parseInt(sc.next());
            int cols = Integer.parseInt(sc.next());

            for(int i = 0; i < rows; i++){
                wareHouse.add(new ArrayList(cols)) ;
                for (int j = 0; j < cols; j++){
                    ((ArrayList) wareHouse.get(i)).add(new Shelf());
                }
            }

            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @brief takes warehouse, reads 'stock.txt' and
     * based on it, fills warehouse with all items correspondingly,
     * and also fills list of allGoods
     *
     * @param wareHouse list representing warehouse, to be filled
     * @param allGoods list of all goods, to be filled
     * @return True if successful, false if exception occured
     */
    public static boolean ReadStock(List wareHouse, List allGoods)
    {
        try
        {
            File file=new File("stock.txt");
            Scanner sc = new Scanner(file);     //file to be scanned
            while (sc.hasNext()){
                int rows = Integer.parseInt(sc.next());
                int cols = Integer.parseInt(sc.next());

                String productName;
                int amount;
                do{
                    productName = sc.next();
                    amount = Integer.parseInt(sc.next());
                    Shelf WhereToStore = ((Shelf) ((ArrayList) wareHouse.get(rows)).get(cols));

                    String finalProductName = productName;
                    int finalAmount = amount;
                    // help: https://stackoverflow.com/a/23004988
                    if(allGoods.stream().anyMatch(o -> {
                        if(((Goods) o).getName().equals(finalProductName)) {
                            for (int acc = 0; acc < finalAmount; acc++){
                                Item tmp = new Item((Goods) o, LocalDate.now(), true);
                                WhereToStore.put(tmp);
                            }
                            return true;
                        }
                        return false;
                    }));
                    else{
                        Goods o = new Goods(productName);
                        for (int acc = 0; acc < amount; acc++){
                            Item tmp = new Item((Goods) o, LocalDate.now(), true);
                            WhereToStore.put(tmp);
                        }
                        allGoods.add(o);
                    }



                }while(sc.hasNext(",")); // sanity check

            }

            return true;
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }
}
