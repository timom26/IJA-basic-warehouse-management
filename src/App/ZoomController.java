package App;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

public class ZoomController {

    public static void SetProperties(ScrollPane MainPane){

        MainPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        MainPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        MainPane.setScaleShape(true);

        Pane content = ((Pane) MainPane.getContent());

        content.setOnScroll((EventHandler<Event>) event -> {

            double zoomWhere = ((ScrollEvent) event).getDeltaY();

            // Default bounds
            // 610
            // 350
            if (zoomWhere > 0){

                //MainPane.setHmax(610*1.7);
                //MainPane.setVmax(350*1.7);

//                MainPane.setScaleX(1.7);
//                MainPane.setScaleY(1.7);

                //Pane tmp = new Pane(content);


                //content




                content.setMinWidth(1220);
                content.setMinHeight(700);
                content.setPrefWidth(1220);
                content.setPrefHeight(700);
                content.getChildren().stream().forEach(node -> {
                    node.setTranslateY(175);
                    node.setTranslateX(305);
                });

                content.setScaleX(2);
                content.setScaleY(2);

                //content.layout();
                //WarehouseController.ResizeAll(1, content);


//                content.setScaleX(1.7);
//                content.setScaleY(1.7);
            }
            else {

                content.setMinWidth(610);
                content.setMinHeight(350);
                content.setPrefWidth(610);
                content.setPrefHeight(350);

                content.getChildren().stream().forEach(node -> {
                    node.setTranslateY(0);
                    node.setTranslateX(0);
                });

                content.setScaleX(1);
                content.setScaleY(1);
            }



            //MainPane.setContent(content);
            //MainPane.setFitToHeight(true);
            //MainPane.setFitToWidth(true);
//            content.setScaleX(content.getScaleX() * ((ScrollEvent) event).getDeltaX());
//            content.setScaleY(content.getScaleY() * ((ScrollEvent) event).getDeltaY());
            /*content.setTranslateX(content.getTranslateX() + ((ScrollEvent) event).getDeltaX());
            content.setTranslateY(content.getTranslateY() + ((ScrollEvent) event).getDeltaY());*/
        });

//        MainPane.setOnScroll((EventHandler) event -> {
//
//
//
//
//            content.setScaleX(content.getScaleX() * ((ScrollEvent) event).getDeltaX());
//            content.setScaleY(content.getScaleY() * ((ScrollEvent) event).getDeltaY());
//            content.setTranslateX(content.getTranslateX() + ((ScrollEvent) event).getDeltaX());
//            content.setTranslateY(content.getTranslateY() + ((ScrollEvent) event).getDeltaY());
//        });



                //.setScaleX(1.7);
        //MainPane.getContent().setScaleY(1.7);
        //MainPane.getContent().setTranslateX(1.7);
        //MainPane.getContent().setTranslateY(1.7);

//        MainPane.addEventHandler(ScrollEvent, new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent event) {
//                event.
//            }
//        }

    }
}
