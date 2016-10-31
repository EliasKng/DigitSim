/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


/**
 *
 * @author Elias
 */
public class DigitSim extends Application {
    private static Stage stage;
    private static Stage loadingStage;
    @Override
    public void start(Stage primaryStage) {
        
        stage = primaryStage;
        openMainWindow();
          
    }
    
    public static double[] getWindowsSize()
    {      
        double d[] = new double[2];
        d[0] = stage.getWidth();
        d[1] = stage.getHeight();
        
        return d;
    }
    
    /**
     * Öffnet das Hauptfenster (den Seminator)
     */
    public static void openMainWindow() {
        stage = GenFunctions.openFXML("DigitSim.fxml", "Seminator", "icon.png");
        stage.setMinWidth(800);
        stage.setMinHeight(600);
    }
    
    /**
     * GEHT NOCH NICHT!
     * @author Elias
     */
    public static void openLoadingWindow() {
        loadingStage.initStyle(StageStyle.UNDECORATED);
        Group root = new Group();
        Scene scene = new Scene(root, 100, 100);

        loadingStage.setScene(scene);
        loadingStage.show();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
