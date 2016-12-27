/*
Thumbswitch von Lukas und Millo
 */
package element;

//Importe
import javafx.scene.shape.*;
import toolbox.Draw;
import toolbox.GenFunctions;
import Gestures.NodeGestures;
import digitsim.DigitSimController;
import static element.Element.*;
import general.Properties;
import java.util.Arrays;
import java.util.Optional;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javax.swing.JOptionPane;

/**
 * 
 * @author lukas und Millo
 * 
 * überarbeitet lukas (23.12.2016) - outputs erzeugen
 * überarbeitet llukas (25.12.2016) - plus,minus und zählen von 0h - fh
 * lukas (27.12.2016) -zählt jetzt bis eingestellte zah)
 * TODO: delete wenn zu hohe/niedrige zahl
 * TODO: update()
 * TODO: fontsize anpassen wenn 2stellig
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
    private int numbMax;
    private int numbMin;
   
    
    //Konstruktor   
    public Element_THUMBSWITCH(double pX, double pY, NodeGestures dNodeGestures){ //Kein InputSilder, hat kein input
        
              
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
        
        grp = new Group( number, rec, recUp, recDown);
                
         Arrays.fill(outputs, 0); //Setzt alle outputs auf '0'
            
            for(int i = 0; i < numOutputs; i++){
                
                double gridOffset = (double) Properties.GetGridOffset();
                
                if(rec.getHeight() <= (numOutputs) * gridOffset) {
                    rec.setHeight((numOutputs) * gridOffset);
                }
                double offsetY = i * gridOffset + gridOffset - 12.5;
             
                outputLines.add(Draw.drawLine((pX + 85), pY + offsetY, (pX + 90), pY + offsetY, Color.BLACK, 5)); //Linie zeichnen
                outputLines.get(i).addEventFilter(MouseEvent.MOUSE_ENTERED, NodeGestures.getOverNodeMouseHanlderEnter());
                outputLines.get(i).addEventFilter(MouseEvent.MOUSE_EXITED, NodeGestures.getOverNodeMouseHanlderExit());
                outputLines.get(i).addEventFilter(MouseEvent.MOUSE_CLICKED, NodeGestures.getOverOutputMouseHanlderClicked(this, 0));
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
        if( numbInt < numbMax){
            numbInt++;
        }else{
            numbInt = numbMin;
        }
        numbString = Integer.toBinaryString(numbInt);
        setLabel(numbInt);
    }
    
    public void minus(){
         if( numbInt > numbMin){
            numbInt--;
        }else{
            numbInt = numbMax;
        }
        numbString = Integer.toBinaryString(numbInt);
        setLabel(numbInt);
    }

    public void setLabel(int x){
        x = x - numbMin;
        number.setText(Integer.toHexString(x));
    }
    
    @Override
    public void update() {                          //hier werden den outputs ihre werte zugewiesen
        for (int i = 0; i <= numOutputs; i++) {    //jedem ausgang wird "seine" stelle aus der binarynummer zugewiesen mit charAt()
            ///outputs[i] = number.getText().charAt(i);
          // outputs[i] = numbString.charAt(i);
        } 
        //char[] charArray = numbString.toCharArray();      
       // outputs[0] = numbString.charAt(0);
       // outputs[1] = numbString.charAt(1);
        
    }

    @Override
    public void showProperties() {
      TextInputDialog dialog = new TextInputDialog("2");
        dialog.setTitle("Thumbswitch");
        dialog.setHeaderText("Ausgänge");
        dialog.setContentText("Anzahl:");
      
      Optional<String> result = dialog.showAndWait();
          
          if (Integer.parseInt(result.get()) >= 9){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Thumbswitch");
            alert.setHeaderText("Ausgänge");
            alert.setContentText("Maximal 8 Ausgänge!");
            alert.showAndWait();
            
           //************
        }
          else if (Integer.parseInt(result.get()) <2){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Thumbswitch");
            alert.setHeaderText("Ausgänge");
            alert.setContentText("Minimum 2 Ausgänge!");
            alert.showAndWait();
            //*******************+
        }
          else if (result.isPresent()){
            numOutputs = Integer.parseInt(result.get());
            outputs = new int[Integer.parseInt(result.get()) - 1];
            
            } 
         
        adjustNumbLength();
    }     
    
    public void adjustNumbLength(){
        
         if(numOutputs == 2){
             
                numbInt = 0b100;
                numbMin = 0b100;
                numbMax = 0b111;
         }
         else if(numOutputs == 3){
             
                numbInt = 0b1000;
                numbMin = 0b1000;
                numbMax = 0b1111;   
         }
         else if(numOutputs == 4){
         
                numbInt = 0b10000;
                numbMin = 0b10000;
                numbMax = 0b11111; 
          }
         else if(numOutputs == 5){
                numbInt = 0b100000;
                numbMin = 0b100000;
                numbMax = 0b111111; 
                
            }
         else if(numOutputs == 6){
                numbInt = 0b1000000;
                numbMin = 0b1000000;
                numbMax = 0b1111111; 
                
             }
         else if(numOutputs == 7){
                numbInt = 0b10000000;
                numbMin = 0b10000000;
                numbMax = 0b11111111; 
                
             }
         else {
                numbInt = 0b100000000;
                numbMin = 0b100000000;
                numbMax = 0b111111111; 
               
         }
        
         numbString = Integer.toBinaryString(numbInt);
    }
}
    
    

