/**
 * @author Timotej Ponek xponek00
 * @author Timotej Kamensky xkamen24
 * @copyright Brno university of technology, faculty of computer science, Czechia.
 * @brief assignment of java application for basic warehouse management system
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


    //TODO accept as an agrument only warehouse struct
    public static boolean ReadRequests(List wareHouse, List allGoods, List Indexes, int rowsMax, int colsMax)
    {
        //TODO here we should already consider cart postion and choose next shelf to pick from accordingly
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
                    //Optional hell = allGoods.stream().filter(o -> (((Goods) o).getName().equals(finalProductName))).findFirst();
                    Object hell;

                    if(allGoods.stream().anyMatch(o -> (((Goods) o).getName().equals(finalProductName)))){
                        hell = allGoods.stream().filter(o -> (((Goods) o).getName().equals(finalProductName))).findFirst().get();
                        System.out.println(((Goods) hell).getName());
                        System.out.println("бутылочка");

                        for(int row = 0; row <rowsMax; row++ ){
                            for(int coll = 0; coll < colsMax; coll++ ){
                                if(((Shelf) ((List) wareHouse.get(row)).get(coll)).containsGoods(((Goods) hell))){
                                    AllCarts.AddGoodsIndexToCart(trolleyID, row, coll, productName, amount);
                                    //return true; //TODO return list or struct or what else
                                    // TODO but Really I should make a Class for this thing
                                };
                            }
                        }

                    }
                    else {
                        System.out.println("лютики");
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

    public static boolean RequestViaGUI(String request, int trolleyID, List wareHouse, List allGoods, int rowsMax, int colsMax, boolean Reset){
        Scanner field = new Scanner(request);
        CartStruct AllCarts = new CartStruct();
        //for now
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
                //Optional hell = allGoods.stream().filter(o -> (((Goods) o).getName().equals(finalProductName))).findFirst();
                Object hell;

                if (allGoods.stream().anyMatch(o -> (((Goods) o).getName().equals(finalProductName)))) {
                    hell = allGoods.stream().filter(o -> (((Goods) o).getName().equals(finalProductName))).findFirst().get();
                    System.out.println(((Goods) hell).getName());
                    System.out.println("бутылочка");

                    for (int row = 0; row < rowsMax; row++) {
                        for (int coll = 0; coll < colsMax; coll++) {
                            if (((Shelf) ((List) wareHouse.get(row)).get(coll)).containsGoods(((Goods) hell))) {
                                AllCarts.AddGoodsIndexToCart(trolleyID, row, coll, productName, amount);
                                //return true; //TODO return list or struct or what else
                                // TODO but Really I should make a Class for this thing
                            }
                        }
                    }

                } else {
                    System.out.println("лютики");
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
