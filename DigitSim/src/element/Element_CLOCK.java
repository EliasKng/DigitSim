/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package element;

import Gestures.NodeGestures;
import digitsim.DigitSim;
import digitsim.DigitSimController;
import static element.Element.elementWidth;
import general.Properties;
import java.util.Arrays;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;
import toolbox.Draw;
import toolbox.GenFunctions;

/**
 *
 * @author Elias
 */
public class Element_CLOCK extends Element{
//Globals
    public static final ElementType.Type TYPE = ElementType.Type.CLOCK; //Der Typ des Bausteines
    public int clock = 1;
    public int baseCLK = Properties.GetThreadDurationMS();
    public int updateEvery = 5;
    public int count = 0;
    //Die Elemente aus denen der Baustein zusammengestezt ist

    //Konstruktor   
    public Element_CLOCK(double pX, double pY, String pPayload, NodeGestures dNodeGestures){ //Kein InputSilder, hat kein input
        //signal zeichnen
        
        pX = pX-elementWidth/2;
        pY = pY-elementHeight/2;
        
        numOutputs = 1;
        outputs = new int[]{0};
        rec = Draw.drawRectangle(pX, pY, elementWidth, elementHeight, 10, 10, Color.BLACK, Properties.getElementOpacity(), 5);           //das Signal zeichnen
        rec.addEventFilter(MouseEvent.MOUSE_ENTERED, NodeGestures.getOverNodeMouseHanlderEnterRec(this));
        rec.addEventFilter(MouseEvent.MOUSE_EXITED, NodeGestures.getOverNodeMouseHanlderExitRec(this));
        outputLines.add(Draw.drawLine((pX + 85), (pY + 29.5), (pX + 90), (pY + 29.5), Color.BLACK, 5));
        outputLines.get(0).addEventFilter(MouseEvent.MOUSE_ENTERED, NodeGestures.getOverNodeMouseHanlderEnter());
        outputLines.get(0).addEventFilter(MouseEvent.MOUSE_EXITED, NodeGestures.getOverNodeMouseHanlderExit());
        outputLines.get(0).addEventFilter(MouseEvent.MOUSE_CLICKED, NodeGestures.getOverOutputMouseHanlderClicked(this, 0));
        numInputs = 1; 
        inputs = new int[]{0};
        grp = new Group(outputLines.get(0), rec);
        inputLines.add(Draw.drawLine((pX - 5), pY, (pX - 15), pY, Color.BLACK, 5)); //Wir können den Input trotzdem benutzen um den Baustein auf 0,1 zu setzen
        inputLines.get(0).setVisible(false);
        grp.getChildren().add(inputLines.get(0));
            
         //Die Hanlder hinzufügenn (Beschreibung der Hander in  DraggableCanvas.java)  
        grp.addEventFilter( MouseEvent.MOUSE_PRESSED, dNodeGestures.getOnMousePressedEventHandler());
        grp.addEventFilter( MouseEvent.MOUSE_DRAGGED, dNodeGestures.getOnMouseDraggedEventHandler());
        grp.addEventFilter( MouseEvent.MOUSE_RELEASED, dNodeGestures.getOnMouseReleasedEventHandler());
        setPayload(pPayload);
    }
     //Diese Methoden müssen überschrieben werden (Beschreibung in der Mutterklasse) 
    
    @Override
    public void update(){  
        if(baseCLK != Properties.GetThreadDurationMS()){ //Falls der Benutzer den Takt umstellt muss der Takt der clock erneut angepasst werden!
            adaptClock(clock);
            baseCLK = Properties.GetThreadDurationMS();
        }
        
        count++;
        if(count >= updateEvery) {
            count = 0;
            if(this.getOutput(0) == 0) {
                this.setOutput(0, 1);
            } else {
                this.setOutput(0, 0);
            }
        }  
    }    
    
    @Override
    public void showProperties(){ //Zeigt das "Eigenschaften"-Fenster für dieses Element
        Element thisElement = this;
        Stage stage = new Stage(StageStyle.DECORATED); //Ein Fenster für die Eigenschaften erstellen und Anzeigen
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.alwaysOnTopProperty();
        stage.getIcons().add(new Image(DigitSim.class.getResourceAsStream( "icon.png" )));
        stage.setResizable(false);
        stage.setMinWidth(140);
        stage.setMinHeight(100);
        stage.setTitle("Eigenschaften"); //titel
        Label lbl = new Label("Takt:");
        lbl.setTranslateX(5); //X/Y Koords
        lbl.setTranslateY(5);
        lbl.setFont(new Font(14)); //Schriftgröße
        lbl.setPrefHeight(20); //Größe
        lbl.setPrefWidth(50);
        TextField tf = new TextField(){ //textfeld um die inputs einzugeben
            @Override public void replaceText(int start, int end, String text) {//Diese funktionen werden ausgeführt wenn man den text ändert
                if (text.matches("[0-9]") || text == "") {//Überorüfen ob nur zahlen eingegeben wurden
                   super.replaceText(start, end, text);//Ja
                }else{
                    this.setText("");//nein, also textfeld leeren
                }
            }
     
           @Override public void replaceSelection(String text) {//Selbe
               if (text.matches("[0-9]") || text == "") {
               super.replaceSelection(text);
               }else{
                    this.setText("");
                }
           }
        };
        tf.setText(String.valueOf(clock));
        tf.setTranslateX(50);
        tf.setTranslateY(5);
        tf.setPrefHeight(20);
        tf.setPrefWidth(50);
        Button btn = new Button("Übernehmen");
        btn.setTranslateX(5);
        btn.setTranslateY(40);
        btn.setPrefHeight(20);
        btn.setPrefWidth(100);
        btn.setOnAction(new EventHandler<ActionEvent>(){//Wird bei "Übernehmen" ausgeführt
            @Override
            public void handle(ActionEvent e){
                int pClock = Integer.parseInt(tf.getText().trim());//String zu Integer
                if(pClock <= 50 && pClock >= 1 && pClock != clock && pClock <= Properties.GetThreadDurationMS()){//Testen ob die inputs sinn machen und sich geändert haben
                    adaptClock(pClock);
                }else{
                    tf.setText(String.valueOf(clock));//zurücksetzen
                    JOptionPane.showMessageDialog(null,
			    "Angebener Takt ist zu hoch! (Takt darf nicht höher als der Standarttakt des Programmes sein!)",
			    "Info",
			    JOptionPane.INFORMATION_MESSAGE);
                } 
                stage.close();
            }         
        });
        tf.setOnKeyPressed(e -> {if(e.getCode() == KeyCode.ENTER){
        btn.fire();}});
        btn.setOnKeyPressed(e -> {if(e.getCode() == KeyCode.ENTER){
        btn.fire();}});
       Scene scene = new Scene(new Group(lbl, tf, btn));
       stage.setScene(scene);
       stage.show();//Fenster zeigen
    }
    
    @Override
    public String getTypeName() {
        return TYPE.name();
    }
    
    public String getPayload() {
        payload = Integer.toString(clock);
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
        adaptClock(Integer.parseInt(payload));
    }
    
    private void adaptClock(int pClock){
        clock = pClock;
        updateEvery = (int)Math.floor(((float)Properties.GetThreadDurationMS() / (float)clock)); //TL;DR: BaseClock / Clock
        count = 0;
    }
}