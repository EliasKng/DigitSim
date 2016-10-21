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


        vec2 v = new vec2();
        v.set(10, 20);   
        System.out.printf("*DEFAULT VEC2*\n%.1f\n%.1f\n", v.x, v.y);
        v.add(new vec2(15, 5));     
        System.out.printf("*ADDED VEC2*\n%.1f\n%.1f\n", v.x, v.y);
        v.sub(25, 15);
        System.out.printf("*SUBTRACTED VEC2*\n%.1f\n%.1f\n", v.x, v.y);
       // Draw.gcLineFromArray(gc, allX, allY, 10, Color.BLACK);
    }
}