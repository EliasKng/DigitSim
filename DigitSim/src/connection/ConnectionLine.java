/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import Gestures.NodeGestures;
import connection.Connection.ConData;
import digitsim.DigitSimController;
import toolbox.Draw;
import general.Properties;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import pathFinder.*;

/**
 *
 * @author Tim & Elias
 */
public class ConnectionLine {
    
    private Group group = new Group();
    private DigitSimController dsc;
    private PathFinder pathFinder = null;
    private List<Node> path;
    private Vector2i[] resetData = new Vector2i[2];
    private ArrayList<Vector2i> points = new ArrayList<>();
    private final int gridOffset = Properties.GetGridOffset();
    private Color currentColor = Color.GREY; //Standart: Schwarz
    private ConData data;
    
    
    public ConnectionLine(Vector2i _start, Vector2i _end, DigitSimController d) {
        resetData[0] = _start;
        resetData[1] = _end;
        dsc = d;
        pathFinder = new PathFinder();
        points.add(_start.divide(gridOffset));
        points.add(_end.divide(gridOffset));
    }
    
    public ConnectionLine(DigitSimController d, ConData data) {
        dsc = d;
        pathFinder = new PathFinder();
        points.add(new Vector2i());
        points.add(new Vector2i());
        this.data = data;
    }
    
    public void reset(ArrayList<Connection.ConData> connections){
        clear();
        points.clear();
        points.add(resetData[0]);
        points.add(resetData[1]);
        update(false, connections);
    }
    
//*******************SET /GET***************************/
    
    public Vector2i getStart() {
        return points.get(0);
    }

    public void setStart(Vector2i start) {
        points.set(0, start.divide(gridOffset));
        resetData[0] = start;
    }

    public Vector2i getEnd() {
        return points.get(points.size() - 1);
    }

    public void setEnd(Vector2i end) {
        points.set(points.size() - 1, end.divide(gridOffset));
        resetData[1] = end;
    }

    public List<Node> getPath() {
        return path;
    }

    public Group getGroup() {
        return group;
    }
    
    public void addPoint(Vector2i vec){ //Einen Punkt hinzufügen
        vec = vec.divide(gridOffset);
        Vector2i temp = points.get(points.size() - 1);
        points.set(points.size() - 1, vec);
        points.add(temp);
        System.out.println("addPoint ConnectionLine-Class");
    }
    
    public void removePoint(Vector2i vec){
        points.remove(vec);
    }
    
    
    public void update(boolean directLine, ArrayList<Connection.ConData> connections) {
        clear();
        
        if(!directLine) {
            for(int i = 0; i < points.size() - 1; i++){
                path = pathFinder.findPath(points.get(i), points.get(i + 1), dsc.getElements(), connections);
                if(path != null) {
                     for(Node currentNode : path) {
                         if(currentNode.parent != null) {  
                            int thisX = currentNode.tile.getX() * gridOffset;
                            int thisY = currentNode.tile.getY() * gridOffset;
                            int parentX = currentNode.parent.tile.getX() * gridOffset;
                            int parentY = currentNode.parent.tile.getY() * gridOffset;
                            Line l = Draw.drawLine(parentX + 10.5, parentY + 10.5, thisX + 10.5, thisY + 10.5, currentColor, Properties.getLineWidth());
                            l.addEventFilter(MouseEvent.MOUSE_PRESSED, NodeGestures.getOverConnectionLineClicked(data));
                            group.getChildren().add(l);
                        }
                    }
            
            } else {
                group.getChildren().add(Draw.drawLine(points.get(0).getX() * gridOffset + 10.5, points.get(0).getY()  * gridOffset + 10.5, points.get(points.size() - 1).getX() * gridOffset + 10.5 ,points.get(points.size() - 1).getY() * gridOffset + 10.5, currentColor,  Properties.getLineWidth()));
            }
            }
            group.addEventFilter(MouseEvent.MOUSE_ENTERED, NodeGestures.getOverNodeMouseHanlderEnterLineGrp());
            group.addEventFilter(MouseEvent.MOUSE_EXITED, NodeGestures.getOverNodeMouseHanlderExitLineGrp());
        } else {
            for(int i = 0; i < points.size() - 1; i++){
                Line l = Draw.drawLine(points.get(i).getX() * gridOffset + 10.5, points.get(i).getY() * gridOffset + 10.5, points.get(i + 1).getX() * gridOffset + 10.5, points.get(i + 1).getY() * gridOffset + 10.5, Color.DARKORANGE, Properties.getLineWidth());
                l.setOpacity(0.6);
                l.addEventFilter(MouseEvent.MOUSE_PRESSED, NodeGestures.getOverConnectionLineClicked(data));
                group.getChildren().add(l);
            }
        }
        dsc.getSimCanvas().getChildren().add(group);
        if(general.Properties.getVisualizeTileCode())
            group.toBack();
    }
    
    
    public void clear() {
        dsc.getSimCanvas().getChildren().remove(group);
        group.getChildren().clear();
    }
    
    public void setColor(int nullOrOne){
        if(nullOrOne == 1){
            currentColor = Color.RED;
        }else{
            currentColor = Color.BLUE;
        }
        
        for(javafx.scene.Node n : group.getChildren()){
            Line l = (Line) n;
            l.setStroke(currentColor);
        }
    }
    
    public void resetColor(){
        currentColor = Color.GRAY;
        for(javafx.scene.Node n : group.getChildren()){
            Line l = (Line) n;
            l.setStroke(currentColor);
        }
    }
}
