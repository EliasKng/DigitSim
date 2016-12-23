/*
Thumbswitch von Lukas und Millo
 */
package element;

//Importe
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

/**
 * 
 * @author lukas und Millo
 * 
 * TODO: nur zahlen als input
 */

public class Element_THUMBSWITCH extends Element{
    //Globals
    public static final String TYPE = "THUMBSWITCH"; //Der Typ des Bausteines
    //Die Elemente aus denen der Baustein zusammengestezt ist
    private Label number;  
    private double pY;
    private double pX;
      //Wir brauchen 2 Labels, da es ansonsten nicht Thread sicher ist

    //Konstruktor   
    public Element_THUMBSWITCH(double pX, double pY, NodeGestures dNodeGestures){ //Kein InputSilder, hat kein input
         
        //Überarbeitet von Elias
        //Der Baustein wird nun (egal bei welcher BausteinWeite/Höhe) plaziert mit der Maus als Mittelpunkt
        pX = pX-elementWidth/2;
        pY = pY-elementHeight/2;
        //******
        this.pX = pX;
        this.pX = pY;     
         showProperties();       
        rec = Draw.drawRectangle(pX, pY, elementWidth, elementHeight, 10, 10, Color.BLACK, Properties.getElementOpacity(), 5);           //das Signal zeichnen
        rec.addEventFilter(MouseEvent.MOUSE_ENTERED, NodeGestures.getOverNodeMouseHanlderEnterRec());
        rec.addEventFilter(MouseEvent.MOUSE_EXITED, NodeGestures.getOverNodeMouseHanlderExitRec());
        //******************
        number = Draw.drawLabel((pX + 20), (pY - 15), "0", Color.BLACK, false, 75);                            //Das label (die  0 oder 1 im block, haben beim erstellen zuerst auf 0) 
        grp = new Group( number, rec);
         Arrays.fill(outputs, 0); //Setzt alle Inputs auf '0'
            
            for(int i = 0; i < numOutputs; i++){
                
                double gridOffset = (double) Properties.GetGridOffset();
                
                if(rec.getHeight() <= (numOutputs) * gridOffset) {
                    rec.setHeight((numOutputs) * gridOffset);
                }
                double offsetY = i * gridOffset + gridOffset - 12.5;
                //Draw.drawLine((pX - 5), pY + offsetY, (pX - 10), pY + offsetY, Color.BLACK, 5)
                outputLines.add(Draw.drawLine((pX + 85), pY + offsetY, (pX + 90), pY + offsetY, Color.BLACK, 5)); //Linie zeichnen
                outputLines.get(i).addEventFilter(MouseEvent.MOUSE_ENTERED, NodeGestures.getOverNodeMouseHanlderEnter());
                outputLines.get(i).addEventFilter(MouseEvent.MOUSE_EXITED, NodeGestures.getOverNodeMouseHanlderExit());
                outputLines.get(i).addEventFilter(MouseEvent.MOUSE_CLICKED, NodeGestures.getOverInputMouseHanlderClicked(this, i));
                grp.getChildren().add(outputLines.get(i)); //Linie hinzufügen   
    
            }
        numInputs = 1; 
        inputs = new int[]{0};
        
        
        inputLines.add(Draw.drawLine((pX - 5), pY, (pX - 15), pY, Color.BLACK, 5)); //Wir können den Input trotzdem benutzen um den Baustein auf 0,1 zu setzen
        inputLines.get(0).setVisible(false);
        grp.getChildren().add(inputLines.get(0));
        
             //Die Hanlder hinzufügen (Beschreibung der Hander in  DraggableCanvas.java)  
        grp.addEventFilter( MouseEvent.MOUSE_PRESSED, dNodeGestures.getOnMousePressedEventHandler());
        grp.addEventFilter( MouseEvent.MOUSE_DRAGGED, dNodeGestures.getOnMouseDraggedEventHandler());
        grp.addEventFilter( MouseEvent.MOUSE_RELEASED, dNodeGestures.getOnMouseReleasedEventHandler());
        
    }

    @Override
    public void update() {
      
    }

    @Override
    public void showProperties() {
      TextInputDialog dialog = new TextInputDialog("2");
        dialog.setTitle("ssssssssss");
        dialog.setHeaderText("textete");
        dialog.setContentText("tststststs");
      
      Optional<String> result = dialog.showAndWait();
         if (result.isPresent()){
             numOutputs = Integer.parseInt(result.get());
             outputs = new int[Integer.parseInt(result.get())];
            }  
         else{
         numOutputs = 2;
         outputs = new int[1];
        }
    }        
}
    
    

