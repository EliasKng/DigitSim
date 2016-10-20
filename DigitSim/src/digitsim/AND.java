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

        Draw.gcDrawCircle(gc, 50, 50, 20, Color.RED);
        Draw.gcDrawLine(gc, 0, 0 ,50, 200, 1d, Color.YELLOW);

      
        double allX[] = new double[10];
        double allY[] = new double[10];
        
        for(int i = 0; i < 10; i++)
        {
            allX[i] = i * 20;
        }
        for(int i = 0; i < 10; i++)
        {
            allY[i] = i * 20;
        }
        Draw.gcLineFromArray(gc, allX, allY, 10, Color.BLACK);
    }
}