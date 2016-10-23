/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 *
 * @author Elias
 * -Überarbeitet von Dominik 22.10.2016 (Zoom verbessert, Das Sichtfenster in die Mitte gesetzt, Größe der Fläche angepasst)
 */
public class DraggableCanvas extends Pane {
    DoubleProperty myScale = new SimpleDoubleProperty(1.0);

    public DraggableCanvas() {
        setPrefSize(8000, 8000);
        setStyle("-fx-background-color: white; -fx-border-color: grey;");

        // add scale transform
        scaleXProperty().bind(myScale);
        scaleYProperty().bind(myScale);
    }
    
    /**
    * Animiert Karo auf simGrid
    *
    * @author Elias
    * Bearbeitet von Tim 16.10.16
    */
    public void addGrid(double width, double heigth) {
        Canvas simGrid = new Canvas();
        GraphicsContext gc = simGrid.getGraphicsContext2D();
        simGrid.setWidth(width);
        simGrid.setHeight(heigth);
        double w = simGrid.getWidth();
        double h = simGrid.getHeight();

        simGrid.setMouseTransparent(true);

        gc = simGrid.getGraphicsContext2D();
       
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
        

        getChildren().add(simGrid);

        //Verschiebt simGrid in den Hintergrund
        simGrid.toBack();
    }

    public double getScale() {
        return myScale.get();
    }

    public void setScale( double scale) {
        myScale.set(scale);
    }

    public void setPivot( double x, double y) {
        setTranslateX(getTranslateX()-x);
        setTranslateY(getTranslateY()-y);
    }
}

/**
 * Maus drag context wird für nodes & scene gestures benötigt
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

    private DragContext nodeDragContext = new DragContext();

    DraggableCanvas canvas;

    public NodeGestures( DraggableCanvas canvas) {
        this.canvas = canvas;
    }

    public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
        return onMousePressedEventHandler;
    }

    public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
        return onMouseDraggedEventHandler;
    }

    private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

        public void handle(MouseEvent event) {

            // linker Mausknopf -> Verschieben
            if( !event.isPrimaryButtonDown())
                return;

            nodeDragContext.mouseAnchorX = event.getSceneX();
            nodeDragContext.mouseAnchorY = event.getSceneY();

            Node node = (Node) event.getSource();

            nodeDragContext.translateAnchorX = node.getTranslateX();
            nodeDragContext.translateAnchorY = node.getTranslateY();

        }

    };

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {

            // left mouse button => dragging
            if( !event.isPrimaryButtonDown())
                return;

            double scale = canvas.getScale();

            Node node = (Node) event.getSource();

            node.setTranslateX(nodeDragContext.translateAnchorX + (( event.getSceneX() - nodeDragContext.mouseAnchorX) / scale));
            node.setTranslateY(nodeDragContext.translateAnchorY + (( event.getSceneY() - nodeDragContext.mouseAnchorY) / scale));

            event.consume();

        }
    };
}

/**
 * Listeners for making the scene's canvas draggable and zoomable
 * -Überarbeitet von Dominik 22.10.2016 (Zoom verbessert, Das Sichtfenster in die Mitte gesetzt, Größe der Fläche angepasst)
 */
class SceneGestures {

    private static final double MAX_SCALE = 2.0d;
    private static final double MIN_SCALE = 0.5d;

    private DragContext sceneDragContext = new DragContext();

    DraggableCanvas simCanvas;

    /*private void placeCanvasMiddle(){//Sichtbereich in die Mitte setzen
         simCanvas.setTranslateX(sceneDragContext.translateAnchorX - (simCanvas.getPrefWidth() / 2));
         simCanvas.setTranslateY(sceneDragContext.translateAnchorY - (simCanvas.getPrefHeight() / 2));
    }*/
    
    public SceneGestures( DraggableCanvas simCanvas) {
        this.simCanvas = simCanvas;
        //placeCanvasMiddle(); //Sichtbereich in die Mitte setzen
    }

    public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
        return onMousePressedEventHandler;
    }

    public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
        return onMouseDraggedEventHandler;
    }

    public EventHandler<ScrollEvent> getOnScrollEventHandler() {
        return onScrollEventHandler;
    }

    private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

        public void handle(MouseEvent event) {

            // right mouse button => panning
            if( !event.isSecondaryButtonDown())
                return;

            sceneDragContext.mouseAnchorX = event.getSceneX();
            sceneDragContext.mouseAnchorY = event.getSceneY();

            sceneDragContext.translateAnchorX = simCanvas.getTranslateX();
            sceneDragContext.translateAnchorY = simCanvas.getTranslateY();

        }

    };

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {

            // right mouse button => panning
            if( !event.isSecondaryButtonDown())
                return;

            simCanvas.setTranslateX(sceneDragContext.translateAnchorX + event.getSceneX() - sceneDragContext.mouseAnchorX);
            simCanvas.setTranslateY(sceneDragContext.translateAnchorY + event.getSceneY() - sceneDragContext.mouseAnchorY);
            
            /*
            if(simCanvas.getTranslateX()>simCanvas.getWidth()*(-simCanvas.getScale())+25) {simCanvas.setTranslateX(25);} //Canvas kann nicht komplett aus dem Bild geschoben werden
            if(simCanvas.getTranslateY()>25) {simCanvas.setTranslateY(25);} //Canvas kann nicht komplett aus dem Bild geschoben werden
            if(simCanvas.getTranslateX()>simCanvas.getWidth()) {simCanvas.setTranslateX(simCanvas.getWidth());}
            */
            event.consume();
        }
    };

    /**
     * Mouse wheel handler: zoom to pivot point
     * -Überarbeitet von Dominik 22.10.2016 (Zoom verbessert, Das Sichtfenster in die Mitte gesetzt, Größe der Fläche angepasst)
     */
    private EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {

        @Override
        public void handle(ScrollEvent event) {

            double delta = 1.15;

            double scale = simCanvas.getScale(); // currently we only use Y, same value is used for X
            double oldScale = scale;

            if (event.getDeltaY() < 0)
                scale /= delta;
            else
                scale *= delta;

            scale = clamp( scale, MIN_SCALE, MAX_SCALE);

            double f = (scale / oldScale)-1;

            double dx = (event.getSceneX() - (simCanvas.getBoundsInParent().getWidth()/2 + simCanvas.getBoundsInParent().getMinX()));
            double dy = (event.getSceneY() - (simCanvas.getBoundsInParent().getHeight()/2 + simCanvas.getBoundsInParent().getMinY()));

            simCanvas.setScale( scale);

            // note: pivot value must be untransformed, i. e. without scaling
            simCanvas.setPivot(f*dx, f*dy);

            event.consume();

        }

    };


    public static double clamp( double value, double min, double max) {

        if( Double.compare(value, min) < 0)
            return min;

        if( Double.compare(value, max) > 0)
            return max;

        return value;
    }
}
