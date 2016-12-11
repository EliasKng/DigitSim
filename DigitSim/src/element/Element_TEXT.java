/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package element;

import digitsim.Draw;
import Gestures.NodeGestures;
import digitsim.DigitSim;
import digitsim.DigitSimController;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
/**
 *
 * @author lukas
 * 26.11.2016
 * 
 * erzeut inputfield und string auf canvas
 * werde aber wohl nen standart text nehmen müssen, weil beim verschieben immes node erzeugt wird
 * 
 * TODO: über eigenschaften ändern können
 * TODO: verschiebbar
 */
public class Element_TEXT extends Element{
   
//Globals
    public static final String TYPE = "TEXT"; //Der Typ des Bausteines
    //Die Elemente aus denen der Baustein zusammengestezt ist
    private Text text;  
    private Element thisEl = this;
    private Color curentColor;
   
//Konstruktor   
   public Element_TEXT(double pX, double pY, int fontSize, String content, Color color,NodeGestures dNodeGestures){
       curentColor = color;
        numInputs = 0;
        numOutputs = 0;
        inputs = new int[]{};
        outputs = new int[]{};
        text = Draw.drawText(pX, pY, content, curentColor, fontSize);
        rec = Draw.drawRectangle(pX - 5, pY - text.getFont().getSize(), text.getLayoutBounds().getWidth() + 10, text.getLayoutBounds().getHeight() + 5, 10, 10, Color.BLACK, 0, 2);  
        rec.setVisible(false);
        grp =  new Group(rec, text);         
        
        //Die Hanlder hinzufügen (Beschreibung der Hander in  DraggableCanvas.java)  
        grp.addEventFilter( MouseEvent.MOUSE_PRESSED, dNodeGestures.getOnMousePressedEventHandler());
        grp.addEventFilter( MouseEvent.MOUSE_DRAGGED, dNodeGestures.getOnMouseDraggedEventHandler());
        grp.addEventFilter( MouseEvent.MOUSE_RELEASED, dNodeGestures.getOnMouseReleasedEventHandler());
    
    }
    @Override
    public void update(){ //Nix, isn text :D
         } 
    
    @Override
    public double getWidth() { //breite
        return text.getLayoutBounds().getWidth();
    }
    
    @Override
    public double getHeight() { //Hähe
        return text.getLayoutBounds().getHeight();
    }
    
    @Override
    public double getX() { //X-Koordinate des Elements, standart wert + verschiebung
       return text.getX() + grp.getTranslateX();
    }

    @Override
    public double getY() { //Y-Koordinate des Elements, standart wert + verschiebung
        return text.getY() + grp.getTranslateY();
    }
    
    @Override
    public void showProperties(){ //Zeigt das "Eigenschaften"-Fenster für dieses Element
       Stage stage = new Stage(StageStyle.DECORATED); //Ein Fenster für die Eigenschaften erstellen und Anzeigen
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.alwaysOnTopProperty();
        stage.getIcons().add(new Image(DigitSim.class.getResourceAsStream( "icon.png" )));
        stage.setResizable(false);
        stage.setMinWidth(190);
        stage.setMinHeight(120);
        stage.setTitle("Eigenschaften"); //titel
        Label lbl = new Label("Schriftgröße:");
        lbl.setTranslateX(5); //X/Y Koords
        lbl.setTranslateY(5);
        lbl.setFont(new Font(14)); //Schriftgröße
        lbl.setPrefHeight(20); //Größe
        lbl.setPrefWidth(85);
        Label lbl2 = new Label("Text:");
        lbl2.setTranslateX(5); //X/Y Koords
        lbl2.setTranslateY(35);
        lbl2.setFont(new Font(14)); //Schriftgröße
        lbl2.setPrefHeight(20); //Größe
        lbl2.setPrefWidth(85);
        Label lbl3 = new Label("Farbe:");
        lbl3.setTranslateX(5); //X/Y Koords
        lbl3.setTranslateY(65);
        lbl3.setFont(new Font(14)); //Schriftgröße
        lbl3.setPrefHeight(20); //Größe
        lbl3.setPrefWidth(85);
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
        tf.setText(String.valueOf((int)text.getFont().getSize()));
        tf.setTranslateX(90);
        tf.setTranslateY(5);
        tf.setPrefHeight(20);
        tf.setPrefWidth(100);
        TextField tf2 = new TextField(text.getText());
        tf2.setTranslateX(90);
        tf2.setTranslateY(35);
        tf2.setPrefHeight(20);
        tf2.setPrefWidth(100);
        
        ChoiceBox cb = new ChoiceBox(FXCollections.observableArrayList( //Auswahl der Farben
        "SCHWARZ", "GRÜN", "BLAU", "ROT", "ORANGE", "BRAUN", "GRAU") //Mögliche farben.... hab nur ein paar geadded (bisher
        );
        cb.setTranslateX(90);
        cb.setTranslateY(65);
        cb.setPrefHeight(20);
        cb.setPrefWidth(100);
        Button btn = new Button("Übernehmen");
        btn.setTranslateX(5);
        btn.setTranslateY(95);
        btn.setPrefHeight(20);
        btn.setPrefWidth(185);
        btn.setOnAction(new EventHandler<ActionEvent>(){//Wird bei "Übernehmen" ausgeführt
            @Override
            public void handle(ActionEvent e){
                int fontSize = Integer.parseInt(tf.getText().trim());//String zu Integer
                Color col = curentColor;
                String content = tf2.getText();
                if(fontSize > 150 || fontSize < 5 || fontSize == (int)text.getFont().getSize()){//Testen ob die inputs sinn machen und sich geändert haben
                    fontSize = (int)text.getFont().getSize();
                } 
                if(content == ""){ //Neuen Text?
                    content = text.getText();
                }
                if(cb.getValue() != null){ //Hat der Benutzer ne farbe ausgewählt?
                    if(cb.getValue().toString() == "SCHWARZ"){
                        col = Color.BLACK;
                    }else if(cb.getValue().toString() == "GRÜN"){
                        col = Color.GREEN;
                    }else if(cb.getValue().toString() == "BLAU"){
                        col = Color.BLUE;
                    }else if(cb.getValue().toString() == "ORANGE"){
                        col = Color.ORANGE;
                    }else if(cb.getValue().toString() == "ROT"){
                        col = Color.RED;
                    }else if(cb.getValue().toString() == "BRAUN"){
                        col = Color.BROWN;
                    }else if(cb.getValue().toString() == "GRAU"){
                        col = Color.GRAY;
                    }
                }
                DigitSimController.getReference().rebuildElement_TEXT(thisEl, fontSize, content, col);
                stage.close();
            }         
        });
       Scene scene = new Scene(new Group(lbl, lbl2, tf, tf2, btn, lbl3, cb));
       stage.setScene(scene);
       stage.show();//Fenster zeigen
    }
    }
       
    
    
    
    
    
    

