/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author Elias
 */
public class DigitSimController extends Pane{
    
    public DigitSimController() {
    }
    
    @FXML
    public void initialize() {
        addGrid();
        GraphicsContext gc = simCanvas.getGraphicsContext2D();
        AND and0 = new AND(gc);
        loadBtnGroup();
    }
    
    

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
    
    public void mItemOpenFileAction(ActionEvent event) {
        
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DigitSimFiles (*.dgs)", "*.dgs");
        fc.getExtensionFilters().add(extFilter);
        File selectedFile = fc.showOpenDialog(null);
        
    }
    
    /**
    * Funktion zum Linien zeichnen
    *
    * @author Tim
    */
    public static void gcDrawLine(GraphicsContext gc, double x, double y, double w, double h, double mul)
    { 
        gc.setLineWidth(gc.getLineWidth()* mul);
        gc.strokeLine( x, y, w, h);
        gc.strokeLine( x, y, w, h);
        gc.setLineWidth(gc.getLineWidth()/ mul);
    }
    
    /**
    * Animiert Karo auf simCanvas
    *
    * @author Elias
    * Bearbeitet von Tim 16.10.16
    */
    public void addGrid() {

        double w = simCanvas.getWidth();
        double h = simCanvas.getHeight();


        simCanvas.setMouseTransparent(false);

        GraphicsContext gc = simCanvas.getGraphicsContext2D();
        
        gc.setStroke(Color.LIGHTGREY);
        gc.setLineWidth(1.0);

        // Karomuster malen
        // offset = linien abstand
        double offset = 21;
        double mul;
        for( double i=offset; i < w; i+=offset) {
                if(i % 5 == 0) mul = 2; //Jede 5. Linie mit doppelter Dicke zeichnen
                else mul = 1;
                                
                gcDrawLine(gc, i, 0, i, h, mul);
                gcDrawLine(gc, 0, i, w, i, mul);
        }
        getChildren().add(simCanvas);
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
}