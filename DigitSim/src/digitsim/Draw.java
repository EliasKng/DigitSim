/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;

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
    * Line zeichnen, die aus mehreren koordinaten als 2 besteht
    * 
    * @author Tim
    * 
    * @param gc GraphicsContent
    * @param dx Array mit X - Koordinaten, jedoch nur die differenz zum LinienUrsprung
    * @param dy Array mit Y - Koordinaten, jedoch nur die differenz zum LinienUrsprung
    * @param lineWidth Liniendicke
    * @param color Linienfarbe
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
    
    /**
    * Funktion zum Linien zeichnen, im Array jedoch wird nur die Differenz zum Linienursprung angegeben
    * 
    * @author Elias
    * 
    * @param gc GraphicsContent
    * @param dx Array mit X - Koordinaten, jedoch nur die differenz zum LinienUrsprung
    * @param dy Array mit Y - Koordinaten, jedoch nur die differenz zum LinienUrsprung
    * @param lineWidth Liniendicke
    * @param color Linienfarbe
    * @param baseX X-Startkoordinate der Linie
    * @param baseY Y-Startkoordinate der Linie
    * 
    */
    public static void gcLineFromArrayBaseCoords(GraphicsContext gc, double dx[], double dy[], double lineWidth, Color color, double baseX, double baseY)
    {        
       for(int i = 0; i < dx.length; i++) {
           dx[i] = dx[i] + baseX;
           dy[i] = dy[i] + baseY;
       }
       gcLineFromArray(gc,dx,dy,lineWidth,color);
    }
    
}