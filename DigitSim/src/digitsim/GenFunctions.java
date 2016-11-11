/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Elias
 */
public class GenFunctions { //Laden der GUI
    /**
     * 
     */
    public static Stage openFXML(String fxmlName, String windowTitle, String iconFileName) {
        try{ //Beschreibung des Designs/Fensters per (F)XML-Datei
            FXMLLoader fxmlLoader = new FXMLLoader(DigitSim.class.getResource(fxmlName)); //Lädt die FXML-Datei
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle(windowTitle);
            stage.setScene(new Scene(root1));  
            stage.getIcons().add(new Image(DigitSim.class.getResourceAsStream( iconFileName )));
            
            stage.show(); //Das Fenster sichtbar machen
           
            return stage; 
            
        } catch(Exception e) { //Error
            ErrorHandler.printError(GenFunctions.class, "Fehler beim laden von " + fxmlName + " & " + windowTitle + " & " + iconFileName);
        }
        return null;
    }
}