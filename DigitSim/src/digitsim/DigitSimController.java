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

/**
 * FXML Controller class
 *
 * @author Elias
 */
public class DigitSimController{
    
    @FXML
    private MenuItem mItemOpenFile;
    
    @FXML
    private ListView listview;
    
    public void mItemOpenFileAction(ActionEvent event) {
        
        FileChooser fc = new FileChooser();
        File selectedFile = fc.showOpenDialog(null);
        
        if (selectedFile != null) {
            listview.getItems().add(selectedFile.getName());
        } else {
            System.out.println("Datei ist nicht vorhanden!");
        }
    }
}