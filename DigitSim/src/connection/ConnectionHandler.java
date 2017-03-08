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
import javafx.scene.paint.Color;

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
                c.updateConnectionLine();
            } if(c.getEndPartner() != null) {
                if((c.getEndPartner().getElement() == e) && !isAlreadyUpdated) {
                    c.updateConnectionLine();
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
     * entfernt eine Connection komplett
     * @param c 
     */
    public static void removeConnection(Connection c) {
        c.removeLine();
        DigitSimController.removeConnectionFromAllConnections(c);
        c = null;
    }
    
    /**
     * Aktualisiert alle Verbindungen & damit auch die Elemente
     */
    public static void updateConnectionStates() {
        List<Connection> allConnections = DigitSimController.getReference().getAllConnections();
        for(Connection c : allConnections) {
            if(!c.isUpdated()) {
                updateConnectionState(c);
            }
        }
        resetConnectionsUptatedSigns();
    }
    
    /**
     * Aktualisiert den State einer Connection
     * @param con 
     */
    public static void updateConnectionState(Connection con) {
        
        //Findet heraus, mit welchen Linien diese verbunden ist
        List<Connection> connectedToEachOther = getAllConnectionsConnectedTo(con);
        
        //Erstellt eine Liste der Ouput states
        List<State> outputStates = getOutputStatesFromConnections(connectedToEachOther);
        if(!doStatesContradict(outputStates)) {
            setConnectionStates(connectedToEachOther, outputStates.get(0));
        } else {
            setConnectionStates(connectedToEachOther, State.UNDEFINED);
        }
        
        for(Connection c : connectedToEachOther) {
            c.setUpdated(true);
        }
        
    }

    /**
     * überprüft, ob sich die verschiedenen States wiedersprechen 
     * @param states
     * @return 
     */
    public static boolean doStatesContradict(List<State> states) {
        State firstState = states.get(0);
        for(State s : states) {
            if(!(s == firstState)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Erstellt eine Liste aus States, von allen Outputs der Connections
     * @param connections
     * @return 
     */
    public static List<State> getOutputStatesFromConnections(List<Connection> connections) {
        List<State> outputStates = new ArrayList();
        
        for(Connection c : connections) {
            ConnectionPartner cP0 = c.getStartPartner();
            ConnectionPartner cP1 = c.getEndPartner();

            //Wenn ein Teil der Verbindung ein ELement und Output ist, dann...
            if((cP0.getPartnerType() == PartnerType.ELEMENT) && !(cP0.isIsInput())) {
                int stateCP0 = cP0.getElement().getOutput(cP0.getIndex());
                State s = HandleState.getState(stateCP0);
                outputStates.add(s);
            } if((cP1.getPartnerType() == PartnerType.ELEMENT) && !(cP1.isIsInput())) {
                int stateCP1 = cP1.getElement().getOutput(cP1.getIndex());
                State s = HandleState.getState(stateCP1);
                outputStates.add(s);
            }
        }
        return outputStates;
    }
    
    /**
     * Ermittelt alle Verbindungen, die mit dieser verbunden sind
     * @param con
     * @return 
     */
    public static List<Connection> getAllConnectionsConnectedTo(Connection con) {
        List<Connection> toCheckList = new ArrayList();
        List<Connection> checkedList = new ArrayList();
        toCheckList.add(con);
        con.setCheckedIfConnectedTo(true);
        
        while(!toCheckList.isEmpty()) {
            Connection c = toCheckList.get(0);
            if(!c.isChecked()) {
                toCheckList.addAll(getAllConnectionsConnectedToDirectly(c));
                c.setChecked(true);
            }
            checkedList.add(c);
            toCheckList.remove(c);
        }
        
        
        for(Connection c : DigitSimController.getAllConnections()) {
            c.setChecked(false);
            c.setCheckedIfConnectedTo(false);
        }
        
        return checkedList;
    }
    
    /**
     * Ermittelt die direkt verbundenen Verbindungen einer Connection
     * @param c
     * @return 
     */
    public static List<Connection> getAllConnectionsConnectedToDirectly(Connection c) {
        List<Connection> connectedTo = new ArrayList();
        
        for(AnchorPoint aP : c.getAnchorPoints()) {
            for(Connection con : aP.getConnectedTo()) {
                if(!con.isCheckedIfConnectedTo()) {
                    connectedTo.add(con);
                    con.setCheckedIfConnectedTo(true);
                }
            }
        }
        ConnectionPartner sP = c.getStartPartner();
        ConnectionPartner eP = c.getEndPartner();
        if(sP.getPartnerType() == PartnerType.CONNECTION) {
            for(Connection con : sP.getAnchorPoint().getConnectedTo()) {
                if(!con.isCheckedIfConnectedTo()) {
                    connectedTo.add(con);
                    con.setCheckedIfConnectedTo(true);
                }
            }
        }  if(eP.getPartnerType() == PartnerType.CONNECTION) {
            for(Connection con : eP.getAnchorPoint().getConnectedTo()) {
                if(!con.isCheckedIfConnectedTo()) {
                    connectedTo.add(con);
                    con.setCheckedIfConnectedTo(true);
                }
            }
        }
        return connectedTo;
    }
    
    /**
     * Setzt die updated boolean aller Connections auf false
     */
    public static void resetConnectionsUptatedSigns() {
        for(Connection c : DigitSimController.getAllConnections()) {
            c.setUpdated(false);
            c.setChecked(false);
            c.setCheckedIfConnectedTo(false);
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
     * Setzt den State von mehreren Connections
     * @param connections
     * @param s 
     */
    public static void setConnectionStates(List<Connection> connections, State s) {
        for(Connection c : connections) {
            setConnectionState(s, c);
        }
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
    
    public static void hideConnectionsRelatedToElement(Element e) {
        ArrayList<Connection> connections = getAllConnectionsRelatedToElement(e);
        
        for(Connection c : connections) {
            c.removeLine();
        }
    }
    
    public static void resetConnection(Connection c) {
        c.resetConnecion();
    }
}
