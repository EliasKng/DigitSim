/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import general.Vector2i;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Elias
 */
public class AnchorPoint {
    private Vector2i coords;
    private double index;
    private List<Connection> connectedTo = new ArrayList();

    public AnchorPoint(double index, Vector2i coords) {
        this.coords = coords;
        this.index = index;
    }

    public AnchorPoint(double index, Vector2i coords, Connection c) {
        addToConnectedTo(c);
        this.coords = coords;
        this.index = index;
    }

    public AnchorPoint() {
    }

    public AnchorPoint(Vector2i coords) {
        this.coords = coords;
    }
    
    //GET / SET

    public Vector2i getCoords() {
        return coords;
    }

    public void setCoords(Vector2i coords) {
        this.coords = coords;
    }

    public double getIndex() {
        return index;
    }

    public void setIndex(double index) {
        this.index = index;
    }

    public List<Connection> getConnectedTo() {
        return connectedTo;
    }

    public void setConnectedTo(List<Connection> connectedTo) {
        this.connectedTo = connectedTo;
    }
    
    public void addToConnectedTo(Connection c) {
        this.connectedTo.add(c);
    }
    
    public void removeFromConnectedTo(Connection c) {
        this.connectedTo.remove(c);
    }
    
    public void removeFromConnectedTo(int index) {
        this.connectedTo.remove(index);
    }
}
