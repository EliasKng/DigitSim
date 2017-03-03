/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathFinder;

import connection.Connection;
import general.Vector2i;
import element.Element;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * @author Elias
 */
public class PathFinder {
    
    
    
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

    public List<Node> findPath(Vector2i start, Vector2i goal, ArrayList<Element> elements, List<Connection> connections) {
        int gridOffet = general.Properties.GetGridOffset();
        start.divide(gridOffet);
        goal.divide(gridOffet);
        List<Node> openList = new ArrayList<Node>();
        List<Node> closedList = new ArrayList<Node>();
        
        TileCode.createTileCode(elements, connections, general.Properties.getVisualizeTileCode());
        
        if(TileCode.isTileSolid(start.getX(), 0, start.getY(), 0))
            return null;
        if(TileCode.isTileSolid(goal.getX(), 0, goal.getY(), 0))
            return null;
        
        //Startnode
        Node current = new Node(start, null, 0, getManhattanDistance(start, goal));
        openList.add(current);
        
        while(openList.size() > 0) {
            Collections.sort(openList, nodeSorter); //sortiert die nodeliste nach der fCost der einzelnen Nodes (siehe NodeComparator)
            current = openList.get(0);
            if(current.tile.equals(goal)) {
                List<Node> path = new ArrayList<Node>();
                while(current.parent != null) {
                    path.add(current);
                    current = current.parent;
                }
                path.add(current);
                openList.clear();
                closedList.clear();
                if(general.Properties.getVisualizeTileCode())
                    TileCode.visualizePath(path);
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
                
                if(!TileCode.isTileAvailible(x, xi, y , yi)) continue;
                if(TileCode.isTileSolid(x, xi, y, yi)) continue;
                
                
                Vector2i a = new Vector2i(x+xi,y+yi);
                double gCost = current.gCost + getManhattanDistance(current.tile, a);
                double hCost = getManhattanDistance(a, goal);
                Node node = new Node(a,current, gCost, hCost);
                
                if(TileCode.isTileInIOArea(x,xi,y,yi)) {
                    node.fCost +=50;
                }
                if(TileCode.isTileInElementArea(x,xi,y,yi)) {
                    node.fCost += 5;
                }
                if(TileCode.isTileOverOtherConnection(x, xi, y, yi)) {
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
            
            if(((parentX == currentX) && (parentX == nodeX))    ||    ((parentY == currentY) && (parentY == nodeY))) {
                return false;
            } else {
                return true;
            }
        }
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
}