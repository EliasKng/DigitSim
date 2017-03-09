package pathFinder;

import connection.Connection;
import digitsim.DigitSimController;
import toolbox.Draw;
import general.Properties;
import element.Element;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

    /**
     * Beschreibt das tileCodeArray mit Daten (wenn sich auf diesem Tile ein Block befindet, wird es auf 1 gesetzt
     * @author Elias
     */
public class TileCode {
    private static int gridOffset = Properties.GetGridOffset();
    private static int[][] tileCode;
    private static int simSizeX = Properties.GetSimSizeX();
    private static int simSizeY = Properties.GetSimSizeY();
    private static Group visualizeNodes = new Group();

    public static void createTileCode(ArrayList<Element> elements, List<Connection> connections, boolean visualize) {
        if(visualize) {
            DigitSimController.getReference().getSimCanvas().getChildren().removeAll(visualizeNodes);
            clearVisualizeGroup();
        }
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
            
            for(int k = eX-1; k < (eX + eWidth+1); k++) {
                for(int o = eY-1; o < (eY + eHeight+1); o++) {
                    int value;
                    if((k >= eX && k < (eX+eWidth)) && (o >= eY && o < (eY + eHeight))) {
                        if(k == eX || k == (eX + eWidth -1)) {
                            value = 2;  //IO Bereich eines Elements
                            if(visualize)
                                visualizeNodes.getChildren().add(Draw.drawCircle(k*21+11.5, o*21+11.5, 3, Color.CORAL, 1, true, 1));
                        } else {
                            value = 1;  //Element
                            if(visualize)
                                visualizeNodes.getChildren().add(Draw.drawCircle(k*21+11.5, o*21+11.5, 3, Color.BLACK, 1, true, 1));
                        }
                    } else {
                        value = 4;      //ElementArea
                        if(visualize)
                                visualizeNodes.getChildren().add(Draw.drawCircle(k*21+11.5, o*21+11.5, 3, Color.BLUE, 1, true, 1));
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
        
        if(connections.size() > 1) {
            for(Connection c : connections) {
                for(int i = 0; i < c.getLineGroup().getChildren().size(); i++) {
                    List lineGroup = c.getAllNodesFromLineGroup();
                    Line line = (Line) lineGroup.get(i);
                    int startX = (int) line.getStartX()/21;
                    int startY = (int) line.getStartY()/21;
                    
                    try {
                        if((tileCode[startX][startY] == 0) || 3 < tileCode[startX][startY]) {
                                tileCode[startX][startY] = 3;
                                if(visualize)
                                    visualizeNodes.getChildren().add(Draw.drawCircle(startX*21+11.5, startY*21+11.5, 3, Color.RED, 1, true, 1));
                            }
                    } catch (Exception e) {
                        System.out.println("Fehler beim Erstellen des Arrays (PATHFINDER) (ConnectionToArray)");
                    }
                }
            }
        }
        
//        if(connections.size() > 1) {
//            for(Connection c : connections) {
//                if(!c.isDirectLine()) {
//                    for(Node n : c.getNodePath()) {
//                        try {
//                            if(tileCode[n.tile.getX()][n.tile.getY()] == 0 || 3 < tileCode[n.tile.getX()][n.tile.getY()]) {
//                                tileCode[n.tile.getX()][n.tile.getY()] = 3;
//                            }
//                        } catch (Exception e) {
//                        System.out.println("Fehler beim Erstellen des Arrays (PATHFINDER) (ConnectionToArray)");
//                        }
//                    }
//                }
//            }
//        }
        
        
        
        if(visualize)
            DigitSimController.getReference().getSimCanvas().getChildren().addAll(visualizeNodes);
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
    
    public static void visualizePath(List<Node> path) {
        DigitSimController.getReference().getSimCanvas().getChildren().removeAll(visualizeNodes);
        for(Node currentNode : path) {
            if(currentNode.parent != null) {
                        
                double thisX = currentNode.tile.getX() * gridOffset+11.5;
                double thisY = currentNode.tile.getY() * gridOffset+11.5;
                        
                visualizeNodes.getChildren().add(Draw.drawCircle(thisX, thisY, 3, Color.RED, 1, true, 1));
            }
        }
        
        DigitSimController.getReference().getSimCanvas().getChildren().addAll(visualizeNodes);
    }
    
    private static void clearVisualizeGroup() {
        visualizeNodes.getChildren().clear();
    }
}
