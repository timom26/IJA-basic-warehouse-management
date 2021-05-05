/**
 * @author Timotej Ponek xponek00
 * @author Timotej Kamensky xkamen24
 * @copyright Brno university of technology, faculty of computer science, Czechia.
 * @brief assignment of java application for basic warehouse management system
 */

package App;

import Reader.CartStruct;
import Reader.WarehouseStruct;
import store.ShoppingCart;

import javafx.event.EventHandler;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class WarehouseController {
    private static List<Rectangle> rects;
    private static List<BlockableGrid> ObstacleGrid;

    protected static List<TrolleyGrid> Trolleys;
    private static Rectangle _parkingArea;
    private static Rectangle _resuplyArea;

    private static double rectHeight;
    private static double rectWidth;
    private static double _horizontalShelfStep;
    private static double _verticalShelfStep;

    private static int stepGrid;
    private static int step;


    // variables responsible for "create multiple obstacles" functionality
    private static boolean castLine = false;
    private static double Linex;
    private static double Liney;

    private static boolean RuntimePropsSet = false;
    protected static WarehouseStruct _controlledWarehouse; //for setting blockages via
    protected static Pane _warehousePane;

    public static enum Direction{
        left,
        right,
        up,
        down
    }

    public static enum UnitOfShift {
        shelfWidth(rectWidth),
        shelfHeight(rectHeight),
        gridSize(stepGrid),
        shelfGridWidth(_horizontalShelfStep),
        shelfGridHeight(_verticalShelfStep),
        Err(0);

        public double measure;

        UnitOfShift(double val)
        {
            this.measure = val;
        }

//        static {
//            for (UnitOfShift e : values()) {
//                BY_VALUE.put(e, e.);
//            }
//        }
//        private static final Map<String, UnitOfShift> BY_LABEL = new HashMap<>();

    }


    public static void SetWarehouse(WarehouseStruct wareHouse){
        _controlledWarehouse = wareHouse;
    }

    public static void PaneDraw(int rows, int cols, Pane warehousePane){
        _warehousePane = warehousePane;

        CreateResuplyArea(warehousePane);
        CreateTrolleys(warehousePane, 1);

        if(rects != null)
            warehousePane.getChildren().removeAll(rects);
        rects = new ArrayList<Rectangle> ();

        double canvasHeight = warehousePane.getHeight() - 20;
        double canvasWidth = warehousePane.getWidth();

        step = 40;
        stepGrid = step/2;
        double stepY = step;
        /** total space in between shelves on x axis */
        double stepX = step*((cols-1)/2);

        rectHeight = (canvasHeight - step)/rows;
        rectWidth = (canvasWidth - stepX - step )/cols;

        double maxX = canvasWidth - stepGrid;
        double maxY = canvasHeight - stepGrid;
        boolean even = true;
        for (double x = stepGrid; x < maxX; x += (even ? step + rectWidth : rectWidth)){
            for (double y = stepGrid; y < maxY; y += rectHeight){
                Rectangle newShelf = new Rectangle(x, y, rectWidth, rectHeight);
                newShelf.setFill(Color.TRANSPARENT);
                newShelf.setStroke(Color.BLACK);
                newShelf.setStrokeWidth(2);

                rects.add(newShelf);
                warehousePane.getChildren().add(newShelf);
            }
            even = !even;
        }

        System.out.println(rows);
        System.out.println(cols);
    }

    private static void SetGridProperties(BlockableGrid grid, int index_x, int index_y){
        grid.setFill(Color.TRANSPARENT);
        grid.setStroke(Color.LIGHTBLUE);
        grid.getStrokeDashArray().addAll(1.0, 4.0);
        grid.setStrokeDashOffset(2);
        grid.setIndexes(index_x, index_y);

        grid.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if(!event.isShiftDown()) {
                    if (grid.getFill().equals(Color.TRANSPARENT)){
                        _controlledWarehouse.addBlockage(grid._index_x, grid._index_y);
                        grid.setFill(Color.BLACK);
                    }
                    else{
                        grid.setFill(Color.TRANSPARENT);
                        _controlledWarehouse.removeBlockage(grid._index_x, grid._index_y);
                    }
                }
            }
        });

        ObstacleGrid.add(grid);
    }


    public static void DrawGrid(int rows, int cols, Pane warehousePane){
        //remove all blockages
        if(ObstacleGrid != null)
            warehousePane.getChildren().removeAll(ObstacleGrid);
        ObstacleGrid = new ArrayList<BlockableGrid> ();

        //set canvas dimensions
        double canvasHeight = warehousePane.getHeight() - 20;
        double canvasWidth = warehousePane.getWidth();
        double maxX = canvasWidth;
        double maxY = canvasHeight - stepGrid;

        int index_X = -1;
        int index_Y = 0;

        boolean even = false;

        for (double x = 0; x < maxX; x += (even ? (2*rectWidth) + stepGrid : stepGrid)){
            for (double y = stepGrid; y < maxY; y += rectHeight){
                BlockableGrid grid = new BlockableGrid(x, y, stepGrid, rectHeight);
                SetGridProperties(grid, index_X, index_Y++);
                warehousePane.getChildren().add(grid);
                grid.toBack();
            }
            index_Y = 0;
            even = !even;
            if(even)
                index_X += 3;
            else
                index_X += 1;
        }

        index_X = -1;
        index_Y = -1;

        even = false;
        boolean touchingShelves = false; // for drawing grid that touches shelves
        int countToTWO = 2;
        for (double y = 0; y < canvasHeight; y += canvasHeight - stepGrid){
            for (double x = 0; x < canvasWidth; x += (even ? rectWidth : stepGrid)){
                BlockableGrid grid;
                if(touchingShelves){
                    grid = new BlockableGrid(x, y, rectWidth, stepGrid);
                }
                else {
                    grid = new BlockableGrid(x, y, stepGrid, stepGrid);
                }

                SetGridProperties(grid, index_X++, index_Y);
                warehousePane.getChildren().add(grid);
                grid.toBack();

                if (countToTWO == 2){
                    touchingShelves = !touchingShelves;
                    countToTWO = 0;
                }
                else {
                    even = !even;
                }

                countToTWO++;
            }
            index_X = -1;
            index_Y = rows;
        }



        // Set this property only once, warehousePane exists throughout the whole program life so
        // without check, we would set property multiple times and get not desired behavior
        if(!RuntimePropsSet){
            warehousePane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent event) {
                    if(castLine && event.isShiftDown()){
                        Line RayCast_toBlock = new Line(Linex,Liney,event.getX(), event.getY());
                        warehousePane.getChildren().add(RayCast_toBlock);
                        for (BlockableGrid r: ObstacleGrid) {
                            if(r.getBoundsInParent().intersects(RayCast_toBlock.getBoundsInParent()))
                                if (r.getFill().equals(Color.TRANSPARENT)){
                                    _controlledWarehouse.addBlockage(r._index_x, r._index_y);
                                    r.setFill(Color.BLACK);
                                }
                                else{
                                    _controlledWarehouse.addBlockage(r._index_x, r._index_y);
                                    r.setFill(Color.TRANSPARENT);
                                }
                        }
                        castLine = false;
                        warehousePane.getChildren().remove(RayCast_toBlock);
                    }
                    else if(event.isShiftDown()){
                        Linex = event.getX();
                        Liney = event.getY();
                        castLine = true;
                    }

                }
            });
            RuntimePropsSet = true;

        }

        /**Here we calculate traveldistances for different situations */
        _horizontalShelfStep = rectWidth/2 + stepGrid/2;
        _verticalShelfStep = rectHeight/2 + stepGrid/2;
    }

    public static void CreateResuplyArea(Pane warehousePane){
        if(_resuplyArea != null)
            warehousePane.getChildren().removeAll(_resuplyArea);
        if(_parkingArea != null)
            warehousePane.getChildren().removeAll(_parkingArea);

        double canvasHeight = warehousePane.getHeight();
        double canvasWidth = warehousePane.getWidth();

        Rectangle ResuplyArea = new Rectangle(canvasWidth-70, canvasHeight-20, 70, 20);
        ResuplyArea.setFill(Color.GREY);
        _resuplyArea = ResuplyArea;
        warehousePane.getChildren().add(_resuplyArea);


        Rectangle ParkingArea = new Rectangle(0, canvasHeight-20, 300, 20);
        ParkingArea.setFill(Color.GREY);
        _parkingArea = ParkingArea;
        warehousePane.getChildren().add(_parkingArea);
    }

    public static void CreateTrolleys(Pane warehousePane, int howMany){

        //argument parsing
        if(howMany > 10){
            return; //we don't allow more than 10 trolleys
        }

        //delete trolleys if any other trolleys exist
        if(Trolleys != null){
            //doesn't work
            //warehousePane.getChildren().removeAll(Trolleys.stream().map(TrolleyGrid -> TrolleyGrid._route).collect(Collectors.toList()));
            //warehousePane.getChildren().removeAll(Trolleys);
            for (TrolleyGrid t: Trolleys) {
                if (t._route != null)
                    warehousePane.getChildren().removeAll(t._route);
                warehousePane.getChildren().removeAll(t);
            }
        }

        //get parameters
        Trolleys = new ArrayList<TrolleyGrid>();
        double canvasHeight = warehousePane.getHeight();
        double canvasWidth = warehousePane.getWidth();

        int x = 5;
        //Note by Tim, 5.5.2021 18:45: changed <= to <, because we want to count amount of carts from 1, not zero
        for(int counter = 0; counter < howMany; counter++ ){
            TrolleyGrid trolley = new TrolleyGrid(x, canvasHeight-15, 10, 10);
            //TrolleyGrid trolley = new TrolleyGrid(x, canvasHeight-30, 10, 10);
            trolley.setFill(Color.CORAL);

            trolley.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>(){
                @Override
                public void handle(MouseEvent event) {
                    System.out.println("We here at coords: [" + trolley.boundedCart.coord_x + ", " + trolley.boundedCart.coord_y + "]");

                    if(trolley.boundedCart == null)
                        return;

                    if(trolley.isRoutePrinted) {
                        warehousePane.getChildren().removeAll(trolley._route);
                        trolley._route = new ArrayList<>();
                        trolley.isRoutePrinted = false;
                    }
                    else{
                        trolley._route = new ArrayList<>();
                        TrolleyController.PrintRoute(trolley.boundedCart.coordList, warehousePane, trolley, trolley.boundedCart.coordIndex);
                        trolley.isRoutePrinted = true;
                    }
                }
            });

            //warehousePane.toFront(trolley);

            Trolleys.add(trolley);
            warehousePane.getChildren().add(trolley);

            trolley.toFront();

            x += 25;
        }
    }


    public static void BoundCartToTrolley(int id, ShoppingCart cart){
        if(id >= Trolleys.size())
            return;
        Trolleys.get(id).boundedCart = cart;
    }


    /*
    systém obsahuje vlastní hodiny, které lze nastavit na výchozí hodnotu a různou rychlost
    po načtení mapy a obsahu skladu začne systém zobrazovat zpracování jednotlivých požadavků (způsob zobrazení je na vaší invenci, postačí značka, kolečko, ...)
    symbol vozíku se postupně posunuje podle aktuálního času a požadavků (aktualizace zobrazení může být např. každých N sekund); pohyb spoje na trase je tedy simulován
    po najetí/kliknutí na symbol vozíku se zvýrazní trasa v mapě a zobrazí jeho aktuální náklad
     */

/** ######################################################################################### */

/** ######################################################################################### */



    /**
     * This wont work bra
     */
    public static void AddTrolleyToolTip(){

        for (TrolleyGrid r: Trolleys) {
            if(r.boundedCart != null){
                Tooltip newToolTip = new Tooltip(r.boundedCart.PrintGoods());

                newToolTip.setShowDelay(Duration.ZERO);
                Tooltip.install(
                        r,
                        newToolTip
                );
            }

        }
    }


    public static void AddShelfToolTip(WarehouseStruct depot){

        int maxRows = depot.getRows();
        int maxCols = depot.getCols();

        int cols = 0;
        int rows = 0;

        for (Rectangle r: rects) {
            Tooltip newToolTip = new Tooltip(depot.PrintGoods(rows++,cols));
            if(rows == maxRows){
                rows = 0;
                cols++;
            }

            //Set color relative to state of shelf
            if(newToolTip.textProperty().getValue() == "Empty")
                ;
            else
                r.setFill(Color.RED);

            newToolTip.setShowDelay(Duration.ZERO);
            Tooltip.install(
                    r,
                    newToolTip
            );
        }
    }


    public static class BlockableGrid extends Rectangle{
        public int _index_x;
        public int _index_y;

        public void setIndexes(int x, int y){
            this._index_x = x;
            this._index_y = y;
        }

        public BlockableGrid(double x, double y, double width, double height){
            super(x, y, width, height);
        }
    }

    public static class TrolleyGrid extends Rectangle{
        public ShoppingCart boundedCart;
        public List<Line> _route;
        public boolean isRoutePrinted = false;

        public void boundCart(ShoppingCart cart){
            this.boundedCart = cart;
        }

        public TrolleyGrid(double x, double y, double width, double height){
            super(x, y, width, height);
        }
    }


}
