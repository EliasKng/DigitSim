/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import javafx.scene.paint.Color;

/**
 *
 * @author Elias
 */
public class HandleState {
    /**
     * Wandelt eine Integer in einen State: 0 = Low, 1 = High, 2 = Undefinded -> alles Andere DEFAULT
     * @param state state
     * @return State
     */
    public static State getState(int state) {
        switch(state) {
            case 0:
                return State.LOW;
             
            case 1:
                return State.HIGH;
                
            case 2:
                return State.UNDEFINED;
                
            default:
                return State.DEFAULT;
        }
    }
    
    /**
     * erzeugt aus einem State eine Integer
     * @param state
     * @return 
     */
    public static int getIntFromState(State state) {
        switch(state) {
            case LOW:
                return 0;
                
            case HIGH:
                return 1;
                
            case UNDEFINED:
                return 2;
               
            default:    //State.DEFAULT
                return -1;
        }
    }
    
    /**
     * gibt die Farbe für den jeweiligen State zurück
     * @param state
     * @return 
     */
    public static Color getColorFromState(State state) {
        switch(state) {
            case HIGH:
                return Color.RED;
            
            case LOW:
                return Color.BLUE;
                
            case UNDEFINED:
                return Color.GOLD;
                
            case DEFAULT:
                return Color.GREEN;
                
            default:
                return Color.BLACK;
        }
    }
}
