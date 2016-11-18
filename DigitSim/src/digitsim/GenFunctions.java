/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author Elias
 */
public class GenFunctions { //Laden der GUI
    /**
     * 
     */
    public static Stage openFXML(String fxmlName, String windowTitle, String iconFileName) {
        try{ //Beschreibung des Designs/Fensters per (F)XML-Datei
            FXMLLoader fxmlLoader = new FXMLLoader(DigitSim.class.getResource(fxmlName)); //Lädt die FXML-Datei
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle(windowTitle);
            stage.setScene(new Scene(root1));  
            stage.getIcons().add(new Image(DigitSim.class.getResourceAsStream( iconFileName )));
            
            stage.show(); //Das Fenster sichtbar machen
           
            return stage; 
            
        } catch(Exception e) { //Error
            ErrorHandler.printError(GenFunctions.class, "Fehler beim laden von " + fxmlName + " & " + windowTitle + " & " + iconFileName);
        }
        return null;
    }
    
    /**
     * Zeigt das Eigenschaftenfenster von Elementen
     */
    public static void showProperties(int numInputs, Element thisElement){ //Zeigt das "Eigenschaften"-Fenster für dieses Element
        Stage stage = new Stage(StageStyle.DECORATED); //Ein Fenster für die Eigenschaften erstellen und Anzeigen
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.alwaysOnTopProperty();
        stage.getIcons().add(new Image(DigitSim.class.getResourceAsStream( "icon.png" )));
        stage.setResizable(false);
        stage.setMinWidth(140);
        stage.setMinHeight(100);
        stage.setTitle("Eigenschaften"); //titel
        Label lbl = new Label("Inputs:");
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
        tf.setText(String.valueOf(numInputs));
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
                int pInputs = Integer.parseInt(tf.getText().trim());//String zu Integer
                if(pInputs < 9 && pInputs > 1 && pInputs != numInputs){//Testen ob die inputs sinn machen und sich geändert haben
                    DigitSimController.rebuildElement(thisElement, pInputs);//Element ändern
                }else{
                    tf.setText(String.valueOf(numInputs));//zurücksetzen
                } 
                stage.close();
            }         
        });
       Scene scene = new Scene(new Group(lbl, tf, btn));
       stage.setScene(scene);
       stage.show();//Fenster zeigen
    }
}