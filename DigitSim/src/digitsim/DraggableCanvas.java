/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;

import java.util.ArrayList;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 *
 * @author Elias
 * -Überarbeitet von Dominik 22.10.2016 (Zoom verbessert, Das Sichtfenster in die Mitte gesetzt, Größe der Fläche angepasst)
 */
public class DraggableCanvas extends Pane { //Arbeitsfläche
    DoubleProperty myScale = new SimpleDoubleProperty(1.0); //Standartzoom auf 1

    public DraggableCanvas() {
        setPrefSize(8000, 8000); //Standartgröße
        setStyle("-fx-background-color: white; -fx-border-color: grey;"); //Farben

        // Die Scalierung einbinden
        scaleXProperty().bind(myScale);
        scaleYProperty().bind(myScale);
    }
    
    /**
    * Animiert Karo auf simGrid
    *
    * @author Elias
    * Bearbeitet von Tim 16.10.16
    */
    public void addGrid(double width, double heigth) { //zeichnet Gitter
        Canvas simGrid = new Canvas();
        GraphicsContext gc = simGrid.getGraphicsContext2D();
        simGrid.setWidth(width);
        simGrid.setHeight(heigth);
        double w = simGrid.getWidth();
        double h = simGrid.getHeight();

        simGrid.setMouseTransparent(true);

       
        // Karomuster malen
        // offset = linien abstand
        double offset = 21;
        double d;
        for( double i=offset; i < w; i+=offset) {
                if(i % 4 == 0) d = 2; //Jede 5. Linie mit doppelter Dicke zeichnen
                else d = 1;
                                
                Draw.gcDrawLine(gc, i, 0, i, h, d, Color.LIGHTGREY);
                Draw.gcDrawLine(gc, 0, i, w, i, d, Color.LIGHTGREY);
        }        
        

        getChildren().add(simGrid); //Die Zeichenfläche hinzufügen

        //Verschiebt simGrid in den Hintergrund, damit die Elemente im Vordergrund sind
        simGrid.toBack();
    }

    public double getScale() { //Liefert die Scalierung
        return myScale.get();
    }

    public void setScale( double scale) { //Setzt die Scalierung
        myScale.set(scale);
    }

    public void setPivot( double x, double y) { //verschiebt das "Sichtfenster"
        setTranslateX(getTranslateX()-x);
        setTranslateY(getTranslateY()-y);
    }
}

/**
 * Maus drag context wird für nodes & scene gestures benötigt, um den Unterscied der Änderungsraten zu berechnen
 */
class DragContext {

    double mouseAnchorX; 
    double mouseAnchorY;

    double translateAnchorX; 
    double translateAnchorY;

}

/**
 * Listener die eine Drag & Drop - Funktin mit der linken Maustaste ermöglichen. Bedenkt auch den Zoom.
 */
class NodeGestures {
    private static ArrayList<Element> elements = new ArrayList<>(); //Elemente
   
    private DragContext nodeDragContext = new DragContext(); //Nötige Daten für Berechnungen

    DraggableCanvas canvas; //Arbeitsfläche

    public NodeGestures( DraggableCanvas canvas) { //Consturctor
        this.canvas = canvas;
    }

    public EventHandler<MouseEvent> getOnMousePressedEventHandler() { //Liefert einen Handler
        return onMousePressedEventHandler;
    }

    public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
        return onMouseDraggedEventHandler;
    }

    private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() { //Handler für "Maus klick"

        public void handle(MouseEvent event) {

            // Linker Mausknopf -> Verschieben
            if( !event.isPrimaryButtonDown())
                return;

            nodeDragContext.mouseAnchorX = event.getSceneX(); //X/Y Koordinaten der Maus speichern (später benötig)
            nodeDragContext.mouseAnchorY = event.getSceneY();

            Node node = (Node) event.getSource(); //Das betroffene Element bekommen

            nodeDragContext.translateAnchorX = node.getTranslateX(); //Die akutelle Änderungsrate speichern (später nötig)
            nodeDragContext.translateAnchorY = node.getTranslateY();

        }

    };

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() { //Handler für "Drag"
        
        public void handle(MouseEvent event ) {
            elements = DigitSimController.getElements();

            // left mouse button => dragging
            if( !event.isPrimaryButtonDown())
                return;

            double scale = canvas.getScale(); //Scalierung
            
            Node node = (Node) event.getSource(); //Das betroffene Element bekommen
            
            //-Node passt sich jetzt ans Gitter an (Author: Dominik)
            //Den Abstand zwischen der alten und der neuen Mausposition berechnen und zur Änderungsrate hinzufügen
            node.setTranslateX(getValueAdaptGrid(nodeDragContext.translateAnchorX + (( event.getSceneX() - nodeDragContext.mouseAnchorX) / scale)));
            node.setTranslateY(getValueAdaptGrid(nodeDragContext.translateAnchorY + (( event.getSceneY() - nodeDragContext.mouseAnchorY) / scale)));

            event.consume(); 
        }
    };
    //Anpassung eines beliebigen Volumen ans Gitter (Author: Dominik)
    private double getValueAdaptGrid(double pV){
        return Math.round(pV / 21) * 21;
    }
}

/**
 * Listeners for making the scene's canvas draggable and zoomable
 * -Überarbeitet von Dominik 22.10.2016 (Zoom verbessert, Das Sichtfenster in die Mitte gesetzt, Größe der Fläche angepasst)
 */
class SceneGestures { //Klasse für Zoom und verschiebung der Arbeitsfläche

    private static final double MAX_SCALE = 2.0d; //Min. & Max. scalierung
    private static final double MIN_SCALE = 0.5d;

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

        }

    };

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {//Handler für "Drag"
        public void handle(MouseEvent event) {

            // right mouse button => panning
            if( !event.isSecondaryButtonDown())
                return;

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