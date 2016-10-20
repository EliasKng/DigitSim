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
 * Alle Funktionen die zeichnen / animieren gehören in diese Klasse
 * @author Elias
 */
public class Draw {
    
    /**
    * Funktion zum Linien zeichnen
    *
    * @author Tim
    */
    public static void gcDrawLine(GraphicsContext gc, double x1, double y1, double x2, double y2, double size, Color color)
    { 
        if(gc.getStroke() != color)
        {
            gc.setStroke(color);
        }
        gc.setLineWidth(size);
        gc.strokeLine( x1, y1, x2, y2);
    }
    
    /**
<<<<<<< Upstream, based on origin/master
    * Animiert Karo auf simCanvas
    *
    * @author Elias
    * Bearbeitet von Tim 16.10.16
    */
    public static void addGrid(Canvas simCanvas, GraphicsContext gc) {

        double w = simCanvas.getWidth();
        double h = simCanvas.getHeight();


        simCanvas.setMouseTransparent(false);

        gc = simCanvas.getGraphicsContext2D();
        

        // Karomuster malen
        // offset = linien abstand
        double offset = 21;
        double d;
        for( double i=offset; i < w; i+=offset) {
                if(i % 4 == 0) d = 2; //Jede 5. Linie mit doppelter Dicke zeichnen
                else d = 1;
                                
                Draw.gcDrawLine(gc, i, 0, i, h, d, Color.LIGHTGREY);
                Draw.gcDrawLine(gc, 0, i, w, i, d, Color.LIGHTGREY);
        }
       //Setzt simCanvas in den Hintergrund
        simCanvas.toBack();
    }

    /**
    * Line zeichnen, die aus mehreren koordinaten als 2 besteht
    * @author Tim
    */
    public static void gcLineFromArray(GraphicsContext gc, double dx[], double dy[], double lineWidth, Color color)
    { 
       int points = 0;
       if(dx.length != dy.length) {
           System.out.println("linie Wurde nicht gezeichnet weil: array Länge stimmt nicht");
       }
       else{
           points = dx.length;
       }
       
       if(points > 1)
       {
            for(int i = 1; i < points; i++)
            {
                gcDrawLine(gc, dx[i], dy[i], dx[i - 1], dy[i - 1], lineWidth, color);   
            }
        }
    }
    
    /**
    * Funktion zum Kreise zeichnen
    *
    * @author Tim
    */
    public static void gcDrawCircle(GraphicsContext gc, double x, double y, double r, Color color)
    { 
        if(gc.getFill() != color)
        {
            gc.setFill(color);
        }
        gc.fillOval(x - r, y - r, 2 * r, 2 * r);
    }
    
}