/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import digitsim.DigitSimController;
import element.Element;
import java.util.List;
import digitsim.*;

/**
 *
 * @author Elias
 */
public class ConnectionHandler {
    
    /**
     * @author Tim
     * provisorisch erstmal hier, da ich keinen geeigneen platz gefunde habe
     * @param index index deselements
     * @return element mit diesem index
     */
    public static Element getElementByIndex(int index) {
        if(index <= DigitSimController.getReference().getElements().size())
            return DigitSimController.getReference().getElements().get(index);
        else
            return null;
    }
    
    public static int getElementIndex(Element e) {
        int size = DigitSimController.getReference().getElements().size();
        int result = -1;
        
        for(int i = 0; i < size; i++) {
            Element current = DigitSimController.getReference().getElements().get(i);
            
            if(current.hashCode() == e.hashCode()) {
                result = i;
            }
        }
        return result;
    }
    
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
    
    public static void updateConnectionsColor() {
        List<Connection> allConnections = DigitSimController.getReference().getAllConnections();
        for(Connection c : allConnections) {
            ConnectionPartner cP0 = c.getStartPartner();
            ConnectionPartner cP1 = c.getEndPartner();
            
            //Wenn ein Teil der Verbindung ein ELement und Output ist, dann...
            int stateCP0 = -1;
            int stateCP1 = -1;
            if((cP0.getPartnerType() == PartnerType.ELEMENT) && !(cP0.isIsInput())) {
                stateCP0 = cP0.getelement().getOutput(cP0.getIndex());
            } else if((cP1.getPartnerType() == PartnerType.ELEMENT) && !(cP1.isIsInput())) {
                stateCP1 = cP1.getelement().getOutput(cP1.getIndex());
            } //HIER MUSS SPÄTER NOCH DIE LOGIK FÜR VERBINDUNGEN ZU ANDEREN CONNECTIONS BESCHRIEBEN WERDEN
            
            if(stateCP0 != -1 && stateCP1 != -1) {  //Sowohl partner 1 als auch partner 2 ist ein Ouput!
                if(stateCP0 == stateCP1) {
                    
                }
            } else {
                
            }
        }
    }
}
