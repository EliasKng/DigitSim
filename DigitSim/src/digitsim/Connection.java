/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Tim
 */
public class Connection {
    
    public class Data
    {
        public int indexFirstElement;
        public boolean typeFirst;
        public int indexFirst;
        public int indexSecondElement;
        public boolean typeSecond;
        public int indexSecond;
    }
    
    public static List<Data> connections = new ArrayList<Data>();


    
/**
 * addConnection(..)
 * Fügtt eine Verbindung zwischen ein und ausgängen von verachiedenen elementen hinzu
 * @author Tim
 * 
 * @param indexFirstElement erstes element (index im main array in der Controlelr kloasse elements)
 * @param indexSecondElement zweites element (index im main array in der Controlelr kloasse elements)
 * param type: 0 für ausgang, 1 für eingang
 * param index: indes des ein oder ausgangs am element
 */
    public void addConnection(int indexFirstElement, boolean typeFirst, int indexFirst, int indexSecondElement, boolean typeSecond, int indexSecond)
    {
        Data data = new Data();
        data.indexFirstElement = indexFirstElement;
        data.indexSecondElement = indexSecondElement;
        data.indexFirst = indexFirst;
        data.indexSecond = indexSecond;
        data.typeFirst = typeFirst;
        data.typeSecond = typeSecond;
        
        connections.add(data);       
    }
    
    void update()
    {
        for(Data d : connections)
        {
         
           if((d.typeFirst == d.typeSecond) && !d.typeFirst) // ausgang mit ausgang verbunden
                continue;
            if((d.typeFirst != d.typeSecond) && !d.typeFirst) // ausgang mit eingang verbunden
            {
                DigitSimController.elements.get(d.indexSecondElement).setInput(d.indexSecond, DigitSimController.elements.get(d.indexFirstElement).getOutput(d.indexFirst));
            }
            if((d.typeFirst != d.typeSecond) && d.typeFirst) // eingang mit ausgang verbunden
            {
                DigitSimController.elements.get(d.indexFirstElement).setInput(d.indexFirst, DigitSimController.elements.get(d.indexSecondElement).getOutput(d.indexSecond));
            }
            if((d.typeFirst == d.typeSecond) && d.typeFirst) // eingang mit eingang verbunden
            {
                int newVal = 0;
                if(DigitSimController.elements.get(d.indexFirstElement).inputs[d.indexFirst] > 0 || DigitSimController.elements.get(d.indexSecondElement).inputs[d.indexSecond] > 0)
                {
                    newVal = 1;
                }        
                DigitSimController.elements.get(d.indexFirstElement).setInput(d.indexFirst, newVal);
                DigitSimController.elements.get(d.indexSecondElement).setInput(d.indexSecond, newVal);
            }
            DigitSimController.elements.get(d.indexFirstElement).update();
            DigitSimController.elements.get(d.indexSecondElement).update();
        }
    }
}
