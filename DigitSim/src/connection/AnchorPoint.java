/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import pathFinder.Vector2i;

/**
 *
 * @author Elias
 */
public class AnchorPoint {
    private int index;
    private Vector2i coords;

    public AnchorPoint(int index, Vector2i coords) {
        this.index = index;
        this.coords = coords;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
    public void incrementIndex() {
        this.index++;
    }

    public Vector2i getCoords() {
        return coords;
    }

    public void setCoords(Vector2i coords) {
        this.coords = coords;
    }
    
}
