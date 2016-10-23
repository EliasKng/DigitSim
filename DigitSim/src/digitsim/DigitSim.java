/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;


/**
 *
 * @author Elias
 */
public class DigitSim extends Application {
    private static Stage stage;
    @Override
  
    public void start(Stage primaryStage) {
        stage = primaryStage;
        stage = GenFunctions.openFXML("DigitSim.fxml", "Seminator", "icon.png");
        stage.setMinWidth(800);
        stage.setMinHeight(600);  
    }
    
    public static double[] getWindowsSize()
    {      
        double d[] = new double[2];
        d[0] = stage.getWidth();
        d[1] = stage.getHeight();
        
        return d;
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
