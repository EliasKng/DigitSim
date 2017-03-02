/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import digitsim.DigitSimController;
import element.Element;
import java.util.ArrayList;
import java.util.Iterator;
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
            if(c.getStartPartner().getElement() == e) {
                isAlreadyUpdated = true;
                c.updateLine();
            } if(c.getEndPartner() != null) {
                if((c.getEndPartner().getElement() == e) && !isAlreadyUpdated) {
                    c.updateLine();
                }
            }
        }
    }
    
    /**
     * Entfernt alle Linien, welche Mit diesem Element in Verbindung stehtn
     * @param e 
     */
    public static void removeAllConnectionsRelatedToElement(Element e) { 
        Iterator<Connection> c = DigitSimController.getAllConnections().iterator();
        
        while(c.hasNext()) {
            Connection con = c.next();
            
            if((con.getStartPartner().getElement() == e) || (con.getEndPartner().getElement() == e)) {
                con.removeLine();  
                c.remove();
            }
        }
    }
    
    /**
     * Entfernt ALLE Verbindungen
     */
    public static void removeAllConnections() {
        for(Connection c : DigitSimController.getAllConnections()) {
            c.removeLine(); //linie wird aus dem DraggableCanvas entfernt
            c = null;       //Objekt wird für dem Garbage-Collector freigegeben (wird gelöscht)
        }
        DigitSimController.clearConnections();
    }
    
    /**
     * Aktualisiert alle Verbindungen & damit auch die Elemente
     */
    public static void updateConnectionStates() {
        List<Connection> allConnections = DigitSimController.getReference().getAllConnections();
        for(Connection c : allConnections) {
            updateConnectionState(c);
        }
    }
    
    public static void updateConnectionState(Connection c) {
        ConnectionPartner cP0 = c.getStartPartner();
        ConnectionPartner cP1 = c.getEndPartner();


        int stateCP0 = -1;
        int stateCP1 = -1;
        
        //Wenn ein Teil der Verbindung ein ELement und Output ist, dann...
        if((cP0.getPartnerType() == PartnerType.ELEMENT) && !(cP0.isIsInput())) {
            stateCP0 = cP0.getElement().getOutput(cP0.getIndex());
        } if((cP1.getPartnerType() == PartnerType.ELEMENT) && !(cP1.isIsInput())) {
            stateCP1 = cP1.getElement().getOutput(cP1.getIndex());
        } //HIER MUSS SPÄTER NOCH DIE LOGIK FÜR VERBINDUNGEN ZU ANDEREN CONNECTIONS BESCHRIEBEN WERDEN
        
        if(stateCP0 != -1 && stateCP1 != -1) {  //Sowohl partner 1 als auch partner 2 ist ein Ouput!
            if(stateCP0 != stateCP1) {          //Die beiden Outputs sind nicht gleich -> undefiniert!
                setConnectionState(State.UNDEFINED, c);
            } else if((stateCP0 == 0) && (stateCP1 == 0)) {     //Die beiden outputs sind gleich & null
                setConnectionState(State.LOW, c);
            } else {                                            //Die beiden outputs sind gleich & eins
                setConnectionState(State.HIGH, c);
            }
        } else {
            switch(stateCP0) {
                case -1:
                    break;

                case 0:
                    setConnectionState(State.LOW, c);
                    break;

                case 1:
                    setConnectionState(State.HIGH, c);
                    break;

                default:
                    break;
            }
            switch(stateCP1) {
                case -1:
                    break;

                case 0:
                    setConnectionState(State.LOW, c);
                    break;

                case 1:
                    setConnectionState(State.HIGH, c);
                    break;

                default:
                    break;
            }
        }
    }
    
    /**
     * Setzt die States ALLER Connections zurück (auf DEFAULT)
     */
    public static void resetConnectionStates() {
        for(Connection c : DigitSimController.getReference().getAllConnections()) {
            resetConnectionState(c);
        }
    }
    
    /**
     * Setzt den state einer bestimmten Connection zurück
     * @param c 
     */
    public static void resetConnectionState(Connection c) {
        c.resetState();
    }
    
    /**
     * setzt den "state" einer bestimmten Connection   
     * @param cS state
     * @param c  connection
     */
    public static void setConnectionState(State cS, Connection c) {
        c.setState(cS);
    }
    
    /**
     * @author Tim
     * provisorisch erstmal hier, da ich keinen geeigneen platz gefunde habe
     * ermittelt das Element aus seinem Index
     * @param index index deselements
     * @return element mit diesem index
     */
    public static Element getElementByIndex(int index) {
        if(index <= DigitSimController.getReference().getElements().size())
            return DigitSimController.getReference().getElements().get(index);
        else
            return null;
    }
    
    /**
     * ermittelt den Index eines Elements
     * @param e Element
     * @return  Index
     */
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
     * gibt eine Liste von Verbindungen zurück, welche mit diesem einen Element in Verbindung stehen
     * @param e
     * @return 
     */
    public static ArrayList<Connection> getAllConnectionsRelatedToElement(Element e) {
       ArrayList<Connection> connections = new ArrayList();
       
       Iterator<Connection> c = DigitSimController.getAllConnections().iterator();
        
        while(c.hasNext()) {
            Connection con = c.next();
            
            if((con.getStartPartner().getElement() == e) || (con.getEndPartner().getElement() == e)) {
                connections.add(con);
            }
        }
        return connections;
    }
    
    public static void drawDirectPreLinesRelatedToElement(Element e) {
        ArrayList<Connection> connections = getAllConnectionsRelatedToElement(e);
        
        for(Connection c : connections) {
            c.drawDirectPreLine();
        }
    }
}
