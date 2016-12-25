/*
Thumbswitch von Lukas und Millo
 */
package element;

//Importe
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
 * @author lukas und Millo
 * 
 * überarbeitet lukas (23.12.2016) - outputs erzeugen
 * überarbeitet llukas (25.12.2016) - plus,minus und zählen von 0h - fh
 * 
 * TODO: nur zahlen als input, max ausgänge
 * TODO: richtiges aktualisieren der ausgänge
 * 
 */

public class Element_THUMBSWITCH extends Element{
    //Globals
    public static final String TYPE = "THUMBSWITCH"; //Der Typ des Bausteines
    //Die Elemente aus denen der Baustein zusammengestezt ist
    private Label number;  
    private Rectangle recUp;
    private Rectangle recDown;
    private Label lblUp;
    private Label lblDown;
    private int numbInt;
    private String numbString;
   
    
    //Konstruktor   
    public Element_THUMBSWITCH(double pX, double pY, NodeGestures dNodeGestures){ //Kein InputSilder, hat kein input
        
        numbInt = 0b10000; 
        numbString = Integer.toBinaryString(numbInt);
        
        //Überarbeitet von Elias
        //Der Baustein wird nun (egal bei welcher BausteinWeite/Höhe) plaziert mit der Maus als Mittelpunkt
        pX = pX-elementWidth/2;
        pY = pY-elementHeight/2;
        
         showProperties();   
         
        rec = Draw.drawRectangle(pX, pY, elementWidth, elementHeight, 10, 10, Color.BLACK, Properties.getElementOpacity(), 5);           //das Signal zeichnen
        rec.addEventFilter(MouseEvent.MOUSE_ENTERED, NodeGestures.getOverNodeMouseHanlderEnterRec());
        rec.addEventFilter(MouseEvent.MOUSE_EXITED, NodeGestures.getOverNodeMouseHanlderExitRec());
      
        recUp = Draw.drawRectangle((pX - 42), pY , 35, 35, 5, 5, Color.BLACK, Properties.getElementOpacity(), 5); 
        recDown = Draw.drawRectangle((pX - 42), (pY + 42), 35, 35, 5, 5, Color.BLACK, Properties.getElementOpacity(), 5); 
        recUp.setFill(Color.RED);
        recDown.setFill(Color.BLUE);
        recUp.addEventFilter(MouseEvent.MOUSE_ENTERED, NodeGestures.getOverNodeMouseHanlderEnterRec());
        recUp.addEventFilter(MouseEvent.MOUSE_EXITED, NodeGestures.getOverNodeMouseHanlderExitRec()); 
        recDown.addEventFilter(MouseEvent.MOUSE_ENTERED, NodeGestures.getOverNodeMouseHanlderEnterRec());
        recDown.addEventFilter(MouseEvent.MOUSE_EXITED, NodeGestures.getOverNodeMouseHanlderExitRec());
        recUp.setOnMouseClicked(e -> plus());
        recDown.setOnMouseClicked(e -> minus());
        
       // lblUp = Draw.drawLabel(recUp.getX() + 10, recUp.getY() - 10, "+", Color.BLACK, false, 40);
       //lblDown = Draw.drawLabel(recDown.getX() + 10, recDown.getY() - 10, "-", Color.BLACK, false, 40);
        
        number = Draw.drawLabel((pX + 20), (pY - 15), "0", Color.BLACK, false, 75);  
        
        grp = new Group( number, rec, recUp, recDown, lblUp);
                
         Arrays.fill(outputs, 0); //Setzt alle Inputs auf '0'
            
            for(int i = 0; i < numOutputs; i++){
                
                double gridOffset = (double) Properties.GetGridOffset();
                
                if(rec.getHeight() <= (numOutputs) * gridOffset) {
                    rec.setHeight((numOutputs) * gridOffset);
                }
                double offsetY = i * gridOffset + gridOffset - 12.5;
             
                outputLines.add(Draw.drawLine((pX + 85), pY + offsetY, (pX + 90), pY + offsetY, Color.BLACK, 5)); //Linie zeichnen
              /*  outputLines.get(i).addEventFilter(MouseEvent.MOUSE_ENTERED, NodeGestures.getOverNodeMouseHanlderEnter());
                outputLines.get(i).addEventFilter(MouseEvent.MOUSE_EXITED, NodeGestures.getOverNodeMouseHanlderExit());
                outputLines.get(i).addEventFilter(MouseEvent.MOUSE_CLICKED, NodeGestures.getOverInputMouseHanlderClicked(this, i)); */
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
    
    public void plus(){
        if( numbInt < 0b11111){
            numbInt++;
        }else{
            numbInt = 0b10000;
        }
        numbString = Integer.toBinaryString(numbInt);
        setLabel(numbInt);
    }
    
    public void minus(){
         if( numbInt > 0b10000){
            numbInt--;
        }else{
            numbInt = 0b11111;
        }
        numbString = Integer.toBinaryString(numbInt);
        setLabel(numbInt);
    }

    public void setLabel(int x){
        x = x - 0b10000;
        number.setText(Integer.toHexString(x));
    }
    
    @Override
    public void update() {                          //hier werden den outputs ihre werte zugewiesen
        for (int i = 0; i <= numOutputs; i++) {    //jedem ausgang wird "seine" stelle aus der binarynummer zugewiesen mit charAt()
           // outputs[i] =Character.getNumericValue(numbString.charAt(i));   
           // outputs[i] = Integer.parseInt(numbString.charAt(i));
        }
    }

    @Override
    public void showProperties() {
      TextInputDialog dialog = new TextInputDialog("2");
        dialog.setTitle("Thumbswitch");
        dialog.setHeaderText("Ausgänge");
        dialog.setContentText("Anzahl:");
      
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
    
    

