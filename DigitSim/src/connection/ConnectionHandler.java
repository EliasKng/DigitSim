/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import digitsim.DigitSimController;
import element.Element;
import java.util.List;

/**
 *
 * @author Elias
 */
public class ConnectionHandler {
    
    /**
     * Aktualisiert alle Verbindungen die mit dem Element verbunden Sind
     * @param e 
     */
    public static void updateAllConnectionsRelatedToElement(Element e) {
        List<Connection> allConnections = DigitSimController.getReference().getAllConnections();
        
        for(Connection c : allConnections) {
            boolean isAlreadyUpdated = false;
            if(c.getStartPartner().getelement() == e) {
                isAlreadyUpdated = true;
                c.updateLine();
            } if((c.getEndPartner().getelement() == e) && !isAlreadyUpdated) {
                c.updateLine();
            }
        }
    }
    
    /**
     * Entfernt alle Linien, welche Mit diesem Element in Verbindung stehtn
     * @param e 
     */
    public static void removeAllConnectionsRelatedToElement(Element e) { 
        for(Connection c : DigitSimController.getReference().getAllConnections()) {
            boolean remove = false;
            if(c.getStartPartner().getelement() == e) 
                remove = true;
                
            if((c.getEndPartner().getelement() == e) && !remove) 
                remove = true;
            
            if(remove) {
                DigitSimController.getReference().removeConnectionFromAllConnections(c);    //entfernt die Linie aus der allConnections Liste
                c.removeLine(); //linie wird aus dem DraggableCanvas entfernt
                c = null;       //Objekt wird für dem Garbage-Collector freigegeben (wird gelöscht)
            }
        }
    }
    
    /**
     * Entfernt ALLE Verbindungen
     */
    public static void removeAllConnections() {
        for(Connection c : DigitSimController.getReference().getAllConnections()) {
            c.removeLine(); //linie wird aus dem DraggableCanvas entfernt
            c = null;       //Objekt wird für dem Garbage-Collector freigegeben (wird gelöscht)
        }
        DigitSimController.clearConnections();
    }
}
