/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package element;

/**
 *
 * @author Dominik 
 * - ALLE NEUEN ELEMENTE MÜSSEN HIER MIT DEM NAMEN REIN!!!
 */
public interface ElementType { 
    public enum Type{
        ELEMENT, SEVENSEG, AND, LED, NAND, NOR, NOT, OR, SIGNAL, TEXT, THUMBSWITCH, XNOR, XOR; //SEVENSEG weil man keine Zahlen an den Anfang schreiben darf!!!
    };
}
