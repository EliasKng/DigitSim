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
        
        int gridOffset = Properties.GetGridOffset();
        
        int arrayWidth = (int) Math.ceil(width / gridOffset); //Berechnet den Wert für die Weite den Arrays (jedes Kästchen soll 1 arrayplatz belegen), desshalb wird die gesamthöhe genommen, durch 21 geteilt und dann aufgerundet
        int arrayHeight = (int) Math.ceil(height / gridOffset);
        int[][] fieldCode = new int[arrayWidth][arrayHeight]; //In diesem 2 dimensionalen array, wird gespeichert ob ein Kästchen (z.B. durch ein Element) geblockt wird
        int eWidth = 6; //Weite der elemente (in 21 schritten) (ist 6 weil die Ausgänge & Eingänge mit dazu gezählt werden)
        int eHeight; //Höhe der elemente (in 21 schritten)
        int eX; //X-Koordinate des Elements (in gridOffset Schritten)
        int eY; //Y-Koordinate des Elements (in gridOffset Schritten)
        
        //Diese Schleife wird für jedes Element einmal durchlaufen
        for(Element i : elements){ //elemente durchgehen...
            eHeight = getElementHeight(i.numInputs);
            eX = (int) i.getX() / gridOffset - 1;
            eY = (int) i.getY() / gridOffset;
            
            
            for(int k = eX-1; k < (eX + eWidth+1); k++) {
                for(int o = eY-1; o < (eY + eHeight+1); o++) {
                    if((k >= eX && k < (eX+eWidth)) && (o >= eY && o < (eY + eHeight))) {
                        fieldCode[k][o] = 1;
                        Rectangle r = Draw.drawRectangle((k) * gridOffset, (o) * gridOffset, gridOffset, gridOffset, 0, 0, Color.BLACK, 0.7, 1);
                        canvas.getChildren().add(r);
                    } else {
                        fieldCode[k][o] = 2;
                        Rectangle r = Draw.drawRectangle((k) * gridOffset, (o) * gridOffset, gridOffset, gridOffset, 0, 0, Color.RED, 0.7, 1);
                        canvas.getChildren().add(r);
                    }
                    
                }
            }
        }
        return fieldCode;
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
    
    /**
     * Berechnet die "Kosten" bis zum Ziel nach der Manhattan-Methode
     * @param cFX = currentFieldXCoordinate : beschreibt die X-Koordinate des aktuellen Feldes
     * @param cFY = currentFieldYCoordinate : beschreibt die Y-Koordinate des aktuellen Feldes
     * @param tFX = targetFieldXCoordinate : beschreibt die X-Koorinate des Ziels
     * @param tFY = targetFieldYCoordinate : beschreibt die Y-Koorinate des Ziels
     * @return retunrt die Entfernung bis zum Ziel
     */
    public int getHCost(int cFX,int cFY,int tFX,int tFY) {
        int hCost = (Math.abs(cFX - tFX)) + (Math.abs(cFY - tFY));
        return hCost;
    }
}
