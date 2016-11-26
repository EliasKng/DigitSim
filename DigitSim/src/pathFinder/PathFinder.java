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
    private int[][] tileCode;
    
    
    public PathFinder() {
        
    }
    
    
    
    
    
    
    
    
    
    
    private Comparator<Node> nodeSorter = new Comparator<Node>() {
        @Override
        public int compare(Node n0, Node n1) {
            if(n1.fCost < n0.fCost) return +1;
            if(n1.fCost > n0.fCost) return -1;
            return 0;
        }
    };

    public List<Node> findPath(Vector2i start, Vector2i goal, ArrayList<Element> elements) {
        List<Node> openList = new ArrayList<Node>();
        List<Node> closedList = new ArrayList<Node>();
        
        createTileCode(elements);
        //Startnode
        Node current = new Node(start, null, 0, getManhattanDistance(start, goal));
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
            for (int i = 0; i < 9; i++) {
                int addCost = 0;
                if(i == 4 || i == 0 || i == 2 || i == 6 || i == 8) continue;
                int x = current.tile.getX();
                int y = current.tile.getY();
                int xi = (i % 3) -1;
                int yi = (i / 3) -1;
                
                if(!isTileAvailible(x, xi, y , yi)) continue;
                if(isTileSolid(x, xi, y, yi)) continue;
                
                
                Vector2i a = new Vector2i(x+xi,y+yi);
                double gCost = current.gCost + getManhattanDistance(current.tile, a);
                double hCost = getManhattanDistance(a, goal);
                Node node = new Node(a,current, gCost, hCost);
                
                if(isDirectionChanged(current, node, start)) {
                    node.fCost++;
                }
                if(isTileInIOArea(x,xi,y,yi)) {
                    node.fCost +=50;
                }
                if(isTileInElementArea(x,xi,y,yi)) {
                    node.fCost += 5;
                }
                
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
    
    private boolean isDirectionChanged(Node current, Node node, Vector2i start) {
        if(current.parent != null) {
            int parentX = current.parent.tile.getX();
            int parentY = current.parent.tile.getY();
            int currentX = current.tile.getX();
            int currentY = current.tile.getY();
            int nodeX = node.tile.getX();
            int nodeY = node.tile.getY(); 
            
//            System.out.println("parentX: \t" +parentX);
//            System.out.println("parentY: \t" +parentY);
//            System.out.println("currentX: \t" +currentX);
//            System.out.println("currentY: \t" +currentY);
//            System.out.println("nodeX: \t\t" +nodeX);
//            System.out.println("nodeY: \t\t" +nodeY);
            
            if(((parentX == currentX) && (parentX == nodeX))    ||    ((parentY == currentY) && (parentY == nodeY))) {
//                System.out.println("Direction Stayed");
                return false;
            } else {
//                System.out.println("Direction CHanged");
                return true;
            }
        }
//        System.out.println("No Parent");
        return false;
    }
    
    private boolean vecInList(List<Node> list, Vector2i vector) {
        for (Node n : list) {
            if(n.tile.equals(vector)) return true;
        }
        return false;
    }
    
    private double getManhattanDistance(Vector2i tile, Vector2i goal) {
        if(tile.getX() != goal.getX() && tile.getY() != goal.getY()) {
            if((Math.abs(tile.getX()+goal.getX()) <= 1) &&(Math.abs(tile.getY()+goal.getY()) <= 1)) {
                return Math.abs(tile.getX()-goal.getX())+Math.abs(tile.getY()-goal.getY())+100;
            }
            
        }
        return Math.abs(tile.getX()-goal.getX())+Math.abs(tile.getY()-goal.getY());
    }
    
    private double getEuclideanDistance(Vector2i tile, Vector2i goal) {
        double dx = tile.getX() - goal.getX();
        double dy = tile.getY() - goal.getY();
        return Math.sqrt(dx * dx + dy * dy);
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
        boolean result = false;
        int status = tileCode[x+xi][y+yi];
        if(status == 1) {
            result = true;
        }
        return result;
    }
    
    /**
     * Wenn sich über diesem Tile ein oder Ausgänge eines Elements befinden, wird true zurückgegeben
     * @return 
     */
    public boolean isTileInIOArea(int x, int xi, int y, int yi) {
        boolean result = false;
        int status = tileCode[x+xi][y+yi];
        if(status == 2) {
            result = true;
        }
        return result;
    }
    
    /**
     * Wenn sich über diesem Tile das Umfeld einess Elements befindet (Umfeld entspricht ein Block größer als das Element), wird true returnt
     * @return 
     */
    public boolean isTileInElementArea(int x, int xi, int y, int yi) {
        boolean result = false;
        int status = tileCode[x+xi][y+yi];
        if(status == 3) {
            result = true;
        }
        return result;
    }
    
    /**
     * Beschreibt das tileCodeArray mit Daten (wenn sich auf diesem Tile ein Block befindet, wird es auf 1 gesetzt
     * @param elements
     * @param width
     * @param height
     * @author Elias
     */
    public void createTileCode(ArrayList<Element> elements) {
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
                    int value;
                    if((k >= eX && k < (eX+eWidth)) && (o >= eY && o < (eY + eHeight))) {
                        if(k == eX || k == (eX + eWidth -1)) {
                            value = 2;
                        } else {
                            value = 1;
                        }
                    } else {
                        value = 3;
                    }
                    try {
                        if((tileCode[k][o] == 0) || value < tileCode[k][o]) {
                            tileCode[k][o] = value;
                        }
                    }catch(Exception e) {
                        System.out.println("Unstimmigkeit beim Erstellen des Arrays (PATHFINDER)");
                    }
                    
                    
                }
            }
        }
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