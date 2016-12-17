package properties;
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import general.Properties;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

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
    private ComboBox boxColor;
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
            
            if (color.equals("Blau")){
             Properties.setGridColor(Color.LIGHTBLUE);
            }
            else if (color.equals("Grün")){
             Properties.setGridColor(Color.LIGHTGREEN);
            }
            else if (color.equals("Rot")){
             Properties.setGridColor(Color.LIGHTCORAL);
            }
            else if (color.equals("Grau")){
             Properties.setGridColor(Color.LIGHTGREY);
            }
            else if (color.equals("Schwarz")){
             Properties.setGridColor(Color.BLACK);
            }
        } 
    
     public void reset() {                          //Zurücksetzen auf anfangswerte
         Properties.setGridColor(Color.LIGHTGREY);
         Properties.setWindowMinX(800);
         Properties.setWindowMinY(600);
         Properties.setSimSizeX(4000);
         Properties.setSimSizeY(4000);
         Properties.setThreadDurationMS(50);
         Properties.setVisualizeTileCode(false);
         
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
      //  Properties.setGridColor(setColor());
        Properties.setWindowMinX(Integer.parseInt(txtWidth.getText()));
        Properties.setWindowMinY(Integer.parseInt(txtHeight.getText()));
        Properties.setSimSizeX(Integer.parseInt(txtCanvasWidth.getText()));
        Properties.setSimSizeY(Integer.parseInt(txtCanvasHeight.getText()));
        Properties.setVisualizeTileCode(checkClose.isSelected());
        Properties.setThreadDurationMS(Integer.parseInt(txtClock.getText()));
        setColor();
        
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
         boxColor.getSelectionModel().select(getColor());
         boxColor.setOnAction(e -> setColor()); //EventHandler 
         
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
       
       //Save
       btnSave.setOnAction(e -> save());
       
       //Reset
       btnReset.setOnAction(e -> reset());
       
    }    
    
}
