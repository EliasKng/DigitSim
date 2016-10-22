/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author Johanna
 */
public class GenFunctions {
    /**
     * 
     */
    public static Stage openFXML(String fxmlName, String windowTitle, String iconFileName) {
        try{ //Beschreibung des Designs per XML-Datei
            FXMLLoader fxmlLoader = new FXMLLoader(DigitSim.class.getResource(fxmlName));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle(windowTitle);
            stage.setScene(new Scene(root1));  
            stage.getIcons().add(new Image(DigitSim.class.getResourceAsStream( iconFileName )));
            
            stage.show();
           
            return stage;
            
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
