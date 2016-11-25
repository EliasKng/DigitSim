/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;


import java.util.ArrayList;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import pathFinder.Node;
import pathFinder.PathFinder;
import pathFinder.Vector2i;

/**
 *Diese Klasse (wird) dazu dienen Verbindungslinien zwischen Bausteinen zu erstellen und zu optimieren
 * @author Elias
 */
public class ConnectionLine {
    private final double lWidth = 2;  //Liniendicke
    private Color lColor = Color.BLACK;   //Linienfarbe
    private double[] lX;    //In diesem Array stecken alle X Koordinaten der Linie
    private double[] lY;    //In diesem Array stecken alle Y Koordinaten der Linie
    private Group group = new Group();  //Stelld die Gruppe dar, in welcher die einzelnen Linien sind
    private List<Node> path;
    private PathFinder pathFinder = new PathFinder();
    private Vector2i start;
    private Vector2i end;
    
    
    /**
     * Konstruktor zum späteren Zeichnen der Linie
     * @param path NodeList, welche den Pfad enthält (wird mit Pathfinder erstellt)
     */
    public ConnectionLine() {
    }
    
    
    //*************SET/GET*********************

    public double getlWidth() {
        return lWidth;
    }

    public Color getlColor() {
        return lColor;
    }

    public void setlColor(Color lColor) {
        this.lColor = lColor;
    }

    public double[] getlX() {
        return lX;
    }

    public void setlX(double[] lX) {
        this.lX = lX;
    }

    public double[] getlY() {
        return lY;
    }

    public void setlY(double[] lY) {
        this.lY = lY;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public List<Node> getPath() {
        return path;
    }

    public void setPath(List<Node> path) {
        this.path = path;
    }

    public Vector2i getStart() {
        return start;
    }

    public void setStart(Vector2i start) {
        this.start = start;
    }

    public Vector2i getEnd() {
        return end;
    }

    public void setEnd(Vector2i end) {
        this.end = end;
    }
    
    //*******************************************

    
    
    /**
     * Diese Funktion wird benötigt um eine Linie zu erstellen
     * @param elements
     * @param start
     * @param goal 
     */
    public void createLine(ArrayList<Element> elements, int lineX1, int lineY1,int lineX2, int lineY2) {
        this.start = convertToVec21(lineX1,lineY1);
        this.end = convertToVec21(lineX2,lineY2);
        System.out.println("VecStart:" +start.getX() +" , " +start.getY());
        System.out.println("VecEnd:" +end.getX() +" , " +end.getY());
        findPath(elements, start, end);
        createArray();
        createGroup();
    }
    
    private Vector2i convertToVec21(int a, int b) {
        Vector2i vec = new Vector2i(a,b);
        return vec;
    }
    
    /**
     * Benutzt den PathFinder um den Weg zum Ziel zu finden
     * @param elements
     * @param start
     * @param goal 
     */
    private void findPath(ArrayList<Element> elements, Vector2i start, Vector2i goal) {
        this.path = pathFinder.findPath(elements, start, goal);
    }
    
    /**
     * Erstellt ein Array aus der vom PathFinder generierten Nodeliste
     */
    private void createArray() {
        lX = new double[path.size()];
        lY = new double[path.size()];
        int k = 0;
        for(Node i : path) {
            double x = i.tile.getX()*21+10.5;
            double y = i.parent.tile.getY()*21+10.5;
            double xParent = i.parent.tile.getX()*21+10.5;
            double yParent = i.tile.getY()*21+10.5;
            lX[k] = xParent;
            lX[k+1] = x;
                    
                    
            k++;
        }
    }
    
    
    /**
     * Erstellt eine Gruppe bestehend aus Linien. Die Koordinaten der Linien werden aus lX[] und lY[] genommen
     * @author Elias
     */
    private void createGroup() {
        if(Draw.checkArraySameLength(lX, lY)) {
            for (int i =0; i < (lX.length-1); i ++) {
                this.group.getChildren().add(Draw.drawLine(lX[i], lY[i],lX[i+1] , lY[i+1], lColor, lWidth));
                DigitSimController.getSimCanvas().getChildren().add(group);
            }
        }
    }
    
    /**
     * löscht die vorhandene Gruppe und erstellt diese neu (wenn z.B. die Farbe geändert wurde ec.)
     * @author Elias
     */
    public void clearGroup()
    {
        group.getChildren().clear();
        DigitSimController.getSimCanvas().getChildren().remove(group);
    }
    public void update() {
        createGroup();
    }
}
