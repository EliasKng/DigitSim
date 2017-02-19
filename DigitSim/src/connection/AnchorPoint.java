/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import general.Vector2i;

/**
 *
 * @author Elias
 */
public class AnchorPoint {
    Vector2i coords;
    int index;

    public AnchorPoint(int index, Vector2i coords) {
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

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
    
}
