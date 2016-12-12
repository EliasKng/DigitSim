package pathFinder;

import connection.Connection;
import digitsim.Properties;
import element.Element;
import java.util.ArrayList;
import java.util.List;

    /**
     * Beschreibt das tileCodeArray mit Daten (wenn sich auf diesem Tile ein Block befindet, wird es auf 1 gesetzt
     * @author Elias
     */
public class TileCode {
    private static int gridOffset = Properties.GetGridOffset();
    private static int[][] tileCode;
    private static int simSizeX = Properties.GetSimSizeX();
    private static int simSizeY = Properties.GetSimSizeY();

    public static void createTileCode(ArrayList<Element> elements, ArrayList<Connection.ConData> connections) {
        int arrayWidth = (int) Math.ceil(simSizeX / gridOffset); //Berechnet den Wert für die Weite den Arrays (jedes Kästchen soll 1 arrayplatz belegen), desshalb wird die gesamthöhe genommen, durch 21 geteilt und dann aufgerundet
        int arrayHeight = (int) Math.ceil(simSizeY / gridOffset);
        tileCode = new int[arrayWidth][arrayHeight]; //In diesem 2 dimensionalen array, wird gespeichert ob ein Kästchen (z.B. durch ein Element) geblockt wird
        int eWidth = 6; //Weite der elemente (in 21 schritten) (ist 6 weil die Ausgänge & Eingänge mit dazu gezählt werden)
        int eHeight; //Höhe der elemente (in 21 schritten)
        int eX; //X-Koordinate des Elements (in gridOffset Schritten)
        int eY; //Y-Koordinate des Elements (in gridOffset Schritten)
        
        //Diese Schleife wird für jedes Element einmal durchlaufen
        for(Element i : elements){ //elemente durchgehen...
            eHeight = getElementHeight(i.getInputCount());
            eX = (int) i.getX() / gridOffset - 1;
            eY = (int) i.getY() / gridOffset;
            
            for(int k = eX-1; k < (eX + eWidth+2); k++) {
                for(int o = eY-1; o < (eY + eHeight+2); o++) {
                    int value;
                    if((k >= eX && k < (eX+eWidth)) && (o >= eY && o < (eY + eHeight))) {
                        if(k == eX || k == (eX + eWidth -1)) {
                            value = 2;  //IO Bereich eines Elements
                        } else {
                            value = 1;  //Element
                        }
                    } else {
                        value = 4;      //ElementArea
                    }
                    try {
                        if((tileCode[k][o] == 0) || value < tileCode[k][o]) {
                            tileCode[k][o] = value;
                        }
                    }catch(Exception e) {
                        System.out.println("Unstimmigkeit beim Erstellen des Arrays (PATHFINDER)");
                    }
                    
                    
                }
            }
        }
        
        for(Connection.ConData c : connections) {
            
        }
    }
    
    
    
    /**
     * berechnet die Höhe des standard Elements anhand seiner Anzahl con Eingängen
     * @param inputs
     * @return 
     */
    public static int getElementHeight(int inputs) {
        int h = 4;
        if (inputs > 4) {
            h=inputs;
        }
        return h;
    }
    
    /**
     * Hat noch keine Funktion, soll später aber überprüfen, ob das "Tile" (bei und ein Kästchen (21 x 21px)) vorhanden ist bisher wird nur geprüft ob das Tile oben oder links aus dem Feld geht
     * @return 
     */
    public static boolean isTileAvailible(int x, int xi, int y, int yi) {
        boolean result = true;
        if((x+xi)<0 || (y+yi)<0) {
            result = false;
        }
        return result;
    }
    
    /**
     * Wenn sich über diesem Tile ein Baustein befindet wird true zurück gegeben
     * @return 
     */
    public static boolean isTileSolid(int x, int xi, int y, int yi) {
        boolean result = false;
        int status = tileCode[x+xi][y+yi];
        if(status == 1) {
            result = true;
        }
        return result;
    }
    
    /**
     * Wenn sich über diesem Tile ein oder Ausgänge eines Elements befinden, wird true zurückgegeben
     * @return 
     */
    public static boolean isTileInIOArea(int x, int xi, int y, int yi) {
        boolean result = false;
        int status = tileCode[x+xi][y+yi];
        if(status == 2) {
            result = true;
        }
        return result;
    }
    
    /**
     * Wenn sich über diesem Tile eine andere Verbindung liegt, wird true zurückgegeben
     * @return 
     */
    public static boolean isTileOverOtherConnection(int x, int xi, int y, int yi) {
        boolean result = false;
        int status = tileCode[x+xi][y+yi];
        if(status == 3) {
            result = true;
        }
        return result;
    }
    
    /**
     * Wenn sich über diesem Tile das Umfeld einess Elements befindet (Umfeld entspricht ein Block größer als das Element), wird true returnt
     * @return 
     */
    public static boolean isTileInElementArea(int x, int xi, int y, int yi) {
        boolean result = false;
        int status = tileCode[x+xi][y+yi];
        if(status == 4) {
            result = true;
        }
        return result;
    }
}
