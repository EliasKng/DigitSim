/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package element;
import digitsim.Draw;
import Gestures.NodeGestures;
import digitsim.Properties;
import element.Element;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.control.Label;
import javax.swing.JOptionPane;

/**
 * @author lukas    25.11.2016
 * -> ist noch abstrakt da noch nicht im controller
 * 
 * hat noch keine funktion
 * outputline ist bissl verschoben
 */
public abstract class Element_SIGNAL extends Element{
//Globals
    public static final String TYPE = "SIGNAL"; //Der Typ des Bausteines
    //Die Elemente aus denen der Baustein zusammengestezt ist
    private Label number;  

    //Konstruktor   
    public Element_SIGNAL(double pX, double pY, int pInputs, NodeGestures dNodeGestures){ //Kein InputSilder, hat kein input
        //signal zeichnen
        
        //Überarbeitet von Elias
        //Der Baustein wird nun (egal bei welcher BausteinWeite/Höhe) plaziert mit der Maus als Mittelpunkt
        pX = pX-elementWidth/2;
        pY = pY-elementHeight/2;
        numOutputs = 1;
        rec = Draw.drawRectangle(pX, pY, elementWidth, elementHeight, 10, 10, Color.BLACK, 0.4, 5);           //das Signal zeichnen
        number = Draw.drawLabel((pX + 20), (pY - 15), "0", Color.BLACK, false, 75);                            //Das label (die  0 oder 1 im block, haben beim erstellen zuerst auf 0) 
        outputLines.add(Draw.drawLine((pX + 85), (pY + 29.5), (pX + 100), (pY + 29.5), Color.BLACK, 5));
 
    numInputs = 0; //Input mit mehr als 1 Eingang ist sinnlos!
            inputs = new int[]{0};
            grp = new Group(rec, outputLines.get(0), number);
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
                double offsetY = i * gridOffset + gridOffset - 11.5;
                
                inputLines.add(Draw.drawLine((pX - 5), pY + offsetY, (pX - 15), pY + offsetY, Color.BLACK, 5));
                grp.getChildren().add(inputLines.get(i));
            }
            
         //Die Hanlder hinzufügenn (Beschreibung der Hander in  DraggableCanvas.java)  
        grp.addEventFilter( MouseEvent.MOUSE_PRESSED, dNodeGestures.getOnMousePressedEventHandler());
        grp.addEventFilter( MouseEvent.MOUSE_DRAGGED, dNodeGestures.getOnMouseDraggedEventHandler());
    }
     //Diese Methoden müssen überschrieben werden (Beschreibung in der Mutterklasse) 
    @Override
    public void update(){                  
    }    
    
     @Override
    public void showProperties(){ //Zeigt das "Eigenschaften"-Fenster für dieses Element
        JOptionPane.showMessageDialog(null,
			    "'SIGNAL' besitzt keine Eigenschaften",
			    "Info",
			    JOptionPane.INFORMATION_MESSAGE);
    }
    
    }

