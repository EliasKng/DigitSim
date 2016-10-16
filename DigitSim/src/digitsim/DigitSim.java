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
import javafx.stage.Stage;


/**
 *
 * @author Elias
 */
public class DigitSim extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try{ //Beschreibung des Designs per XML-Datei
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DigitSim.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Seminator");
            stage.setScene(new Scene(root1));  
            stage.show();
            stage.setMinWidth(800);
            stage.setMinHeight(600);
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
