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
        gc.fillOval(10, 60, 30, 30);
        
        Draw.gcDrawLine(gc, 0, 0 ,50, 200, 1d, Color.YELLOW);
    }

}
