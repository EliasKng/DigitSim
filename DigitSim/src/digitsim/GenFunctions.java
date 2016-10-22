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
import javafx.application.Application;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
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