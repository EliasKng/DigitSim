/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Dominik
 */
public class ErrorHandler {
    
   //Globals
    private static final String dataName = "Error_Log.txt"; //Name der Error-Datei
    
   private ErrorHandler(){}//Es soll nicht möglich sein ein Objekt dieser Klasse anzulegen
    
   public static void printError(Object obj, String msg){ //einen Error in die Error_Log.txt Datei schreiben
		try {
			FileWriter fw = new FileWriter(dataName, true); //Datei anlegen und Reinschreiben
			BufferedWriter bw = new BufferedWriter(fw);
			String emsg = "(" + new Date().toString() + ")" + "[ERROR IN <" + obj.getClass() + ">] " + msg + "\n";
			bw.write(emsg);
			bw.flush();
			bw.close();
			fw.close();
		} catch (IOException e) { //Fehler, tritt meistens auf wenn die Datei offen ist
			JOptionPane.showMessageDialog(null,
				    "Bitte die " + dataName + " Datei schließen!",
				    "ERROR",
				    JOptionPane.ERROR_MESSAGE);
		}
		
                //Dem Benutzer über den Error informieren
		JOptionPane.showMessageDialog(null,
			    "Ein Fehler ist aufgetreten! Siehe " + dataName + "!",
			    "ERROR",
			    JOptionPane.ERROR_MESSAGE);
	}
	
	public static void clearLog(){ //Die Error_Log.txt Datei löschen (zum resetten)
		try {
			FileWriter fw = new FileWriter("Error_Log.txt", false);
			fw.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,
				    "Bitte die " + dataName + " Datei schließen!",
				    "ERROR",
				    JOptionPane.ERROR_MESSAGE);
		}
		
	}
}
