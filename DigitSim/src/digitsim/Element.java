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
 */
abstract class Element {
    //Globals
    public static final String TYPE = "ELEMENT";
    protected Group grp;
    protected int numOutputs;
    protected int numInputs;
    protected int[] inputs;
    protected int[] outputs;
    
    //Setter/getter
    public Group getGroup(){
        return grp;
    }
    
    abstract public double getX(); //Abstrakte Methoden die überschrieben werden müssen.
    abstract public double getY();
    abstract public double getWidth();
    abstract public double getHeight();
    abstract public void setInput(int pInput, int pValue); //Einen Input auswählen und auf 0 o. 1 setzen
    abstract public int getInputCount();       //Anzahl der Inputs bekommen (z.b 2 bei nem normalen AND)
    abstract public int getOutputCount();     //anzahl der Outputs (Z.b 1 bei AND)
    abstract public double getInputX(int pInput); //Koordinaten des jeweiligen Inputs bekommen
    abstract public double getInputY(int pInput);
    abstract public double getOutputX(int pOutput); //Koordinaten des jeweiligen Outputs bekommen
    abstract public double getOutputY(int pOutput);
    abstract public void update(); //Updatet den Block
    
    public void setTranslateX(double pX){ //Bewegt das Element
        grp.setTranslateX(pX);
    }
    
    public void setTranslateY(double pY){ //Bewegt das Element
        grp.setTranslateY(pY);
    }
    
    public int getOutput(int pOut) {
        update();
        return outputs[pOut];
    }
}
