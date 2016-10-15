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
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author Elias
 */
public class DigitSimController{
    
    @FXML
    public void initialize() {
        Circle circle = new Circle(50,Color.BLUE);
        circle.relocate(20, 20);
        Rectangle rectangle = new Rectangle(100,100,Color.RED);
        rectangle.relocate(70,70);
        simPane.getChildren().addAll(circle,rectangle);
    }

    @FXML
    private Pane simPane;
    
    @FXML
    private MenuItem mItemOpenFile;
    
    @FXML
    private ListView listview;
    
    public void mItemOpenFileAction(ActionEvent event) {
        
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DigitSimFiles (*.dgs)", "*.dgs");
        fc.getExtensionFilters().add(extFilter);
        File selectedFile = fc.showOpenDialog(null);
        
    }
    
}