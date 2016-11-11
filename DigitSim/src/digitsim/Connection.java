/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;

import java.util.ArrayList;
import javafx.scene.shape.Line;

/**
 *
 * @author Tim
 */
public class Connection { //Speichert die Verbindungen
    
    public class Data //Daten einer Verbinden (Zwischen zwei In oder Outputs
    {
        public int indexFirstElement;
        public boolean typeFirst;
        public int indexFirst;
        public int indexSecondElement;
        public boolean typeSecond;
        public int indexSecond;      
        public Line connectionLine;
    }
    
    public static ArrayList<Data> connections = new ArrayList<Data>(); //Speichert alle Verbindungne


    
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
        Data data = new Data();
        data.indexFirstElement = indexFirstElement;
        data.indexSecondElement = indexSecondElement;
        data.indexFirst = indexFirst;
        data.indexSecond = indexSecond;
        data.typeFirst = typeFirst;
        data.typeSecond = typeSecond;
        
        connections.add(data);    
    }
    
    void update() //Geht alle Verbindungen durch und schaltet sie durch
    {
        for(Data d : connections)
        {

           // public static Line drawLine(double p1X, double p1Y, double p2X, double p2Y, Color dColor,double dWidth){
            double lineX1;
            double lineY1;
            double lineX2;
            double lineY2;
         
           if((d.typeFirst == d.typeSecond) && !d.typeFirst) // ausgang mit ausgang verbunden
           {
           /* lineX1 = DigitSimController.elements.get(d.indexFirstElement).getOutputX(d.indexFirst);
            lineY1 = DigitSimController.elements.get(d.indexFirstElement).getOutputY(d.indexFirst);
            lineX2 = DigitSimController.elements.get(d.indexSecondElement).getOutputX(d.indexFirst);
            lineY2 = DigitSimController.elements.get(d.indexSecondElement).getOutputX(d.indexFirst);
             d.connectionLine = Draw.drawLine(lineX1, lineY1, lineX2, lineY2, Color.BLACK, 2);*/
           }
            if((d.typeFirst != d.typeSecond) && !d.typeFirst) // ausgang mit eingang verbunden
            {
               /* lineX1 = DigitSimController.elements.get(d.indexFirstElement).getOutputX(d.indexFirst);
                lineY1 = DigitSimController.elements.get(d.indexFirstElement).getOutputY(d.indexFirst);
                lineX2 = DigitSimController.elements.get(d.indexSecondElement).getInputX(d.indexFirst);
                lineY2 = DigitSimController.elements.get(d.indexSecondElement).getInputY(d.indexFirst);
                d.connectionLine = Draw.drawLine(lineX1, lineY1, lineX2, lineY2, Color.BLACK, 2);*/
                DigitSimController.getElements().get(d.indexSecondElement).setInput(d.indexSecond, DigitSimController.getElements().get(d.indexFirstElement).getOutput(d.indexFirst));
            }
            if((d.typeFirst != d.typeSecond) && d.typeFirst) // eingang mit ausgang verbunden
            {
             /*   lineX1 = DigitSimController.elements.get(d.indexFirstElement).getInputX(d.indexFirst);
                lineY1 = DigitSimController.elements.get(d.indexFirstElement).getInputY(d.indexFirst);
                lineX2 = DigitSimController.elements.get(d.indexSecondElement).getOutputX(d.indexFirst);
                lineY2 = DigitSimController.elements.get(d.indexSecondElement).getOutputY(d.indexFirst);
                d.connectionLine = Draw.drawLine(lineX1, lineY1, lineX2, lineY2, Color.BLACK, 2);*/
                DigitSimController.getElements().get(d.indexFirstElement).setInput(d.indexFirst, DigitSimController.getElements().get(d.indexSecondElement).getOutput(d.indexSecond));
            }
            if((d.typeFirst == d.typeSecond) && d.typeFirst) // eingang mit eingang verbunden
            {
              /*  lineX1 = DigitSimController.elements.get(d.indexFirstElement).getInputX(d.indexFirst);
                lineY1 = DigitSimController.elements.get(d.indexFirstElement).getInputY(d.indexFirst);
                lineX2 = DigitSimController.elements.get(d.indexSecondElement).getInputX(d.indexFirst);
                lineY2 = DigitSimController.elements.get(d.indexSecondElement).getInputY(d.indexFirst);
                d.connectionLine = Draw.drawLine(lineX1, lineY1, lineX2, lineY2, Color.BLACK, 2);*/
                
                int newVal = 0;
                if(DigitSimController.getElements().get(d.indexFirstElement).inputs[d.indexFirst] > 0 || DigitSimController.getElements().get(d.indexSecondElement).inputs[d.indexSecond] > 0)
                {
                    newVal = 1;
                }
                
                DigitSimController.getElements().get(d.indexFirstElement).setInput(d.indexFirst, newVal);
                DigitSimController.getElements().get(d.indexSecondElement).setInput(d.indexSecond, newVal);
            }
            //Die Beusteine selbst (auf ihre Logik) updaten
            DigitSimController.getElements().get(d.indexFirstElement).update();
            DigitSimController.getElements().get(d.indexSecondElement).update();
        }
    }
}
