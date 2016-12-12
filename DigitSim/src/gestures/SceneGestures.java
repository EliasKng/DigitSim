/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gestures;


import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;

/**
 * Listeners for making the scene's canvas draggable and zoomable
 * -Überarbeitet von Dominik 22.10.2016 (Zoom verbessert, Das Sichtfenster in die Mitte gesetzt, Größe der Fläche angepasst)
 */
public class SceneGestures { //Klasse für Zoom und verschiebung der Arbeitsfläche

    private static final double MAX_SCALE = 2.0d; //Min. & Max. scalierung
    private static final double MIN_SCALE = 0.5d;
    private static boolean temp = false;
    private DragContext sceneDragContext = new DragContext();

    DraggableCanvas simCanvas; //Arbeitsfläche
    AnchorPane simPane;

    private void placeCanvasMiddle(){//Sichtbereich in die Mitte setzen
         simCanvas.setTranslateX(sceneDragContext.translateAnchorX - (simCanvas.getPrefWidth() / 2));
         simCanvas.setTranslateY(sceneDragContext.translateAnchorY - (simCanvas.getPrefHeight() / 2));
    }
    
    public SceneGestures( DraggableCanvas pSimCanvas, AnchorPane pSimPane) { //Constructor
        this.simCanvas = pSimCanvas;
        this.simPane = pSimPane;
        placeCanvasMiddle(); //Sichtbereich in die Mitte setzen
    }

    public EventHandler<MouseEvent> getOnMousePressedEventHandler() {//Liefert einen Handler
        return onMousePressedEventHandler;
    }

    public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
        return onMouseDraggedEventHandler;
    }
    
    public EventHandler<MouseEvent> getOnMouseReleasedEventHandler() {
        return onMouseDraggedReleasedHandler;
    }

    public EventHandler<ScrollEvent> getOnScrollEventHandler() {
        return onScrollEventHandler;
    }

    private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() { //Handler für "Maus klick"

        public void handle(MouseEvent event) {

            // right mouse button => panning
            if( !event.isSecondaryButtonDown())
                return;
            
            sceneDragContext.mouseAnchorX = event.getSceneX(); //X/Y Koordinaten der Maus speichern (später benötig)
            sceneDragContext.mouseAnchorY = event.getSceneY();

            sceneDragContext.translateAnchorX = simCanvas.getTranslateX(); //Die akutelle Änderungsrate speichern (später nötig)
            sceneDragContext.translateAnchorY = simCanvas.getTranslateY();
            temp = true;

        }

    };
    
    private EventHandler<MouseEvent> onMouseDraggedReleasedHandler = new EventHandler<MouseEvent>() { //Handler für "Maus klick"

        public void handle(MouseEvent event) {

            temp = false;
        }

    };

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {//Handler für "Drag"
        public void handle(MouseEvent event) {
            
            // right mouse button => panning
            if( !event.isSecondaryButtonDown())
                return;
            if(!temp){
                return;
            }
            if(NodeGestures.getContextMenu().isShowing()){
                NodeGestures.getContextMenu().hide();
            }
            //Berechnet den Abstand zwischen der alten und der neuen Mausposition
            simCanvas.setTranslateX(sceneDragContext.translateAnchorX + event.getSceneX() - sceneDragContext.mouseAnchorX);
            simCanvas.setTranslateY(sceneDragContext.translateAnchorY + event.getSceneY() - sceneDragContext.mouseAnchorY);
            
            holdCanvasInVisibleArea(simPane); //Hält die Canvas im Sichtbaren bereich
            event.consume();
        }
    };

    /**
     * Mouse wheel handler: zoom to pivot point
     * -Überarbeitet von Dominik 22.10.2016 (Zoom verbessert, Das Sichtfenster in die Mitte gesetzt, Größe der Fläche angepasst)
     */
    private EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() { //Handler für "Zoom"

        @Override
        public void handle(ScrollEvent event) {

            double delta = 1.15; //Zoomrate / Zoomgeschwindigkeit

            double scale = simCanvas.getScale(); //scalierung
            double oldScale = scale; 

            if (event.getDeltaY() < 0) //Überorüfen in welche richtung das Mausrad gedreht wird
                scale /= delta;
            else
                scale *= delta;

            scale = clamp( scale, MIN_SCALE, MAX_SCALE); //Gibt Scale zurück falls es zwischen Min und Max ist, ansonsten das jeweile min / max

            double f = (scale / oldScale)-1;

            //Koordinaten für das Sichtfenster berechnen
            double dx = (event.getSceneX() - (simCanvas.getBoundsInParent().getWidth()/2 + simCanvas.getBoundsInParent().getMinX()));
            double dy = (event.getSceneY() - (simCanvas.getBoundsInParent().getHeight()/2 + simCanvas.getBoundsInParent().getMinY()));

            //Setzt das "Sichtfenster" an eine angepasste Stelle 
            simCanvas.setScale( scale);

            // note: pivot value must be untransformed, i. e. without scaling
            simCanvas.setPivot(f*dx, f*dy);
            
            holdCanvasInVisibleArea(simPane); //Sorgt dafür das die Arbeitsfläche nicht aus dem Sichtbereich geschoben werden kann

            event.consume();

        }

    };


    public static double clamp( double value, double min, double max) { //Gibt den Wert zurück der zwischen Min und Max liegt, ansonsten das jeweile min / max

        if( Double.compare(value, min) < 0)
            return min;

        if( Double.compare(value, max) > 0)
            return max;

        return value;
    }
    
    /**
     * Da sich beim Zoomen die Größe des ursprünglichen Canvas nicht verändert, verschiebt sich translateX & Y
     * diese passen dann nicht mehr zum sichtbaren Canvas. Diese Funktion gibt jedoch das TransLateX aus, welches zu dem sichtbaren Canvas passt.
     * @Autor Elias
     * @param canvas simCanvas
     * @return 
     */
    public double getRealTranslateX(DraggableCanvas canvas) {
        double transX;
        double falseTransX = canvas.getTranslateX();
        double scale = canvas.getScale();
        transX = (canvas.getWidth()*0.5) * scale - (canvas.getWidth()*0.5);
        transX = falseTransX - transX;
        return transX;
    }
    
    /**
     * Da sich beim Zoomen die Größe des ursprünglichen Canvas nicht verändert, verschiebt sich translateX & Y
     * diese passen dann nicht mehr zum sichtbaren Canvas. Diese Funktion gibt jedoch das TransLateY aus, welches zu dem sichtbaren Canvas passt.
     * @Autor Elias
     * @param canvas simCanvas
     * @return 
     */
    public double getRealTranslateY(DraggableCanvas canvas) {
        double transY;
        double falseTransY = canvas.getTranslateY();
        Double scale = canvas.getScale();
        transY = (canvas.getHeight()*0.5) * scale - (canvas.getHeight()*0.5);
        transY =  falseTransY - transY;
        
        return transY;
    }
    
    public void setRealTranslateX(DraggableCanvas canvas, double transX) {
        double falseTransX = canvas.getTranslateX();
        double scale = canvas.getScale();
        double X = (canvas.getWidth()*0.5) * scale - (canvas.getWidth()*0.5);
        falseTransX = transX + X;
        
        canvas.setTranslateX(falseTransX);
    }
    
    public void setRealTranslateY(DraggableCanvas canvas, double transY) {
        double falseTransY = canvas.getTranslateY();
        double scale = canvas.getScale();
        double Y = (canvas.getHeight()*0.5) * scale - (canvas.getHeight()*0.5);
        falseTransY = transY + Y;
        
        canvas.setTranslateY(falseTransY);
    }
    
    public void holdCanvasInVisibleArea(AnchorPane simPane) { //Hält die Arbeitsfläche im Sichtbereich
        double realTransX = getRealTranslateX(simCanvas);
        double realTransY = getRealTranslateY(simCanvas);
        double scale = simCanvas.getScale();
        double simPaneWidth = simPane.getWidth();
        double simPaneHeight = simPane.getHeight();
        double simCanvasWidth = simCanvas.getWidth();
        double simCanvasHeight = simCanvas.getHeight();
        
        //Checkt ob simCanvas zu weit nach unten oder rechts verschoben wurde
        if(realTransX>25){setRealTranslateX(simCanvas, 25);}
        if(realTransY>25){setRealTranslateY(simCanvas, 25);}
        
        //Checkt ob simCanvas zu weit nach oben oder links verschoben wurde
        if((-(realTransX+(simCanvasWidth*scale-simCanvasWidth)) > (simCanvasWidth-simPaneWidth+25))) {
            simCanvas.setTranslateX(-(simCanvasWidth + ((simCanvasWidth*0.5) * scale - (simCanvasWidth*0.5)) - simPaneWidth+25));
        }
        if((-(realTransY+(simCanvasHeight*scale-simCanvasHeight)) > (simCanvasHeight-simPaneHeight+25))) {
            simCanvas.setTranslateY(-(simCanvasHeight + ((simCanvasHeight*0.5) * scale - (simCanvasHeight*0.5)) - simPaneHeight+25));
        }
    }   
}
