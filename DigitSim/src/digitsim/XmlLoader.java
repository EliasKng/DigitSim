/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package digitsim;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *Läd abildungen von Objecten(in Xml) aus einer xml formatierten Datei und erstellt das Object
 * Bassiert auf JAXB, für infos -> Google: JAXB
 * @author Dominik
 */
public class XmlLoader {
    
    private XmlLoader(){};
    
    public static Object loadObject(String path, Class s){ //Ein Object aus einer XML-Datei laden (Class -> Die Klasse, zu welcher das Object später konvertiert wird
        Object o = null;
        try{
            File xmlfile = new File(path); //Datei öffnen
            JAXBContext jaxbContext = JAXBContext.newInstance(s); //Die Klasse festlegen, aus welcher das Object gebaut wird
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller(); //Die Formatierung erstellen
            o = jaxbUnmarshaller.unmarshal(xmlfile); //Das Object laden
        }
        catch(JAXBException ex)
        {
             System.out.println(ex);
             ErrorHandler.printError(s, "JAXB-Error beim laden");
        }
        return o;
    }
    
    //Ein Object als XML-Datei speichern
    public static void saveObject(String path, Object o){
        try{
            File xmlfile = new File(path); //Die Datei zum speichern
            JAXBContext jaxbContext = JAXBContext.newInstance(o.getClass());
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            // formatiert die Datei, ergänzt Zeilenumbrüche zur besseren lesbarkeit
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Speichert das Objekt in einer Xml-Datei 
            jaxbMarshaller.marshal(o, xmlfile);
        }
        catch(JAXBException ex)
        {
            ex.printStackTrace();
            ErrorHandler.printError(XmlLoader.class, "JAXB-Error beim abbilden");
        }
    }
    
    public static boolean fileExist(String path){ //testen ob es eine datei auch wirklich gibt
        File file = new File(path);
        return file.exists();
    }
}
