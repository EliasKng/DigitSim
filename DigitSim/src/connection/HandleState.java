/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import general.State;
import javafx.scene.paint.Color;

/**
 *
 * @author Elias
 */
public class HandleState {
    private static final int aND[][] = {{0,0,0,0},{0,1,3,3},{0,3,3,3},{0,3,3,3}};
    private static  final int oR[][] =  {{0,1,3,3},{1,1,1,1},{3,1,3,3},{3,3,3,3}};
    private static final int xOR[][] = {{0,1,3,3},{1,0,3,3},{3,3,3,3},{3,3,3,3}};
    private static final int nOT[] = {1,0,3,3};
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
                
            case 3:
                return State.NULL;                   
                
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
               
            case NULL:
                return 3;
                
            case DEFAULT:
                return 4;
                
            default:
                return 4;
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
                
            case NULL:
                return Color.GRAY;
                
            default:
                return Color.BLACK;
        }
    }
    
    public static State logicAND(State s0, State s1) {
        int sInt0 = getIntFromState(s0);
        int sInt1 = getIntFromState(s1);
        int res = aND[sInt0][sInt1];
        return getState(res);
    }
    
    public static State logicNAND(State s0, State s1) {
        int sInt0 = getIntFromState(s0);
        int sInt1 = getIntFromState(s1);
        int res0 = aND[sInt0][sInt1];
        int res = nOT[res0];
        return getState(res);
    }
    
    public static State logicOR(State s0, State s1) {
        int sInt0 = getIntFromState(s0);
        int sInt1 = getIntFromState(s1);
        int res = oR[sInt0][sInt1];
        return getState(res);
    }
    
    public static State logicNOR(State s0, State s1) {
        int sInt0 = getIntFromState(s0);
        int sInt1 = getIntFromState(s1);
        int res0 = oR[sInt0][sInt1];
        int res = nOT[res0];
        return getState(res);
    }
    
    public static State logicXOR(State s0, State s1) {
        int sInt0 = getIntFromState(s0);
        int sInt1 = getIntFromState(s1);
        int res0 = xOR[sInt0][sInt1];
        int res = nOT[res0];
        return getState(res);
    }
    
    public static State logicXNOR(State s0, State s1) {
        int sInt0 = getIntFromState(s0);
        int sInt1 = getIntFromState(s1);
        int res = xOR[sInt0][sInt1];
        return getState(res);
    }
    
    public static State logicNOT(State s) {
        int sInt = getIntFromState(s);
        int res = nOT[sInt];
        return getState(res);
    }
    
}
