/**
 * @author Timotej Ponek xponek00
 * @author Timotej Kamensky xkamen24
 * @copyright Brno university of technology, faculty of computer science, Czechia.
 * @brief assignment of java application for basic warehouse management system
 */
package App;

import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

public class ZoomController {

    public static void SetProperties(ScrollPane MainPane){

        MainPane.setScaleShape(true);
        Pane content = ((Pane) MainPane.getContent());

        content.setOnScroll(event -> {
            if(!event.isControlDown())
                return;

            double zoomWhere = ((ScrollEvent) event).getDeltaY();

            // Default bounds
            // 610
            // 350
            if (zoomWhere > 0){

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

        });

    }
}
