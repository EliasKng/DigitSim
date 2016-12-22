/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;

import general.Properties;
import toolbox.GenFunctions;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javax.swing.JOptionPane;


/**
 *
 * @author Elias
 */
public class DigitSim extends Application { //Hauptklasse
    private static Stage stage; //Hauptfenster
    private static Stage loadingStage; //Loading Screen
    @Override
    public void start(Stage primaryStage) { //Wird nach dem launch ausgeführt
        
        stage = primaryStage;
        openMainWindow();
          
    }
    
    public static double[] getWindowsSize() //Liefert die Fenstergröße in einem Array
    {      
        double d[] = new double[2];
        d[0] = stage.getWidth();
        d[1] = stage.getHeight();
        
        return d;
    }
    
    /**
     * Öffnet das Hauptfenster (den Seminator)
     */
    private static void openMainWindow() {
        stage = GenFunctions.openFXML(DigitSim.class, "DigitSim.fxml", "Seminator", "icon.png");
        stage.setMinWidth(Properties.GetWindowMinX());
        stage.setMinHeight(Properties.GetWindowMinY());
        
        stage.setOnCloseRequest(
            new EventHandler<WindowEvent>() {         
                @Override
                public void handle(WindowEvent event) {
                    if(Properties.isAskOnExit()){
                       int confirmed = JOptionPane.showConfirmDialog(null, "Willst du das Programm wirklich schließen?", "Exit", JOptionPane.YES_NO_OPTION);
                       if (confirmed == JOptionPane.YES_OPTION) {
                           DigitSimController.getReference().mItemCloseAction(null);
                       }
                    }else{
                        DigitSimController.getReference().mItemCloseAction(null);  
                    }
                    event.consume();
                }});
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
    public static void main(String[] args) { //Hier startet das Programm
        Properties.load(); //Einstellungen laden
        launch(args);
    }
    
}
