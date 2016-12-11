/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package element;
import digitsim.Draw;
import Gestures.NodeGestures;
import digitsim.GenFunctions;
import digitsim.Properties;
import element.Element;
import static element.Element.elementWidth;
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
public class Element_SIGNAL extends Element{
//Globals
    public static final String TYPE = "SIGNAL"; //Der Typ des Bausteines
    //Die Elemente aus denen der Baustein zusammengestezt ist
    private Label number1;  
    private Label number2;  //Wir brauchen 2 Labels, da es ansonsten nicht Thread sicher ist

    //Konstruktor   
    public Element_SIGNAL(double pX, double pY, NodeGestures dNodeGestures){ //Kein InputSilder, hat kein input
        //signal zeichnen
        
        //Überarbeitet von Elias
        //Der Baustein wird nun (egal bei welcher BausteinWeite/Höhe) plaziert mit der Maus als Mittelpunkt
        pX = pX-elementWidth/2;
        pY = pY-elementHeight/2;
        numOutputs = 1;
        outputs = new int[]{0};
        rec = Draw.drawRectangle(pX, pY, elementWidth, elementHeight, 10, 10, Color.BLACK, Properties.getElementOpacity(), 5);           //das Signal zeichnen
        rec.addEventFilter(MouseEvent.MOUSE_ENTERED, GenFunctions.getOverNodeMouseHanlderEnterRec());
        rec.addEventFilter(MouseEvent.MOUSE_EXITED, GenFunctions.getOverNodeMouseHanlderExitRec());
        number1 = Draw.drawLabel((pX + 20), (pY - 15), "0", Color.BLACK, false, 75);                            //Das label (die  0 oder 1 im block, haben beim erstellen zuerst auf 0) 
        number2 = Draw.drawLabel((pX + 20), (pY - 15), "1", Color.RED, false, 75); 
        number2.setVisible(false);
        outputLines.add(Draw.drawLine((pX + 85), (pY + 29.5), (pX + 100), (pY + 29.5), Color.BLACK, 5));
        outputLines.get(0).addEventFilter(MouseEvent.MOUSE_ENTERED, GenFunctions.getOverNodeMouseHanlderEnter());
        outputLines.get(0).addEventFilter(MouseEvent.MOUSE_EXITED, GenFunctions.getOverNodeMouseHanlderExit());
        numInputs = 1; 
        inputs = new int[]{0};
        grp = new Group(outputLines.get(0), number1, number2, rec);
        inputLines.add(Draw.drawLine((pX - 5), pY, (pX - 15), pY, Color.BLACK, 5)); //Wir können den Input trotzdem benutzen um den Baustein auf 0,1 zu setzen
        inputLines.get(0).setVisible(false);
        grp.getChildren().add(inputLines.get(0));
            
         //Die Hanlder hinzufügenn (Beschreibung der Hander in  DraggableCanvas.java)  
        grp.addEventFilter( MouseEvent.MOUSE_PRESSED, dNodeGestures.getOnMousePressedEventHandler());
        grp.addEventFilter( MouseEvent.MOUSE_DRAGGED, dNodeGestures.getOnMouseDraggedEventHandler());
    }
     //Diese Methoden müssen überschrieben werden (Beschreibung in der Mutterklasse) 
    @Override
    public void update(){  
        if(inputs[0] == 1){
            outputs[0] = 1;
            outputLines.get(0).setStroke(Color.RED);
            number1.setVisible(false);
            number2.setVisible(true);
        }else{
            outputs[0] = 0;
            outputLines.get(0).setStroke(Color.BLACK);
            number1.setVisible(true);
            number2.setVisible(false);
        }
    }    
    
    @Override
    public void showProperties(){ //Zeigt das "Eigenschaften"-Fenster für dieses Element
        JOptionPane.showMessageDialog(null,
			    "'SIGNAL' besitzt keine Eigenschaften",
			    "Info",
			    JOptionPane.INFORMATION_MESSAGE);
    }
    
    }

