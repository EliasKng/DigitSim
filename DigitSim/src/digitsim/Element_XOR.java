/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;

import java.awt.Font;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Elias (Nach Dominiks AND - Vorlage)
 * -Überarbeitet von Dome 11.11.2016
 */
public class Element_XOR extends Element{

    //Globals
    public static final String TYPE = "OR"; //Der Typ des Bausteines
    private Rectangle rec;  //Die Elemente aus denen der Baustein zusammengestezt ist
    private Label lbl;
    private Label lbl2;
    private ArrayList<Line> lines = new ArrayList<>();
    private Line l0;
    private static final double elementWidth = 80;
    private static final double elementHeight = 80;
    
    //Constructor
    public Element_XOR(double pX, double pY, int pInputs, NodeGestures dNodeGestures){
        outputs = new int[]{0}; //Outputs
        
        //Der Baustein wird (egal bei welcher BausteinWeite/Höhe) plaziert mit der Maus als Mittelpunkt
        pX = pX-elementWidth/2;
        pY = pY-elementHeight/2;
        numOutputs = 1;
        rec = Draw.drawRectangle(pX, pY, elementWidth, elementHeight, 10, 10, Color.BLACK, 0.4, 5);           //das OR zeichnen
        lbl = Draw.drawLabel((pX+2), (pY - 17), "=" , Color.BLACK, false, 75);
        lbl2 = Draw.drawLabel((pX+40), (pY - 15), "1" , Color.BLACK, false, 75);
        l0 = Draw.drawLine((pX + 80), (pY + 40), (pX + 100), (pY + 40), Color.BLACK, 5); 
        
            numInputs = pInputs;
            inputs = new int[numInputs];
            Arrays.fill(inputs, 0); //Setzt alle Inputs auf '0
            grp = new Group(rec, lbl, l0, lbl2);
            for(int i = 0; i < numInputs; i++)
            {
                //  * Überarbeitet von Tim 05.11.16
                // korrekte stelle für jeden eingang berechnen, egal wie viele eingänge
                // *Überarbeitet von Elias 11.11.16
                // Bausteine passen sich nun automatisch mit ihrer Höhe an die anzahl der Eingänge an
                if(rec.getHeight() <= (numInputs+1)*21) {
                    rec.setHeight((numInputs+1)*21);
                }
                double offsetY = i*21 + 21 - 1;
                
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
    public double getWidth(){
        return elementWidth;
    }
    
    @Override
    public double getHeight(){
        return elementWidth;
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
        boolean allON = true;
        boolean allOFF = true;
        for(int i = 0; i < numInputs; i++){ //Eingänge durchiterieren & Logik überprüfen
            if(inputs[i] == 0){
                allON = false;
            }else{
                allOFF = false;
            }
        }
        if(allON || allOFF){
            outputs[0] = 0;
            l0.setStroke(Color.BLACK);
        }else{
            outputs[0] = 1;
            l0.setStroke(Color.RED);
        }  
    }
}

