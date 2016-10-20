/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;


import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Pane;


/**
 *
 * @author Elias
 */
public class AND {
    public AND(GraphicsContext gc) {
        gc.setFill(Color.RED);
       // gc.setStroke(Color.YELLOW);
       // gc.setLineWidth(20);
       // gc.strokeLine(40, 10, 10, 40);
        gc.fillOval(10, 60, 30, 30);
        
        Draw.gcDrawLine(gc, 0, 0 ,50, 200, 1d, Color.YELLOW);
        
        /*Rectangle rect1 = new Rectangle(100,100);
        rect1.setTranslateX(450);
        rect1.setTranslateY(450);
        rect1.setStroke(Color.BLUE);
        rect1.setFill(Color.BLUE.deriveColor(1, 1, 1, 0.5));'*/
    }

}
