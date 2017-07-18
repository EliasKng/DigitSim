/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import general.State;
import java.util.List;
import javafx.application.Platform;
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
                Platform.runLater(digitsim.DigitSimController::outputMessage_UNDEFINED);
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
    
    /**
     * Verknupft zwei States logisch mit "And"
     * @param s0
     * @param s1
     * @return 
     */
    public static State logicAND(State s0, State s1) {
        int sInt0 = getIntFromState(s0);
        int sInt1 = getIntFromState(s1);
        int res = aND[sInt0][sInt1];
        return getState(res);
    }
    
    /**
     * Verknuepft saemtliche States logisch mit "And"
     */
    public static State logicNAND(List<State> states) {
//        int finalState = getIntFromState(states[0]);
//        for(int i = 1; i < states.length -1; i++) {
//            finalState = aND[finalState][getIntFromState(states[i])];
//        }
//      S
        State finalState = null;
        State interimresult = null;
        for(State s : states) {
            if(interimresult == null) {
                interimresult = s;
                continue;
            }
            
                System.out.println(finalState);
                System.out.println(s);
                System.out.println();
                interimresult = getState(aND[getIntFromState(finalState)][getIntFromState(s)]);
                finalState = getState(nOT[getIntFromState(interimresult)]);
            
        }

        
        return finalState;
    }
    
    /**
     * Verknuepft saemtliche States logisch mit "And"
     * Danach invertiert es das Ergebnis die NAND Logik wird zurueckgegeben
     */
    public static State logicAND(List<State> states) {
        State finalState = null;
        for(State s : states) {
            if(finalState == null) {
                finalState = s;
                continue;
            }
            
                System.out.println(finalState);
                System.out.println(s);
                System.out.println();
                finalState = getState(aND[getIntFromState(finalState)][getIntFromState(s)]);
            
        }

        
        return finalState;
    }
    
    /**
     * Verknuepft saemtliche States logisch mit "OR"
     * Danach invertiert es das Ergebnis die NOR Logik wird zurueckgegeben
     */
    public static State logicNOR(List<State> states) {
      State finalState = null;
        for(State s : states) {
            if(finalState == null) {
                finalState = s;
                continue;
            }
            
                System.out.println(finalState);
                System.out.println(s);
                System.out.println();
                finalState = getState(oR[getIntFromState(finalState)][getIntFromState(s)]);
                finalState = getState(nOT[getIntFromState(finalState)]);
        }

        
        return finalState;  
    }
    
    /**
     * Verknuepft saemtliche States logisch mit "OR"
     */
    public static State logicOR(List<State> states) {
        State finalState = null;
        for(State s : states) {
            if(finalState == null) {
                finalState = s;
                continue;
            }
            
                System.out.println(finalState);
                System.out.println(s);
                System.out.println();
                finalState = getState(oR[getIntFromState(finalState)][getIntFromState(s)]);
            
        }

        
        return finalState; 
    }
    
    /**
     * Verknuepft saemtliche States logisch mit "XOR"
     */
    public static State logicXOR(List<State> states) {
        State finalState = null;
        for(State s : states) {
            if(finalState == null) {
                finalState = s;
                continue;
            }
            
                System.out.println(finalState);
                System.out.println(s);
                System.out.println();
                finalState = getState(xOR[getIntFromState(finalState)][getIntFromState(s)]);
            
        }

        
        return finalState;
    }
    
    /**
     * Verknuepft saemtliche States logisch mit "XOR"
     * Danach invertiert es das Ergebnis die XNOR Logik wird zurueckgegeben
     */
    public static State logicXNOR(List<State> states) {
        State finalState = null;
        State interimresult = null;
        for(State s : states) {
            if(interimresult == null) {
                interimresult = s;
                continue;
            }
            
                System.out.println(finalState);
                System.out.println(s);
                System.out.println();
                interimresult = getState(xOR[getIntFromState(finalState)][getIntFromState(s)]);
                finalState = getState(nOT[getIntFromState(interimresult)]);
            
        }

        
        return finalState;
    }
    
    /**
     * Verknupft einen State logisch mit "Not"
     * @param s
     * @return 
     */
    public static State logicNOT(State s) {
        int sInt = getIntFromState(s);
        int res = nOT[sInt];
        return getState(res);
    }
    
    /**
     * Komplementiert einen State
     * @param s
     * @return 
     */
    public static State cplState(State s) {
        if(s == State.HIGH)
            return State.LOW;
        else
            return State.HIGH;
    }
    public static State logicDTFF(State s0, State s1) {
        int sInt0 = getIntFromState(s0);
        int sInt1 = getIntFromState(s1);
        int res = oR[sInt0][sInt1];
        return getState(res);
    }
    public static State logicFULLADDER(State s0, State s1) {
        int sInt0 = getIntFromState(s0);
        int sInt1 = getIntFromState(s1);
        int res = oR[sInt0][sInt1];
        return getState(res);
    }
}
