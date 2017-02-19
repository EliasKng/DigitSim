/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connection;

import element.Element;

/**
 *
 * @author Elias
 */
public class ConnectionPartner {
    //Globals
    private PartnerType partnerType;
    
    private Element element;   //PartnerElement
    private boolean isInput;    //ist In oder Output?
    private int index;      //Index des IN / Outputs
    
    
    private Connection connection;    //Wenn isElement = false -> Verbindungspartner ist eine Verbindungslinie und kein Element (connection = Verbindungslinienpartner)
    private AnchorPoint anchorPoint; //Verbindungs-AnchorPoint (partner muss ein AnchorPoint sein)

    public ConnectionPartner(Element element, boolean isInput, int index) {
        this.partnerType = PartnerType.ELEMENT;
        this.element = element;
        this.isInput = isInput;
        this.index = index;
    }

    public ConnectionPartner(Connection connection, AnchorPoint anchorPoint) {
        this.partnerType = PartnerType.CONNECTIONLINE;
        this.connection = connection;
        this.anchorPoint = anchorPoint;
    }
    
    //*************GET/SET**********

    public PartnerType getPartnerType() {
        return partnerType;
    }

    public void setPartnerType(PartnerType partnerType) {
        this.partnerType = partnerType;
    }

    public Element getelement() {
        return element;
    }

    public void setelement(Element element) {
        this.element = element;
    }

    public boolean isIsInput() {
        return isInput;
    }

    public void setIsInput(boolean isInput) {
        this.isInput = isInput;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Connection getconnection() {
        return connection;
    }

    public void setconnection(Connection connection) {
        this.connection = connection;
    }

    public AnchorPoint getanchorPoint() {
        return anchorPoint;
    }

    public void setanchorPoint(AnchorPoint anchorPoint) {
        this.anchorPoint = anchorPoint;
    }
}
