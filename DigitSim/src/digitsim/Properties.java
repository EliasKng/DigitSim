/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;

import javafx.scene.paint.Color;

/**
 *
 * @author Elias, Tim
 */
public class Properties {
    
    // Damit änderungen sichtbar werden muss man unter Run auf "Clean and Build Project" klicken!
    
    // Simgrid Stuff
    private static final int simSizeX = 4000;
    private static final int simSizeY = 4000;    
    private static final int gridOffset = 21;
    private static Color gridColor = Color.LIGHTGREY;
    
    // Thread Stuff
    private static final int threadDurationMS = 50;
    
    // Window Stuff
    private static final int windowMinX = 800;
    private static final int windowMinY = 600;
    
    
    // Getters
    public static int GetSimSizeX()
    {
        return simSizeX;
    }
    
    public static int GetSimSizeY()
    {
        return simSizeY;
    }
    
    public static int GetGridOffset()
    {
        return gridOffset;
    }
    
    public static Color GetGridColor()
    {
        return gridColor;
    }
    
    public static int GetThreadDurationMS()
    {
        return threadDurationMS;
    }
    public static int GetWindowMinX()
    {
        return windowMinX;
    }
    
    public static int GetWindowMinY()
    {
        return windowMinY;
    }
}
