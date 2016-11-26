/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gestures;

/**
 * Maus drag context wird für nodes & scene gestures benötigt, um den Unterscied der Änderungsraten zu berechnen
 */
public class DragContext {

    double mouseAnchorX; 
    double mouseAnchorY;

    double translateAnchorX; 
    double translateAnchorY;

}
