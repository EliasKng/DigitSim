/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;

import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Elias
 */
public class Simulation extends Thread {
    private boolean stop = true;
    public Connection allConnections; //Verbindungen zwischen Elementen werden hier gespeichert
    private static ArrayList<Element> elements = new ArrayList<>(); //Elemente

    public void setAllConnections(Connection allConnections) {
        this.allConnections = allConnections;
    }
   
    
    public void setStop(boolean stop) {
        this.stop = stop;
    }
    
    public Simulation() {
    }
 
    @Override
    public void run() {
        Simulation.elements = DigitSimController.getElements();
        this.allConnections = DigitSimController.getAllConnections();
        int i = 0;
        //while(!stop) {
            elements.forEach(e -> e.update()); //Geht alle Elemente durch und Updaten sie. ACHTUNG: Lambda schreibweise! Infos -> https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html
            allConnections.update();
            System.out.println(i);
            i++;
            
            // Thread schlafen
            try {
                // fuer 3 Sekunden
                sleep(TimeUnit.SECONDS.toMillis(1));
            } catch (InterruptedException ex) {
                Logger.getLogger(Simulation.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        //}
            
    }
}
