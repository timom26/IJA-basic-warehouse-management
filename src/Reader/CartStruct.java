/**
 * @author Timotej Ponek xponek00
 * @author Timotej Kamensky xkamen24
 * @copyright Brno university of technology, faculty of computer science, Czechia.
 * @brief implementation of a Cart management system
 */
package Reader;

import store.ShoppingCart;

import java.util.ArrayList;
import java.util.List;

public class CartStruct {

    public static List<Trolley> allOrders;
    public static List<ShoppingCart> allCarts;


    public void Reset(){
        allOrders = new ArrayList<>();
        allCarts = new ArrayList<>();
    }

    public boolean AddOrReplaceOrder(int id){
        if(allOrders.size() < id){
            Trolley newTrolley = new Trolley();
            allOrders.add(newTrolley);
            return true;
        }
        else{
            Trolley newTrolley = new Trolley();
            allOrders.remove(id - 1);
            allOrders.add(id - 1, newTrolley);
            return true;
        }
    }

    public boolean AddOrder(int id){
        if(allOrders.size() < id){
            Trolley newTrolley = new Trolley();
            allOrders.add(newTrolley);
            return true;
        }
        else
            return false; // trolley exist, cannot create new
    }

    public void AddCart(ShoppingCart toAdd){
        allCarts.add(toAdd);
    }


    /*Boolean*/
    public void AddGoodsIndexToCart(int id, int row, int coll, String GoodsName, int amount){
        Trolley toAdd = allOrders.get(id-1);

        toAdd.AddIndex(row, coll, GoodsName, amount);

    }

    public Trolley GetCart(int id){
        return allOrders.get(id-1);
    }


    //************************************************************************************//
    public class Trolley{
        public List<targetIndex> allWaypoints;

        public Trolley(){
            allWaypoints = new ArrayList<>();
        }

        public void AddIndex(int row, int coll, String GoodsName, int amount){
            /* if(allWaypoints.contains(o -> ((Index) o).GoodsName.contentEquals(GoodsName))){ } */
            /** check if goods are already in requsts and add new point */
            if(allWaypoints.stream().anyMatch(o -> o.GoodsName.equals(GoodsName))){
                targetIndex tmp = allWaypoints.stream().filter(o -> o.GoodsName.contentEquals(GoodsName)).findFirst().get();
                tmp.AddPoints(row, coll);
            }
            /** Create new index with name and amount */
            else {
                targetIndex tmp = new targetIndex();
                tmp.setIndexName(GoodsName);
                tmp.amount = amount;
                tmp.AddPoints(row, coll);
                allWaypoints.add(tmp);
            }
        }

        public targetIndex GetIndex(String GoodsName){
            if(allWaypoints.stream().anyMatch(o -> o.GoodsName.equals(GoodsName))){
                return allWaypoints.stream().filter(o -> o.GoodsName.contentEquals(GoodsName)).findFirst().get();
            }
            else
                return null;
        }

        //************************************************************************************//
        public class targetIndex {
            private String GoodsName;
            private int amount; //TODO
            private ArrayList<OnePoint> points;

            public targetIndex(){
                points = new ArrayList<>();
            }

            public void setIndexName(String name) {
                GoodsName = name;
            }

            public String getIndexName() {
                return this.GoodsName;
            }

            public int getIndexAmount() {
                return this.amount;
            }

            public void AddPoints(int x, int y) {
                OnePoint tmp = new OnePoint(x, y);
                points.add(tmp);
            }

            public OnePoint GetFirstPoint() {
                return points.get(0);
            }

            public OnePoint GetPoint(int index) {
                return points.size() > index ? points.get(index) : null;
            }

            //************************************************************************************//
            public class OnePoint{
                private int x;
                private int y;

                public OnePoint(int x, int y){
                    this.x = x;
                    this.y = y;

                };

                public void setPoints(int x, int y){
                    this.x = x;
                    this.y = y;
                }

                public void getPoint(int xGetter, int yGetter){
                    xGetter = this.x;
                    yGetter = this.y;
                }

                public int getX() {
                    return x;
                }

                public int getY() {
                    return y;
                }
            }
            //************************************************************************************//

        }
        //************************************************************************************//

    }
    //************************************************************************************//


}
