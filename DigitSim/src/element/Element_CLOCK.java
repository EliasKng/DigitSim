/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package element;

import Gestures.NodeGestures;
import static element.Element.elementWidth;
import general.Properties;
import java.util.Arrays;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javax.swing.JOptionPane;
import toolbox.Draw;
import toolbox.GenFunctions;

/**
 *
 * @author Elias
 */
public class Element_CLOCK extends Element{
//Globals
    public static final ElementType.Type TYPE = ElementType.Type.CLOCK; //Der Typ des Bausteines
    public int hertz = 1;
    public int updateEvery = 10000;
    public int count = 0;
    //Die Elemente aus denen der Baustein zusammengestezt ist

    //Konstruktor   
    public Element_CLOCK(double pX, double pY, NodeGestures dNodeGestures){ //Kein InputSilder, hat kein input
        //signal zeichnen
        
        pX = pX-elementWidth/2;
        pY = pY-elementHeight/2;
        
        numOutputs = 1;
        outputs = new int[]{0};
        rec = Draw.drawRectangle(pX, pY, elementWidth, elementHeight, 10, 10, Color.BLACK, Properties.getElementOpacity(), 5);           //das Signal zeichnen
        rec.addEventFilter(MouseEvent.MOUSE_ENTERED, NodeGestures.getOverNodeMouseHanlderEnterRec());
        rec.addEventFilter(MouseEvent.MOUSE_EXITED, NodeGestures.getOverNodeMouseHanlderExitRec());
        outputLines.add(Draw.drawLine((pX + 85), (pY + 29.5), (pX + 90), (pY + 29.5), Color.BLACK, 5));
        outputLines.get(0).addEventFilter(MouseEvent.MOUSE_ENTERED, NodeGestures.getOverNodeMouseHanlderEnter());
        outputLines.get(0).addEventFilter(MouseEvent.MOUSE_EXITED, NodeGestures.getOverNodeMouseHanlderExit());
        outputLines.get(0).addEventFilter(MouseEvent.MOUSE_CLICKED, NodeGestures.getOverOutputMouseHanlderClicked(this, 0));
        numInputs = 1; 
        inputs = new int[]{0};
        grp = new Group(outputLines.get(0), rec);
        inputLines.add(Draw.drawLine((pX - 5), pY, (pX - 15), pY, Color.BLACK, 5)); //Wir können den Input trotzdem benutzen um den Baustein auf 0,1 zu setzen
        inputLines.get(0).setVisible(false);
        grp.getChildren().add(inputLines.get(0));
            
         //Die Hanlder hinzufügenn (Beschreibung der Hander in  DraggableCanvas.java)  
        grp.addEventFilter( MouseEvent.MOUSE_PRESSED, dNodeGestures.getOnMousePressedEventHandler());
        grp.addEventFilter( MouseEvent.MOUSE_DRAGGED, dNodeGestures.getOnMouseDraggedEventHandler());
        grp.addEventFilter( MouseEvent.MOUSE_RELEASED, dNodeGestures.getOnMouseReleasedEventHandler());
    }
     //Diese Methoden müssen überschrieben werden (Beschreibung in der Mutterklasse) 
    
    @Override
    public void update(){  
        count++;
        if(count >= updateEvery) {
            count = 0;
            if(this.getOutput(0) == 0) {
                this.setOutput(0, 1);
            } else {
                this.setOutput(0, 0);
            }
        }
        
    }    
    
    @Override
    public void showProperties(){ //Zeigt das "Eigenschaften"-Fenster für dieses Element
        JOptionPane.showMessageDialog(null,
			    "'SIGNAL' besitzt keine Eigenschaften",
			    "Info",
			    JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    public String getTypeName() {
        return TYPE.name();
    }
}