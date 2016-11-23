/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;

import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Elias
 */
public class Pathfinder {
    
    
    public Pathfinder() {
        
    }
    
    public int[][] createArray(ArrayList<Element> elements, double width, double height, DraggableCanvas canvas) {
        int arrayWidth = (int) Math.ceil(width/21); //Berechnet den Wert für die Weite den Arrays (jedes Kästchen soll 1 arrayplatz belegen), desshalb wird die gesamthöhe genommen, durch 21 geteilt und dann aufgerundet
        int arrayHeight = (int) Math.ceil(height/21);
        int[][] blocked = new int[arrayWidth][arrayHeight]; //In diesem 2 dimensionalen array, wird gespeichert ob ein Kästchen (z.B. durch ein Element) geblockt wird
        int eWidth = 6; //Weite der elemente (in 21 schritten) (ist 6 weil die Ausgänge & Eingänge mit dazu gezählt werden)
        int eHeight; //Höhe der elemente (in 21 schritten)
        int eX; //X-Koordinate des Elements (in 21 Schritten)
        int eY; //Y-Koordinate des Elements (in 21 Schritten)
        
        //Diese Schleife wird für jedes Element einmal durchlaufen
        for(Element i : elements){ //elemente durchgehen...
            eHeight = getElementHeight(i.numInputs);
            eX = (int) i.getX()/21 - 1;
            eY = (int) i.getY()/21;
            
            for(int k = eX; k < (eX + eWidth); k++) {
                for(int o = eY; o < (eY + eHeight); o++) {
                    blocked[k][o] = 1;
                    Rectangle r = Draw.drawRectangle((k)*21, (o)*21,21,21,0,0, Color.BLACK, 0.7, 1);
                    canvas.getChildren().add(r);
                }
            }
        }
        return blocked;
    }
    
    /**
     * berechnet die Höhe des standard Elements anhand seiner Anzahl con Eingängen
     * @param inputs
     * @return 
     */
    public int getElementHeight(int inputs) {
        int h = 4;
        if (inputs > 4) {
            h=inputs;
        }
        return h;
    }

}
