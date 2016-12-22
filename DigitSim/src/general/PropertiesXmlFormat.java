/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package general;

import javafx.scene.paint.Color;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *Klasse wird benötigt um die Einstellungen als XML abzubilden, für informationen -> Google: JAXB
 * @author Dominik
 */
@XmlRootElement //Das Wurzelelement
public class PropertiesXmlFormat {
    private int simSizeX;
    private int simSizeY;    
    private int gridOffset;
    private double blue, red, green, alpha;
    private int threadDurationMS;
    private int windowMinX;
    private int windowMinY;
    private boolean askOnExit;

    @XmlElement
    public boolean getAskOnExit() {
        return askOnExit;
    }

    public void setAskOnExit(boolean askOnExit) {
        this.askOnExit = askOnExit;
    }

    @XmlElement //Element, besteht jeweils aus set + get Funktionen
    public double getBlue() {
        return blue;
    }

    public void setBlue(double blue) {
        this.blue = blue;
    }

    @XmlElement
    public double getRed() {
        return red;
    }

    public void setRed(double red) {
        this.red = red;
    }

    @XmlElement
    public double getGreen() {
        return green;
    }

    public void setGreen(double green) {
        this.green = green;
    }

    @XmlElement
    public double getAlpha() {
        return alpha;
    }

    public void setAlpha(double alpha) {
        this.alpha = alpha;
    }
    
    public Color convertGridColor(){
        return new Color(red, green, blue, alpha);
    }

    //Constructor (JAXB braucht einen default constructor (leeren c.))
    public PropertiesXmlFormat(){}
    
    public PropertiesXmlFormat(int simSizeX, int simSizeY, int gridOffset, Color color, int threadDurationMS, int windowMinX, int windowMinY, boolean pAskOnExit){
        this.simSizeX = simSizeX;
        this.simSizeY = simSizeY;
        this.gridOffset = gridOffset;
        this.threadDurationMS = threadDurationMS;
        this.windowMinX = windowMinX;
        this.windowMinY = windowMinY;
        this.blue = color.getBlue();
        this.green = color.getGreen();
        this.red = color.getRed();
        this.alpha = color.getOpacity();
        this.askOnExit = pAskOnExit;
    } 

    @XmlElement
    public int getSimSizeX() {
        return simSizeX;
    }

    public void setSimSizeX(int simSizeX) {
        this.simSizeX = simSizeX;
    }

    @XmlElement
    public int getSimSizeY() {
        return simSizeY;
    }

    public void setSimSizeY(int simSizeY) {
        this.simSizeY = simSizeY;
    }

    @XmlElement
    public int getGridOffset() {
        return gridOffset;
    }

    public void setGridOffset(int gridOffset) {
        this.gridOffset = gridOffset;
    }

    @XmlElement
    public int getThreadDurationMS() {
        return threadDurationMS;
    }

    public void setThreadDurationMS(int threadDurationMS) {
        this.threadDurationMS = threadDurationMS;
    }

    @XmlElement
    public int getWindowMinX() {
        return windowMinX;
    }

    public void setWindowMinX(int windowMinX) {
        this.windowMinX = windowMinX;
    }

    @XmlElement
    public int getWindowMinY() {
        return windowMinY;
    }

    public void setWindowMinY(int windowMinY) {
        this.windowMinY = windowMinY;
    }
    

}
