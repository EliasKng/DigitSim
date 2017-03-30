/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;
import general.Properties;
import java.lang.Thread;

/**
 *
 * @author Dominik
 */
public class SimThread extends Thread { //Ein Thread kann nebenbei laufen (auf einem andren Prozessorkern), sprich er blockiert nicht unser Hauptprogramm während er läuft und hat eine bessere performance

    private int rounds = 0;
    private boolean sendMessage = false;
    private DigitSimController dsController;

    public SimThread(DigitSimController pDSController) { //Constructor
        dsController = pDSController; //Referenz auf den Controller, damit wir dessen Funktionen benutzen können
    }

    @Override
    public void run() { //Diese Funktion arbeitet der Thread ab
        int duration = 1000 / Properties.GetThreadDurationMS();
        while (!this.isInterrupted() && !dsController.intThread) { //Man kann den Thread mit thread.interrupt() den befehl zum stoppen geben, hier wird geprüft ob dies passiert ist
            Long diff = System.currentTimeMillis(); //Die Aktuelle zeit in Millis auslesen
            dsController.run(); //Alle Elemente updaten etc.
            diff = Math.abs((diff -= System.currentTimeMillis())); //Rausfinden wieviel Zeit vergangen ist (Differenz)
            try{
                long tmp = duration - diff;        
                if(tmp > 0){
                   System.out.println(tmp);
                   Thread.sleep(tmp); //50ms Warten, Wir ziehen die Differenz ab, damit wir IMMER 50ms haben SPRICH: Unser Simulator läuft im Normalfall mit einer Frequenz von 20Hz 
                   if(rounds > 0)
                       rounds--;
                }else if(tmp < 0){
                    rounds += 2;
                }
            }catch(Exception e){
                //Nichts tun! Es wird abgebrochen sobald die 50s vorbei sind.
            }
            if(rounds > 10 && !sendMessage){
                sendMessage = true;
                dsController.outputMessage("[WARNING]Takt ist zu hoch, bitte runterstellen");
            }
        }
    }
}
