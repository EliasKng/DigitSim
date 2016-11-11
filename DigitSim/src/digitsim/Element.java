/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;

import javafx.scene.Group;

/**
 *
 * @author Dominik
 * -Überarbeitet von Dome 11.11.2016
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
    protected int[] inputs; //Werte der In und Outputs (0 o. 1)
    protected int[] outputs;
    
    //Setter/getter
    public Group getGroup(){
        return grp;
    }
    
    abstract public double getX(); //Abstrakte Methoden die überschrieben werden müssen.
    abstract public double getY(); //X/Y Koord.
    abstract public double getWidth(); //Breite/Höhe
    abstract public double getHeight();
    abstract public void setInput(int pInput, int pValue); //Einen Input auswählen und auf 0 o. 1 setzen
    abstract public int getInputCount();       //Anzahl der Inputs bekommen (z.b 2 bei nem normalen AND)
    abstract public int getOutputCount();     //anzahl der Outputs (Z.b 1 bei AND)
    abstract public double getInputX(int pInput); //Koordinaten des jeweiligen Inputs bekommen
    abstract public double getInputY(int pInput);
    abstract public double getOutputX(int pOutput); //Koordinaten des jeweiligen Outputs bekommen
    abstract public double getOutputY(int pOutput);
    abstract public void update(); //Updatet den Block (überorüft alle Inputs, durchläuft die Logik und setzt die Outputs dementsprechend (Ändert auch die Farben))
    
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
}
