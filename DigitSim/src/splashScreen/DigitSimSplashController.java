/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splashScreen;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.Initializable;

/**
 * FXML Controller class
 *
 * @author Elias
 */
public class DigitSimSplashController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        new SplashScreen().start();
    }    
    
    class SplashScreen extends Thread {
        @Override
        public void run() {
            try {
                Platform.runLater(digitsim.DigitSim::loadMainWindow);    
                
                Thread.sleep(5000);
                         
                Platform.runLater(digitsim.DigitSim::hideSplashScreen);
                Platform.runLater(digitsim.DigitSim::showMainWindow);
            } catch (InterruptedException ex) {
                Logger.getLogger(DigitSimSplashController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
