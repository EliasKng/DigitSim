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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author Elias
 */
public class DigitSimController extends Pane{
    
    @FXML
    public void initialize() {
        addGrid();
    }


    
    @FXML
    private MenuItem mItemOpenFile;
    

    
    @FXML
    private Canvas simCanvas;
    
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
                if(i % 5 == 0) mul = 2;
                else mul = 1;
                                
                gcDrawLine(gc, i, 0, i, h, mul);
                gcDrawLine(gc, 0, i, w, i, mul);
        }

        getChildren().add(simCanvas);

        simCanvas.toBack();
    }
    
}