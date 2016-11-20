/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;
import java.lang.Thread;

/**
 *
 * @author Dominik
 */
public class SimThread extends Thread { //Ein Thread kann nebenbei laufen (auf einem andren Prozessorkern), sprich er blockiert nicht unser Hauptprogramm während er läuft und hat eine bessere performance

    private DigitSimController dsController;

    public SimThread(DigitSimController pDSController) { //Constructor
        dsController = pDSController; //Referenz auf den Controller, damit wir dessen Funktionen benutzen können
    }

    @Override
    public void run() { //Diese Funktion arbeitet der Thread ab
        while (!this.isInterrupted()) { //Man kann den Thread mit thread.interrupt() den befehl zum stoppen geben, hier wird geprüft ob dies passiert ist
            Long diff = System.currentTimeMillis(); //Die Aktuelle zeit in Millis auslesen
            dsController.run(); //Alle Elemente updaten etc.
            Math.abs(diff -= System.currentTimeMillis()); //Rausfinden wieviel Zeit vergangen ist (Differenz)
            try{
            this.wait(50 - clampDiff(diff)); //50ms Warten, Wir ziehen die Differenz ab, damit wir IMMER 50ms haben SPRICH: Unser Simulator läuft im Normalfall mit einer Frequenz von 20Hz
            }catch(Exception e){
                //Nichts tun! Es wird abgebrochen sobald die 50s vorbei sind.
            }
        }
    }
    
    public long clampDiff(long diff) {
        if(diff > 50){
            diff = 50;
        }
        return diff;
    }
}
