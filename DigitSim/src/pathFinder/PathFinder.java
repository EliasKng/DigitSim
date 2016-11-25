/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathFinder;

import digitsim.Draw;
import digitsim.Element;
import digitsim.Properties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Elias
 */
public class PathFinder {
    private int simSizeX = Properties.GetSimSizeX();
    private int simSizeY = Properties.GetSimSizeY();
    private ArrayList<Element> elements;
    private int[][] tileCode;
    
    
    public PathFinder(ArrayList<Element> elements) {
        this.elements = elements;
        
    }
    
    private Comparator<Node> nodeSorter = new Comparator<Node>() {
        @Override
        public int compare(Node n0, Node n1) {
            if(n1.fCost < n0.fCost) return +1;
            if(n1.fCost > n0.fCost) return -1;
            return 0;
        }
    };

    public List<Node> findPath(Vector2i start, Vector2i goal) {
        List<Node> openList = new ArrayList<Node>();
        List<Node> closedList = new ArrayList<Node>();
        
        createTileCode();
        //Startnode
        Node current = new Node(start, null, 0, getDistance(start, goal));
        openList.add(current);
        
        while(openList.size() > 0) {
            //sortiert die nodeliste nach der fCost der einzelnen Nodes (siehe NodeComparator)
            Collections.sort(openList, nodeSorter);
            current = openList.get(0);
            if(current.tile.equals(goal)) {
                List<Node> path = new ArrayList<Node>();
                while(current.parent != null) {
                    path.add(current);
                    current = current.parent;
                }
                openList.clear();
                closedList.clear();
                System.out.println("Path found!!!");
                return path;
            }
            openList.remove(current);
            closedList.add(current);
            for (int i = 1; i < 9; i = i + 2) { //i kann folgende Werte annehmen: 1,3,5,7
                //if(i == 4 || i == 0) continue;
                int addCost = 0;
                int x = current.tile.getX();
                int y = current.tile.getY();
                int xi = (i % 3) -1;
                int yi = (i / 3) -1;
                
                if(!isTileAvailible(x, xi, y , yi)) continue;   
                if(isTileSolid(x, xi, y, yi)) continue;
                if(isTileInElementArea(x, xi, y, yi)) {
                    addCost = 15;
                }
                
                Vector2i a = new Vector2i(x+xi,y+yi);
                double gCost = current.gCost + getDistance(current.tile, a);
                double hCost = getDistance(a, goal) + addCost;
                Node node = new Node(a,current, gCost, hCost);
                if(vecInList(closedList, a) && gCost >= node.gCost) continue;
                if(!vecInList(openList, a) || gCost < node.gCost) {
                    openList.add(node);
                }
            }
        }
        closedList.clear();
        System.out.println("No path found");
        return null;
    }
    
    private boolean vecInList(List<Node> list, Vector2i vector) {
        for (Node n : list) {
            if(n.tile.equals(vector)) return true;
        }
        return false;
    }
    
    private double getDistance(Vector2i tile, Vector2i goal) {
        /*double dx = tile.getX() - goal.getX();
        double dy = tile.getY() - goal.getY();
        return Math.sqrt(dx * dx + dy * dy);*/
        
        int hCost = (Math.abs(tile.getX() - goal.getX())) + (Math.abs(tile.getY() - goal.getY()));
        return hCost;
    }
    
    
    /**
     * Hat noch keine Funktion, soll später aber überprüfen, ob das "Tile" (bei und ein Kästchen (21 x 21px)) vorhanden ist bisher wird nur geprüft ob das Tile oben oder links aus dem Feld geht
     * @return 
     */
    public boolean isTileAvailible(int x, int xi, int y, int yi) {
        boolean result = true;
        if((x+xi)<0 || (y+yi)<0) {
            result = false;
        }
        return result;
    }
    
    /**
     * Wenn sich über diesem Tile ein Baustein befindet wird true zurück gegeben
     * @return 
     */
    public boolean isTileSolid(int x, int xi, int y, int yi) {
        if(tileCode[x+xi][y+yi] == 1) {
            return true;
        }
        return false;
    }
    
    /**
     * Wenn sich dieses Tile im Umfeld eines Bausteines befindet (Umfeld eines Elementes entspricht auf allen Seiten eine Tile mehr) wird true zurück gegeben
     * @return 
     */
    public boolean isTileInElementArea(int x, int xi, int y, int yi) {
        if(tileCode[x+xi][y+yi] == 2) {
            return true;
        }
        return false;
    }
    
    /**
     * Beschreibt das tileCodeArray mit Daten (wenn sich auf diesem Tile ein Block befindet, wird es auf 1 gesetzt
     * @param elements
     * @param width
     * @param height
     * @author Elias
     */
    public void createTileCode() {
        int gridOffset = Properties.GetGridOffset();
        
        int arrayWidth = (int) Math.ceil(simSizeX / gridOffset); //Berechnet den Wert für die Weite den Arrays (jedes Kästchen soll 1 arrayplatz belegen), desshalb wird die gesamthöhe genommen, durch 21 geteilt und dann aufgerundet
        int arrayHeight = (int) Math.ceil(simSizeY / gridOffset);
        tileCode = new int[arrayWidth][arrayHeight]; //In diesem 2 dimensionalen array, wird gespeichert ob ein Kästchen (z.B. durch ein Element) geblockt wird
        int eWidth = 6; //Weite der elemente (in 21 schritten) (ist 6 weil die Ausgänge & Eingänge mit dazu gezählt werden)
        int eHeight; //Höhe der elemente (in 21 schritten)
        int eX; //X-Koordinate des Elements (in gridOffset Schritten)
        int eY; //Y-Koordinate des Elements (in gridOffset Schritten)
        
        //Diese Schleife wird für jedes Element einmal durchlaufen
        for(Element i : elements){ //elemente durchgehen...
            eHeight = getElementHeight(i.getInputCount());
            eX = (int) i.getX() / gridOffset - 1;
            eY = (int) i.getY() / gridOffset;
            
            for(int k = eX-1; k < (eX + eWidth+1); k++) {
                for(int o = eY-1; o < (eY + eHeight+1); o++) {
                    if((k >= eX && k < (eX+eWidth)) && (o >= eY && o < (eY + eHeight))) {
                        tileCode[k][o] = 1;// = 1 bedeuted, dass da ein Element ist, sprich das Feld is geblockt
                    } else {
                        tileCode[k][o] = 2;// = 2 bedeuted, dass da das Umfeld eines Elements ist, sprich das Feld ist nicht geblockt, es fällt dem pathfinder jedoch schwer, diesen Weg zu nehmen, weil die fCost erhöht wird
                    }
                    
                }
            }
        }
        this.tileCode = tileCode;
    }
    
    /**
     * berechnet die Höhe des standard Elements anhand seiner Anzahl con Eingängen
     * @param inputs
     * @return 
     */
    public int getElementHeight(int inputs) {
        int h = 4;
        if (inputs > 4) {
            h=inputs;
        }
        return h;
    }
    
}
