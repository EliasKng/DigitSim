package digitsim;
import general.Properties;
import toolbox.GenFunctions;
import connection.Connection;
import Gestures.DraggableCanvas;
import Gestures.NodeGestures;
import Gestures.SceneGestures;
import connection.Connection.ConData;
import element.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
/**
 * Digitsim.fxml Controller class
 *
 * @author El_ias
 * -Überarbeitet von Dominik 22.10.2016
 */
public class DigitSimController extends Pane{
    //************************ Globals ********************************
    private static DraggableCanvas simCanvas = new DraggableCanvas(); //Arbeitsfläche / Fläche zum zeichnen
    private static ArrayList<Element> elements; //Alle Elemente kommen hier rein, static damit andere Klassen (einfach) darauf zugreifen können
    private static NodeGestures nodeGestures;  //Die handler für die Nodes (z.B Elemente) beziehen
    private SceneGestures sceneGestures; //Die handler für die Arbeitsfläche beziehen
    private static Connection allConnections; //Verbindungen zwischen Elementen werden hier gespeichert
    private SimThread runningThread = new SimThread(this); //Thread der, falls gestartet, immer die Elemente & Verbindungen updatet
    private static boolean locked = false; //Wenn wir das Programm starten setzen wir locked auf True, damit das Programm blokiert wird und man während der Simulation nichts ändern kann!
    private static DigitSimController refThis;
    private static final ObservableList outputMessages = FXCollections.observableArrayList();
    private static boolean connectionPointDragging = false;
    private static ConData currentConData = null;

     /**
     * FXML OBJEKT-Erstellungs-Bereich:
     * Jedes Element, welches in der DigitSim.FXML verwendet wird, muss in folgendem wege im Code noch erstellt werden.
     */
    @FXML 
    private MenuItem mItemOpenFile;
    @FXML
    private MenuItem MenuSimuRun;
    @FXML
    private MenuItem MenuSimuStop;
    @FXML
    private ListView<String> outputList;
    @FXML 
    private ToggleButton btnAND;    
    @FXML
    private ToggleButton btnOR;    
    @FXML
    private ToggleButton btnNOT;
    @FXML
    private ToggleButton btnNOR;    
    @FXML
    private ToggleButton btnXOR;    
    @FXML
    private ToggleButton btnNAND;
    @FXML
    private ToggleButton btnXNOR;
    @FXML
    private AnchorPane simPane;
    @FXML
    private Button btnStart;
    @FXML
    private Button btnPause;
    @FXML
    private Slider inputSlider;
    @FXML
    private ToggleButton btnLogicToggle;
    @FXML
    private ToggleButton btnLED;
    @FXML
    private ToggleButton btnSIGNAL;
    @FXML
    private ToggleButton btnTEXT;
    @FXML
    private ToggleButton btnTHUMBSWITCH; 
    
    //Constructor (leer)
    public DigitSimController() {
        refThis = this;
    }   
    
    @FXML
    public void initialize() {//initialize Funktion: wird direkt beim Starten der FXML aufgerufe
        addSimCanvas(); //Handler zum erstellen neuer Elemente zur Arbeitsfläche hinzufügen & Die Arbeitsfläche reinladen
        setSliderProperties(); //Einstellungen für den Silder zum einstellen der Eingange von Grundbausteinen
        outputList.setItems(outputMessages);
        outputList.setFocusTraversable(false);
        outputMessages.add("[INFO]Leeres Projekt erstellen");
        simCanvas.addGrid(simCanvas.getPrefWidth(), simCanvas.getPrefHeight()); //Gitter zeichnen
        loadBtnGroup(); //Alle Buttons die ein Element auswählen in eine Gruppe packen, damit immer nur einer ausgewählt ist

               
        //Handler für funktionen wie Drag, Zoom etc.
        nodeGestures = new NodeGestures( simCanvas);
        sceneGestures = new SceneGestures(simCanvas, simPane);            
        
        //EVENT FILTER (Diese werden ausgelöst sobald ein gewisses Ereignis eintritt)
        simPane.addEventFilter( MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler()); //Wenn z.b die Maus gedrückt wird, wird der getOnMousePressedEventHanlder ausgeführt
        simPane.addEventFilter( MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        simPane.addEventFilter( MouseEvent.MOUSE_RELEASED, sceneGestures.getOnMouseReleasedEventHandler());
        simPane.addEventFilter( ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());  
        elements = new ArrayList<Element>(); //Beschreibung oben
        //Klasse für die Verbindungen    
        allConnections = new Connection(this);  
        outputMessages.add("[INFO]Projekterstellung erfolgreich");
    }
    
    
    @FXML
    private void addSimCanvas() {
        simCanvas.addEventFilter(MouseEvent.MOUSE_PRESSED, getCanvasMouseKlickedEventHandler());
        simCanvas.addEventFilter(MouseEvent.MOUSE_MOVED, getCanvasMouseMovedEventHandler());
       simCanvas.addEventFilter(MouseEvent.MOUSE_DRAGGED, getCanvasMouseDraggedEventHandler());
        simCanvas.addEventFilter(MouseEvent.MOUSE_RELEASED, getCanvasMouseReleasedEventHandler());
        simPane.getChildren().addAll(simCanvas); //die Arbeitsfläche auf das Panel setzen
    }    
    
    private EventHandler getCanvasMouseKlickedEventHandler(){ //Wenn auf der Arbeitsfläche geklickt wird
        return new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event){     
                if(isLocked()){ //Schauen ob das Programm blokiert ist (erklärung: siehe DigitSimController oben)
                    return;
                }
                if(event.isPrimaryButtonDown() && !isMouseOverNode(event)){
                    addElement(event); //Neuen Baustein einfügen
                }  
            }
        };
    }
    
    
    private EventHandler getCanvasMouseMovedEventHandler(){ //Falls man bereits einen Input/Output ausgewählt halt erscheint eine Linie, diese Funktion sorgt daüfr das die Linie dem Mauszeiger folgt
        return new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event){
                allConnections.updateConLine(event);
            }
        };
    }
    
    private EventHandler getCanvasMouseDraggedEventHandler(){ //Falls man bereits einen Input/Output ausgewählt halt erscheint eine Linie, diese Funktion sorgt daüfr das die Linie dem Mauszeiger folgt
        return new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event){
                if(connectionPointDragging){
                    allConnections.removeLastPoint(currentConData);
                    allConnections.addPoint(currentConData, event, true);
                }
            }
        };
    }
    
    private EventHandler getCanvasMouseReleasedEventHandler(){ //Falls man bereits einen Input/Output ausgewählt halt erscheint eine Linie, diese Funktion sorgt daüfr das die Linie dem Mauszeiger folgt
        return new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event){
                if(connectionPointDragging){
                    allConnections.removeLastPoint(currentConData);
                    allConnections.addPoint(currentConData, event, false);
                    connectionPointDragging = false;
                    simCanvas.getScene().setCursor(Cursor.DEFAULT);
                }
            }
        };
    }
      
    
    public void setConnectionPointDragging(boolean b){
        connectionPointDragging = b;
    }
    
    public boolean getConnectionPointDragging(){
        return connectionPointDragging;
    }
    
    public  ConData getCurrentConData() {
        return currentConData;
    }

    public void setCurrentConData(ConData currentConData) {
        DigitSimController.currentConData = currentConData;
    }
    

    /**
    * Bildet nötige Gruppen für Togglebuttons (damit immer nur einer Selected sein kann)
    * 
    * @author Elias
    * 
    */
    public void loadBtnGroup() {
        ToggleGroup group = new ToggleGroup();
        btnAND.setToggleGroup(group);
        btnOR.setToggleGroup(group);
        btnNOT.setToggleGroup(group);
        btnNAND.setToggleGroup(group);
        btnNOR.setToggleGroup(group);
        btnXOR.setToggleGroup(group);
        btnXNOR.setToggleGroup(group);
        btnLED.setToggleGroup(group);
        btnSIGNAL.setToggleGroup(group);
        btnTEXT.setToggleGroup(group);
        btnTHUMBSWITCH.setToggleGroup(group); 
    }
    
    /**
    * Einstellungen für den Slider, mit dem man die Anzahl von Inputs bestimmt
    * @author Elias
    * 
    */
    public void setSliderProperties() {
        inputSlider.setMax(8);
        inputSlider.setMin(2);
        inputSlider.setShowTickLabels(true);
        inputSlider.setShowTickMarks(true);
        inputSlider.setMajorTickUnit(2);
        inputSlider.setMinorTickCount(1);
        inputSlider.setBlockIncrement(2);
    }
    //*********ON ACTION bereich: wird verwendet um z.B. Buttonclicks auszuwerten**********
    /**
    * Öffnet einen Filebrowser um eine Datei zu Öffnen.
    * 
    * @author Elias
    * -Bearbeitet von Dominik 22.10.16
    * -Bearbeitet von Tim 23.10.16
    * -Bearbeitet von Dominik 31.10.16
    * -Bearbeitet von Dominik 12.11.16
    */  
    
    public void btnLogicToggleOnAction(ActionEvent event) {
        //Für Tests
        //TEST:
    }
    
    public void mItemCloseAction(ActionEvent event){ //Programm schließen
        if(runningThread.isAlive()){
            runningThread.interrupt(); //Wenn der Thread beim schließen läuft muss dieser geschlossen werden, sonst läuft er weiter
        }
        System.exit(0);
    }
    
    public void mItemOpenFileAction(ActionEvent event) { //Datei öffnen      
        File selectedFile = chooseFile("DigitSimFiles (*.dgs)", "*.dgs");  //Datei Auswählen
    }
    public void mItemPropertiesOnAction(ActionEvent event) { //Einstellungs-Fenster öffnen
        Stage stage;
        stage = GenFunctions.openFXML(properties.PropertiesController.class, "Properties.fxml", "Einstellungen", "icon.png"); //Öffnen des "Einstellungen"-Fensters
        stage.setResizable(false);
    }
    public void mItemHelpOnAction(ActionEvent event) { //Hilfe öffnen
        Stage stage;
        stage = GenFunctions.openFXML(help.HelpController.class,"Help.fxml", "Hilfe", "icon.png"); //Öffnen des "Hilfe"-Fensters
        stage.setWidth(600);
        stage.setResizable(false);
    }
    
    public void mItemNewOnAction(ActionEvent event) { //Hilfe öffnen  
        elements.clear();
        simCanvas.getChildren().clear();
        simCanvas.addGrid(simCanvas.getPrefWidth(), simCanvas.getPrefHeight());
        allConnections.clear();
    }
    public void btnStartOnAction(ActionEvent event) {   
        outputMessages.clear();
        btnStart.setDisable(true);
        btnPause.setDisable(false);
        runningThread = new SimThread(this);
        outputMessages.add("[INFO] Starten der Simulation");
        runningThread.start(); //Den Thread starten, d.h alle Elemente & Connections werden regelmäßig geupdated
        
        locked = true; //Programm blockieren (siehe erklärung oben)
  }     
    public void btnPauseOnAction(ActionEvent event) {   
        runningThread.interrupt(); //Den Thread anhalten
        resetElements(); //Alles resetzen
        btnPause.setDisable(true);
        btnStart.setDisable(false);
        outputMessages.clear();
        locked = false;
    }
    
    public void mItemSimRunOnAction(ActionEvent event) { //Datei öffnen      
        btnStart.fire();
    }
    
    public void mItemSimStopOnAction(ActionEvent event) { //Datei öffnen      
        btnPause.fire();
    }
    
    public void inputSliderOnDragDone() { //Den Wert vom Slider runden
        double value = inputSlider.getValue();
        value = Math.round(value);
        inputSlider.setValue(value);
    }
    public File chooseFile(String description, String extension){ //Die Funktion öffnet einen Filebrowser um eine Datei auszuwählen und lädt dise anschließend.
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(description, extension);
        fc.getExtensionFilters().add(extFilter);
        File selectedFile = fc.showOpenDialog(null);  
        return selectedFile;
    }
    
    /**
     * 
     * Author: Dominik
     * 
     * Erstellt einen neuen Baustein & added diesen zu den Elementen sowie der Arbeitsfläche
     */
    public void addElement(MouseEvent event){
      if(btnAND.isSelected()){ //And  
            elements.add(new Element_AND(getXAdaptGrid(event), getYAdaptGrid(event), (int) inputSlider.getValue(), nodeGestures));
            simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());        
      } 
      else if(btnOR.isSelected()){ //Or
            elements.add(new Element_OR(getXAdaptGrid(event), getYAdaptGrid(event), (int) inputSlider.getValue(), nodeGestures));
            simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());        
      } 
      else if(btnNOT.isSelected()){ //Not
            elements.add(new Element_NOT(getXAdaptGrid(event), getYAdaptGrid(event), 1, nodeGestures));
            simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());        
      } 
      else if(btnNOR.isSelected()){ //NOR
            elements.add(new Element_NOR(getXAdaptGrid(event), getYAdaptGrid(event),(int) inputSlider.getValue(), nodeGestures));
            simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());        
      }
      else if(btnXOR.isSelected()){ //Xor
            elements.add(new Element_XOR(getXAdaptGrid(event), getYAdaptGrid(event), (int) inputSlider.getValue(), nodeGestures));
            simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());        
      }
      else if(btnNAND.isSelected()){ //Nand
            elements.add(new Element_NAND(getXAdaptGrid(event), getYAdaptGrid(event), (int) inputSlider.getValue(), nodeGestures));
            simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());        
      }
      else if(btnXNOR.isSelected()){ //Nand
            elements.add(new Element_XNOR(getXAdaptGrid(event), getYAdaptGrid(event), (int) inputSlider.getValue(), nodeGestures));
            simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());        
      }
      else if (btnLED.isSelected()) { //LED
            elements.add(new Element_LED(getXAdaptGrid(event), getYAdaptGrid(event), 1, nodeGestures));
            simCanvas.getChildren().add(elements.get(elements.size() -1).getGroup());
      }
      else if(btnSIGNAL.isSelected()){ //Signal
          elements.add(new Element_SIGNAL(getXAdaptGrid(event), getYAdaptGrid(event), nodeGestures));
          simCanvas.getChildren().add(elements.get(elements.size() -1).getGroup());
      }
      else if(btnTHUMBSWITCH.isSelected()){ //switch
          elements.add(new Element_THUMBSWITCH(getXAdaptGrid(event), getYAdaptGrid(event), nodeGestures));
          simCanvas.getChildren().add(elements.get(elements.size() -1).getGroup());
      }
      else if(btnTEXT.isSelected()){ //Text
          TextInputDialog dialog = new TextInputDialog("");    //Eingabefesnster 
          dialog.setTitle("Textfeld");                            
          dialog.setHeaderText("Eingabe:");
          Optional<String> result = dialog.showAndWait();         //Fenster wird anezeigt und eingabe gespeichert
          if(result.get().isEmpty() || result.get().trim().equals(""))
              return;
          elements.add(new Element_TEXT(getXAdaptGrid(event), getYAdaptGrid(event), 30, result.get(), Color.BLACK, nodeGestures));
          simCanvas.getChildren().add(elements.get(elements.size() -1).getGroup());
      }
    }
   
    /**
     * Gibt zurück ob sich die Maus über einer Node (Element) befindet
     * Author: Dominik (06.11.2016)
     * --Editiert von Elias 11.11.2016
     */
    public boolean isMouseOverNode(MouseEvent event) { //Tested ob die Maus über einem Element (Node) ist, static damit man es in DraggableCanvas.java benutzen kann
        boolean result = false;
        
        for(Element i : elements){ //elemente durchgehen...
            double addition = 0;
            if(i.getInputCount()>3) {
                addition = (i.getInputCount()-3)*Properties.GetGridOffset();
            }
            double element_x = i.getX() - (i.getWidth() / 2.4);         //X,Width,Height werden an das Element angepasst, da das Element in der mitte der Maus plaziert wird! 
            double element_y = i.getY() - (i.getWidth() / 2.4);
            double element_w = i.getWidth() + (i.getWidth() / 1.2);     //Es mag etwas viel vorkommen (besonderst bei den Y Coords, ist allerdings notwendig
            double element_h = i.getHeight() +addition + (i.getHeight() / 1.2);   //wegen den out & inputs!!
            
            //Abfrage ob sich der Mauszeiger im Block eines Elements befindet.
            if(event.getX() > element_x && event.getX() < element_x + element_w && event.getY() > element_y && event.getY() < element_y + element_h) {
                result = true;
            }          
        }
        return result;
    }  
    
    /**
     * Nimmt die X-Mauskoordinate und passt sie an das Grid an
     * @Author Elias
     */
    public double getXAdaptGrid(MouseEvent event) { 
        return Math.round(event.getX() / Properties.GetGridOffset()) * Properties.GetGridOffset();
    }
    
    /**
     * Nimmt die Y-Mauskoordinate und passt sie an das Grid an
     * @Author Elias
     */
    public double getYAdaptGrid(MouseEvent event) { 
        return Math.round(event.getY() / Properties.GetGridOffset()) * Properties.GetGridOffset();
    }
    
    public ArrayList<Element> getElements(){ //Über diese Methode können andere Klassen auf die Elemente zugreifen
        return elements;
    }
    
    public void rebuildElement(Element e, int inputs){ //Löscht ein Element und baut es neu auf mit den gegebenen Inputs
          allConnections.removeAllConncectionsRelatedTo(e);
          if(e.getClass().equals(Element_AND.class)){ //Rausfinden um welches Element es sich handelt
              elements.add(new Element_AND(e.getX() + (e.getWidth() / 2), e.getY() + (e.getHeight() / 2), inputs, nodeGestures)); //Element aufbau wie oben bei addElement() [Bei X/Y die hälfte der Höhe und Weite abziehen um die richtige Position zu bekommen]
              simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
              simCanvas.getChildren().remove(e.getGroup()); //Element von der Arbeitsfläche und aus den elements löschen
              elements.remove(e);
          }else if(e.getClass().equals(Element_NAND.class)){
              elements.add(new Element_NAND(e.getX() + (e.getWidth() / 2), e.getY() + (e.getHeight() / 2), inputs, nodeGestures));
              simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
              simCanvas.getChildren().remove(e.getGroup());
              elements.remove(e);
          }else if(e.getClass().equals(Element_OR.class)){
              elements.add(new Element_OR(e.getX() + (e.getWidth() / 2), e.getY() + (e.getHeight() / 2), inputs, nodeGestures));
              simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
              simCanvas.getChildren().remove(e.getGroup());
              elements.remove(e);
          }else if(e.getClass().equals(Element_NOR.class)){
              elements.add(new Element_NOR(e.getX() + (e.getWidth() / 2), e.getY() + (e.getHeight() / 2), inputs, nodeGestures));
              simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
              simCanvas.getChildren().remove(e.getGroup());
              elements.remove(e);
          }else if(e.getClass().equals(Element_XNOR.class)){
              elements.add(new Element_XNOR(e.getX() + (e.getWidth() / 2), e.getY() + (e.getHeight() / 2), inputs, nodeGestures));
              simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
              simCanvas.getChildren().remove(e.getGroup());
              elements.remove(e);    
          }else if(e.getClass().equals(Element_XOR.class)){
              elements.add(new Element_XOR(e.getX() + (e.getWidth() / 2), e.getY() + (e.getHeight() / 2), inputs, nodeGestures));
              simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
              simCanvas.getChildren().remove(e.getGroup());
              elements.remove(e);    
          }
    }
    
    public void rebuildElement_TEXT(Element e, int fontSize, String content, Color color){
              elements.add(new Element_TEXT(e.getX(), e.getY(), fontSize, content, color, nodeGestures));
              simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
              simCanvas.getChildren().remove(e.getGroup());
              elements.remove(e); 
    }
        
    public void run(){
        elements.forEach(e -> e.update()); //Geht alle Elemente durch und Updaten sie. ACHTUNG: Lambda schreibweise! Infos -> https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html   
        allConnections.update(); //Alle Verbindungen updaten
    }
    
    //Methode und Boolean sind jetzt static für LED (und signal vlcht?) *Lukas*
    public static boolean isLocked(){ //Schauen ob das Programm blokiert ist (erklärung: siehe DigitSimController oben)
        return locked;
    }
    
    private void resetElements(){
        outputMessages.add("[INFO]Alle Elemente zurücksetzen..");
        elements.forEach(e -> e.reset()); //Alle Elemente reseten
        allConnections.reset();
    }
    
    public static ObservableList getOutputMessages() {
        return outputMessages;
    }
        
    //****************GET/SET*************************************************************************************
    public static DraggableCanvas getSimCanvas() {
        return simCanvas;
    }

    public static Connection getAllConnections() {
        return allConnections;
    }
    
    public static DigitSimController getReference(){ //Rückgabe einer Referenz (für Klassen die sonst keine besitzen)
        return refThis;
    }
    
    public void reloadGridColor(){
        simCanvas.redrawGrid();
    }
    
    public void reloadMinAndMaxWindowSize(){
        Stage stage = (Stage) simPane.getScene().getWindow();
        stage.setMinWidth(Properties.GetWindowMinX());
        stage.setMinHeight(Properties.GetWindowMinY());
    }
}
