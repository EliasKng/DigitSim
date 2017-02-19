/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general;

/**
 *
 * @author Elias
 */
public class Vector2i {
    private int x, y;
    
    public Vector2i() {
        set(0, 0);
    }
    
    public Vector2i(Vector2i vector) {
        set(vector.x, vector.y);
    }
    
    public Vector2i(int x, int y) {
        set(x, y);
    }
    
    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public Vector2i add(Vector2i vector) {
        this.x += vector.x;
        this.y += vector.y;
        return this;
    }
    
    public Vector2i substract(Vector2i vector) {
        this.x -= vector.x;
        this.y -= vector.y;
        return this;
    }
    
    public Vector2i setX(int x) {
        this.x = x;
        return this;
    }
    
    public Vector2i setY(int y) {
        this.y = y;
        return this;
    }
    
    public Vector2i divide(int value) {
        this.x /= value;
        this.y /= value;
        
        return this;
    }
    
    public boolean equals(Object object) {
        if(!(object instanceof Vector2i)) return false;
        Vector2i vec = (Vector2i) object;
        if(vec.getX() == this.getX() && vec.getY() == this.getY()) return true;
        return false;
    }
}
