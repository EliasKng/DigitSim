package digitsim;

import java.awt.MouseInfo;
import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
/**
 * Digitsim.fxml Controller class
 *
 * @author Elias
 */
public class DigitSimController extends Pane{
    public DigitSimController() {
    }
    
    
    @FXML
    public void initialize() {//initialize Funktion: wird direkt beim Starten der FXML aufgerufen.
        GraphicsContext gc = simCanvas.getGraphicsContext2D();
        Draw.addGrid(simCanvas,gc);
        AND and0 = new AND(gc);
        loadBtnGroup();
        MouseEvent event;
        
        
       
    }
    
    
    
    /**
     * FXML OBJEKT-Erstellungs-Bereich:
     * Jedes Element, welches in der DigitSim.FXML verwendet wird, muss in folgendem wege im Code noch erstellt werden.
     */
    @FXML 
    private MenuItem mItemOpenFile;
    @FXML 
    private Canvas simCanvas;
    @FXML 
    private ToggleButton btnAND;    
    @FXML
    private ToggleButton btnOR;    
    @FXML
    private ToggleButton btnNOT;
    @FXML
    private ToggleButton btnNOR;    
    @FXML
    private ToggleButton btnXOR;    
    @FXML
    private ToggleButton btnNAND;
    
    
    
    

    /**
    * Bildet nötige Gruppen für Togglebuttons (damit immer nur einer Selected sein kann)
    * 
    * @author Elias
    * 
    */
    public void loadBtnGroup() {
        ToggleGroup group = new ToggleGroup();
        btnAND.setToggleGroup(group);
        btnOR.setToggleGroup(group);
        btnNOT.setToggleGroup(group);
        btnNAND.setToggleGroup(group);
        btnNOR.setToggleGroup(group);
        btnXOR.setToggleGroup(group);
        
    }
    //*************************************ON ACTION bereich: wird verwendet um z.B. Buttonclicks auszuwerten***********************************************
    /**
    * Öffnet das Datei Öffnen DialogFenster
    * 
    * @author Elias
    */
    public void mItemOpenFileAction(ActionEvent event) {
        
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DigitSimFiles (*.dgs)", "*.dgs");
        fc.getExtensionFilters().add(extFilter);
        File selectedFile = fc.showOpenDialog(null);   
    }
    public void mItemPropertiesOnAction(ActionEvent event) {
        try{ //Beschreibung des Designs per XML-Datei
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Properties.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Properties");
            stage.setScene(new Scene(root1));  
            
            stage.show();
            stage.getIcons().add(new Image(DigitSim.class.getResourceAsStream( "icon.png" )));
            stage.setMinWidth(400);
            stage.setMinHeight(200);
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 
     * @param event 
     * @return Returnd die Koordinaten der Maus
     * 
     */
    public double[] getMouseCoordinates(MouseEvent event) {
        //Coords[0] = x coordinate
        //Coords[1] = y Coordinate
        double coords[] = new double[2];
        coords[0] = event.getX();
        coords[1] = event.getY();
        return coords;
    }
    
    
}
      