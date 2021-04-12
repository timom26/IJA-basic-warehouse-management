package Reader;

import store.Shelf;

import java.util.ArrayList;
import java.util.List;
/**
 * represents warehouse as a grid of shelves
 * */
public class WarehouseStruct {
    public List shelves = new ArrayList();
    public List goods = new ArrayList();

    private int rows;
    private int cols;


    /**
     * @brief returns list of goods from a given shelf
     * @param row coord of a shelf
     * @param coll coord of a shelf
     * @return string, containing all goods
     */
    public String PrintGoods(int row, int coll){
        return ((Shelf) ((ArrayList) shelves.get(row)).get(coll)).getAll();
    }

    /**
     * @brief returns max rows of the warehouse
     * @return rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * @brief set rows of a warehouse
     * @param rows - set this value
     */
    public void setRows(int rows) {
        this.rows = rows;
    }

    /**
     * @brief returns max cols of the warehouse
     * @return cols
     */
    public int getCols() {
        return cols;
    }

    /**
     * @brief set cols of a warehouse
     * @param cols set this value
     */
    public void setCols(int cols) {
        this.cols = cols;
    }
}
