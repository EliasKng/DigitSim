/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import digitsim.DigitSimController;
import element.Element;
import java.util.List;

/**
 *
 * @author Elias
 */
public class ConnectionUpdater {
    public static void updateAllConnectionsRelatedToElement(Element e) {
        List<Connection> allConnections = DigitSimController.getReference().getAllConnections();
        
        for(Connection c : allConnections) {
            boolean isAlreadyUpdated = false;
            if(c.getStartPartner().getelement() == e) {
                isAlreadyUpdated = true;
                c.updateLine();
            } if((c.getEndPartner().getelement() == e) && !isAlreadyUpdated) {
                c.updateLine();
            }
        }
    }
}
