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
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javax.swing.JOptionPane;

/**
 *
 * @author Dominik
 * -Überarbeitet von Dome 06.11.2016
 * -Überarbeitet von Dome 11.11.2016
 */
public class Element_NOT extends Element{

    //Globals
    public static final String TYPE = "NOT"; //Der Typ des Bausteines
    private Rectangle rec;  //Die Elemente aus denen der Baustein zusammengestezt ist
    private Label lbl;
    private ArrayList<Line> lines = new ArrayList<>();
    private Line l0;
    private Circle cOutput;
    
    //Constructor
    public Element_NOT(double pX, double pY, int pInputs, NodeGestures dNodeGestures){//Baustein zeichnen
        outputs = new int[]{0}; //Outputs
        
        //Überarbeitet von Elias
        //Der Baustein wird nun (egal bei welcher BausteinWeite/Höhe) plaziert mit der Maus als Mittelpunkt
        pX = pX-elementWidth/2;
        pY = pY-elementHeight/2;
        numOutputs = 1;
        rec = Draw.drawRectangle(pX, pY, elementWidth, elementHeight, 10, 10, Color.BLACK, 0.4, 5);           //das AND zeichnen
        lbl = Draw.drawLabel((pX + 20), (pY - 15), "!", Color.BLACK, false, 75);                            //Das Ausrufezeichen Brauchwn wir nicht, da wir ja einen Kreis hinter das Bauteil setzten (damit es wie ein NOT aussieht)
        l0 = Draw.drawLine((pX + 95), (pY + 40), (pX + 100), (pY + 40), Color.BLACK, 5);
        cOutput = Draw.drawCircle(pX+88, pY+40, 5, Color.BLACK, 5, false, 5);

        
            numInputs = 1; //Input mit mehr als 1 Eingang ist sinnlos!
            inputs = new int[]{0};
            grp = new Group(rec, l0, lbl, cOutput);
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
            
         //Die Hanlder hinzufügenn (Beschreibung der Hander in  DraggableCanvas.java)  
        grp.addEventFilter( MouseEvent.MOUSE_PRESSED, dNodeGestures.getOnMousePressedEventHandler());
        grp.addEventFilter( MouseEvent.MOUSE_DRAGGED, dNodeGestures.getOnMouseDraggedEventHandler());
    }
    
    //Diese Methoden müssen überschrieben werden (Beschreibung in der Mutterklasse)
    @Override
    public double getX() {
       return rec.getX() + grp.getTranslateX();
    }

    @Override
    public double getY() {
        return rec.getY() + grp.getTranslateY();
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
        if(inputs[0] == 0){
            outputs[0] = 1;
            lines.get(0).setStroke(Color.RED);
        }else{
            outputs[0] = 0;
            lines.get(0).setStroke(Color.BLACK);
        }
    }
    
     @Override
    public void showProperties(){ //Zeigt das "Eigenschaften"-Fenster für dieses Element
        JOptionPane.showMessageDialog(null,
			    "'NOT' besitzt keine Eigenschaften",
			    "Info",
			    JOptionPane.INFORMATION_MESSAGE);
    }
}
