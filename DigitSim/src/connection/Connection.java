/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import Gestures.NodeGestures;
import digitsim.DigitSimController;
import element.Element;
import general.Properties;
import general.Vector2i;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
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
    private Group lineGroup = new Group();                            //Gruppe von Linien (die die gesamte Linie darstellt)
    private Group pointGroup = new Group();                                   //Gruppe von Punkten (anchorPoints, durch die die Linie verlaufen muss)
    private Group tempGroup = new Group();                                // in dieser gruppe wird die "orangene Linie" gespeichert
    
    //Dieser Comparator vergleicht den Index von AnchorPoints und sortiert diese
    private Comparator<AnchorPoint> anchorPointSprter = new Comparator<AnchorPoint>() {
        @Override
        public int compare(AnchorPoint aP0, AnchorPoint aP1) {
            if(aP1.index < aP0.index) return +1;
            if(aP1.index > aP0.index) return -1;
            return 0;
        }
    };
    

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
    
    public Connection(DigitSimController dsc, ConnectionPartner startPartner, ConnectionPartner endPartner){
        this.dsc = dsc;
        this.startPartner = startPartner;
        this.endPartner = endPartner;
        this.updateLine();
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
        
        List<javafx.scene.Node> allNodes = getAllNodesFromLineGroup();
                
        for(javafx.scene.Node n : allNodes) {
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
     * ändert die Farbe der Verbindung auf die Angegebene
     * @param color 
     */
    public void setSpecialColor(Color color) {
        List<javafx.scene.Node> allNodes = getAllNodesFromLineGroup();
                
        for(javafx.scene.Node n : allNodes) {
            Line l = (Line) n;
            l.setStroke(color);
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
     * Erstellt aus den Attributen die Linie und speichert sie in lineGroup und pointGroup
     */
    public void createConnection() {
        processPartners();
        createLineGroup();
        createPointGroup();
    }
    
    /**
     * Entfernt die Linie & AnchorPoints aus dem dsc
     */
    public void removeLine() {
        if((this.pointGroup != null) && (this.lineGroup != null)) {
            this.dsc.getSimCanvas().getChildren().remove(this.lineGroup);
            this.dsc.getSimCanvas().getChildren().remove(this.pointGroup);
        }
        if(this.tempGroup != null) {
            this.dsc.getSimCanvas().getChildren().remove(this.tempGroup);
        }
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
        startCoords.adaptToHalfGrid();
        endCoords.adaptToHalfGrid();
        
        AnchorPoint start = new AnchorPoint(indexStart, startCoords);
        AnchorPoint end = new AnchorPoint(indexEnd, endCoords);
        
        
        if(anchorPoints.size() > 1) {
            this.anchorPoints.set(0, start);
            this.anchorPoints.set(anchorPoints.size()-1, end);
        }
        else {
            this.anchorPoints.clear();
            this.anchorPoints.add(start);
            this.anchorPoints.add(end);
        }
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
        
        //EventFilter hinzufügen (Linie färbt sich orange, wenn man über diese hovert
        this.lineGroup.addEventFilter(MouseEvent.MOUSE_ENTERED, NodeGestures.getOverNodeMouseHanlderEnterLineGrp(this));
        this.lineGroup.addEventFilter(MouseEvent.MOUSE_EXITED, NodeGestures.getOverNodeMouseHanlderExitLineGrp(this));
    }
    
    public void createPointGroup() {
        int gridOffset = general.Properties.GetGridOffset();
        Color c = HandleState.getColorFromState(this.state);
        
        for(AnchorPoint ap : this.anchorPoints) {
            Circle circle = Draw.drawCircle(ap.getCoords().getX(), ap.getCoords().getY(), 5, c, 1, true, 1);
            circle.addEventFilter(MouseEvent.MOUSE_ENTERED, NodeGestures.getOverNodeMouseHanlderEnterCircle(circle));
            circle.addEventFilter(MouseEvent.MOUSE_EXITED, NodeGestures.getOverNodeMouseHanlderExitCircle(circle));
            circle.addEventFilter(MouseEvent.MOUSE_DRAGGED, NodeGestures.getAnchorPointDraggedEventHandler(ap, this));
            circle.addEventFilter(MouseEvent.MOUSE_RELEASED, NodeGestures.getAnchorPointOnDragDoneEventHandler(ap, this));
            pointGroup.getChildren().add(circle);
        }
    }
    
    /**
     * Erstellt eine Liniengruppe aus einer Nodeliste
     * @param completePath 
     */
    public void createLineGroupFromNodeList(ArrayList<List<Node>> completePath) {
        
        double gO = general.Properties.GetGridOffset(); //grid Offset
        int lineWidth = Properties.getLineWidth();
        Color c = HandleState.getColorFromState(this.state);
        
        
        
        for(List<Node> vertexNodeList : completePath) {
            Node child = null;
            //verbinde alle Eckpunkte
            Group tempGroup = new Group(); //in dieser Gruppe werden alle Linien von AnchorPoint zu AnchorPoint gespeichert
            for(Node n : vertexNodeList) {
                
                if(child != null) {
                    Line line = Draw.drawLine(child.tile.getX()*gO+10.5, child.tile.getY()*gO+10.5, n.tile.getX()*gO+10.5, n.tile.getY()*gO+10.5, c, lineWidth);
                    
                    tempGroup.getChildren().add(line);
                } 
                child = n;
            }
            AnchorPoint aP0 = this.anchorPoints.get(completePath.indexOf(vertexNodeList));
            AnchorPoint aP1 = this.anchorPoints.get(completePath.indexOf(vertexNodeList)+1);
            tempGroup.addEventFilter(MouseEvent.MOUSE_PRESSED, NodeGestures.getOverConnectionLinePartClicked(aP0,aP1, this));
            this.lineGroup.getChildren().add(tempGroup);
        }
    } 
    
    /**
     * erstellt aus den anchorPoints einen NodePath mit dem PathFinder
     * @return 
     */
    public ArrayList<List<Node>> getPath() {
        ArrayList<List<Node>> completePath = new ArrayList();
        for(int i = 0; i < anchorPoints.size() -1; i++) { //Versuche kompletten Pfad mit dem Pathfinder zu erzeugen
            Vector2i start = new Vector2i(anchorPoints.get(i).getCoords());
            Vector2i end = new Vector2i(anchorPoints.get(i + 1).getCoords());
            List<Node> path = pathFinder.findPath(start, end, dsc.getElements(), dsc.getAllConnections());
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
        AnchorPoint parentPoint = null;
        Color c = HandleState.getColorFromState(this.state);
        for(AnchorPoint ap : anchorPoints) {
            if(parentPoint != null) {
                Line l = Draw.drawLine(parentPoint.getCoords(), ap.getCoords(), c, Properties.getLineWidth());
                l.addEventFilter(MouseEvent.MOUSE_CLICKED, NodeGestures.getOverConnectionLinePartClicked(parentPoint, ap, this));
                this.lineGroup.getChildren().add(new Group(l));
            }
            parentPoint = ap;
        }
    }
    
    /**
     * setzt sämtliche Parameter auf die Standartwerte zurück
     */
    public void resetAttributes() {
        //this.anchorPoints = new ArrayList();
        this.directLine = false;
        this.lineGroup = new Group();
        this.pointGroup = new Group();
    }
    
    
    /**
     * Setzt den State der Verbindung auf die Inputs, sodass die Signale vom ELement weiter verarbeitet werden können
     */
    public void updatePartnerState() {
        if((this.startPartner.getPartnerType() == PartnerType.ELEMENT)&&(this.startPartner.isIsInput())) {
            this.startPartner.getElement().setInput(startPartner.getIndex(), HandleState.getIntFromState(state));
        }
        if((this.endPartner.getPartnerType() == PartnerType.ELEMENT)&&(this.endPartner.isIsInput())) {
            this.endPartner.getElement().setInput(endPartner.getIndex(), HandleState.getIntFromState(state));
        }
    }
    
    
    /**
     * erstellt die orangene Verbindungslinie und löscht die Alte
     */
    public void drawDirectPreLine() {
        processPartners();
        
        removeTempGroup();
        
        AnchorPoint childPoint = null;
        
        for(AnchorPoint aP : this.anchorPoints) {
            if(childPoint != null) {
                Line l = Draw.drawLine(childPoint.coords, aP.coords, Color.DARKORANGE, 5);
                l.setOpacity(0.6);
                this.tempGroup.getChildren().add(l);
            } 
            childPoint = aP;
        }    
        
        this.dsc.getSimCanvas().getChildren().add(this.tempGroup);
    }
    
    /**
     * entfernt die orangene Verbindungslinie
     */
    public void removeTempGroup() {
        if(this.tempGroup != null) {
            dsc.getSimCanvas().getChildren().remove(tempGroup);
            this.tempGroup.getChildren().clear();
        }
    }
    
    /**
     * In der lineGroup (globalVariable) werden nicht die Linien direkt gespeichert, sondern Gruppen in denen sich die Linien befinden.
     * Diese Funktion hold die Linien aus den Untergruppen und gibt diese als Liste zurück
     * @return 
     */
    public List<javafx.scene.Node> getAllNodesFromLineGroup() {
        List<javafx.scene.Node> allNodes = new ArrayList();
        
        for(javafx.scene.Node n : this.lineGroup.getChildren()) {
            Group g = (Group) n;
            for(javafx.scene.Node node : g.getChildren()) {
                allNodes.add(node);
            }
        }
        
        return allNodes;
    }
    
    /**
     * Fügt einen AnchorPoint hinzu
     * @param aP 
     */
    public void addAnchorPoint(AnchorPoint aP) {
        this.anchorPoints.add(1, aP);
        sortAnchorPoints();
        updateLine();
    }
    
    /**
     * Sortiert all anchorPoints und weist ihrem Index ganze Zahlen zu
     */
    public void sortAnchorPoints() {
        
        Collections.sort(this.anchorPoints, this.anchorPointSprter); //Sortiert anchorPoints nach ihrem Index
        
        List<AnchorPoint> sortedAnchorPoints = new ArrayList();
        
        for(int i = 0; i < this.anchorPoints.size(); i++) {
            AnchorPoint tempAP = this.anchorPoints.get(i);
            tempAP.setIndex(i);
            sortedAnchorPoints.add(tempAP);
        }
        
        this.anchorPoints = sortedAnchorPoints;
    }
    
    public void moveAnchorPoint(AnchorPoint aP) {
        this.anchorPoints.set((int) aP.index, aP);
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
