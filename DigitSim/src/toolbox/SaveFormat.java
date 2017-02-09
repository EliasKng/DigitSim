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
    int numElements;
    Type[] type;
    double[] ePosX,ePosY;
    int[] eNumInputs, eNumOutputs;
    String[] payload;
    
    //Verbindung
    int numConnections;
    int[] indexFirstElement;       // index des ersten elements im array
    boolean[] typeFirst;           // ein oder ausgang
    int[] indexFirst;              // index des jeweiligen ein oder ausgangs am ersten element
    int[] indexSecondElement;      // index des zweiten elements im array
    boolean[] typeSecond;          // ein oder ausgang
    int[] indexSecond;             // index des jeweiligen ein oder ausgangs am zweiten element
    
    public SaveFormat(int numElements, int numConnections){
        this.numElements = numElements;
        this.numConnections = numConnections;
        type = new Type[numElements];
        ePosX = new double[numElements];
        ePosY = new double[numElements];
        eNumInputs = new int[numElements];
        eNumOutputs = new int[numElements];
        payload = new String[numElements];
        
        indexFirstElement = new int[numConnections];
        typeFirst = new boolean[numConnections];
        indexFirst = new int[numConnections];
        indexSecondElement = new int[numConnections];
        typeSecond = new boolean[numConnections];
        indexSecond = new int[numConnections];
    }

    //Einstellungen
    int simSizeX;
    int simSizeY;    

    public int getNumElements() {
        return numElements;
    }

    public void setNumElements(int numElements) {
        this.numElements = numElements;
    }

    public Type[] getType() {
        return type;
    }

    public void setType(Type[] type) {
        this.type = type;
    }

    public double[] getePosX() {
        return ePosX;
    }

    public void setePosX(double[] ePosX) {
        this.ePosX = ePosX;
    }

    public double[] getePosY() {
        return ePosY;
    }

    public void setePosY(double[] ePosY) {
        this.ePosY = ePosY;
    }

    public int[] geteNumInputs() {
        return eNumInputs;
    }

    public void seteNumInputs(int[] eNumInputs) {
        this.eNumInputs = eNumInputs;
    }

    public int[] geteNumOutputs() {
        return eNumOutputs;
    }

    public void seteNumOutputs(int[] eNumOutputs) {
        this.eNumOutputs = eNumOutputs;
    }

    public String[] getPayload() {
        return payload;
    }

    public void setPayload(String[] payload) {
        this.payload = payload;
    }

    public int getNumConnections() {
        return numConnections;
    }

    public void setNumConnections(int numConnections) {
        this.numConnections = numConnections;
    }

    public int[] getIndexFirstElement() {
        return indexFirstElement;
    }

    public void setIndexFirstElement(int[] indexFirstElement) {
        this.indexFirstElement = indexFirstElement;
    }

    public boolean[] getTypeFirst() {
        return typeFirst;
    }

    public void setTypeFirst(boolean[] typeFirst) {
        this.typeFirst = typeFirst;
    }

    public int[] getIndexFirst() {
        return indexFirst;
    }

    public void setIndexFirst(int[] indexFirst) {
        this.indexFirst = indexFirst;
    }

    public int[] getIndexSecondElement() {
        return indexSecondElement;
    }

    public void setIndexSecondElement(int[] indexSecondElement) {
        this.indexSecondElement = indexSecondElement;
    }

    public boolean[] getTypeSecond() {
        return typeSecond;
    }

    public void setTypeSecond(boolean[] typeSecond) {
        this.typeSecond = typeSecond;
    }

    public int[] getIndexSecond() {
        return indexSecond;
    }

    public void setIndexSecond(int[] indexSecond) {
        this.indexSecond = indexSecond;
    }

    public int getSimSizeX() {
        return simSizeX;
    }

    public void setSimSizeX(int simSizeX) {
        this.simSizeX = simSizeX;
    }

    public int getSimSizeY() {
        return simSizeY;
    }

    public void setSimSizeY(int simSizeY) {
        this.simSizeY = simSizeY;
    }
    
}
