/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package element;

import Gestures.NodeGestures;
import connection.HandleState;
import element.Element;
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
public class Element_SRFF extends Element {
     //Globals
    public static final ElementType.Type TYPE = ElementType.Type.JKFF; //Der Typ des Bausteines
   //Die Elemente aus denen der Baustein zusammengestezt ist
    private final Label lblX;
    private final Label lblY;
    private final Label lblCI;
    private final Label lblCO;
    private final Label lblS;
    private final Label lblVA;
    private State lastClockState;
    private final Element thisElement = this; //Referenz auf sich selbst

    //Constructor
    public Element_SRFF(double pX, double pY, NodeGestures dNodeGestures){ //Baustein zeichnen
        outputs = new int[2]; //Outputs

        double gridOffset = (double) Properties.GetGridOffset();
        
        pX = pX-elementWidth/2;
        pY = pY-elementHeight/2;
        numOutputs = 2;
        rec = Draw.drawRectangle(pX, pY, elementWidth, elementHeight, 10, 10, Color.BLACK, Properties.getElementOpacity(), 5);           //das AND zeichnen
        rec.addEventFilter(MouseEvent.MOUSE_ENTERED, NodeGestures.getOverNodeMouseHanlderEnterRec(this));
        rec.addEventFilter(MouseEvent.MOUSE_EXITED, NodeGestures.getOverNodeMouseHanlderExitRec(this));
        lblVA = Draw.drawLabel((pX + 25), (pY - 2), "SR", Color.BLACK, false, 20);
        lblX = Draw.drawLabel((pX + 10), pY + 15.5, "S", Color.BLACK, false, 18);
        lblY = Draw.drawLabel((pX + 10), pY + 38, "C", Color.BLACK, false, 18);
        lblCI = Draw.drawLabel((pX + 10), pY +39 + 16.5, "R", Color.BLACK, false, 18);
        lblS = Draw.drawLabel((pX + 60), pY + 17.5, "Q", Color.BLACK, false, 18);
        lblCO = Draw.drawLabel((pX + 55), pY + 42.5, "!Q", Color.BLACK, false, 18);
        outputs[0] = 3;
        outputs[1] = 3;
        
            numInputs = 3;
            inputs = new int[numInputs];
            Arrays.fill(inputs, 0); //Setzt alle Inputs auf '0'
            grp = new Group(lblVA, lblX, lblY, lblCO, lblS, lblCI, rec);
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
                
                
                if ( i < numOutputs ){
                  outputLines.add(Draw.drawLine((pX + 85), pY + offsetY * 1.1 + 20, (pX + 90), pY + offsetY * 1.1 + 20, Color.BLACK, 5));
                  outputLines.get(i).addEventFilter(MouseEvent.MOUSE_ENTERED, NodeGestures.getOverNodeMouseHanlderEnter());
                  outputLines.get(i).addEventFilter(MouseEvent.MOUSE_EXITED, NodeGestures.getOverNodeMouseHanlderExit());
                  outputLines.get(i).addEventFilter(MouseEvent.MOUSE_CLICKED, NodeGestures.getOverOutputMouseHanlderClicked(this, i));  
                  grp.getChildren().add(outputLines.get(i)); //Linie hinzufügen
                }
             
                
                inputLines.add(Draw.drawLine((pX - 5), pY + offsetY + 20, (pX - 10), pY + offsetY + 20, Color.BLACK, 5)); //Linie zeichnen
                inputLines.get(i).addEventFilter(MouseEvent.MOUSE_ENTERED, NodeGestures.getOverNodeMouseHanlderEnter());
                inputLines.get(i).addEventFilter(MouseEvent.MOUSE_EXITED, NodeGestures.getOverNodeMouseHanlderExit());
                inputLines.get(i).addEventFilter(MouseEvent.MOUSE_CLICKED, NodeGestures.getOverInputMouseHanlderClicked(this, i));
                grp.getChildren().add(inputLines.get(i)); //Linie hinzufügen
            }
            
          //Die Hanlder hinzufügenn (Beschreibung der Hander in  DraggableCanvas.java)
        grp.addEventFilter( MouseEvent.MOUSE_PRESSED, dNodeGestures.getOnMousePressedEventHandler());
        grp.addEventFilter( MouseEvent.MOUSE_DRAGGED, dNodeGestures.getOnMouseDraggedEventHandler());
        grp.addEventFilter( MouseEvent.MOUSE_RELEASED, dNodeGestures.getOnMouseReleasedEventHandler());
    }
    
   //Diese Methoden müssen überschrieben werden (Beschreibung in der Mutterklasse)
    @Override
    public void update(){ 
        State currentClockState = HandleState.getState(inputs[1]);
        System.out.println("LastState: " +lastClockState);
        System.out.println("Current: " +currentClockState);
        if(currentClockState == lastClockState || currentClockState != State.HIGH) {
//            if((outputs[0] == 3 ||outputs[1] == 3) && (inputs[0] != 3 && inputs[1] != 3 && inputs[2] != 3)) {
//                outputs[0] = 0;
//                outputs[1] = 0;
//            }
            lastClockState = currentClockState;
            return;
        }
        if(inputs[0] == 1 && inputs[2] == 0) { //Set
            outputs[0] = 1;
            outputs[1] = HandleState.getIntFromState(HandleState.logicNOT(HandleState.getState(outputs[0])));
            lastClockState = currentClockState;
        } else if(inputs[0] == 0 && inputs[2] == 1) { //ReSet
            outputs[0] = 0;
            outputs[1] = HandleState.getIntFromState(HandleState.logicNOT(HandleState.getState(outputs[0])));
            lastClockState = currentClockState;
            return;
        }
        
//        if((outputs[0] == 3 ||outputs[1] == 3) && (inputs[0] != 3 && inputs[1] != 3 && inputs[2] != 3)) {
//                outputs[0] = 0;
//                outputs[1] = 0;
//        }
        
        lastClockState = currentClockState;
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
