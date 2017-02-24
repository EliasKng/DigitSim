/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import digitsim.DigitSimController;
import element.Element;
import general.Properties;
import general.Vector2i;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import pathFinder.Node;
import pathFinder.PathFinder;
import toolbox.Draw;

/**
 *
 * @author Elias
 */
public class Connection {
    //Globals
    private List<AnchorPoint> anchorPoints = new ArrayList();   //alle Punkte durch die die Linie verläuft + Anfang & Ende
    private DigitSimController dsc;                             //Abbild vom DigitSimController
    private ConnectionPartner startPartner;                     //VerbindungsLinienPartner (start)
    private ConnectionPartner endPartner;                       //VerbindungsLinienPartner (ende)
    private boolean directLine = false;                         // wird die Linie direkt verlegt oder nicht
    private PathFinder pathFinder = new PathFinder();
    private State state = State.DEFAULT;                              //Für die Simulation relevant -> gibt den Digitalen Status der Verbindung an (An/Aus/Undefiniert)
    
    //Zum Zeichen relevante Globals
    private Group lineGroup = new Group();                                    //Gruppe von Linien (die die gesamte Linie darstellt)
    private Group pointGroup = new Group();                                   //Gruppe von Punkten (anchorPoints, durch die die Linie verlaufen muss)
    

    //Konstruktoren
    public Connection(Vector2i start, Vector2i end, DigitSimController dsc) {
        AnchorPoint startAP = new AnchorPoint(0, start);
        AnchorPoint endAP = new AnchorPoint(1, end);
        this.anchorPoints.add(startAP);
        this.anchorPoints.add(endAP);
        
        this.dsc = dsc;
    }
    
    public Connection(Vector2i start, DigitSimController dsc) {
        AnchorPoint startAP = new AnchorPoint(0, start);
        this.anchorPoints.add(startAP);
        this.dsc = dsc;
    }

    public Connection(DigitSimController dsc, Connection partnerConnection, AnchorPoint partnerAnchorPoint) {
        this.startPartner = new ConnectionPartner(partnerConnection, partnerAnchorPoint);
        this.dsc = dsc;
    }
    
    public Connection(DigitSimController dsc, Element element, boolean isInput, int index) {
        this.startPartner = new ConnectionPartner(element, isInput, index);
        this.dsc = dsc;
    }

    
    
    /**************************Methoden*******************************/
    
    /**
     * wenn der LineMouseFollower aktiv ist und auf den Linienpartner geklickt wird, soll diese Funktion aufgerufen werden
     * @param end Die Endkoordinate der Linie (nun bekannt)
     */
    public void finishLine(Vector2i end) { 
        dsc.removeTemporaryLine();
        AnchorPoint endAP = new AnchorPoint(1, end);
        this.anchorPoints.add(endAP);
        this.updateLine();
    }
    
    /**
     * wenn der LineMouseFollower aktiv ist und auf den Linienpartner geklickt wird, soll diese Funktion aufgerufen werden
     * @param element hier wird das Elemnt des verbindungsendes übergeben
     * @param isInput true: ist ein Iput false: is kein Input
     * @param index des IN-/Outputs
     */
    public void finishLine(Element element, boolean isInput, int index) { 
        dsc.removeTemporaryLine();
        this.endPartner = new ConnectionPartner(element, isInput, index);
        this.updateLine();
    }
    
    /**
     * wenn der LineMouseFollower aktiv ist und auf den Linienpartner geklickt wird, soll diese Funktion aufgerufen werden
     * @param connection hier wird die Verbindungslinie übergeben, mit welcher diese Verbindungslinie verbunden werden soll
     * @param anchorPoint hier wird der AnchorPoint übergeben (mit dem die Linie verbunden werden wird)
     */
    public void finishLine(Connection connection, AnchorPoint anchorPoint) { 
        dsc.removeTemporaryLine();
        this.endPartner = new ConnectionPartner(connection, anchorPoint);
        this.updateLine();
    }
    
    /**
     * Aktualisiert die Verbindungsfarbe nach der currentColor
     */
    public void updateColor() {
        Color color = HandleState.getColorFromState(this.state);
        
        
        
        
        for(javafx.scene.Node n : this.lineGroup.getChildren()){
            Line l = (Line) n;
            l.setStroke(color);
        }
        
        for(javafx.scene.Node n : this.pointGroup.getChildren()){
            Circle c = (Circle) n;
            c.setStroke(color);
            c.setFill(color);
        }
    }
    
    /**
     * Aktualisiert die komplette Linie (Farbe & Verlauf)!
     */
    public void updateLine() {
        removeLine();    //Entfernt die alte Linie
        
        resetAttributes(); //Setzt die Attribute zurück
        
        createConnection(); //erstellt die Verbindung (berechnen)
        
        drawConnection();   //malt die berechnete VErbindung
    }
    
    /**
     * Entfernt die Linie & AnchorPoints aus dem dsc
     */
    public void removeLine() {
        if((this.pointGroup != null) && (this.lineGroup != null)) {
            this.dsc.getSimCanvas().getChildren().remove(this.lineGroup);
            this.dsc.getSimCanvas().getChildren().remove(this.pointGroup);
        }
    }
    
    /**
     * Erstellt aus den Attributen die Linie und speichert sie in lineGroup und pointGroup
     */
    public void createConnection() {
        processPartners();
        createLineGroup();
        createPointGroup();
    }
    
    /**
     * "Malt" die aktuelle Linie auf den dsc
     */
    public void drawConnection() {
        this.dsc.getSimCanvas().getChildren().addAll(this.lineGroup, this.pointGroup);
    }
    
    /**
     * Erstellt Aus den beiden "Verbindungspartnern" Den start und das Ende der Linie (und speichert diese als AnchorPoint)
     */
    public void processPartners() {
        int indexStart = 0;
        int indexEnd;
        
        //Suche passenden Index für den Endpunkt -> wenn es sonst keine Punkte gibt, muss er 1 sein und sonst den höchsten Index der Liste haben
        if(anchorPoints.isEmpty()) {
            indexEnd = 1;
        } else {
            indexEnd = anchorPoints.size()-1;
        }
        
        Vector2i startCoords = processPartner(this.startPartner);
        Vector2i endCoords = processPartner(this.endPartner);
        AnchorPoint start = new AnchorPoint(indexStart, startCoords);
        AnchorPoint end = new AnchorPoint(indexEnd, endCoords);
        this.anchorPoints.add(start);
        this.anchorPoints.add(end);
    }
    
    /**
     * Kreiert Koordinaten aus einem partner
     * @param partner in dem Verbindungspartner (partner) befinden sich alle nötigen Informationen um Koordinaten zu bestimmen
     * @return 
     */
    public Vector2i processPartner(ConnectionPartner partner) {
        if(partner.getPartnerType() == PartnerType.CONNECTIONLINE) {    //es handelt sich um eine Verbindungslinie -> Koordinaten des AnchorPoints?
            Vector2i coords = new Vector2i(partner.getanchorPoint().getCoords().getX(),partner.getanchorPoint().getCoords().getY());//Koordinaten setzen sich aus der x&y koordinate des AnchorPoints zustanden
            return coords;
        }
        if(partner.getPartnerType() == PartnerType.ELEMENT) {   //Es handelt sich um ein Element -> In/Output?
            Element element = partner.getElement();
            Vector2i coords = new Vector2i();
            
            if(partner.isIsInput()) {   //Es handelt sich um einen Input (die Koordinate von einem Inputs wird hier bestimmt)
                coords.setX((int) element.getInputX(partner.getIndex()));
                coords.setY((int) element.getInputY(partner.getIndex()));
                return coords;
            } else {                    //Es handelt sich um einen Output (die Koordinate von einem Ouputs wird hier bestimmt)
                coords.setX((int) element.getOutputX(partner.getIndex()));
                coords.setY((int) element.getOutputY(partner.getIndex()));
                return coords;
            }
        }
        System.out.println("Unbekannte Art von Verbindungspartnerart!"); //wenn man hier angelangt, dann wird diese Art von Partner noch nicht unterstützt
        return null;
    }
    
    /**
     * erstellt die Verbindungslinie
     */
    public void createLineGroup() {
        ArrayList<List<Node>> completePath = getPath();
        
        if(!this.directLine) {
            createLineGroupFromNodeList(completePath);
        } else {
            createDirectLineGroup();
        }
    }
    
    public void createPointGroup() {
        int gridOffset = general.Properties.GetGridOffset();
        Color c = HandleState.getColorFromState(this.state);
        for(AnchorPoint ap : this.anchorPoints) {
            Circle circle = Draw.drawCircle(ap.getCoords().getX()*gridOffset+10.5, ap.getCoords().getY()*gridOffset+10.5, 5, c, 1, true, 1);
            pointGroup.getChildren().add(circle);
        }
    }
    
    /**
     * Erstellt eine Liniengruppe aus einer Nodeliste
     * @param completePath 
     */
    public void createLineGroupFromNodeList(ArrayList<List<Node>> completePath) {
        List<Node> nodePath = new ArrayList();
        
        //Lässt die "Liste aus Listen" zu nur einer Liste werden
        for(List<Node> i : completePath) {
            for(Node n : i) {
                nodePath.add(n);
            }
        }
        
        List<Node> vertexNodes = findVertexNodes(nodePath);
        double gO = general.Properties.GetGridOffset(); //grid Offset
        int lineWidth = Properties.getLineWidth();
        
        Node child = null;
        //verbinde alle Eckpunkte
        Color c = HandleState.getColorFromState(this.state);
        for(Node n : vertexNodes) {
            if(child != null) {
                Line line = Draw.drawLine(child.tile.getX()*gO+10.5, child.tile.getY()*gO+10.5, n.tile.getX()*gO+10.5, n.tile.getY()*gO+10.5, c, lineWidth);
                this.lineGroup.getChildren().add(line);
            } 
            child = n;
        }
        
    } 
    
    /**
     * erstellt aus den anchorPoints einen NodePath mit dem PathFinder
     * @return 
     */
    public ArrayList<List<Node>> getPath() {
        ArrayList<List<Node>> completePath = new ArrayList();
        
        for(int i = 0; i < anchorPoints.size() -1; i++) { //Versuche kompletten Pfad mit dem Pathfinder zu erzeugen
            List<Node> path = pathFinder.findPath(anchorPoints.get(i).getCoords(), anchorPoints.get(i + 1).getCoords(), dsc.getElements(), dsc.getAllConnections());
            if(path == null) {
                this.directLine = true;
                return null;
            } else {
                completePath.add(path);
            }
        }
        return completePath;
    }
    
    /**
     * Findet aus einer Liste von Nodes die Eckpunkte heraus
     * @param allNodes  die Liste, von welcher die Eckpunke gesucht werden sollen
     * @return Eckpunkte
     */
    public List<Node> findVertexNodes(List<Node> allNodes) {
        List<Node> vertexNodes = new ArrayList();
        
        //Füge Start Node hinzu
        vertexNodes.add(allNodes.get(0));
        
        //Finde EckPunkte
        for(int i = 1; i < allNodes.size()-1; i++) {
            Node node = allNodes.get(i);
            Node parent = allNodes.get(i+1);
            Node child = allNodes.get(i-1);
            
            //Überprüft ob sich die Richtung geändert hat
            if(didDirectionChange(node, child, parent)) {
                vertexNodes.add(node);
            }
        }
        //Füge End-Node hinzu
        vertexNodes.add(allNodes.get(allNodes.size()-1));
        
        return vertexNodes;
    }
    
    /**
     * überprüft ob sich die Richtung geändert hat
     */
    public boolean didDirectionChange(Node node, Node child, Node parent) {
        boolean didXChange;
        boolean didYChange;
                
        if((child.tile.getX()==node.tile.getX()) && (node.tile.getX() == parent.tile.getX())) {
            didXChange = false;
        } else {
            didXChange = true;
        }
            
        if((child.tile.getY()==node.tile.getY()) && (node.tile.getY() == parent.tile.getY())) {
            didYChange = false;
        } else {
            didYChange = true;
        }
         
        
        if(didXChange && didYChange) {
            return true;
        }
        return false;
    }
    
    /**
     * Erstellt aus den AnchorPoints eine direkt verlegte Linie
     */
    public void createDirectLineGroup() {
        Vector2i parentPoint = null;
        Color c = HandleState.getColorFromState(this.state);
        for(AnchorPoint ap : anchorPoints) {
            if(parentPoint != null) {
                Line l = Draw.drawLine(parentPoint, ap.getCoords(), c, Properties.getLineWidth());
                this.lineGroup.getChildren().add(l);
            }
            parentPoint = ap.getCoords();
        }
    }
    
    /**
     * setzt sämtliche Parameter auf die Standartwerte zurück
     */
    public void resetAttributes() {
        this.anchorPoints = new ArrayList();
        this.directLine = false;
        this.lineGroup = new Group();
        this.pointGroup = new Group();
    }
    
    
    
    public void updatePartnerState() {
        if((this.startPartner.getPartnerType() == PartnerType.ELEMENT)&&(this.startPartner.isIsInput())) {
            this.startPartner.getElement().setInput(startPartner.getIndex(), HandleState.getIntFromState(state));
        }
        if((this.endPartner.getPartnerType() == PartnerType.ELEMENT)&&(this.endPartner.isIsInput())) {
            this.endPartner.getElement().setInput(endPartner.getIndex(), HandleState.getIntFromState(state));
        }
    }
    
    //**********************GET/SET************************/
    
    public List<AnchorPoint> getAnchorPoints() {
        return anchorPoints;
    }

    public Color getCurrentColor() {
        return HandleState.getColorFromState(this.state);
    }

    public DigitSimController getDsc() {
        return dsc;
    }

    public Group getLineGroup() {
        return lineGroup;
    }

    public Group getPointGroup() {
        return pointGroup;
    }

    public ConnectionPartner getStartPartner() {
        return startPartner;
    }

    public ConnectionPartner getEndPartner() {
        return endPartner;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
        updateColor();
        updatePartnerState();
    }
    
    public void resetState() {
        this.state = state.DEFAULT;
        updateColor();
    }
}
