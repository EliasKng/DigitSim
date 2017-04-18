/*
Thumbswitch von Lukas und Millo
 */
package element;

//Importe
import toolbox.Draw;
import Gestures.NodeGestures;
import digitsim.DigitSimController;
import static element.Element.*;
import general.Properties;
import java.util.Arrays;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;


/**
 * 
 * @author lukas und Millo
 * 
 * überarbeitet lukas (23.12.2016) - outputs erzeugen
 * überarbeitet lukas (25.12.2016) - plus,minus und zählen von 0h - fh
 * überarbeitet lukas (27.12.2016) - zählt jetzt bis eingestellte zahl
 * überarbeitet lukas (02.01.2017) - number passt größe an und outputs gehen
 * TODO: delete wenn zu hohe/niedrige zahl
 * TODO: erzeugt manchmal ne Exeption
 * TODO: einstellungen -> rebuild
 * 
 */

public class Element_THUMBSWITCH extends Element{
    public static final Type TYPE = Type.THUMBSWITCH; //Der Typ des Bausteines
    //Die Elemente aus denen der Baustein zusammengestezt ist
    private Text number;  
    private Rectangle recUp;
    private Rectangle recDown;
    private Label lblUp;
    private Label lblDown;
    private int numbInt;
    private String numbString;
    private int numbMax;
    private int numbMin;
    private Label test;
    private Element thisEl = this;
    private boolean y;
    
    //Konstruktor   
    public Element_THUMBSWITCH(double pX, double pY, NodeGestures dNodeGestures){ //Kein InputSilder, hat kein input
        
        //Überarbeitet von Elias
        //Der Baustein wird nun (egal bei welcher BausteinWeite/Höhe) plaziert mit der Maus als Mittelpunkt
        pX = pX-elementWidth/2;
        pY = pY-elementHeight/2;
        
         properties();
         //*********************************************
        // test = Draw.drawLabel(pX, pY + 75, "xx", Color.DARKTURQUOISE, false, 50);
         //********************************************
         
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
        
       lblUp = Draw.drawLabel(recUp.getX() + (recUp.getWidth()/6) - 2, recUp.getY() - (recUp.getHeight()/1.5) + 8, "+", Color.BLACK, false, 40);
       lblDown = Draw.drawLabel(recDown.getX() + (recDown.getWidth()/6) + 1, recDown.getY() - (recDown.getHeight() /1.5), "-", Color.BLACK, false, 50);
       lblUp.setMouseTransparent(true);
       lblDown.setMouseTransparent(true);
        
        number = Draw.drawText(pX +(elementWidth/4) - 2, pY +(elementHeight/1.25) , "0", Color.BLACK, 75);  //elementWidth/height sind hier immernoch gleich
        
        
        //*********************************************************
        grp = new Group( number, rec, recUp, recDown ,lblUp, lblDown );
        //***************************************+
        
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
                outputLines.get(i).addEventFilter(MouseEvent.MOUSE_CLICKED, NodeGestures.getOverOutputMouseHanlderClicked(this, i));
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
        setLabel();
       // test.setText(numbString);
    }
    
    public void minus(){
         if( numbInt > numbMin){
            numbInt--;
        }else{
            numbInt = numbMax;
        }
        numbString = Integer.toBinaryString(numbInt);
        setLabel();
       // test.setText(numbString);
    }

    public void setLabel(){
       int x = numbInt - numbMin;
        number.setText(Integer.toHexString(x).toUpperCase());
        if(number.getText().length() != 1){
            number.setFont(Font.font (number.getFont().getName(), 60));
            number.setX(getX() + (elementWidth/7));
        }else{
            number.setFont(Font.font (number.getFont().getName(), 75));
            number.setX(getX() + (elementWidth/4));
        }
    }
    
    public void adjustNumbLength(){ //wird beim erstellen und ändern aufgerufen
        
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
    
    public void properties(){
        TextInputDialog dialog = new TextInputDialog("2");
        dialog.setTitle("Thumbswitch");
        dialog.setHeaderText("Ausgänge");
        dialog.setContentText("Anzahl:");
       
      
      //Optional<String> result = dialog.showAndWait();
        String result;
        dialog.showAndWait();
               
         result = dialog.getResult();
         boolean x = true;
         
         while(x == true){
            
            dialog.setOnCloseRequest(e -> {
                y = true;
            });
          if (Integer.parseInt(result) > 4){
              
              Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Thumbswitch");
              alert.setHeaderText("Ausgänge");
              alert.setContentText("Maximal 4 Ausgänge!");
              
              alert.showAndWait();
              dialog.showAndWait();
              result = dialog.getResult();
              
              x = true;
          
            }else if (Integer.parseInt(result) < 2){
               
               Alert alert = new Alert(AlertType.INFORMATION);
               alert.setTitle("Thumbswitch");
               alert.setHeaderText("Ausgänge");
               alert.setContentText("Minimum 2 Ausgänge!");
               
               alert.showAndWait();
              dialog.showAndWait();
              result = dialog.getResult();
               
              x = true;
           
            //*******************+
            }/*else if(y == true){
               numOutputs = 2;
               outputs = new int[2];
               x = false;
              DigitSimController.getReference().closeElement_THUMBSWITCH(thisEl);
            }*/else {
              numOutputs = Integer.parseInt(result);
              outputs = new int[Integer.parseInt(result) ];
              x = false;
            
            } 
         
        } 
        adjustNumbLength();
    }
    
    @Override
    public void update() {   //hier werden den outputs ihre werte zugewiesen    <ThisTookMeWayTooLong.png>
        
        StringBuilder builder = new StringBuilder(numbString);       //NumbString wird reversed(umgekehrt),  
        String reversedNumbString = builder.reverse().toString();    // da .charAt() von links nach rechts zählt
        
        for(int i = 0; i < numOutputs; i++){   
                                                                      // es wird nacheinander jede Stelle der ReversedNumbString durchgegangen                                                      
            if(Character.getNumericValue(reversedNumbString.charAt(i)) == 1){ //Wenn die Stelle in unserer binären String 1 ist, setzen wir logischerweise auch                 
                outputs[i] = 1;                                      //den Output auf 1 und die Linie rot
            }else{
                outputs[i] = 0;
            }
        }
    }
    
    @Override
    public void showProperties() {   
       DigitSimController.getReference().rebuildElement(thisEl, 0);
    } 
    
    @Override
    public String getTypeName() {
        return TYPE.name();
    }
}
  
    
    

