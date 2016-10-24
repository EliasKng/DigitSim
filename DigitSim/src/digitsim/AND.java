/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;


/**
 *
 * @author Elias
 */
public class AND {
    public AND(DraggableCanvas simCanvas) {
        double w =simCanvas.getWidth();
        double h =simCanvas.getHeight();
        Canvas canvas = new Canvas();
        canvas.setWidth(w);
        canvas.setHeight(h);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Draw.gcDrawCircle(gc, 50, 50, 20, Color.RED);
        Draw.gcDrawLine(gc, 0, 0 ,50, 200, 1d, Color.YELLOW);


        vec2 v = new vec2();
        v.set(10, 20);   
        System.out.printf("*DEFAULT VEC2*\n%.1f\n%.1f\n", v.x, v.y);
        v.add(new vec2(15, 5));     
        System.out.printf("*ADDED VEC2*\n%.1f\n%.1f\n", v.x, v.y);
        v.sub(25, 15);
        System.out.printf("*SUBTRACTED VEC2*\n%.1f\n%.1f\n", v.x, v.y);
       
       
        double bX = 350;
        double bY = 350;
        
        double aX[] = new double[3];
        double aY[] = new double[3];
        aX[0] = 0;
        aY[0] = 0;
        aX[1] = 20;
        aY[1] = 30;
        aX[2] = 20;
        aY[2] = 0;
        Draw.gcLineFromArrayBaseCoords(gc, aX, aY, 5,Color.RED, bX, bY);
    }
}