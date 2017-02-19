/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import digitsim.DigitSimController;
import general.Properties;
import general.Vector2i;
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
    private MouseEvent event;
    private DigitSimController dsc;
    private Line line;

    public LineMouseFollower(Vector2i start, MouseEvent event, DigitSimController dsc) {
        this.start = start;
        this.event = event;
        this.dsc = dsc;
    }
    
    @Override
    public void run() {
        
       while(!this.isInterrupted()) {
           removeLine();
           this.line = Draw.drawLine(start.getX(), start.getY(), event.getX(), event.getY(), Color.DARKORANGE, Properties.getLineWidth());
           this.line.setOpacity(0.6);
           this.drawLine();
       }
    }
    
    private void drawLine() {   //MAlt die Linie auf dem dsc
        dsc.getChildren().add(this.line);
    }
    
    private void removeLine() { //Entfernt die "alte Linie" vom dsc
        if(this.line != null) {
            dsc.getChildren().remove(this.line);
        }
    }
}
