/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;


import javafx.scene.Group;
import javafx.scene.paint.Color;

/**
 *Diese Klasse (wird) dazu dienen Verbindungslinien zwischen Bausteinen zu erstellen und zu optimieren
 * @author Elias
 */
public class ConnectionLine {
    private double lWidth = 2;  //Liniendicke
    private Color lColor = Color.BLACK;   //Linienfarbe
    private double[] lX;    //In diesem Array stecken alle X Koordinaten der Linie
    private double[] lY;    //In diesem Array stecken alle Y Koordinaten der Linie
    private Group group = new Group();  //Stelld die Gruppe dar, in welcher die einzelnen Linien sind
    
    /**
     * Erstellt ein Objekt von dieser Klasse mit standard Attributen
     * @param lX In diesem Array stecken alle X Koordinaten der Linie
     * @param lY In diesem Array stecken alle Y Koordinaten der Linie
     */
    public ConnectionLine(double lX[], double lY[]) {
        this.lX = new double[lX.length];
        this.lY = new double[lY.length];
        this.lX = lX;
        this.lY = lY;
        this.lWidth = 2;
        createGroup();
    }
    
     /**
     * Erstellt ein Objekt von dieser Klasse mit standard Attributen
     * @param reserved größe des arrays wenn man ohne koordinaten erstellen will
     * @param lWidth gitb die Dicke der Linie an
     */
    public ConnectionLine(int reserved, double lWidth) {
        this.lX = new double[reserved];
        this.lY = new double[reserved];

        this.lWidth = lWidth;
        createGroup();
    }
    
    /**
     * Erstellt ein Objekt von dieser Klasse mit standard Attributen
     * @param lX In diesem Array stecken alle X Koordinaten der Linie
     * @param lY In diesem Array stecken alle Y Koordinaten der Linie
     * @param lWidth gitb die Dicke der Linie an
     */
    public ConnectionLine(double lX[], double lY[], double lWidth) {
        this.lX = new double[lX.length];
        this.lY = new double[lY.length];
        this.lX = lX;
        this.lY = lY;
        this.lWidth = lWidth;
        createGroup();
    }
    
    
    //*************SET/GET*********************
    public double getlWidth() {
        return lWidth;
    }

    public void setlWidth(double lWidth) {
        this.lWidth = lWidth;
    }

    public Color getlColor() {
        return lColor;
    }

    public void setlColor(Color lColor) {
        this.lColor = lColor;
    }

    public double[] getlX() {
        return lX;
    }

    public void setlX(double[] lX) {
        this.lX = lX;
    }

    public double[] getlY() {
        return lY;
    }

    public void setlY(double[] lY) {
        this.lY = lY;
    }
    
    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
    //*******************************************

    
    
    /**
     * Erstellt eine Gruppe bestehend aus Linien. DIe Koordinaten der Linien werden aus lX[] und lY[] genommen
     * @author Elias
     */
    private void createGroup() {
        if(Draw.checkArraySameLength(lX, lY)) {
            for (int i =0; i < (lX.length-1); i ++) {
                this.group.getChildren().add(Draw.drawLine(GenFunctions.getXYAdaptGrid(lX[i]), GenFunctions.getXYAdaptGrid(lY[i]),GenFunctions.getXYAdaptGrid(lX[i+1]) , GenFunctions.getXYAdaptGrid(lY[i+1]), lColor, lWidth));
                DigitSimController.getSimCanvas().getChildren().add(group);
            }
        }
    }
    
    /**
     * löscht die vorhandene Gruppe und erstellt diese neu (wenn z.B. die Farbe geändert wurde ec.)
     * @author Elias
     */
    public void clearGroup()
    {
        group.getChildren().clear();
        DigitSimController.getSimCanvas().getChildren().remove(group);
    }
    public void update() {
        
        createGroup();
    }
    
    
}
