package digitsim;

import general.Properties;
import toolbox.GenFunctions;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javax.swing.JOptionPane;


/**
 *
 * @author Elias
 */
public class DigitSim extends Application { //Hauptklasse
    private static Stage splashScreen; //Ladefenster
    private static Stage stage; //Hauptfenster
    @Override
    public void start(Stage primaryStage) throws InterruptedException { //Wird nach dem launch ausgeführt
        loadSplashScreen();
        splashScreen.show();
        stage = primaryStage;
          
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
    public static void loadMainWindow() {
        stage = GenFunctions.openFXML(DigitSim.class, "DigitSim.fxml", "Seminator", "icon.png", StageStyle.DECORATED);
        stage.setMinWidth(Properties.GetWindowMinX());
        stage.setMinHeight(Properties.GetWindowMinY());
        stage.show();
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
    
    public static void loadSplashScreen() {
        splashScreen = GenFunctions.openFXML(splashScreen.DigitSimSplashController.class, "DigitSimSplash.fxml", "SplashScreen", "icon.png", StageStyle.UNDECORATED);
    }
    
    public static void hideSplashScreen() {
        splashScreen.hide();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) { //Hier startet das Programm
        Properties.load(); //Einstellungen laden
        launch(args);
    }
    
}
