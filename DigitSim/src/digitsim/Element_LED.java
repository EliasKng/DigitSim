/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;
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
        pY = pY-elementHeight/2;
        numOutputs = 0; //Kein Ouptut benötigt!
        rec = Draw.drawRectangle(pX, pY, elementWidth, elementHeight, 100, 100, Color.BLACK, 0.4, 5);           //die led zeichnen
       outputLines.add(Draw.drawLine((pX + 95), (pY + 29.5), (pX + 100), (pY + 29.5), Color.BLACK, 5));
       outputLines.get(0).setVisible(false);  //Outputline ist unsichtbaar, muss wegen vererbung aber vorhanden sein!
        indicator = Draw.drawCircle(pX + 40, pY  + 40, 5, Color.BLUE, 5, true, 65);     //TODO: wenn zustandlos hier annfangsfarbe ändern
        numInputs = 1; //Input mit mehr als 1 Eingang ist sinnlos!
            inputs = new int[]{0};
            grp =  new Group(rec, outputLines.get(0), indicator);
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
                // Da rec jetzt kreis ist wird die input line auf der mittlerer höhe der led gezeichnet
                inputLines.add(Draw.drawLine((pX - 5), pY + rec.getWidth()/2, (pX - 15), pY + rec.getWidth()/2 , Color.BLACK, 5));
                grp.getChildren().add(inputLines.get(i));
            }
            //Die Hanlder hinzufügen (Beschreibung der Hander in  DraggableCanvas.java)  
        grp.addEventFilter( MouseEvent.MOUSE_PRESSED, dNodeGestures.getOnMousePressedEventHandler());
        grp.addEventFilter( MouseEvent.MOUSE_DRAGGED, dNodeGestures.getOnMouseDraggedEventHandler());
    }
	
     //Diese Methoden müssen überschrieben werden (Beschreibung in der Mutterklasse) 
    @Override
    public void update(){
             indicator.setVisible(true);
             if(inputs[0] == 1){
                indicator.setStroke(Color.RED);
             }else if (inputs[0] == 0){
                indicator.setStroke(Color.BLUE);
              }else{
              indicator.setStroke(Color.GREEN); //TODO: wenn es ZUstandlos gibt hier Farbe
             }
        }
       
      @Override
    public void showProperties(){ //Zeigt das "Eigenschaften"-Fenster für dieses Element
        JOptionPane.showMessageDialog(null,
			    "'LED' besitzt keine Eigenschaften",
			    "Info",
			    JOptionPane.INFORMATION_MESSAGE);
    }
    }   
  
