/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;

import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Dominik
 * -Überarbeitet von Dome 11.11.2016
 * -Überarbeitet von Dome 13.11.2016
 * -Überarbeitet von Dome 14.11.2016
 * 
 * Die Klasse ist die Mutterklasse/Mainclass für die Elemente
 * Alle zukünftigen Elemente müssen von dieser Klasse erben und die vorgeschriebenen Methoden implementieren. ALs Beispiel kann z.b das Element_AND genutzt werden!
 */
abstract class Element { //Abstakte Klasse, nur zur Vererbung, es kann kein direktes Object dieser Klasse erstellt werden
    //Globals für ALLE Elemente
    public static final String TYPE = "ELEMENT"; //Benutzen wir als eine Art "ENUM"
    protected Group grp; //Protected da die Erbenden Klassen auch auf diese zugreifen können (geht bei private nicht!)
    protected int numOutputs; //Anzahl In und Outputs
    protected int numInputs;
    protected static double elementWidth = 80; //Standartbreite (Kann geändert werden!)
    protected static double elementHeight = 80; //selbe mit höhe
    protected int[] inputs; //Werte der In und Outputs (0 o. 1)
    protected int[] outputs;
    protected ArrayList<Line> inputLines = new ArrayList<>(); //Die linien der inputs
    protected ArrayList<Line> outputLines = new ArrayList<>(); //Die linien der outputs
    protected Rectangle rec; //Jeder Baustein wird ein Recheck als "Hülle" haben.
    
    //Setter/getter
    public Group getGroup(){
        return grp;
    }
   
    //Abstrakte methoden MÜSSEN überschrieben werden
    abstract public void update(); //Updatet den Block (überorüft alle Inputs, durchläuft die Logik und setzt die Outputs dementsprechend (Ändert auch die Farben))
    abstract public void showProperties(); //Zeigt das "Eigenschaften"-Fenster
    
    //Normale Methoden, diese werden auch vererbt!!!!
    public void setTranslateX(double pX){ //Bewegt das Element
        grp.setTranslateX(pX);
    }
    
    public void setTranslateY(double pY){ //Bewegt das Element
        grp.setTranslateY(pY);
    }
    
    public int getOutput(int pOut) { //Liefert den Wert des Outputs an der Stelle pOut
        update();
        return outputs[pOut];
    }
    
    public double getWidth() { //breite
        return elementWidth;
    }
    
    public double getHeight() { //Hähe
        return elementHeight;
    }
    
    public double getInputX(int pInput) { //Gibt die X-Koordinate eines Inputs zurück (also von der Linie)
        if(pInput < numInputs && pInput >= 0){
            return inputLines.get(pInput).getEndX() + grp.getTranslateX();
        }
        return 0;
    }

    public double getInputY(int pInput) { //Gibt die Y-Koordinate eines Inputs zurück (also von der Linie)
        if(pInput < numInputs && pInput >= 0){
            return inputLines.get(pInput).getEndY() + grp.getTranslateY();
        }
        return 0;
    }
    
    public double getOutputX(int pOutput) { //Gibt die X-Koordinate eines Outputs zurück (also von der Linie)
         if(pOutput < numOutputs && pOutput >= 0){
            return outputLines.get(pOutput).getEndX();
        }
        return 0;
    }

    public double getOutputY(int pOutput) { //Gibt die Y-Koordinate eines Outputs zurück (also von der Linie)
           if(pOutput < numOutputs && pOutput >= 0){
            return outputLines.get(pOutput).getEndY();
        }
        return 0;
    }  
    
    public int getInputCount() { //Anzahl an inputs
        return numInputs;
    }
    
    public int getOutputCount() { //Anzahl an outputs
        return numOutputs;
    }
 
    public double getX() { //X-Koordinate des Elements, standart wert + verschiebung
       return rec.getX() + grp.getTranslateX();
    }

    public double getY() { //Y-Koordinate des Elements, standart wert + verschiebung
        return rec.getY() + grp.getTranslateY();
    }
    
    public void setInput(int pInput, int pValue) { //Einen Input setzen, mit pInput den zu setzenden Input auswählen und pValue der Wert (0 o. 1)
        if(pInput >= 0 && pInput < numInputs){
            inputs[pInput] = pValue;
            if(pValue == 1){
                inputLines.get(pInput).setStroke(Color.RED);
            }else{
                inputLines.get(pInput).setStroke(Color.BLACK);
            }
        }
    }
}
