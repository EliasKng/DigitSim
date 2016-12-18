/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package element;
import toolbox.Draw;
import Gestures.NodeGestures;
import digitsim.DigitSimController;
import toolbox.GenFunctions;
import general.Properties;
import element.Element;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javax.swing.JOptionPane;
/**
 *
 * @author lukas 24.11.2016
 * Überarbeitet am 25.11.2016 ist jetzt kreis 
 *
 *TODO: Wenn Zustanlos farbe festlegen
 * TODO: evt. kleinere LEDs ?
 */

public class Element_LED extends Element{
   
//Globals
    public static final String TYPE = "LED"; //Der Typ des Bausteines
    //Die Elemente aus denen der Baustein zusammengestezt ist
    private Circle indicator;  
    
//Konstruktor   
    public Element_LED(double pX, double pY, int pInputs, NodeGestures dNodeGestures){ //Kein InputSilder, da nur ein Input

//LED zeichnen
         
    //Überarbeitet von Elias
        //Der Baustein wird nun (egal bei welcher BausteinWeite/Höhe) plaziert mit der Maus als Mittelpunkt
        pX = pX-elementWidth/2;
        pY = pY-elementHeight/2 + 11.5;
        numOutputs = 0; //Kein Ouptut benötigt!
        rec = Draw.drawRectangle(pX, pY, elementWidth, elementHeight, 100, 100, Color.BLACK, Properties.getElementOpacity(), 5);           //die led zeichnen
        rec.addEventFilter(MouseEvent.MOUSE_ENTERED, NodeGestures.getOverNodeMouseHanlderEnterRec());
        rec.addEventFilter(MouseEvent.MOUSE_EXITED, NodeGestures.getOverNodeMouseHanlderExitRec());
        outputLines.add(Draw.drawLine((pX + 95), (pY + 29.5), (pX + 100), (pY + 29.5), Color.BLACK, 5));
        outputLines.get(0).setVisible(false);  //Outputline ist unsichtbaar, muss wegen vererbung aber vorhanden sein!
        indicator = Draw.drawCircle(pX + 40, pY  + 40, 5, Color.BLUE, 5, true, 65);     //TODO: wenn zustandlos hier annfangsfarbe ändern
        indicator.setVisible(false);
        numInputs = 1; //Input mit mehr als 1 Eingang ist sinnlos!
        inputs = new int[]{0};
        inputLines.add(Draw.drawLine((pX - 5), (Math.round((pY + (elementHeight/ 2)) / Properties.GetGridOffset()) * Properties.GetGridOffset()) - 10.5, (pX - 10), (Math.round((pY + (elementHeight/2)) / Properties.GetGridOffset()) * Properties.GetGridOffset()) - 10.5, Color.BLACK, 5));
        inputLines.get(0).addEventFilter(MouseEvent.MOUSE_ENTERED, NodeGestures.getOverNodeMouseHanlderEnter());
        inputLines.get(0).addEventFilter(MouseEvent.MOUSE_EXITED, NodeGestures.getOverNodeMouseHanlderExit());
        inputLines.get(0).addEventFilter(MouseEvent.MOUSE_CLICKED, NodeGestures.getOverInputMouseHanlderClicked(this, 0));
        grp =  new Group(indicator, outputLines.get(0), rec, inputLines.get(0));
        //Die Hanlder hinzufügen (Beschreibung der Hander in  DraggableCanvas.java)  
        grp.addEventFilter( MouseEvent.MOUSE_PRESSED, dNodeGestures.getOnMousePressedEventHandler());
        grp.addEventFilter( MouseEvent.MOUSE_DRAGGED, dNodeGestures.getOnMouseDraggedEventHandler());
        grp.addEventFilter( MouseEvent.MOUSE_RELEASED, dNodeGestures.getOnMouseReleasedEventHandler());
    }
	
     //Diese Methoden müssen überschrieben werden (Beschreibung in der Mutterklasse) 
    @Override
    public void update(){
        if(DigitSimController.isLocked()){
             indicator.setVisible(true);
             if(inputs[0] == 1){
                indicator.setStroke(Color.RED);
             }else if (inputs[0] == 0){
                indicator.setStroke(Color.BLUE);
              }else{
              indicator.setStroke(Color.GREEN); //TODO: wenn es ZUstandlos gibt hier Farbe
             }
        }else{
            indicator.setVisible(false);
        }
        }
       
      @Override
    public void showProperties(){ //Zeigt das "Eigenschaften"-Fenster für dieses Element
        JOptionPane.showMessageDialog(null,
			    "'LED' besitzt keine Eigenschaften",
			    "Info",
			    JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    public void reset(){ //Simple reset methode (kann natürlich überschireben werden, für spezielle Bausteine)
        for(int i = 0; i < getInputCount(); i++){ //Alle Eingänge auf 0 setzen
                setInput(i, 0);
            }
        update();
        indicator.setVisible(false);
    }
    }   
  
