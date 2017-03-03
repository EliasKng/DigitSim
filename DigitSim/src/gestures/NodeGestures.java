/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gestures;

import connection.Connection;
import connection.ConnectionHandler;
import digitsim.DigitSimController;
import element.Element;
import general.Properties;
import element.Element_SIGNAL;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

/**
 *
 * @author Elias
 */
/**
 * Listener die eine Drag & Drop - Funktin mit der linken Maustaste ermöglichen. Bedenkt auch den Zoom.
 */
public class NodeGestures {
    private static ArrayList<Element> elements = new ArrayList<>(); //Elemente
    private static ContextMenu contextMenu;
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
               contextMenu.hide();
               elements = DigitSimController.getReference().getElements();
               Element temp = findElement();
               if(temp != null){
                   canvas.getChildren().remove(temporaryGroup); //Die "Zeichnung" entfernen, da diese bestehen bleibt wenn das Element gelöscht wird
                   ConnectionHandler.removeAllConnectionsRelatedToElement(temp);
                   elements.remove(temp); //Das Element entfernen
               }
            }});      
        
        propertiesItem.setOnAction(new EventHandler<ActionEvent>() { //Wird ausgelößt wenn man bei einem Element "Eigenschaften" auswählt
            public void handle(ActionEvent e) {
               contextMenu.hide();
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
            if(contextMenu.isShowing()){
                    contextMenu.hide();
                    return;
            }
            // Rechtsklick
            if( !event.isPrimaryButtonDown()){
                temporaryGroup = (Group) event.getSource();
                contextMenu.show((Node)event.getSource(), Side.RIGHT, 0, 0); //Rechtsklick-Menü anzeigen
                event.consume(); 
                return;
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
            if(DigitSimController.getReference().isLocked()){ //Schauen ob das Programm blokiert ist (erklärung: siehe DigitSimController oben)
               return;
            }
            
            if(contextMenu.isShowing()){
                contextMenu.hide();
                return;
            }
            
            elements = DigitSimController.getReference().getElements();

            // left mouse button => dragging
            if( !event.isPrimaryButtonDown()){
                return;
            }
            dragged = true;
            
            
            
            double scale = canvas.getScale(); //Scalierung
            
            Node node = (Node) event.getSource(); //Das betroffene Element bekommen
            
            
            
            int translXBefore =(int) node.getTranslateX();
            int translYBefore =(int) node.getTranslateY();
            
            //-Node passt sich jetzt ans Gitter an (Author: Dominik)
            //Den Abstand zwischen der alten und der neuen Mausposition berechnen und zur Änderungsrate hinzufügen
            node.setTranslateX(getValueAdaptGrid(nodeDragContext.translateAnchorX + (( event.getSceneX() - nodeDragContext.mouseAnchorX) / scale)));
            node.setTranslateY(getValueAdaptGrid(nodeDragContext.translateAnchorY + (( event.getSceneY() - nodeDragContext.mouseAnchorY) / scale)));
            
            temporaryGroup = (Group) event.getSource();
            
            //Nur wenn das Element auch wirklich verschoben wurde, die Linie updaten
            if(translXBefore != node.getTranslateX() || translYBefore != node.getTranslateY()) {
                connection.ConnectionHandler.hideConnectionsRelatedToElement(findElementNum());
                connection.ConnectionHandler.drawDirectPreLinesRelatedToElement(findElementNum());
               //ConnectionUpdater.updateAllConnectionsRelatedToElement(findElementNum()); //Verbindungen updaten die das Akutelle Element betreffen
            }
            event.consume(); 
        }
    };
    
    private EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() { //Handler für "Drag"
        
        public void handle(MouseEvent event ) {
            if(dragged == true) {
                dragged = false;
                ConnectionHandler.removeDirectPreLinesRelatedToElement(findElementNum());
                ConnectionHandler.updateAllConnectionsRelatedToElement(findElementNum()); //Verbindungen updaten die das Akutelle Element betreffen
                Node node = (Node) event.getSource(); //Das betroffene Element bekommen
                node.setCursor(Cursor.DEFAULT);
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
    
    public static ContextMenu getContextMenu(){
        return contextMenu;
    }
    
    private Element findElementNum(){ //Die Nummer des Elements finden (über die akutelle gruppe)
         elements = DigitSimController.getReference().getElements();
         int i = -1;
         for(Element e : elements){ //Alle Elemente durchgehen, um das zu finden das Ausgewählt ist
             i++;
                   if(e.getGroup().hashCode() == temporaryGroup.hashCode()){ //Der HashCode eines Objektes ist immer EINMALIG, sozusagen eine "Personalnummer", eignet sich daher gut für den "Gleichheitstest"
                       return e;
                 } 
          }
         return null;
    }
    
    /*
    Handler die aktiviert werden wenn die Maus über einer Note ist, 'Entered' (hier kurz Enter) wenn die Maus über der Node ist, 'Exit' wenn die Maus die Note verlässt
    */
    public static EventHandler<MouseEvent> getOverNodeMouseHanlderExit(){
        return new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event){
                if(DigitSimController.isLocked())
                    return;
                Node src = (Node) event.getSource();
                Line line = (Line) src;
                line.setStroke(Color.BLACK);
                line.setCursor(Cursor.DEFAULT);
            }
        };
    }
    
    public static EventHandler<MouseEvent> getOverNodeMouseHanlderEnter(){
        return new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event){
                if(DigitSimController.isLocked())
                    return;
                Node src = (Node) event.getSource();
                Line line = (Line) src;
                line.setStroke(Color.DARKORANGE);
                line.setCursor(Cursor.HAND);
            }
        };
    }
    
    public static EventHandler<MouseEvent> getOverNodeMouseHanlderClick(){
        return new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event){
                if(DigitSimController.isLocked())
                    return;
                Node src = (Node) event.getSource();
                Line line = (Line) src;
                line.setStroke(Color.DARKORANGE);
                line.setCursor(Cursor.HAND);
            }
        };
    }
    
    public static EventHandler<MouseEvent> getOverNodeMouseHanlderEnterRec(){
        return new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event){
                if(DigitSimController.isLocked())
                    return;
                Node src = (Node) event.getSource();
                Rectangle rec = (Rectangle) src;
                rec.setStroke(Color.DARKORANGE);
                rec.setCursor(Cursor.MOVE);
            }
        };
    }
    
    public static EventHandler<MouseEvent> getOverNodeMouseHanlderExitRec(){
        return new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event){
                if(DigitSimController.isLocked())
                    return;
                Node src = (Node) event.getSource();
                Rectangle rec = (Rectangle) src;
                rec.setStroke(Color.BLACK);
                rec.setCursor(Cursor.DEFAULT);
            }
        };
    }
    
    public static EventHandler<MouseEvent> getOverNodeMouseHanlderEnterCircle(){
        return new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event){
                if(DigitSimController.isLocked())
                    return;
                Node src = (Node) event.getSource();
                Circle c = (Circle) src;
                c.setStroke(Color.DARKORANGE);
            }
        };
    }
    
    public static EventHandler<MouseEvent> getOverNodeMouseHanlderExitCircle(){
        return new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event){
                if(DigitSimController.isLocked())
                    return;
                Node src = (Node) event.getSource();
                Circle c = (Circle) src;
                c.setStroke(Color.BLACK);
            }
        };
    }
    
    public static EventHandler<MouseEvent> getOverNodeMouseHanlderEnterCircleLine(){
        return new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event){
                if(DigitSimController.isLocked())
                    return;
                Node src = (Node) event.getSource();
                Circle c = (Circle) src;
                Line l = (Line) src;
                c.setStroke(Color.DARKORANGE);
                l.setStroke(Color.DARKORANGE);
            }
        };
    }
    
    public static EventHandler<MouseEvent> getOverNodeMouseHanlderExitCircleLine(){
        return new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event){
                if(DigitSimController.isLocked())
                    return;
                Node src = (Node) event.getSource();
                Circle c = (Circle) src;
                Line l = (Line) src;
                c.setStroke(Color.BLACK);
                l.setStroke(Color.BLACK);
            }
        };
    }
    
    public static EventHandler<MouseEvent> getOverNodeMouseHanlderEnterLineGrp(Connection c){
        return new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event){
                if(DigitSimController.isLocked())
                    return;
                
                c.setSpecialColor(Color.DARKORANGE);
            }
        };
    }
    
    public static EventHandler<MouseEvent> getOverNodeMouseHanlderExitLineGrp(Connection c){
        return new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event){
                if(DigitSimController.isLocked())
                    return;
                
                c.updateColor();
            }
        };
    }
        
    public static EventHandler<MouseEvent> getOverInputMouseHanlderClicked(Element e, int inputIndex){
        return new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event){
                if(DigitSimController.isLocked()){
               return;   
            }
                DigitSimController dsc = DigitSimController.getReference();
                dsc.addConnection(e, true, inputIndex, event);
            }
        };
    }
    
    public static EventHandler<MouseEvent> getOverOutputMouseHanlderClicked(Element e, int outputIndex){
        return new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event){
                if(DigitSimController.isLocked()){
               return;   
            }
                DigitSimController dsc = DigitSimController.getReference();
                dsc.addConnection(e, false, outputIndex, event);
            }
        };
    }
    
    public static EventHandler<MouseEvent> getOverConnectionLineClicked(/*ConData d*/){
        return new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event){
            if(DigitSimController.isLocked()){
               return;   
            }
            if(event.getButton() == MouseButton.SECONDARY){
            ContextMenu menu= new ContextMenu();
            MenuItem deleteItem = new MenuItem("Entfernen");
            MenuItem resetItem = new MenuItem("Zurücksetzen");
            deleteItem.setOnAction(new EventHandler<ActionEvent>() { //Wird ausgelößt wenn man bei einem Element "Entfernen" auswählt
                
               public void handle(ActionEvent e) {
               contextMenu.hide();
//               DigitSimController.getAllConnectionsOLD().removeConnection(d);
            }});      
            resetItem.setOnAction(new EventHandler<ActionEvent>() { //Wird ausgelößt wenn man bei einem Element "Entfernen" auswählt
               public void handle(ActionEvent e) {
               contextMenu.hide();
//               DigitSimController.getAllConnectionsOLD().resetCon(d);
            }});     
              menu.getItems().addAll(deleteItem, resetItem);   
              menu.show((Node)event.getSource(), Side.LEFT, event.getX(), event.getY());
              
            }else{
//                   DigitSimController.getAllConnectionsOLD().resetLastPoint(); //Sorgt dafür dass man mehrere Punkte setzen kann, was buggy ist daher auskommentiert (Kannst es aber gern mal testen!)
                   DigitSimController.getReference().setConnectionPointDragging(true); 
//                   DigitSimController.getReference().setCurrentConData(d);
                   Node n = (Node) event.getSource();
                   n.getScene().setCursor(Cursor.CLOSED_HAND);
            }
            event.consume();
            }  
        };             
    }            
}
