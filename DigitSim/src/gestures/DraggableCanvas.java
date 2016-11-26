/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Gestures;

import digitsim.Draw;
import digitsim.Properties;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;

public class DraggableCanvas extends Pane { //Arbeitsfläche
    DoubleProperty myScale = new SimpleDoubleProperty(1.0); //Standartzoom auf 1

    public DraggableCanvas() {
        setPrefSize(Properties.GetSimSizeX(), Properties.GetSimSizeY()); //Standartgröße
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
        double d;
        for( double i = Properties.GetGridOffset(); i < w; i += Properties.GetGridOffset()) {
                if(i % 4 == 0) d = 2; //Jede 5. Linie mit doppelter Dicke zeichnen
                else d = 1;
                                
                Draw.gcDrawLine(gc, i, 0, i, h, d, Properties.GetGridColor());
                Draw.gcDrawLine(gc, 0, i, w, i, d, Properties.GetGridColor());
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
