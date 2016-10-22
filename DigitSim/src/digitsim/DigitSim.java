/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
 *
 * @author Elias
 */
public class DigitSim extends Application {
    @Override
    public void start(Stage primaryStage) {
        Stage stage;
        stage = GenFunctions.openFXML("DigitSim.fxml", "Seminator", "icon.png");
        stage.setMinWidth(800);
        stage.setMinHeight(600);       
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
