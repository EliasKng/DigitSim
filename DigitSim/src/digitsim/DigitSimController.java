package digitsim;

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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
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
        addGrid(simCanvas,gc);
        AND and0 = new AND(gc);
        loadBtnGroup();
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
    * Animiert Karo auf simCanvas
    *
    * @author Elias
    * Bearbeitet von Tim 16.10.16
    */
    public void addGrid(Canvas simCanvas, GraphicsContext gc) {

        double w = simCanvas.getWidth();
        double h = simCanvas.getHeight();


        simCanvas.setMouseTransparent(false);

        gc = simCanvas.getGraphicsContext2D();
        

        // Karomuster malen
        // offset = linien abstand
        double offset = 21;
        double d;
        for( double i=offset; i < w; i+=offset) {
                if(i % 4 == 0) d = 2; //Jede 5. Linie mit doppelter Dicke zeichnen
                else d = 1;
                                
                Draw.gcDrawLine(gc, i, 0, i, h, d, Color.LIGHTGREY);
                Draw.gcDrawLine(gc, 0, i, w, i, d, Color.LIGHTGREY);
        }
       //Setzt simCanvas in den Hintergrund
        simCanvas.toBack();
    }

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
}