/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package element;

import toolbox.GenFunctions;
import Gestures.NodeGestures;
import general.Properties;
import java.util.Arrays;
import javafx.scene.Group;
import javafx.scene.effect.Bloom;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import toolbox.Draw;

/**
 *
 * @author Anthony und Niklas
 *
 *
 * 
 */
public class Element_7SegBCD extends Element{
    //Globals
    public static final ElementType.Type TYPE = ElementType.Type.SEVENSEG;
    //Die Elemente aus denen der Baustein zusammengestezt ist
    private Element thisElement = this; //Referenz auf sich selbst
    private int elementHeight = 210;
    private int elementWidth = 164;
    Line lineSeg0;
    Line lineSeg1;
    Line lineSeg2;
    Line lineSeg3;
    Line lineSeg4;
    Line lineSeg5;
    Line lineSeg6;
    //private ArrayList<Line> segments = new ArrayList<Line>();
            
    //Constructor
    public Element_7SegBCD(double pX, double pY, int pInputs, NodeGestures dNodeGestures){//Baustein zeichnen
        
        
        outputs = new int[]{0}; //Outputs
        
        //Der Baustein wird (egal bei welcher BausteinWeite/Höhe) plaziert mit der Maus als Mittelpunkt
        pX = pX-elementWidth/2;
        pY = pY-elementHeight/2;
        numOutputs = 0;
        
        rec = Draw.drawRectangle(pX, pY, elementWidth, elementHeight, 10, 10, Color.BLACK, Properties.getElementOpacity(), 5);           
        rec.addEventFilter(MouseEvent.MOUSE_ENTERED, NodeGestures.getOverNodeMouseHanlderEnterRec());
        rec.addEventFilter(MouseEvent.MOUSE_EXITED, NodeGestures.getOverNodeMouseHanlderExitRec());
        outputLines.add(Draw.drawLine((pX + 95), (pY + 29.5), (pX + 100), (pY + 29.5), Color.BLACK, 5));
        outputLines.get(0).setVisible(false);  //Outputline ist unsichtbaar, muss wegen vererbung aber vorhanden sein!
        
        
        numInputs = pInputs;
        inputs = new int[numInputs];
        Arrays.fill(inputs, 0); //Setzt alle Inputs auf '0
        grp = new Group(outputLines.get(0), rec);
        for(int i = 0; i < numInputs; i++)
        {
            
            double gridOffset = (double) Properties.GetGridOffset();
            
            if(rec.getHeight() <= (numInputs) * gridOffset) {
                rec.setHeight((numInputs) * gridOffset);
            }
            double offsetY = i * gridOffset + gridOffset - 10.5;
            
            inputLines.add(Draw.drawLine((pX - 5), pY + offsetY, (pX - 10), pY + offsetY, Color.BLACK, 5)); //Linie zeichnen
            inputLines.get(i).addEventFilter(MouseEvent.MOUSE_ENTERED, NodeGestures.getOverNodeMouseHanlderEnter());
            inputLines.get(i).addEventFilter(MouseEvent.MOUSE_EXITED, NodeGestures.getOverNodeMouseHanlderExit());
            inputLines.get(i).addEventFilter(MouseEvent.MOUSE_CLICKED, NodeGestures.getOverInputMouseHanlderClicked(this, i));
            grp.getChildren().add(inputLines.get(i));
        }
        
        //************Die Segmente der 7SEG anzeige werden hier "Erstellt"
        Group seg = new Group();
        this.lineSeg0 = Draw.drawLine(pX+45, pY+21, pX+120, pY+21, Color.BLACK, 5);
        this.lineSeg1 = Draw.drawLine(pX+126, pY+25, pX+126, pY+100, Color.BLACK, 5);
        this.lineSeg2 = Draw.drawLine(pX+126, pY+110, pX+126, pY+185, Color.BLACK, 5);
        this.lineSeg3 = Draw.drawLine(pX+45, pY+189, pX+120, pY+189, Color.BLACK, 5);
        this.lineSeg4 = Draw.drawLine(pX+39, pY+110, pX+39, pY+185, Color.BLACK, 5);
        this.lineSeg5 = Draw.drawLine(pX+39, pY+25, pX+39, pY+100, Color.BLACK, 5);
        this.lineSeg6 = Draw.drawLine(pX+45, pY+105, pX+121, pY+105, Color.BLACK, 5);
        this.lineSeg0.setEffect(new Bloom());
        
        seg.getChildren().addAll(lineSeg0, lineSeg1, lineSeg2, lineSeg3, lineSeg4, lineSeg5, lineSeg6);
        
        
        grp.getChildren().add(seg);
        rec.toFront();
         //Die Hanlder hinzufügenn (Beschreibung der Hander in  DraggableCanvas.java)  
        grp.addEventFilter( MouseEvent.MOUSE_PRESSED, dNodeGestures.getOnMousePressedEventHandler());
        grp.addEventFilter( MouseEvent.MOUSE_DRAGGED, dNodeGestures.getOnMouseDraggedEventHandler());
        grp.addEventFilter( MouseEvent.MOUSE_RELEASED, dNodeGestures.getOnMouseReleasedEventHandler());
    }
    
    //
    //Diese Methoden müssen überschrieben werden (Beschreibung in der Mutterklasse)
    @Override
    public void update(){

        
    //0    
    if(inputs[0] == 0 && inputs[1] == 0 && inputs[2] == 0 && inputs[3] == 0){
            lineSeg0.setStroke(Color.BLUE);
            lineSeg1.setStroke(Color.BLUE);
            lineSeg2.setStroke(Color.BLUE);
            lineSeg3.setStroke(Color.BLUE);
            lineSeg4.setStroke(Color.BLUE);
            lineSeg5.setStroke(Color.BLUE);
        }
    //1
     if(inputs[0] == 0 && inputs[1] == 0 && inputs[2] == 0 && inputs[3] == 1){
            lineSeg1.setStroke(Color.BLUE);
            lineSeg2.setStroke(Color.BLUE);
        }
    //2
      if(inputs[0] == 0 && inputs[1] == 0 && inputs[2] == 1 && inputs[3] == 0){
            lineSeg0.setStroke(Color.BLUE);
            lineSeg1.setStroke(Color.BLUE);   
            lineSeg3.setStroke(Color.BLUE);
            lineSeg4.setStroke(Color.BLUE);
            lineSeg6.setStroke(Color.BLUE);          
        } 
    //3
       if(inputs[0] == 0 && inputs[1] == 0 && inputs[2] == 1 && inputs[3] == 1){
            lineSeg0.setStroke(Color.BLUE);
            lineSeg1.setStroke(Color.BLUE);
            lineSeg2.setStroke(Color.BLUE);
            lineSeg3.setStroke(Color.BLUE);
            lineSeg6.setStroke(Color.BLUE);
        } 
    //4  
       if(inputs[0] == 0 && inputs[1] == 1 && inputs[2] == 0 && inputs[3] == 0){
            lineSeg1.setStroke(Color.BLUE);
            lineSeg2.setStroke(Color.BLUE);
            lineSeg5.setStroke(Color.BLUE);
            lineSeg6.setStroke(Color.BLUE);  
        }
    //5   
        if(inputs[0] == 0 && inputs[1] == 1 && inputs[2] == 0 && inputs[3] == 1){
            lineSeg0.setStroke(Color.BLUE);
            lineSeg2.setStroke(Color.BLUE);
            lineSeg3.setStroke(Color.BLUE);
            lineSeg5.setStroke(Color.BLUE);
            lineSeg6.setStroke(Color.BLUE);    
        }
    //6    
        if(inputs[0] == 0 && inputs[1] == 1 && inputs[2] == 1 && inputs[3] == 0){
            lineSeg0.setStroke(Color.BLUE);
            lineSeg2.setStroke(Color.BLUE);
            lineSeg3.setStroke(Color.BLUE);
            lineSeg4.setStroke(Color.BLUE);
            lineSeg5.setStroke(Color.BLUE);
            lineSeg6.setStroke(Color.BLUE);
        }
    //7    
        if(inputs[0] == 0 && inputs[1] == 1 && inputs[2] == 1 && inputs[3] == 1){
            lineSeg0.setStroke(Color.BLUE);
            lineSeg1.setStroke(Color.BLUE);
            lineSeg2.setStroke(Color.BLUE);
        }  
    //8    
        if(inputs[0] == 1 && inputs[1] == 0 && inputs[2] == 0 && inputs[3] == 0){
            lineSeg0.setStroke(Color.BLUE);
            lineSeg1.setStroke(Color.BLUE);
            lineSeg2.setStroke(Color.BLUE);
            lineSeg3.setStroke(Color.BLUE);
            lineSeg4.setStroke(Color.BLUE);
            lineSeg5.setStroke(Color.BLUE);
            lineSeg6.setStroke(Color.BLUE);
        }   
    //9   
        if(inputs[0] == 1 && inputs[1] == 0 && inputs[2] == 0 && inputs[3] == 1){
            lineSeg0.setStroke(Color.BLUE);
            lineSeg1.setStroke(Color.BLUE);
            lineSeg2.setStroke(Color.BLUE);
            lineSeg3.setStroke(Color.BLUE);
            lineSeg5.setStroke(Color.BLUE);
            lineSeg6.setStroke(Color.BLUE);
        } 
    //A    
        if(inputs[0] == 1 && inputs[1] == 0 && inputs[2] == 1 && inputs[3] == 0){
            lineSeg0.setStroke(Color.BLUE);
            lineSeg1.setStroke(Color.BLUE);
            lineSeg2.setStroke(Color.BLUE);
            lineSeg4.setStroke(Color.BLUE);
            lineSeg5.setStroke(Color.BLUE);
            lineSeg6.setStroke(Color.BLUE);
        } 
    //B    
        if(inputs[0] == 1 && inputs[1] == 0 && inputs[2] == 1 && inputs[3] == 1){
            lineSeg2.setStroke(Color.BLUE);
            lineSeg3.setStroke(Color.BLUE);
            lineSeg4.setStroke(Color.BLUE);
            lineSeg5.setStroke(Color.BLUE);
            lineSeg6.setStroke(Color.BLUE);
        }
    //C    
        if(inputs[0] == 1 && inputs[1] == 1 && inputs[2] == 0 && inputs[3] == 0){
            lineSeg0.setStroke(Color.BLUE);
            lineSeg3.setStroke(Color.BLUE);
            lineSeg4.setStroke(Color.BLUE);
            lineSeg5.setStroke(Color.BLUE);
        } 
    //D    
        if(inputs[0] == 1 && inputs[1] == 1 && inputs[2] == 0 && inputs[3] == 1){
            lineSeg1.setStroke(Color.BLUE);
            lineSeg2.setStroke(Color.BLUE);
            lineSeg3.setStroke(Color.BLUE);
            lineSeg4.setStroke(Color.BLUE);
            lineSeg6.setStroke(Color.BLUE);
        }
    //E    
        if(inputs[0] == 1 && inputs[1] == 1 && inputs[2] == 1 && inputs[3] == 0){
            lineSeg0.setStroke(Color.BLUE);
            lineSeg3.setStroke(Color.BLUE);
            lineSeg4.setStroke(Color.BLUE);
            lineSeg5.setStroke(Color.BLUE);
            lineSeg6.setStroke(Color.BLUE);
        } 
    //F
        if(inputs[0] == 1 && inputs[1] == 1 && inputs[2] == 1 && inputs[3] == 1){
            lineSeg0.setStroke(Color.BLUE);
            lineSeg4.setStroke(Color.BLUE);
            lineSeg5.setStroke(Color.BLUE);
            lineSeg6.setStroke(Color.BLUE);
        } 
    
    }   
    

             
 

     
    
    @Override
    public void showProperties(){ //Zeigt das "Eigenschaften"-Fenster für dieses Element
        GenFunctions.showBasicElementProperties(numInputs, thisElement);
    }

    @Override
    public String getTypeName() {
        return TYPE.name();
    }

}
