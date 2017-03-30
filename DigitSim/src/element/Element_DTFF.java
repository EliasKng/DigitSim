/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package element;

import Gestures.NodeGestures;
import connection.HandleState;
import static element.Element.elementWidth;
import general.Properties;
import general.State;
import java.util.Arrays;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import toolbox.Draw;
import toolbox.GenFunctions;

/**
 *
 * @author Tim
 */
public class Element_DTFF extends Element {
     //Globals
    public static final ElementType.Type TYPE = ElementType.Type.DTFF; //Der Typ des Bausteines
   //Die Elemente aus denen der Baustein zusammengestezt ist
    private final Label lblDTFF;
    private final Label lblD;
    private final Label lblCLK;
    private final Label lblQ;
    private final Label lblNotQ;
    private int lastClockState = 0;
    private int currentClockState = 3;
    private final Element thisElement = this; //Referenz auf sich selbst
    
    private int currentSavedValue = 0;
    //Constructor
    public Element_DTFF(double pX, double pY, NodeGestures dNodeGestures){ //Baustein zeichnen
        outputs = new int[2]; //Outputs

        double gridOffset = (double) Properties.GetGridOffset();
        
        //Überarbeitet von Elias
        //Der Baustein wird nun (egal bei welcher BausteinWeite/Höhe) plaziert mit der Maus als Mittelpunkt
        pX = pX-elementWidth/2;
        pY = pY-elementHeight/2;
        numOutputs = 2;
        rec = Draw.drawRectangle(pX, pY, elementWidth, elementHeight, 10, 10, Color.BLACK, Properties.getElementOpacity(), 5);           //das AND zeichnen
        rec.addEventFilter(MouseEvent.MOUSE_ENTERED, NodeGestures.getOverNodeMouseHanlderEnterRec());
        rec.addEventFilter(MouseEvent.MOUSE_EXITED, NodeGestures.getOverNodeMouseHanlderExitRec());
        lblDTFF = Draw.drawLabel((pX + 15), (pY - 2), "DTFF", Color.BLACK, false, 20);
        lblD = Draw.drawLabel((pX + 10), pY + 17.5, "D", Color.BLACK, false, 18);
        lblCLK = Draw.drawLabel((pX + 10), pY + 42.5, ">", Color.BLACK, false, 18);
        lblQ = Draw.drawLabel((pX + 60), pY + 17.5, "Q", Color.BLACK, false, 18);
        lblNotQ = Draw.drawLabel((pX + 60), pY + 42.5, "!Q", Color.BLACK, false, 18);
        outputs[0] = 3;
        outputs[1] = 3;
        
            numInputs = 2;
            inputs = new int[numInputs];
            Arrays.fill(inputs, 0); //Setzt alle Inputs auf '0'
            grp = new Group(lblDTFF, lblD, lblCLK, lblQ, lblNotQ, rec);
            for(int i = 0; i < numInputs; i++)
            {
                //  * Überarbeitet von Tim 23.03.17
                // Bausteine passen sich nun automatisch mit ihrer Höhe an die anzahl der Eingänge an
                inputs[i] = 3;
                
                if(rec.getHeight() <= (numInputs) * gridOffset) {
                    rec.setHeight((numInputs) * gridOffset);
                }
                
                // angepasst für immer 2 inputs
                double offsetY = i * gridOffset + gridOffset - 12.5;
                
                
  
                outputLines.add(Draw.drawLine((pX + 85), pY + offsetY + 20, (pX + 90), pY + offsetY + 20, Color.BLACK, 5));
                outputLines.get(i).addEventFilter(MouseEvent.MOUSE_ENTERED, NodeGestures.getOverNodeMouseHanlderEnter());
                outputLines.get(i).addEventFilter(MouseEvent.MOUSE_EXITED, NodeGestures.getOverNodeMouseHanlderExit());
                outputLines.get(i).addEventFilter(MouseEvent.MOUSE_CLICKED, NodeGestures.getOverOutputMouseHanlderClicked(this, 0));              
                
                inputLines.add(Draw.drawLine((pX - 5), pY + offsetY + 20, (pX - 10), pY + offsetY + 20, Color.BLACK, 5)); //Linie zeichnen
                inputLines.get(i).addEventFilter(MouseEvent.MOUSE_ENTERED, NodeGestures.getOverNodeMouseHanlderEnter());
                inputLines.get(i).addEventFilter(MouseEvent.MOUSE_EXITED, NodeGestures.getOverNodeMouseHanlderExit());
                inputLines.get(i).addEventFilter(MouseEvent.MOUSE_CLICKED, NodeGestures.getOverInputMouseHanlderClicked(this, i));
                grp.getChildren().add(inputLines.get(i)); //Linie hinzufügen
                grp.getChildren().add(outputLines.get(i)); //Linie hinzufügen
            }
            
          //Die Hanlder hinzufügenn (Beschreibung der Hander in  DraggableCanvas.java)
        grp.addEventFilter( MouseEvent.MOUSE_PRESSED, dNodeGestures.getOnMousePressedEventHandler());
        grp.addEventFilter( MouseEvent.MOUSE_DRAGGED, dNodeGestures.getOnMouseDraggedEventHandler());
        grp.addEventFilter( MouseEvent.MOUSE_RELEASED, dNodeGestures.getOnMouseReleasedEventHandler());
    }
    
    //Diese Methoden müssen überschrieben werden (Beschreibung in der Mutterklasse)
    @Override
    public void update(){ 
        State s0 = HandleState.getState(inputs[0]);
        State s1 = HandleState.getState(inputs[1]);
        State result = HandleState.logicOR(s0, s1);
        int orTrue = HandleState.getIntFromState(result);

        // rausfinden, ob es sich um eine taktflanke handelt oder nicht
        lastClockState = currentClockState;
        currentClockState = inputs[1];
        boolean clockPulseEdge = (lastClockState != currentClockState); 
        
        if(orTrue == 1) {
            if(inputs[1] == 1 && clockPulseEdge) {
                currentSavedValue = inputs[0];
                
                // taktflankensteuerung
                lastClockState = currentClockState;
            }
        }
        if(orTrue > 1) {        
            outputs[0] = 3;
            outputs[1] = 3;
            outputLines.get(0).setStroke(Color.GREY);
            outputLines.get(1).setStroke(Color.GREY); 
            return;
        } 
        
        outputs[0] = currentSavedValue;
        outputs[1] = currentSavedValue == 0 ? 1 : 0;
        outputLines.get(0).setStroke(outputs[0] == 1 ? Color.RED : Color.BLUE);
        outputLines.get(1).setStroke(outputs[1] == 1 ? Color.RED : Color.BLUE);
     
    }
    
    @Override
    public void showProperties(){ //Zeigt das "Eigenschaften"-Fenster für dieses Element
        GenFunctions.showBasicElementProperties(numInputs, thisElement);
    }
    
    @Override
    public String getTypeName() {
        return TYPE.name();
    }
}
