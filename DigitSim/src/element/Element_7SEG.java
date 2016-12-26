/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package element;

import javafx.scene.shape.*;
import toolbox.Draw;
import toolbox.GenFunctions;
import Gestures.NodeGestures;
import static element.Element.*;
import general.Properties;
import java.util.Arrays;
import java.util.Optional;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Anthony und Niklas
 */
public class Element_7SEG extends Element{

//Globals
    public static final String TYPE = "7 Segment"; //Der Typ des Bausteines 
    
    //Die Elemente aus denen der Baustein zusammengestezt ist
    private Label lbl;
    private Label lbl2;
    private Line lUnderL;
    private Element thisElement = this; //Referenz auf sich selbst

//Konstruktor   
    public Element_7SEG(double pX, double pY, int pInputs, NodeGestures dNodeGestures){
       
        
//7Se  gment zeichnen
        pX = pX-elementWidth/2;  //Anpassung an das Muster
        pY = pY-elementHeight/2;
        numOutputs = 0;          //Keine Outputs
        rec = Draw.drawRectangle(pX, pY, elementWidth, elementHeight, 10, 10, Color.BLACK, Properties.getElementOpacity(), 5);     
        rec.addEventFilter(MouseEvent.MOUSE_ENTERED, NodeGestures.getOverNodeMouseHanlderEnterRec());
        rec.addEventFilter(MouseEvent.MOUSE_EXITED, NodeGestures.getOverNodeMouseHanlderExitRec());
        
       
        
    } 

    public Element_7SEG(double xAdaptGrid, double yAdaptGrid, NodeGestures nodeGestures) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void showProperties() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
