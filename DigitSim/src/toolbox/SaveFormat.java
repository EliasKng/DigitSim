/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package toolbox;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Dominik
 * - Benötigt zum Speichern/Laden von Projekten
 */
@XmlRootElement
public class SaveFormat implements element.ElementType{
    //Element
    int numElements; //Anzahl Elemente
    String[] type; //Typ des elements
    double[] ePosX,ePosY; //Position
    int[] eNumInputs, eNumOutputs; //Anzahl in- & outputs
    String[] payload; //Payload (siehe Element.java)
    
    
    //Connection(2xPartner)
    int numConnections;
    String[][] conType;
    //Falls Element:
    int[][] conElementIndex;
    boolean[][] conInOrOutput;
    int[][] conIOIndex;
    
    //Falls Connection + Anchor Point
    int[][] conIndex;
    double[][] conX;  //Anchor Point
    double[][] conY; //Anchor Point
    
    public SaveFormat(){
        
    }
    
    public SaveFormat(int numElements, int numConnections){
        this.numElements = numElements;
        this.numConnections = numConnections;
        type = new String[numElements];
        ePosX = new double[numElements];
        ePosY = new double[numElements];
        eNumInputs = new int[numElements];
        eNumOutputs = new int[numElements];
        payload = new String[numElements];
        conType = new String[numConnections][2];
        conElementIndex = new int[numConnections][2];
        conInOrOutput = new boolean[numConnections][2];
        conIOIndex = new int[numConnections][2];
        conIndex = new int[numConnections][2];
        conX = new double[numConnections][2];
        conY = new double[numConnections][2];
    }

    //Einstellungen
    int simSizeX;
    int simSizeY;    
    @XmlElement
    public int getNumConnections() {
        return numConnections;
    }

    public void setNumConnections(int numConnections) {
        this.numConnections = numConnections;
    }
    
    @XmlElement
    public String[][] getConType() {
        return conType;
    }

    public void setConType(String[][] conType) {
        this.conType = conType;
    }
    @XmlElement
    public int[][] getConElementIndex() {
        return conElementIndex;
    }

    public void setConElementIndex(int[][] conElementIndex) {
        this.conElementIndex = conElementIndex;
    }
    @XmlElement
    public boolean[][] getConInOrOutput() {
        return conInOrOutput;
    }

    public void setConInOrOutput(boolean[][] conInOrOutput) {
        this.conInOrOutput = conInOrOutput;
    }
    @XmlElement
    public int[][] getConIOIndex() {
        return conIOIndex;
    }

    public void setConIOIndex(int[][] conIOIndex) {
        this.conIOIndex = conIOIndex;
    }
    @XmlElement
    public int[][] getConIndex() {
        return conIndex;
    }

    public void setConIndex(int[][] conIndex) {
        this.conIndex = conIndex;
    }
    @XmlElement
    public double[][] getConX() {
        return conX;
    }

    public void setConX(double[][] conX) {
        this.conX = conX;
    }
    @XmlElement
    public double[][] getConY() {
        return conY;
    }

    public void setConY(double[][] conY) {
        this.conY = conY;
    }
 
    @XmlElement
    public int getNumElements() {
        return numElements;
    }

    public void setNumElements(int numElements) {
        this.numElements = numElements;
    }

    @XmlElement
    public String[] getType() {
        return type;
    }

    public void setType(String[] type) {
        this.type = type;
    }
@XmlElement
    public double[] getePosX() {
        return ePosX;
    }

    public void setePosX(double[] ePosX) {
        this.ePosX = ePosX;
    }
@XmlElement
    public double[] getePosY() {
        return ePosY;
    }

    public void setePosY(double[] ePosY) {
        this.ePosY = ePosY;
    }
@XmlElement
    public int[] geteNumInputs() {
        return eNumInputs;
    }

    public void seteNumInputs(int[] eNumInputs) {
        this.eNumInputs = eNumInputs;
    }
@XmlElement
    public int[] geteNumOutputs() {
        return eNumOutputs;
    }

    public void seteNumOutputs(int[] eNumOutputs) {
        this.eNumOutputs = eNumOutputs;
    }
@XmlElement
    public String[] getPayload() {
        return payload;
    }

    public void setPayload(String[] payload) {
        this.payload = payload;
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
    
}
