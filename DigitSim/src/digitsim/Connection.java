/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;

import static digitsim.DigitSimController.getElements;
import java.util.ArrayList;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

/**
 *
 * @author Tim
 */
public class Connection { //Speichert die Verbindungen
    
    public class ConData                       // Daten einer Verbindung
    {
        public int indexFirstElement;       // index des ersten elements im array
        public boolean typeFirst;           // ein oder ausgang
        public int indexFirst;              // index des jeweiligen ein oder ausgangs am ersten element
        public int indexSecondElement;      // index des zweiten elements im array
        public boolean typeSecond;          // ein oder ausgang
        public int indexSecond;             // index des jeweiligen ein oder ausgangs am zweiten element
        public Line connectionLine;         // line, die beide elemente verbindet
    }
    
    public static ArrayList<ConData> connections = new ArrayList<ConData>(); //Speichert alle Verbindungne
    
/**
 * addConnection(..)
 * Fügtt eine Verbindung zwischen ein und ausgängen von verachiedenen elementen hinzu
 * @author Tim
 * 
 * @param indexFirstElement erstes element (index im main array in der Controlelr klasse elements)
 * @param indexSecondElement zweites element (index im main array in der Controlelr klasse elements)
 * param type: 0 für ausgang, 1 für eingang
 * param index: index des ein oder ausgangs am element
 */
    public void addConnection(int indexFirstElement, boolean typeFirst, int indexFirst, int indexSecondElement, boolean typeSecond, int indexSecond)
    {
        ConData data = new ConData();
        data.indexFirstElement = indexFirstElement;
        data.indexSecondElement = indexSecondElement;
        data.indexFirst = indexFirst;
        data.indexSecond = indexSecond;
        data.typeFirst = typeFirst;
        data.typeSecond = typeSecond;
        
        connections.add(data);   
    }
    
    /* -Bearbeitet von Tim 14.11.16 */
    void update() //Geht alle Verbindungen durch und schaltet sie durch
    {
        for(ConData d : connections)
        {
            double lineX1 = 0;
            double lineY1 = 0;
            double lineX2 = 0;
            double lineY2 = 0;
         
           if((d.typeFirst == d.typeSecond) && !d.typeFirst) // ausgang mit ausgang verbunden
           {
            lineX1 = DigitSimController.getElements().get(d.indexFirstElement).getOutputX(d.indexFirst);
            lineY1 = DigitSimController.getElements().get(d.indexFirstElement).getOutputY(d.indexFirst);
            lineX2 = DigitSimController.getElements().get(d.indexSecondElement).getOutputX(d.indexFirst);
            lineY2 = DigitSimController.getElements().get(d.indexSecondElement).getOutputX(d.indexFirst);
           }
           else if((d.typeFirst != d.typeSecond) && !d.typeFirst) // ausgang mit eingang verbunden
            {
                lineX1 = DigitSimController.getElements().get(d.indexFirstElement).getOutputX(d.indexFirst);
                lineY1 = DigitSimController.getElements().get(d.indexFirstElement).getOutputY(d.indexFirst);
                lineX2 = DigitSimController.getElements().get(d.indexSecondElement).getInputX(d.indexFirst);
                lineY2 = DigitSimController.getElements().get(d.indexSecondElement).getInputY(d.indexFirst);
                DigitSimController.getElements().get(d.indexSecondElement).setInput(d.indexSecond, DigitSimController.getElements().get(d.indexFirstElement).getOutput(d.indexFirst));
            }
            else if((d.typeFirst != d.typeSecond) && d.typeFirst) // eingang mit ausgang verbunden
            {
                lineX1 = DigitSimController.getElements().get(d.indexFirstElement).getInputX(d.indexFirst);
                lineY1 = DigitSimController.getElements().get(d.indexFirstElement).getInputY(d.indexFirst);
                lineX2 = DigitSimController.getElements().get(d.indexSecondElement).getOutputX(d.indexFirst);
                lineY2 = DigitSimController.getElements().get(d.indexSecondElement).getOutputY(d.indexFirst);
                DigitSimController.getElements().get(d.indexFirstElement).setInput(d.indexFirst, DigitSimController.getElements().get(d.indexSecondElement).getOutput(d.indexSecond));
            }
            else if((d.typeFirst == d.typeSecond) && d.typeFirst) // eingang mit eingang verbunden
            {
                lineX1 = DigitSimController.getElements().get(d.indexFirstElement).getInputX(d.indexFirst);
                lineY1 = DigitSimController.getElements().get(d.indexFirstElement).getInputY(d.indexFirst);
                lineX2 = DigitSimController.getElements().get(d.indexSecondElement).getInputX(d.indexFirst);
                lineY2 = DigitSimController.getElements().get(d.indexSecondElement).getInputY(d.indexFirst);
                int newVal = DigitSimController.getElements().get(d.indexFirstElement).inputs[d.indexFirst];
                
                
                DigitSimController.getElements().get(d.indexFirstElement).setInput(d.indexFirst, newVal);
                DigitSimController.getElements().get(d.indexSecondElement).setInput(d.indexSecond, newVal);
            }
            
            // linien zeichenen
            if(d != null)
            DigitSimController.getSimCanvas().getChildren().remove(d.connectionLine);
            d.connectionLine = Draw.drawLine(lineX1, lineY1, lineX2, lineY2, Color.BLACK, 4);
            DigitSimController.getSimCanvas().getChildren().add(d.connectionLine);
             
            //Die Beusteine selbst (auf ihre Logik) updaten
            DigitSimController.getElements().get(d.indexFirstElement).update();
            DigitSimController.getElements().get(d.indexSecondElement).update();
        }
    }
    

     /**
     * @author tim
     * sehen ob nah an einem input geklickt wurde
     * (erstes von 2)
     */  
    
    public static final int EINDEX = 0; // element index
    public static final int CINDEX = 1; // connection index
    public static final int CETYPE = 2; // input(1) oder output(0)
    
    public int[] closeToInOrOut(MouseEvent event)
    {
        int result[] = new int[3];
        for(int n = 0; n <  DigitSimController.getElements().size(); n++)
        {
            // schauen ob in der nähe eines inputs geklickt wurde
            for(int i = 0; i < getElements().get(n).numInputs; i++) {
                double dInX = getElements().get(n).getInputX(i);
                double dInY = getElements().get(n).getInputY(i);
                if(Draw.isInArea(event.getX(), event.getY(), dInX, dInY, 10))
                {
                   result[EINDEX] = n; // element index
                   result[CINDEX] = i; // input index
                   result[CETYPE] = 1; // es ist ein input
                   return result;
                }
            } 
            
            // schauen ob in der nähe eines outputs geklickt wurde
            for(int i = 0; i < getElements().get(n).numOutputs; i++) {
                double dInX = getElements().get(n).getOutputX(i);
                double dInY = getElements().get(n).getOutputY(i);
                if(Draw.isInArea(event.getX(), event.getY(), dInX, dInY, 10))
                {
                   result[EINDEX] = n; // element index
                   result[CINDEX] = i; // output index
                   result[CETYPE] = 0; // es ist ein output
                   return result;
                }
            } 
        }
        return null;
    }
    
    //Author: Dominik
    //Löscht alle Verbindungen
    public void clear(){
        connections.clear();
    }
    
    // Author Tim
    // bestimmte verbindung entfernen
    public void removeConnection(ConData data)
    {
        connections.remove(data);
    }
    
    public ArrayList<ConData> getConnectionData(){ //Verbindungen zurückgeben
        return connections;
    }
    
    public ConData getSpecificConnectionData(int choice){ //Einzelne Verbindungen zurückgeben
        return connections.get(choice);
    }
}
