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
            updateConnectionState(c);
        }
        resetConnectionsUptatedSign();
    }
    
    public static void updateConnectionState(Connection c) {
        
    }
    
    public static void getAllConnectionsConnectedToHandler(Connection con) {
        List<Connection> toCheckList = new ArrayList();
        List<Connection> checkedList = new ArrayList();
        List<Connection> allConnections = DigitSimController.getAllConnections();
        List<ConnectionPartner> allConnectionPartnersOfTypeConnection = new ArrayList();
        toCheckList.add(con);
        
        for(Connection connection : allConnections) {
            if(connection.getStartPartner().getPartnerType() == PartnerType.CONNECTION) {
                allConnectionPartnersOfTypeConnection.add(connection.getStartPartner());
            }
            if(connection.getEndPartner().getPartnerType() == PartnerType.CONNECTION) {
                allConnectionPartnersOfTypeConnection.add(connection.getEndPartner());
            }
        }
        
        while(!toCheckList.isEmpty()) {
            Connection c = toCheckList.get(0);
            if(!c.isChecked()) {
                toCheckList.addAll(getAllConnectionsConnectedTo(c, allConnectionPartnersOfTypeConnection));
                c.setChecked(true);
            }
            checkedList.add(c);
            toCheckList.remove(c);
        }
        resetConnectionsUptatedSign();
        System.out.println("toCHeckLength: " +checkedList.size());
        
    }
    
    public static List<Connection> getAllConnectionsConnectedTo(Connection c, List<ConnectionPartner> allConnectionPartnersOfTypeConnection) {
        List<Connection> connectedTo = new ArrayList();
        
        
        for(AnchorPoint aP : c.getAnchorPoints()) {
            for(ConnectionPartner cP : allConnectionPartnersOfTypeConnection) {
                if(aP.hashCode() == cP.getAnchorPoint().hashCode()) {
                        Connection abc = getConnectionFromPartner(cP);
                        connectedTo.add(abc);
                        abc.setSpecialColor(Color.CORAL); 
                }
            }
        }
        
        return connectedTo;
    }
    
    public static Connection getConnectionFromPartner(ConnectionPartner cP) {
        for(Connection c : DigitSimController.getAllConnections()) {
            if(c.getStartPartner() == cP) {
                return c;
            }
            if(c.getEndPartner() == cP) {
                return c;
            }
        }
        return null;
    }
    
    /**
     * Setzt die updated boolean aller Connections auf false
     */
    public static void resetConnectionsUptatedSign() {
        for(Connection c : DigitSimController.getAllConnections()) {
            c.setUpdated(false);
            c.setAdded(false);
            c.setChecked(false);
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
