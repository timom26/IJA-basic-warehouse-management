package Reader;

import store.ShoppingCart;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class CartStruct {

    public static List<Trolley> allOrders;
    public static List<ShoppingCart> allCarts;


    public void Reset(){
        allOrders = new ArrayList<>();
        allCarts = new ArrayList<>();
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
    public void AddGoodsIndexToCart(int id, int row, int coll, String GoodsName){
        Trolley toAdd = allOrders.get(id-1);

        toAdd.AddIndex(row, coll, GoodsName);

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

        public void AddIndex(int row, int coll, String GoodsName){
            /* if(allWaypoints.contains(o -> ((Index) o).GoodsName.contentEquals(GoodsName))){ } */
            if(allWaypoints.stream().anyMatch(o -> o.GoodsName.equals(GoodsName))){
                targetIndex tmp = allWaypoints.stream().filter(o -> o.GoodsName.contentEquals(GoodsName)).findFirst().get();
                tmp.AddPoints(row, coll);
                allWaypoints.add(tmp);
            }
            else {
                targetIndex tmp = new targetIndex();
                tmp.setIndexName(GoodsName);
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

            public String GetIndexName() {
                return this.GoodsName;
            }

            public void AddPoints(int x, int y) {
                OnePoint tmp = new OnePoint(x, y);
                points.add(tmp);
            }

            public OnePoint GetFirstPoint() {
                return points.get(0);
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