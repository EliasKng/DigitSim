/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

/**
 *
 * @author Elias
 */
public class DraggableCanvas extends Pane {
    DoubleProperty myScale = new SimpleDoubleProperty(1.0);

    public DraggableCanvas() {
        setPrefSize(700, 700);
        setStyle("-fx-background-color: white; -fx-border-color: blue;");

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
    public void addGrid() {
        Canvas simGrid = new Canvas();
        GraphicsContext gc = simGrid.getGraphicsContext2D();
        simGrid.setWidth(700);
        simGrid.setHeight(700);
        double w = simGrid.getWidth();
        double h = simGrid.getHeight();


        simGrid.setMouseTransparent(false);

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
