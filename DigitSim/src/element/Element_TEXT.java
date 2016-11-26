/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package element;

import digitsim.Draw;
import Gestures.NodeGestures;
import digitsim.DigitSimController;
import digitsim.GenFunctions;
import element.Element;
import java.util.Optional;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javax.swing.JOptionPane;
/**
 *
 * @author lukas
 * 26.11.2016
 * 
 * erzeut inputfield und string auf canvas
 * werde aber wohl nen standart text nehmen müssen, weil beim verschieben immes node erzeugt wird
 * 
 * TODO: über eigenschaften ändern können
 * TODO: verschiebbar
 */
public class Element_TEXT extends Element{
   
//Globals
    public static final String TYPE = "TEXT"; //Der Typ des Bausteines
    //Die Elemente aus denen der Baustein zusammengestezt ist
   private Label text;  
   private int fontSize;
   
//Konstruktor   
   public Element_TEXT(double pX, double pY, NodeGestures dNodeGestures){
        TextInputDialog dialog = new TextInputDialog("kys");    //Eingabefesnster 
        dialog.setTitle("Textfeld");                            
        dialog.setHeaderText("Eingabe:");
        Optional<String> result = dialog.showAndWait();         //Fenster wird anezeigt und eingabe gespeichert
        
        fontSize= 30;
               
        text = Draw.drawLabel((pX + 10), (pY - 15), result.get(), Color.BLACK, false, fontSize);
               
        pX = text.getPrefWidth();
        pY = text.getPrefHeight();
        rec = Draw.drawRectangle(pX, pY, elementWidth, elementHeight, 10, 10, Color.BLACK, 0.4, 5);   
        
        grp =  new Group(rec, text );
              
        
        //Die Hanlder hinzufügen (Beschreibung der Hander in  DraggableCanvas.java)  
        grp.addEventFilter( MouseEvent.MOUSE_PRESSED, dNodeGestures.getOnMousePressedEventHandler());
        grp.addEventFilter( MouseEvent.MOUSE_DRAGGED, dNodeGestures.getOnMouseDraggedEventHandler());
    
    }
    @Override
    public void update(){ 
         } 
    
    @Override
    public void showProperties(){ //Zeigt das "Eigenschaften"-Fenster für dieses Element
        JOptionPane.showMessageDialog(null,
			    "'LED' besitzt keine Eigenschaften",
			    "Info",
			    JOptionPane.INFORMATION_MESSAGE);
    }
    }
       
    
    
    
    
    
    

