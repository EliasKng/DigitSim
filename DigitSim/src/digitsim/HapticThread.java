package digitsim;

import element.Element;
import java.lang.Thread;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *Dieser THread ist für die "Haptik" verantwortlich. Also z.B. sorgt er dafür, dass man eine Ausgabe bekommt,wenn mann über einen Aus/Eingang eines Elementes hovert
 * @author Elias
 */
public class HapticThread extends Thread {
    private DigitSimController dsController;

    public HapticThread(DigitSimController pDSController) { //Constructor
        dsController = pDSController; //Referenz auf den Controller, damit wir dessen Funktionen benutzen können
    }

    @Override
    public void run() { //Diese Funktion arbeitet der Thread ab
        ArrayList<Element> elements = dsController.getElements();
        getAllXYElementCoorinates(elements);
    }
    
    
    /**
     * erstellt ein array, welches die X & Y Koordinaten aller Iin & Outputs beinhaltet
     * @param elements
     * @return 
     */
    private int[][] getAllXYElementCoorinates(ArrayList<Element> elements){
        int IOCount = getNumberOfInOutputs(elements);
        int[][] allXY = new int[IOCount][2];
        
        int counter = 0;
        
        for (int n = 0; n < dsController.getElements().size(); n++) {
            // X & Y Koordinaten aller Eingänge speichern
            for (int i = 0; i < dsController.getElements().get(n).numInputs; i++) {
                double dInX = dsController.getElements().get(n).getInputX(i);
                double dInY = dsController.getElements().get(n).getInputY(i);
                allXY[counter][0] =(int) dInX;
                allXY[counter][1] =(int) dInY;
                Circle c = Draw.drawCircle(dInX, dInY, 2, Color.CORAL,0.0,false, 1);
                dsController.getSimCanvas().getChildren().add(c);
                counter++;
            }

            // X & Y Koordinaten aller Eingänge speichern
            for (int i = 0; i < dsController.getElements().get(n).getNumOutputs(); i++) {
                double dInX = dsController.getElements().get(n).getOutputX(i);
                double dInY = dsController.getElements().get(n).getOutputY(i);
                Circle c = Draw.drawCircle(dInX, dInY, 2, Color.CORAL,0.0,false, 1);
                dsController.getSimCanvas().getChildren().add(c);
                allXY[counter][0] =(int) dInX;
                allXY[counter][1] =(int) dInY;
                counter++;
            }
        }
        
        return null;
    }
    
    /**
     * Gibt die Anzahl der Ein und Ausgänge aller Elemente zurück
     * @param elements
     * @return 
     */
    private int getNumberOfInOutputs(ArrayList<Element> elements) {
        int count = 0;
        for(Element i : elements) {
            count += i.getOutputCount();
            count += i.getInputCount();
        }
        System.out.println("ANZAHL ALLER EIN&AUSGÄNGE: " +count);
        return count;
        
    }
}
