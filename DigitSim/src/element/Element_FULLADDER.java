/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package element;

import Gestures.NodeGestures;
import connection.HandleState;
import element.Element;
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
public class Element_FULLADDER extends Element {
     //Globals
    public static final ElementType.Type TYPE = ElementType.Type.DTFF; //Der Typ des Bausteines
   //Die Elemente aus denen der Baustein zusammengestezt ist
    private final Label lblX;
    private final Label lblY;
    private final Label lblCI;
    private final Label lblCO;
    private final Label lblS;
    private final Label lblVA;
    private final Element thisElement = this; //Referenz auf sich selbst

    //Constructor
    public Element_FULLADDER(double pX, double pY, NodeGestures dNodeGestures){ //Baustein zeichnen
        outputs = new int[2]; //Outputs

        double gridOffset = (double) Properties.GetGridOffset();
        
        pX = pX-elementWidth/2;
        pY = pY-elementHeight/2;
        numOutputs = 2;
        rec = Draw.drawRectangle(pX, pY, elementWidth, elementHeight, 10, 10, Color.BLACK, Properties.getElementOpacity(), 5);           //das AND zeichnen
        rec.addEventFilter(MouseEvent.MOUSE_ENTERED, NodeGestures.getOverNodeMouseHanlderEnterRec(this));
        rec.addEventFilter(MouseEvent.MOUSE_EXITED, NodeGestures.getOverNodeMouseHanlderExitRec(this));
        lblVA = Draw.drawLabel((pX + 15), (pY - 2), "VA", Color.BLACK, false, 20);
        lblX = Draw.drawLabel((pX + 10), pY + 17.5, "X", Color.BLACK, false, 18);
        lblY = Draw.drawLabel((pX + 10), pY + 40, "Y", Color.BLACK, false, 18);
        lblCI = Draw.drawLabel((pX + 10), pY + 41 + 16.5, "CI", Color.BLACK, false, 18);
        lblS = Draw.drawLabel((pX + 60), pY + 17.5, "S", Color.BLACK, false, 18);
        lblCO = Draw.drawLabel((pX + 55), pY + 42.5, "CO", Color.BLACK, false, 18);
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
        State s0 = HandleState.getState(inputs[0]);
        State s1 = HandleState.getState(inputs[1]);
        State result = HandleState.logicFULLADDER(s0, s1);
        
        int resultStateInt = HandleState.getIntFromState(result);
        
        if(resultStateInt > 1) {
            outputs[0] = HandleState.getIntFromState(result);
            outputs[1] = HandleState.getIntFromState(result);
            return;
        }
        
        // Carry Out
        outputs[1] = (inputs[0] == 0 && inputs[1] == 1 && inputs[2] == 1) || 
                (inputs[0] == 1 && inputs[1] == 0 && inputs[2] == 1) || 
                (inputs[0] == 1 && inputs[1] == 1 && inputs[2] == 0) || 
                (inputs[0] == 1 && inputs[1] == 1 && inputs[2] == 1) 
                ? 1 : 0;
        
        // Sum out
        outputs[0] = (inputs[0] == 0 && inputs[1] == 0 && inputs[2] == 1) || 
                (inputs[0] == 0 && inputs[1] == 1 && inputs[2] == 0) || 
                (inputs[0] == 1 && inputs[1] == 0 && inputs[2] == 0) || 
                (inputs[0] == 1 && inputs[1] == 1 && inputs[2] == 1) 
                ? 1 : 0;
        
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
