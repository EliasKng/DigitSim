/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;



import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * FXML Controller class
 *
 * @author Elias
 */
public class DigitSimController extends Pane{
    
    @FXML
    public void initialize() {
        System.out.println("Gridadding");
        addGrid();
    }


    
    @FXML
    private MenuItem mItemOpenFile;
    
    @FXML
    private ListView listview;
    
    @FXML
    private Canvas simCanvas;
    
    public void mItemOpenFileAction(ActionEvent event) {
        
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DigitSimFiles (*.dgs)", "*.dgs");
        fc.getExtensionFilters().add(extFilter);
        File selectedFile = fc.showOpenDialog(null);
        
    }
    
    /**
    * Animiert Karo auf simCanvas
    *
    * @author Elias
    */
    public void addGrid() {

        double w = simCanvas.getWidth();
        double h = simCanvas.getHeight();


        simCanvas.setMouseTransparent(true);

        GraphicsContext gc = simCanvas.getGraphicsContext2D();

        gc.setStroke(Color.DARKSEAGREEN);
        gc.setLineWidth(0.7);

        // Karomuster malen
        double offset = 21;
        for( double i=offset; i < w; i+=offset) {
            gc.strokeLine( i, 0, i, h);
            gc.strokeLine( 0, i, w, i);
        }

        getChildren().add( simCanvas);

        simCanvas.toBack();
    }
    
}