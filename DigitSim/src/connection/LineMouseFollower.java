/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import digitsim.DigitSimController;
import general.Properties;
import general.Vector2i;
import javafx.application.Platform;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import toolbox.Draw;

/**
 *
 * @author Elias
 */
public class LineMouseFollower extends Thread {
    //Globals
    private Vector2i start;
    private DigitSimController dsc;
    private Line line;
    private Vector2i mouseCoords;

    public LineMouseFollower(Vector2i start, DigitSimController dsc) {
        this.start = start;
        this.dsc = dsc;
    }

    @Override
    public void run() {
       
       while(!this.isInterrupted()) {
           removeLine();
           this.mouseCoords = new Vector2i(dsc.getMouseCoords());
           this.line = Draw.drawLine(start.getX(), start.getY(), mouseCoords.getX(), mouseCoords.getY(), Color.DARKORANGE, Properties.getLineWidth());
           this.line.setOpacity(0.6);
           this.drawLine();
          
       }
    }
    
    private void drawLine() {   //MAlt die Linie auf dem dsc
        dsc.getSimCanvas().getChildren().add(this.line);
    }
    
    private void removeLine() { //Entfernt die "alte Linie" vom dsc
        if(this.line != null) {
            dsc.getSimCanvas().getChildren().remove(this.line);
        }
    }

    public Vector2i getMouseCoords() {
        return mouseCoords;
    }

    public void setMouseCoords(Vector2i mouseCoords) {
        this.mouseCoords = mouseCoords;
    }
}
