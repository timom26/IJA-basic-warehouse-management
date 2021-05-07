/**
 * @author Timotej Ponek xponek00
 * @author Timotej Kamensky xkamen24
 * @copyright Brno university of technology, faculty of computer science, Czechia.
 * @brief implementation of functions reading input files from ~/data
 */
package Reader;

import store.Goods;
import store.Item;
import store.Shelf;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

public class Read {

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
            File file=new File("data/warehouse.txt");
            Scanner sc = new Scanner(file);     //file to be scanned
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
            File file=new File("data/stock.txt");
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

                }while(sc.hasNext(",") && sc.next().contentEquals(",")); // sanity check

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
     * @brief takes warehouse, reads 'requests.txt' and
     * based on it, creates requests for each trolley found
     * in 'requests.txt'
     *
     * @param wareHouse list representing warehouse
     * @param allGoods list of all goods
     * @return True if successful, false if exception occured
     */
    public static boolean ReadRequests(List wareHouse, List allGoods, List Indexes, int rowsMax, int colsMax)
    {
        try
        {
            File file = new File("data/requests.txt");
            Scanner sc = new Scanner(file);     //file to be scanned
            CartStruct AllCarts = new CartStruct();
            AllCarts.Reset();

            while (sc.hasNext()){
                int trolleyID = Integer.parseInt(sc.next()); // id of trolley for which orders are made
                AllCarts.AddOrder(trolleyID);

                String productName;
                int amount;
                do{
                    productName = sc.next();
                    amount = Integer.parseInt(sc.next());

                    String finalProductName = productName;
                    Object tryToGet;

                    if(allGoods.stream().anyMatch(o -> (((Goods) o).getName().equals(finalProductName)))){
                        tryToGet = allGoods.stream().filter(o -> (((Goods) o).getName().equals(finalProductName))).findFirst().get();

                        for(int row = 0; row <rowsMax; row++ ){
                            for(int coll = 0; coll < colsMax; coll++ ){
                                if(((Shelf) ((List) wareHouse.get(row)).get(coll)).containsGoods(((Goods) tryToGet))){
                                    AllCarts.AddGoodsIndexToCart(trolleyID, row, coll, productName, amount);
                                };
                            }
                        }
                    }
                }while(sc.hasNext(",") && sc.next().contentEquals(",")); // sanity check

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
     * @brief Reads requests stored in String for trolley given
     * by TrolleyID
     *
     * @param request string to scan
     * @param trolleyID
     * @param wareHouse list representing warehouse
     * @param allGoods list of all goods
     * @param rowsMax
     * @param colsMax
     * @param Reset true - clears previous requests, else it keeps them as the were
     * @return
     */
    public static boolean RequestViaGUI(String request, int trolleyID, List wareHouse, List allGoods, int rowsMax, int colsMax, boolean Reset){
        Scanner field = new Scanner(request);
        CartStruct AllCarts = new CartStruct();

        //delete orders given before
        if(Reset){
            AllCarts.Reset();
            AllCarts.AddOrder(trolleyID);
        }
        else {
            AllCarts.AddOrReplaceOrder(trolleyID);
        }

        String productName;
        int amount;
        try {
            while (field.hasNext()) {
                productName = field.next();
                amount = Integer.parseInt(field.next());

                String finalProductName = productName;
                Object tryToGet;

                if (allGoods.stream().anyMatch(o -> (((Goods) o).getName().equals(finalProductName)))) {
                    tryToGet = allGoods.stream().filter(o -> (((Goods) o).getName().equals(finalProductName))).findFirst().get();

                    for (int row = 0; row < rowsMax; row++) {
                        for (int coll = 0; coll < colsMax; coll++) {
                            if (((Shelf) ((List) wareHouse.get(row)).get(coll)).containsGoods(((Goods) tryToGet))) {
                                AllCarts.AddGoodsIndexToCart(trolleyID, row, coll, productName, amount);
                            }
                        }
                    }
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

}
