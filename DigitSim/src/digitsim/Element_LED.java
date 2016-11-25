/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javax.swing.JOptionPane;

/**
 *
 * @author lukas
 * 
 *TODO: Je nach vorstellung das aussehen ändern,  momentan nur elementblock mit kreis 
 *TODO: Fragezeichen visible und indicator invisible machen, nach beenden der simulation 
 *TODO: Entfernen
 */

public class Element_LED extends Element{
   
//Globals
    public static final String TYPE = "LED"; //Der Typ des Bausteines
    //Die Elemente aus denen der Baustein zusammengestezt ist
    private Label lbl;
    private Circle indicator;  
    
//Konstruktor   
    public Element_LED(double pX, double pY, int pInputs, NodeGestures dNodeGestures){ //Kein InputSilder, da nur ein Input

//LED zeichnen
        //outputs = new int[]{0};
   
    //Überarbeitet von Elias
        //Der Baustein wird nun (egal bei welcher BausteinWeite/Höhe) plaziert mit der Maus als Mittelpunkt
        pX = pX-elementWidth/2;
        pY = pY-elementHeight/2;
        numOutputs = 0; //Kein Ouptut benötigt!
        rec = Draw.drawRectangle(pX, pY, elementWidth, elementHeight, 10, 10, Color.BLACK, 0.4, 5);           //die led zeichnen
        lbl = Draw.drawLabel((pX + 20), (pY - 15), "?", Color.BLACK, false, 75);                            //Das fragezeichen wird nur angezeigt, wenn in die simulation nicht läuft
       outputLines.add(Draw.drawLine((pX + 95), (pY + 29.5), (pX + 100), (pY + 29.5), Color.BLACK, 5));
       outputLines.get(0).setVisible(false);  //Outputline ist unsichtbaar, muss wegen vererbung aber vorhanden sein!
        indicator = Draw.drawCircle(pX + 40, pY  + 40, 5, Color.BLUE, 5, true, 65);
        indicator.setVisible(false);
         numInputs = 1; //Input mit mehr als 1 Eingang ist sinnlos!
            inputs = new int[]{0};
            grp =  new Group(rec, outputLines.get(0), lbl, indicator);
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
        if (DigitSimController.isLocked() == true){
             lbl.setVisible(false);
             indicator.setVisible(true);
             if(inputs[0] == 1){
                indicator.setStroke(Color.RED);
                indicator.setFill(Color.RED);
             }else{
                indicator.setStroke(Color.BLUE);
                indicator.setFill(Color.BLUE);
             }
            }else if (DigitSimController.isLocked() == false){
           lbl.setVisible(true);
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
    }
    
    

