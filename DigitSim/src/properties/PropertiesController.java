package properties;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import digitsim.DigitSimController;
import general.Properties;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Elias
 *      -14.12.16 gui zusammengesetzt (lukas) 
 *      -17.12.16 einstellungen werden jetzt geändert (lukas)
 * 
 * Vor dem hochpushen muss die propertiesclass gelöscht werden!
 * 
 * TODO: grenzwerte einfügen 
 * TODO: direktes übernehmen der änderungen
 * IDEA: evt. andere/mehr farben oder mit Colorpicker arbeiten? soll sich das fenster nach dem speichern sich schließen? 
 */
public class PropertiesController implements Initializable { //Klasse für das Einstellungs-Fenster

    /**
     * Initializes the controller class.
     */
        //GUI-Elements
    @FXML
    private TextField txtClock;
    @FXML
    private TextField txtHeight;
    @FXML
    private TextField txtWidth;
    @FXML
    private TextField txtCanvasHeight;
    @FXML
    private TextField txtCanvasWidth;
    @FXML
    private ChoiceBox boxColor; //ChoiceBox lukas, nicht ComboBox!
    @FXML
    private CheckBox checkClose;
    @FXML
    private Button btnReset;
    @FXML
    private Button btnSave;
    
     //Constructor
    public PropertiesController() { 
    }
    public int getColor(){   //checkt die aktuelle gridfarbe 
            int  index = 0;   
          
            if(Properties.GetGridColor().equals(Color.LIGHTBLUE)) {           //TIL to use .equals, bc == checks for same instance
            index = 0;          
            }
            if(Properties.GetGridColor().equals(Color.LIGHTGREEN)) {
            index = 1;          
            }
            if(Properties.GetGridColor().equals(Color.LIGHTCORAL)) {
            index = 2;          
            }
            if(Properties.GetGridColor().equals(Color.LIGHTGREY)) {
            index = 3;          
            }
            if(Properties.GetGridColor().equals(Color.BLACK)) {
            index = 4; 
            }
            return index;
        }
    
     public void setColor(){
            String color = boxColor.getValue().toString();
            boolean change = false;
            if (color.equals("Blau")){
             Properties.setGridColor(Color.LIGHTBLUE);
             change = true;
            }
            else if (color.equals("Grün")){
             Properties.setGridColor(Color.LIGHTGREEN);
             change = true;
            }
            else if (color.equals("Rot")){
             Properties.setGridColor(Color.LIGHTCORAL);
             change = true;
            }
            else if (color.equals("Grau")){
             Properties.setGridColor(Color.LIGHTGREY);
             change = true;
            }
            else if (color.equals("Schwarz")){
             Properties.setGridColor(Color.BLACK);
             change = true;
            }
            if(change){
                DigitSimController.getReference().reloadGridColor();
            }
        } 
    
     public void reset() {                          //Zurücksetzen auf anfangswerte
         Properties.reset();
         boxColor.getSelectionModel().select(getColor());
        
         //Window  
       txtWidth.setText(Integer.toString(Properties.GetWindowMinX()));
       txtHeight.setText(Integer.toString(Properties.GetWindowMinY()));
         
       //Canvas
       txtCanvasWidth.setText(Integer.toString(Properties.GetSimSizeX()));
       txtCanvasHeight.setText(Integer.toString(Properties.GetSimSizeY())); 
       
       //Thread
       txtClock.setText(Integer.toString(Properties.GetThreadDurationMS()));
       
       //Tile-Code
       checkClose.setSelected(Properties.getVisualizeTileCode());
    }
     
     public void save() {
         if(Integer.parseInt(txtWidth.getText()) >= 600 && Integer.parseInt(txtWidth.getText()) >= 600){
            Properties.setWindowMinX(Integer.parseInt(txtWidth.getText()));
            Properties.setWindowMinY(Integer.parseInt(txtHeight.getText()));
            DigitSimController.getReference().reloadMinAndMaxWindowSize();
        }else{
            txtWidth.setText(Integer.toString(Properties.GetWindowMinX()));
            txtHeight.setText(Integer.toString(Properties.GetWindowMinY()));
            JOptionPane.showMessageDialog(null,
			    "Angegebene Fenster Größe ist zu klein! (Min. 600 in Breite/Höhe)",
			    "Info",
			    JOptionPane.INFORMATION_MESSAGE);
         }
  
         if(Integer.parseInt(txtCanvasWidth.getText()) >= 1000 && Integer.parseInt(txtCanvasWidth.getText()) >= 1000){
             if(Integer.parseInt(txtCanvasWidth.getText()) != Properties.GetSimSizeX() && Integer.parseInt(txtCanvasWidth.getText()) != Properties.GetSimSizeY()){
            Properties.setSimSizeX(Integer.parseInt(txtCanvasWidth.getText()));
            Properties.setSimSizeY(Integer.parseInt(txtCanvasHeight.getText()));
            txtCanvasWidth.setText(Integer.toString(Properties.GetSimSizeX()));
            txtCanvasHeight.setText(Integer.toString(Properties.GetSimSizeY())); 
            JOptionPane.showMessageDialog(null,
			    "Die neue Arbeitsfläche wird erst bei einem neuen Projekt sichtbar!",
			    "Info",
			    JOptionPane.INFORMATION_MESSAGE);
             }
         }else{
            txtCanvasWidth.setText(Integer.toString(Properties.GetSimSizeX()));
            txtCanvasHeight.setText(Integer.toString(Properties.GetSimSizeY())); 
            JOptionPane.showMessageDialog(null,
			    "Angegebene Fläche der Arbeitsfläche ist zu klein! (Min. 1000 in X/Y-Richtung",
			    "Info",
			    JOptionPane.INFORMATION_MESSAGE);
         }

        if(Integer.parseInt(txtClock.getText()) <= 50){
            Properties.setThreadDurationMS(Integer.parseInt(txtClock.getText()));
        }else{
            txtClock.setText(Integer.toString(Properties.GetThreadDurationMS()));
                        JOptionPane.showMessageDialog(null,
			    "Angebener Takt ist zu hoch! (Max. 50Hz und Min. 1Hz)",
			    "Info",
			    JOptionPane.INFORMATION_MESSAGE);
        }
       Properties.setAskOnExit(checkClose.isSelected());
       Properties.save();
       Stage stage = (Stage) boxColor.getScene().getWindow();
       stage.close(); //Fenster schließen nach dem Speichern
       
        }
     
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        //Colorpick
        boxColor.getItems().addAll(FXCollections.observableArrayList(
                "Blau",
                "Grün",
                "Rot",
                "Grau",
                "Schwarz"
            ));
         boxColor.getSelectionModel().select(getColor()); //Handler entfern -> Einstellungen sollen erst bei "Speichern" übernommen werden
         
       //Window  
       txtWidth.setText(Integer.toString(Properties.GetWindowMinX()));
       txtHeight.setText(Integer.toString(Properties.GetWindowMinY()));
         
       //Canvas
       txtCanvasWidth.setText(Integer.toString(Properties.GetSimSizeX()));
       txtCanvasHeight.setText(Integer.toString(Properties.GetSimSizeY())); 
       
       //Thread
       txtClock.setText(Integer.toString(Properties.GetThreadDurationMS()));
       
       //Tile-Code
       checkClose.setSelected(Properties.isAskOnExit());
       
       //Save
       btnSave.setOnAction(e -> save());
       
       //Reset
       btnReset.setOnAction(e -> reset());
       
    }    
    
}
