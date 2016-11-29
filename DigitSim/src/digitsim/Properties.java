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
    //DIES HIER SIND DIE DEFAULT WERTE! falls es noch keine propetie datei gibt. (Wenn man das Programm zum ersten mal benutzt)
    //Wer hier neue Einszellungen einfügt muss diese auch bei save, load und in PropertiesXmlFormat.java einfügen!
    // Simgrid Stuff
    private static int simSizeX = 4000;
    private static int simSizeY = 4000;    
    private static int gridOffset = 21;
    private static Color gridColor = Color.LIGHTGREY;
    
    // Thread Stuff
    private static int threadDurationMS = 50;
    
    // Window Stuff
    private static int windowMinX = 800;
    private static int windowMinY = 600;
    
    //Element Stuff
    private static double elementOpacity = 0.4;
    private static int lineWidth = 5;
    
    
        
    public static void save(){ //Einstellungen speichern
        PropertiesXmlFormat saveFile = new PropertiesXmlFormat(simSizeX, simSizeY, gridOffset, gridColor, threadDurationMS, windowMinX, windowMinY);
        XmlLoader.saveObject("Properties.semiProp", saveFile);
    }
    
    public static void load(){ //Einstellungen laden
        if(!XmlLoader.fileExist("Properties.semiProp"))
            return;
        PropertiesXmlFormat saveFile = (PropertiesXmlFormat) XmlLoader.loadObject("Properties.semiProp", PropertiesXmlFormat.class);
        simSizeX = saveFile.getSimSizeX();
        simSizeY = saveFile.getSimSizeY();
        gridOffset = saveFile.getGridOffset();
        gridColor = saveFile.convertGridColor();
        threadDurationMS = saveFile.getThreadDurationMS();
        windowMinX = saveFile.getWindowMinX();
        windowMinY = saveFile.getWindowMinY();
    }

    public static double getElementOpacity() {
        return elementOpacity;
    }

    public static void setElementOpacity(double ElementOpacity) {
        Properties.elementOpacity = ElementOpacity;
    }

    public static void setSimSizeX(int simSizeX) {
        Properties.simSizeX = simSizeX;
        save();
    }

    public static void setSimSizeY(int simSizeY) {
        Properties.simSizeY = simSizeY;
        save();
    }

    public static void setGridOffset(int gridOffset) {
        Properties.gridOffset = gridOffset;
        save();
    }

    public static void setGridColor(Color gridColor) {
        Properties.gridColor = gridColor;
        save();
    }

    public static void setThreadDurationMS(int threadDurationMS) {
        Properties.threadDurationMS = threadDurationMS;
        save();
    }

    public static void setWindowMinX(int windowMinX) {
        Properties.windowMinX = windowMinX;
        save();
    }

    public static void setWindowMinY(int windowMinY) {
        Properties.windowMinY = windowMinY;
        save();
    }
    
    public static void setLineWidth(int width) {
        Properties.lineWidth = width;
        save();
    }
    
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

    public static int getLineWidth() {
        return lineWidth;
    }
    
    
}
