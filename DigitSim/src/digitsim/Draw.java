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
        gc.setStroke(color);
        gc.setLineWidth(size);
        gc.strokeLine( x1, y1, x2, y2);
    }
}
