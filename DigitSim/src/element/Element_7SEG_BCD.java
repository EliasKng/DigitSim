/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package element;

import toolbox.GenFunctions;
import Gestures.NodeGestures;
import connection.HandleState;
import connection.State;
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
public class Element_7SEG_BCD extends Element{
    //Globals
    public static final ElementType.Type TYPE = ElementType.Type.SEVENSEG;
    //Die Elemente aus denen der Baustein zusammengestezt ist
    private Element thisElement = this; //Referenz auf sich selbst
    private int elementHeight = 210;
    private int elementWidth = 164;
    private Line lineSeg0;
    private Line lineSeg1;
    private Line lineSeg2;
    private Line lineSeg3;
    private Line lineSeg4;
    private Line lineSeg5;
    private Line lineSeg6;
    //private ArrayList<Line> segments = new ArrayList<Line>();
            
    //Constructor
    public Element_7SEG_BCD(double pX, double pY, int pInputs, NodeGestures dNodeGestures){//Baustein zeichnen
        
        
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

        State states[] = new State[4];

        states[0] = HandleState.getState(inputs[0]);
        states[1] = HandleState.getState(inputs[1]);
        states[2] = HandleState.getState(inputs[2]);
        states[3] = HandleState.getState(inputs[3]);

        if(states[3] == State.HIGH) {
            if(states[2] == State.HIGH) {
                if(states[1] == State.HIGH) {
                    if(states[0] == State.HIGH) {           //1111 - F
                        lineSeg0.setStroke(Color.RED);
                        lineSeg4.setStroke(Color.RED);
                        lineSeg5.setStroke(Color.RED);
                        lineSeg6.setStroke(Color.RED);
                        lineSeg1.setStroke(Color.GREY);
                        lineSeg2.setStroke(Color.GREY);
                        lineSeg3.setStroke(Color.GREY);
                    } else {                                //1110 - E
                        lineSeg0.setStroke(Color.RED);
                        lineSeg3.setStroke(Color.RED);
                        lineSeg4.setStroke(Color.RED);
                        lineSeg5.setStroke(Color.RED);
                        lineSeg6.setStroke(Color.RED);
                        lineSeg1.setStroke(Color.GREY);
                        lineSeg2.setStroke(Color.GREY);
                    }
                } else {
                    if(states[0] == State.HIGH) {           //1101 - D
                        lineSeg1.setStroke(Color.RED);
                        lineSeg2.setStroke(Color.RED);
                        lineSeg3.setStroke(Color.RED);
                        lineSeg4.setStroke(Color.RED);
                        lineSeg6.setStroke(Color.RED);
                        lineSeg0.setStroke(Color.GREY);
                        lineSeg5.setStroke(Color.GREY);
                    } else {                                //1100 - C
                        lineSeg0.setStroke(Color.RED);
                        lineSeg3.setStroke(Color.RED);
                        lineSeg4.setStroke(Color.RED);
                        lineSeg5.setStroke(Color.RED);
                        lineSeg1.setStroke(Color.GREY);
                        lineSeg2.setStroke(Color.GREY);
                        lineSeg6.setStroke(Color.GREY);
                    }
                }
            } else {
                if(states[1] == State.HIGH) {
                    if(states[0] == State.HIGH) {           //1011 - B
                        lineSeg2.setStroke(Color.RED);
                        lineSeg3.setStroke(Color.RED);
                        lineSeg4.setStroke(Color.RED);
                        lineSeg5.setStroke(Color.RED);
                        lineSeg6.setStroke(Color.RED);
                        lineSeg0.setStroke(Color.GREY);
                        lineSeg1.setStroke(Color.GREY);
                    } else {                                //1010 - A
                        lineSeg0.setStroke(Color.RED);
                        lineSeg1.setStroke(Color.RED);
                        lineSeg2.setStroke(Color.RED);
                        lineSeg4.setStroke(Color.RED);
                        lineSeg5.setStroke(Color.RED);
                        lineSeg6.setStroke(Color.RED);
                        lineSeg3.setStroke(Color.GREY);
                    }
                } else {
                    if(states[0] == State.HIGH) {           //1001 - 9
                        lineSeg0.setStroke(Color.RED);
                        lineSeg1.setStroke(Color.RED);
                        lineSeg2.setStroke(Color.RED);
                        lineSeg3.setStroke(Color.RED);
                        lineSeg5.setStroke(Color.RED);
                        lineSeg6.setStroke(Color.RED);
                        lineSeg4.setStroke(Color.GREY);
                    } else {                                //1000 - 8
                        lineSeg0.setStroke(Color.RED);
                        lineSeg1.setStroke(Color.RED);
                        lineSeg2.setStroke(Color.RED);
                        lineSeg3.setStroke(Color.RED);
                        lineSeg4.setStroke(Color.RED);
                        lineSeg5.setStroke(Color.RED);
                        lineSeg6.setStroke(Color.RED);
                    }
                }
            }
        } else {
            if(states[2] == State.HIGH) {
                if(states[1] == State.HIGH) {
                    if(states[0] == State.HIGH) {           //0111 - 7
                        lineSeg0.setStroke(Color.RED);
                        lineSeg1.setStroke(Color.RED);
                        lineSeg2.setStroke(Color.RED);
                        lineSeg3.setStroke(Color.GREY);
                        lineSeg4.setStroke(Color.GREY);
                        lineSeg5.setStroke(Color.GREY);
                        lineSeg6.setStroke(Color.GREY);
                        
                    } else {                                //0110 - 6
                        lineSeg0.setStroke(Color.RED);
                        lineSeg1.setStroke(Color.GREY);
                        lineSeg2.setStroke(Color.RED);
                        lineSeg3.setStroke(Color.RED);
                        lineSeg4.setStroke(Color.RED);
                        lineSeg5.setStroke(Color.RED);
                        lineSeg6.setStroke(Color.RED);
                    }
                } else {
                    if(states[0] == State.HIGH) {           //0101 - 5
                        lineSeg0.setStroke(Color.RED);
                        lineSeg2.setStroke(Color.RED);
                        lineSeg3.setStroke(Color.RED);
                        lineSeg5.setStroke(Color.RED);
                        lineSeg6.setStroke(Color.RED);
                        lineSeg1.setStroke(Color.GREY);
                        lineSeg4.setStroke(Color.GREY);
                    } else {                                //0100 - 4
                        lineSeg1.setStroke(Color.RED);
                        lineSeg2.setStroke(Color.RED);
                        lineSeg5.setStroke(Color.RED);
                        lineSeg6.setStroke(Color.RED);  
                        lineSeg0.setStroke(Color.GREY);
                        lineSeg3.setStroke(Color.GREY);
                        lineSeg4.setStroke(Color.GREY);
                    }
                }
            } else {
                if(states[1] == State.HIGH) {
                    if(states[0] == State.HIGH) {           //0011 - 3
                        lineSeg0.setStroke(Color.RED);
                        lineSeg1.setStroke(Color.RED);
                        lineSeg2.setStroke(Color.RED);
                        lineSeg3.setStroke(Color.RED);
                        lineSeg4.setStroke(Color.GREY);
                        lineSeg5.setStroke(Color.GREY);
                        lineSeg6.setStroke(Color.RED);
                    } else {                                //0010 - 2
                        lineSeg0.setStroke(Color.RED);
                        lineSeg1.setStroke(Color.RED);
                        lineSeg2.setStroke(Color.GREY);
                        lineSeg3.setStroke(Color.RED);
                        lineSeg4.setStroke(Color.RED);
                        lineSeg6.setStroke(Color.RED);  
                    }
                } else {
                    if(states[0] == State.HIGH) {           //0001 - 1
                        lineSeg0.setStroke(Color.GREY);
                        lineSeg1.setStroke(Color.RED);
                        lineSeg2.setStroke(Color.RED);
                        lineSeg3.setStroke(Color.GREY);
                        lineSeg4.setStroke(Color.GREY);
                        lineSeg5.setStroke(Color.GREY);
                        lineSeg6.setStroke(Color.GREY);
                    } else {                                //0000 - 0
                        lineSeg0.setStroke(Color.RED);
                        lineSeg1.setStroke(Color.RED);
                        lineSeg2.setStroke(Color.RED);
                        lineSeg3.setStroke(Color.RED);
                        lineSeg4.setStroke(Color.RED);
                        lineSeg5.setStroke(Color.RED);
                        lineSeg6.setStroke(Color.GREY);
                    }
                }
            }
        }
    }   
    
    @Override
    public void reset(){ //Simple reset methode (kann natürlich überschireben werden, für spezielle Bausteine)
        lineSeg0.setStroke(Color.GREY);
        lineSeg1.setStroke(Color.GREY);
        lineSeg2.setStroke(Color.GREY);
        lineSeg3.setStroke(Color.GREY);
        lineSeg4.setStroke(Color.GREY);
        lineSeg5.setStroke(Color.GREY);
        lineSeg6.setStroke(Color.GREY);
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
