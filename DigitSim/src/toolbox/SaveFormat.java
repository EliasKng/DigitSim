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
    
    public SaveFormat(){
        
    }
    
    public SaveFormat(int numElements, int numConnections){
        this.numElements = numElements;
        type = new String[numElements];
        ePosX = new double[numElements];
        ePosY = new double[numElements];
        eNumInputs = new int[numElements];
        eNumOutputs = new int[numElements];
        payload = new String[numElements];
    }

    //Einstellungen
    int simSizeX;
    int simSizeY;    

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
