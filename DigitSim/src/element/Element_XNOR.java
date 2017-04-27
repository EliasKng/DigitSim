/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package element;

import toolbox.Draw;
import toolbox.GenFunctions;
import Gestures.NodeGestures;
import connection.HandleState;
import general.Properties;
import element.Element;
import static element.Element.elementWidth;
import general.State;
import java.util.Arrays;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 *  ----> ACHTUNG: Um dieses Element zu verstehen sollte zuerst "Element.java" (Die Mutterklasse) studiert werden!
 * 
 * @author Luca, Camillo (17.11.2016)


 */
public class Element_XNOR extends Element{

    //Globals
    public static final Type TYPE = Type.XNOR;; //Der Typ des Bausteines
   //Die Elemente aus denen der Baustein zusammengestezt ist
    private Label lbl;
    private Element thisElement = this; //Referenz auf sich selbst
    
    //Constructor
    public Element_XNOR(double pX, double pY, int pInputs, NodeGestures dNodeGestures){ //Baustein zeichnen
        outputs = new int[]{0}; //Outputs

       
        //Der Baustein wird nun (egal bei welcher BausteinWeite/Höhe) plaziert mit der Maus als Mittelpunkt
        pX = pX-elementWidth/2;
        pY = pY-elementHeight/2;
        numOutputs = 1;
        rec = Draw.drawRectangle(pX, pY, elementWidth, elementHeight, 10, 10, Color.BLACK, Properties.getElementOpacity(), 5);           //das XNOR zeichnen
        rec.addEventFilter(MouseEvent.MOUSE_ENTERED, NodeGestures.getOverNodeMouseHanlderEnterRec(this));
        rec.addEventFilter(MouseEvent.MOUSE_EXITED, NodeGestures.getOverNodeMouseHanlderExitRec(this));
        lbl = Draw.drawLabel((pX + 15), (pY - 20), "=", Color.BLACK, false, 75);
        outputLines.add(Draw.drawLine((pX + 85), (pY + 29.5), (pX + 90), (pY + 29.5), Color.BLACK, 5));
        outputLines.get(0).addEventFilter(MouseEvent.MOUSE_ENTERED, NodeGestures.getOverNodeMouseHanlderEnter());
        outputLines.get(0).addEventFilter(MouseEvent.MOUSE_EXITED, NodeGestures.getOverNodeMouseHanlderExit());
        outputLines.get(0).addEventFilter(MouseEvent.MOUSE_CLICKED, NodeGestures.getOverOutputMouseHanlderClicked(this, 0));

        
            numInputs = pInputs;
            inputs = new int[numInputs];
            Arrays.fill(inputs, 0); //Setzt alle Inputs auf '0'
            grp = new Group(lbl, outputLines.get(0), rec);
            for(int i = 0; i < numInputs; i++)
            {
            
                //  * Überarbeitet von Tim 05.11.16
                //  * Überarbeitet von Tim 21.11.16
                // korrekte stelle für jeden eingang berechnen, egal wie viele eingänge
                // *Überarbeitet von Elias 11.11.16
                // Bausteine passen sich nun automatisch mit ihrer Höhe an die anzahl der Eingänge an
                
                double gridOffset = (double) Properties.GetGridOffset();
                
                if(rec.getHeight() <= (numInputs) * gridOffset) {
                    rec.setHeight((numInputs) * gridOffset);
                }
                double offsetY = i * gridOffset + gridOffset - 12.5;
                
                inputLines.add(Draw.drawLine((pX - 5), pY + offsetY, (pX - 10), pY + offsetY, Color.BLACK, 5)); //Linie zeichnen
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
        State result = HandleState.logicXNOR(s0, s1);
        outputs[0] = HandleState.getIntFromState(result);
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
