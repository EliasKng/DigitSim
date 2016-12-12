/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import connection.Connection;
import digitsim.DigitSimController;
import digitsim.Draw;
import digitsim.GenFunctions;
import digitsim.Properties;
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
    private Vector2i start;
    private Vector2i end;
    private final int gridOffset = Properties.GetGridOffset();
    private Color currentColor = Color.GREY; //Standart: Schwarz
    
    
    public ConnectionLine(Vector2i _start, Vector2i _end, DigitSimController d) {
        dsc = d;
        pathFinder = new PathFinder();
        start = _start.divide(gridOffset);
        end = _end.divide(gridOffset);
    }
    
    public ConnectionLine(DigitSimController d) {
        dsc = d;
        pathFinder = new PathFinder();
        start = new Vector2i();
        end = new Vector2i();
    }
    
//*******************SET /GET***************************/
    
    public Vector2i getStart() {
        return start;
    }

    public void setStart(Vector2i start) {
        this.start = start.divide(gridOffset);
    }

    public Vector2i getEnd() {
        return end;
    }

    public void setEnd(Vector2i end) {
        this.end = end.divide(gridOffset);
    }

    public List<Node> getPath() {
        return path;
    }

    public Group getGroup() {
        return group;
    }
    
    
    
    
    
    public void update(boolean directLine,ArrayList<Connection.ConData> connections) {
        clear();
        
        if(!directLine) {
            path = pathFinder.findPath(start, end, dsc.getElements(), connections);
            if(path != null) {
                for(Node currentNode : path) {
                    if(currentNode.parent != null) {
                        
                        int thisX = currentNode.tile.getX() * gridOffset;
                        int thisY = currentNode.tile.getY() * gridOffset;
                        int parentX = currentNode.parent.tile.getX() * gridOffset;
                        int parentY = currentNode.parent.tile.getY() * gridOffset;
                        
                        group.getChildren().add(Draw.drawLine(parentX + 10.5, parentY + 10.5, thisX + 10.5, thisY + 10.5, currentColor, Properties.getLineWidth()));
                    }
                }
            
            } else {
                group.getChildren().add(Draw.drawLine(start.getX() * gridOffset + 10.5, start.getY()  * gridOffset + 10.5, end.getX() * gridOffset + 10.5 ,end.getY() * gridOffset + 10.5, currentColor,  Properties.getLineWidth()));
            }
            group.addEventFilter(MouseEvent.MOUSE_ENTERED, GenFunctions.getOverNodeMouseHanlderEnterLineGrp());
            group.addEventFilter(MouseEvent.MOUSE_EXITED, GenFunctions.getOverNodeMouseHanlderExitLineGrp());
        } else {
            Line l = Draw.drawLine(start.getX() * gridOffset + 10.5, start.getY() * gridOffset + 10.5, end.getX() * gridOffset + 10.5, end.getY() * gridOffset + 10.5, Color.DARKORANGE, Properties.getLineWidth());
            group.getChildren().add(l);
        }
        dsc.getSimCanvas().getChildren().add(group);
    }
    
    public void clear() {
        dsc.getSimCanvas().getChildren().remove(group);
        group.getChildren().clear();
    }
    
    public void setColor(int nullOrOne){
        if(nullOrOne == 1){
            currentColor = Color.RED;
        }else{
            currentColor = Color.GRAY;
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
