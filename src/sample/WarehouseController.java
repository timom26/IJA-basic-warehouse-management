/**
 * @author Timotej Ponek xponek00
 * @author Timotej Kamensky xkamen24
 * @copyright Brno university of technology, faculty of computer science, Czechia.
 * @brief assignment of java application for basic warehouse management system
 */

package sample;

import Reader.Read;
import Reader.WarehouseStruct;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class WarehouseController {
    private static List<Rectangle> rects;
    private static List<Rectangle> ObstacleGrid;

    private static double rectHeight;
    private static double rectWidth;

    private static boolean castLine = false;
    private static double Linex;
    private static double Liney;

    private static int step;


    public static void PaneDraw(int rows, int cols, Pane warehousePane){

//        for (Rectangle r :rects) {
//
//        }
        if(rects != null)
            warehousePane.getChildren().removeAll(rects);
        rects = new ArrayList<Rectangle> ();

        double canvasHeight = warehousePane.getHeight();
        double canvasWidth = warehousePane.getWidth();

        step = 20;
        double stepY = step;
        double stepX = step*(cols/2); //step*(((double)cols)/2);// + (cols % 2 == 0 ? 0 : step);

        rectHeight = (canvasHeight - stepY - step)/rows;
        rectWidth = (canvasWidth - stepX - (cols%2 == 0 ? step : 2*step))/cols;

        //MouseEvent handlers

        double maxX = canvasWidth - step;
        double maxY = canvasHeight - step;
        boolean even = true;
        for (double x = step; x < maxX; x += (even ? step + rectWidth : rectWidth)){
            for (double y = step; y < maxY; y += rectHeight){
                Rectangle novinka = new Rectangle(x, y, rectWidth, rectHeight);
                novinka.setFill(Color.TRANSPARENT);
                novinka.setStroke(Color.BLACK);
//                novinka.getStrokeDashArray().addAll(1.0, 4.0);
//                novinka.setStrokeDashOffset(2);
                novinka.setStrokeWidth(2);

                rects.add(novinka);
                warehousePane.getChildren().add(novinka);
            }
            even = !even;
        }

        System.out.println(rows);
        System.out.println(cols);
    }


    private static void SetGridProperties(Rectangle grid){
        grid.setFill(Color.TRANSPARENT);
        grid.setStroke(Color.LIGHTBLUE);
        grid.getStrokeDashArray().addAll(1.0, 4.0);
        grid.setStrokeDashOffset(2);

        grid.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if(!event.isShiftDown()) {
                    if (grid.getFill().equals(Color.TRANSPARENT))
                        grid.setFill(Color.BLACK);
                    else
                        grid.setFill(Color.TRANSPARENT);
                }
            }
        });

        ObstacleGrid.add(grid);
    }


    public static void DrawGrid(int rows, int cols, Pane warehousePane){
        if(ObstacleGrid != null)
            warehousePane.getChildren().removeAll(ObstacleGrid);
        ObstacleGrid = new ArrayList<Rectangle> ();

        double canvasHeight = warehousePane.getHeight();
        double canvasWidth = warehousePane.getWidth();

        double maxX = canvasWidth - step;
        double maxY = canvasHeight - step;

        boolean even = true;

        for (double x = step; x < maxX; x += (even ? step + rectWidth : rectWidth)){
            for (double y = 0; y < canvasHeight; y += maxY){
                Rectangle grid = new Rectangle(x, y, rectWidth, step);
                SetGridProperties(grid);
                warehousePane.getChildren().add(grid);
            }
            even = !even;
        }

        for (double x = 0; x < canvasWidth; x += step + 2*rectWidth){
            for (double y = 0; y < canvasHeight; y += maxY){
                Rectangle grid = new Rectangle(x, y, step, step);
                SetGridProperties(grid);
                warehousePane.getChildren().add(grid);
            }
        }

        for (double x = 0; x < canvasWidth; x += step + 2*rectWidth){
            for (double y = step; y < maxY; y += rectHeight){
                Rectangle grid = new Rectangle(x, y, step, rectHeight);
                SetGridProperties(grid);
                warehousePane.getChildren().add(grid);
            }
        }

        if(cols%2 == 1){
            double x = canvasWidth - step;
            for (double y = step; y < maxY; y += rectHeight){
                Rectangle grid = new Rectangle(x, y, step, rectHeight);
                SetGridProperties(grid);
                warehousePane.getChildren().add(grid);
            }
            for (double y = 0; y < canvasHeight; y += maxY) {
                Rectangle grid = new Rectangle(x, y, step, step);
                SetGridProperties(grid);
                warehousePane.getChildren().add(grid);
            }
        }

        warehousePane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(castLine && event.isShiftDown()){
                    Line alo = new Line(Linex,Liney,event.getX(), event.getY());
                    warehousePane.getChildren().add(alo);
                    for (Rectangle r: ObstacleGrid) {
                        if(r.getBoundsInParent().intersects(alo.getBoundsInParent()))
                            if (r.getFill().equals(Color.TRANSPARENT))
                                r.setFill(Color.BLACK);
                            else
                                r.setFill(Color.TRANSPARENT);
                    }
                    castLine = false;
                    warehousePane.getChildren().remove(alo);
                }
                else if(event.isShiftDown()){
                    Linex = event.getX();
                    Liney = event.getY();
                    castLine = true;
                }

            }
        });

    }


    public static void AddToolTip(WarehouseStruct depot){

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

}
