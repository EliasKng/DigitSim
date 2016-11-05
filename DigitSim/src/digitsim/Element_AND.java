/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;

import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Dominik
 */
public class Element_AND extends Element{

    public static final String TYPE = "AND"; //Der Typ des Bausteines
    private Rectangle rec;  //Die Elemente aus denen der Baustein zusammengestezt ist
    private Label lbl;
    private ArrayList<Line> lines = new ArrayList<>();
    private Line l0;
    
    //Constructor
    public Element_AND(double pX, double pY, int pInputs, NodeGestures dNodeGestures){
        outputs = new int[]{0}; //Outputs
        numOutputs = 1;
        rec = Draw.drawRectangle(pX, pY, 75, 75, 25, 25, Color.BLACK, 0.0, 5);           //das AND zeichnen
        lbl = Draw.drawLabel((pX + 12), (pY - 15), "&", Color.BLACK, false, 70);
        l0 = Draw.drawLine((pX + 80), (pY + 37), (pX + 100), (pY + 37), Color.BLACK, 5);

        
            numInputs = pInputs;
            inputs = new int[]{0, 0, 0, 0};
            grp = new Group(rec, lbl, l0);
            for(int i = 0; i < numInputs; i++)
            {
                //  * Überarbeitet von Tim 05.11.16
                // korrekte stelle für jeden eingang berechnen, egal wie viele eingänge
                double offsetY = rec.getHeight() / (numInputs + 1) + (rec.getHeight() / (numInputs + 1)) * i;
                
                lines.add(Draw.drawLine((pX - 5), pY + offsetY, (pX - 15), pY + offsetY, Color.BLACK, 5));
                grp.getChildren().add(lines.get(i));
            }
            
          
        grp.addEventFilter( MouseEvent.MOUSE_PRESSED, dNodeGestures.getOnMousePressedEventHandler());
        grp.addEventFilter( MouseEvent.MOUSE_DRAGGED, dNodeGestures.getOnMouseDraggedEventHandler());
    }
    
    //Diese Methoden müssen überschrieben werden (Beshcreibung in der Mutterklasse)
    @Override
    public double getX() {
       return rec.getX();
    }

    @Override
    public double getY() {
        return rec.getY();
    }
    
    @Override
    public void setInput(int pInput, int pValue) {
        if(pInput >= 0 && pInput < numInputs){
            inputs[pInput] = pValue;
            if(pValue == 1){
                lines.get(pInput).setStroke(Color.RED);
            }else{
                lines.get(pInput).setStroke(Color.BLACK);
            }
        }
    }

    @Override
    public int getOutput(int pOut) {
        update();
        if(pOut == 1){
            if(numOutputs == 4){
                return (inputs[0] == 1 && inputs[1] == 1 && inputs[2] == 1 && inputs[3] == 1) ? 1 : 0;  
            }else{
                return (inputs[0] == 1 && inputs[1] == 1) ? 1 : 0; 
            }
        }
        return 0;
    }

    @Override
    public int getInputCount() {
        return numInputs;
    }
    
    @Override
    public int getOutputCount() {
        return numOutputs;
    }

    @Override
    public double getInputX(int pInput) {
        if(pInput < numInputs && pInput >= 0){
            return lines.get(pInput).getEndX();
        }
        return 0;
    }

    @Override
    public double getInputY(int pInput) {
        if(pInput < numInputs && pInput >= 0){
            return lines.get(pInput).getEndY();
        }
        return 0;
    }

    @Override
    public double getOutputX(int pOutput) {
         if(pOutput < numOutputs && pOutput >= 0){
            return l0.getEndX();
        }
        return 0;
    }

    @Override
    public double getOutputY(int pOutput) {
           if(pOutput < numOutputs && pOutput >= 0){
            return l0.getEndY();
        }
        return 0;
    }  
    
    @Override
    public void update(){
        if(numInputs == 4){
            if(inputs[0] == 1 && inputs[1] == 1 && inputs[2] == 1 && inputs[3] == 1){
                l0.setStroke(Color.RED);
            }else{
                l0.setStroke(Color.BLACK);
            }
        }else{
            if(inputs[0] == 1 && inputs[1] == 1){
                l0.setStroke(Color.RED);
            }else{
            l0.setStroke(Color.BLACK);
        }
        }
    }
}
