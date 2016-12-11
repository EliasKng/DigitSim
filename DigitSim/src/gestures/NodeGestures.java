/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gestures;

import digitsim.DigitSimController;
import digitsim.Properties;
import element.Element;
import element.Element_SIGNAL;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;

/**
 *
 * @author Elias
 */
/**
 * Listener die eine Drag & Drop - Funktin mit der linken Maustaste ermöglichen. Bedenkt auch den Zoom.
 */
public class NodeGestures {
    private static ArrayList<Element> elements = new ArrayList<>(); //Elemente
    private ContextMenu contextMenu;
    private Group temporaryGroup;

    private static boolean dragged;
    
    private DragContext nodeDragContext = new DragContext(); //Nötige Daten für Berechnungen

    DraggableCanvas canvas; //Arbeitsfläche

    public NodeGestures( DraggableCanvas canvas) { //Consturctor
        this.canvas = canvas;
        this.contextMenu = new ContextMenu(); //Menü das bei einem Rechtsklick auf ein Element angezeigt wird
        addContext();
    }
    
    //Rechtsklick Menü-Items hinzufügen
    private void addContext(){
        MenuItem deleteItem = new MenuItem("Entfernen");
        MenuItem propertiesItem = new MenuItem("Eigenschaften");
        
        deleteItem.setOnAction(new EventHandler<ActionEvent>() { //Wird ausgelößt wenn man bei einem Element "Entfernen" auswählt
            public void handle(ActionEvent e) {
               elements = DigitSimController.getReference().getElements();
               Element temp = findElement();
               if(temp != null){
                   canvas.getChildren().remove(temporaryGroup); //Die "Zeichnung" entfernen, da diese bestehen bleibt wenn das Element gelöscht wird
                   DigitSimController.getReference().getConnections().removeAllConncectionsRelatedTo(temp);
                   elements.remove(temp); //Das Element entfernen
               }
            }});      
        
        propertiesItem.setOnAction(new EventHandler<ActionEvent>() { //Wird ausgelößt wenn man bei einem Element "Eigenschaften" auswählt
            public void handle(ActionEvent e) {
               elements = DigitSimController.getReference().getElements();
               Element temp = findElement();
               if(temp != null){
                   temp.showProperties();
               }
            }});
        
        contextMenu.getItems().addAll(deleteItem, propertiesItem);
    }

    public EventHandler<MouseEvent> getOnMousePressedEventHandler() { //Liefert einen Handler
        return onMousePressedEventHandler;
    }

    public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
        return onMouseDraggedEventHandler;
    }
    
    public EventHandler<MouseEvent> getOnMouseReleasedEventHandler() {
        return onMouseReleasedEventHandler;
    }

    private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() { //Handler für "Maus klick"

        public void handle(MouseEvent event) {
            if(DigitSimController.getReference().isLocked()){ //Schauen ob das Programm blokiert ist (erklärung: siehe DigitSimController oben)
                temporaryGroup = (Group) event.getSource();
                Element temp = findElement();
                if(temp instanceof  Element_SIGNAL){ //Dass Signal and und aus schalten, per rechtsklick (geht nur wenn die Simulation läuft)
                    int i = temp.getOutput(0);
                    if(i == 1){
                        temp.setInput(0, 0);
                    }else{
                        temp.setInput(0, 1);
                    }
                }
                return;
            }
            // Rechtsklick
            if( !event.isPrimaryButtonDown()){
                temporaryGroup = (Group) event.getSource();
                contextMenu.show((Node)event.getSource(), Side.RIGHT, 0, 0); //Rechtsklick-Menü anzeigen
                event.consume(); 
            }else{//Linksklick -> Verschieben
            nodeDragContext.mouseAnchorX = event.getSceneX(); //X/Y Koordinaten der Maus speichern (später benötig)
            nodeDragContext.mouseAnchorY = event.getSceneY();

            Node node = (Node) event.getSource(); //Das betroffene Element bekommen

            nodeDragContext.translateAnchorX = node.getTranslateX(); //Die akutelle Änderungsrate speichern (später nötig)
            nodeDragContext.translateAnchorY = node.getTranslateY(); 
            }
        }

    };

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() { //Handler für "Drag"
        
        public void handle(MouseEvent event ) {
            if(DigitSimController.getReference().isLocked()) //Schauen ob das Programm blokiert ist (erklärung: siehe DigitSimController oben)
               return;
            elements = DigitSimController.getReference().getElements();

            // left mouse button => dragging
            if( !event.isPrimaryButtonDown())
                return;

            dragged = true;
            
            double scale = canvas.getScale(); //Scalierung
            
            Node node = (Node) event.getSource(); //Das betroffene Element bekommen
            
            //-Node passt sich jetzt ans Gitter an (Author: Dominik)
            //Den Abstand zwischen der alten und der neuen Mausposition berechnen und zur Änderungsrate hinzufügen
            node.setTranslateX(getValueAdaptGrid(nodeDragContext.translateAnchorX + (( event.getSceneX() - nodeDragContext.mouseAnchorX) / scale)));
            node.setTranslateY(getValueAdaptGrid(nodeDragContext.translateAnchorY + (( event.getSceneY() - nodeDragContext.mouseAnchorY) / scale)));
            
            temporaryGroup = (Group) event.getSource();
            
            DigitSimController.getReference().getConnections().drawDirectLineUpdate(findElementNum()); //Verbindungen updaten die das Akutelle Element betreffen
            event.consume(); 
        }
    };
    
    private EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() { //Handler für "Drag"
        
        public void handle(MouseEvent event ) {
            if(dragged == true) {
                dragged = false;
                DigitSimController.getReference().getConnections().drawUpdate(findElementNum()); //Verbindungen updaten die das Akutelle Element betreffen
            }
            
        }
    };
    
    //Anpassung eines beliebigen Volumen ans Gitter (Author: Dominik)
    private double getValueAdaptGrid(double pV){
        double offset = Properties.GetGridOffset();
        return Math.round(pV / offset) * offset;
    }
    
    private Element findElement(){ //Ein Element finden (über die akutelle gruppe)
         elements = DigitSimController.getReference().getElements();
         for(Element i : elements){ //Alle Elemente durchgehen, um das zu finden das Ausgewählt ist
                   if(i.getGroup().hashCode() == temporaryGroup.hashCode()){ //Der HashCode eines Objektes ist immer EINMALIG, sozusagen eine "Personalnummer", eignet sich daher gut für den "Gleichheitstest"
                       return i;
                 } 
          }
         return null;
    }
    
    private int findElementNum(){ //Die Nummer des Elements finden (über die akutelle gruppe)
         elements = DigitSimController.getReference().getElements();
         int i = -1;
         for(Element e : elements){ //Alle Elemente durchgehen, um das zu finden das Ausgewählt ist
             i++;
                   if(e.getGroup().hashCode() == temporaryGroup.hashCode()){ //Der HashCode eines Objektes ist immer EINMALIG, sozusagen eine "Personalnummer", eignet sich daher gut für den "Gleichheitstest"
                       return i;
                 } 
          }
         return -1;
    }
}
