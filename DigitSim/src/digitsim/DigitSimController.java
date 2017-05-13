package digitsim;
import general.Properties;
import toolbox.GenFunctions;
import Gestures.DraggableCanvas;
import Gestures.NodeGestures;
import Gestures.SceneGestures;
import connection.*;
import element.*;
import general.Vector2i;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.swing.JOptionPane;
import toolbox.SaveFormat;
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
    private static List<Connection> allConnections = new ArrayList(); //Alle Verbindungen werden hier gespeichert
    private SimThread runningThread = new SimThread(this); //Thread der, falls gestartet, immer die Elemente & Verbindungen updatet
    private static boolean locked = false; //Wenn wir das Programm starten setzen wir locked auf True, damit das Programm blokiert wird und man während der Simulation nichts ändern kann!
    private static DigitSimController refThis;
    private static final ObservableList outputMessages = FXCollections.observableArrayList();
    private String currentProjectPath = "";
    private boolean unfinishedConnection = false;
    private Vector2i mouseCoords = new Vector2i();
    private Line temporaryLine;//In dieser gruppe steckt die orangene halb durchsichtige Linie (beim Verlegen von Connections)
    private int tmpLoadCounter = 0;
    private static ProgramMode programMode = ProgramMode.IDLE;
    private static boolean elementHovered = false;
    private boolean intThread = false;
    private List<ToggleButton> toggleBtnList = new ArrayList(); //In dieser Gruppe stecken alle ToggleButtons

     /**
     * FXML OBJEKT-Erstellungs-Bereich:
     * Jedes Element, welches in der DigitSim.FXML verwendet wird, muss in folgendem wege im Code noch erstellt werden.
     */
    @FXML 
    private MenuItem mItemOpenFile;
    @FXML 
    private MenuItem mItemSaveFileAs;
    @FXML 
    private MenuItem mItemSaveFile;
    @FXML
    private MenuItem MenuSimuRun;
    @FXML
    private MenuItem MenuSimuStop;
    @FXML
    private MenuItem mItemNewFile;
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
    @FXML
    private ToggleButton btn7SEG;
    @FXML
    private ToggleButton btn7SegBCD;
    @FXML
    private ToggleButton btnCLOCK;
    @FXML
    private ToggleButton btnDTFF;
    @FXML
    private ToggleButton btnVA;
    @FXML
    private ToggleButton btnJKFF;
    @FXML
    private ToggleButton btnSRFF;
    
    
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
        outputMessages.add("[INFO]Neues Projekt erstellt!");
    }
    
    
    @FXML
    private void addSimCanvas() {
        simCanvas.addEventFilter(MouseEvent.MOUSE_PRESSED, getCanvasMouseKlickedEventHandler());
        simCanvas.addEventFilter(MouseEvent.MOUSE_MOVED, getCanvasMouseMovedEventHandler());
        simPane.getChildren().addAll(simCanvas); //die Arbeitsfläche auf das Panel setzen
    }    
    
    private EventHandler getCanvasMouseKlickedEventHandler(){ //Wenn auf der Arbeitsfläche geklickt wird
        return new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event){     
                if(isLocked()){ //Schauen ob das Programm blokiert ist (erklärung: siehe DigitSimController oben)
                    return;
                }
                if(event.isPrimaryButtonDown() && !isMouseOverNode(event) && programMode != ProgramMode.CONNECTIONEDITING && !elementHovered){
                    addElement(event); //Neuen Baustein einfügen
                }  else if(event.isPrimaryButtonDown() && !isMouseOverNode(event) && programMode == ProgramMode.CONNECTIONEDITING) {
                    Connection c = allConnections.get(allConnections.size()-1);
                    int index = c.getAnchorPoints().size();
                    Vector2i coords = new Vector2i((int)event.getX(), (int)event.getY());
                    coords.adaptToHalfGrid();
                    AnchorPoint aP = new AnchorPoint(index, coords, c);
                    allConnections.get(allConnections.size()-1).addAnchorPoint(aP);
                }
            }
        };
    }
    
    
    private EventHandler getCanvasMouseMovedEventHandler(){ //Falls man bereits einen Input/Output ausgewählt halt erscheint eine Linie, diese Funktion sorgt daüfr das die Linie dem Mauszeiger folgt
        return new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event){
                if(!allConnections.isEmpty()) {
                    Connection c = allConnections.get(allConnections.size()-1);
                    if(!c.isConnectionProcessFinished()) {
                        c.drawDirectPreLineToMouse(new Vector2i((int)event.getX(), (int)event.getY()));
                    }
                }
            }
        };
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
        btn7SEG.setToggleGroup(group);
        btn7SegBCD.setToggleGroup(group);
        btnCLOCK.setToggleGroup(group);
        btnDTFF.setToggleGroup(group);
        btnVA.setToggleGroup(group);
        btnJKFF.setToggleGroup(group);
        btnSRFF.setToggleGroup(group);

        this.toggleBtnList.add(btnAND);
        this.toggleBtnList.add(btnOR);
        this.toggleBtnList.add(btnNOT);
        this.toggleBtnList.add(btnNAND);
        this.toggleBtnList.add(btnNOR);
        this.toggleBtnList.add(btnXOR);
        this.toggleBtnList.add(btnXNOR);
        this.toggleBtnList.add(btnLED);
        this.toggleBtnList.add(btnSIGNAL);
        this.toggleBtnList.add(btnTEXT);
        this.toggleBtnList.add(btnTHUMBSWITCH);
        this.toggleBtnList.add(btn7SEG);
        this.toggleBtnList.add( btn7SegBCD);
        this.toggleBtnList.add(btnCLOCK);
        this.toggleBtnList.add(btnDTFF);
        this.toggleBtnList.add(btnVA);
        this.toggleBtnList.add(btnJKFF);
        this.toggleBtnList.add(btnSRFF);
        
    }
    
    public void unselectAllButtons() {
        for(ToggleButton tB : this.toggleBtnList) {
            tB.setSelected(false);
        }
    }
    
    public void disableAllButtons() {
        for(ToggleButton tB : this.toggleBtnList) {
            tB.setDisable(true);
        }
    }
    public void enableAllButtons() {
        for(ToggleButton tB : this.toggleBtnList) {
            tB.setDisable(false);
        }
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
        run();
    }
    
    public void mItemCloseAction(ActionEvent event){ //Programm schließen
        if(runningThread.isAlive()){
            runningThread.interrupt(); //Wenn der Thread beim schließen läuft muss dieser geschlossen werden, sonst läuft er weiter
        }
        System.exit(0);
    }
    
    public void mItemOpenFileAction(ActionEvent event) { //Datei öffnen  
          int confirmed = JOptionPane.showConfirmDialog(null, "Ungespeichertes geht verloren! Dennoch fortfahren?", "Neues Projekt", JOptionPane.YES_NO_OPTION);
            if (confirmed == JOptionPane.NO_OPTION) {
                return;
            }  
        File selectedFile = chooseFile("DigitSimFiles (*.dgs)", "*.dgs");  //Datei Auswählen
        if(selectedFile.exists() && selectedFile.canRead() && selectedFile.getPath().endsWith(".dgs")){
            this.clearElements();
            currentProjectPath = selectedFile.getPath();
            loadProject();
        }
    }
    
    public void mItemSaveFileAction(ActionEvent event) { //Datei öffnen      
        if(currentProjectPath == ""){
            mItemSaveFileAsAction(null);
            return;
        }
        saveProject(); 
    }
        
    public void mItemSaveFileAsAction(ActionEvent event) { //Datei öffnen      
        File selectedFile = getFilePath("DigitSimFiles (*.dgs)", "*.dgs");  //Datei Auswählen
            if(!selectedFile.getPath().endsWith(".dgs")){
                selectedFile = new File(selectedFile.getPath() + ".dgs");
            }
            currentProjectPath = selectedFile.getPath();
            mItemSaveFileAction(null);  
    }
    
    public void mItemNewFileAction(ActionEvent event) { //Datei öffnen      
       int confirmed = JOptionPane.showConfirmDialog(null, "Ungespeichertes geht verloren! Dennoch fortfahren?", "Neues Projekt", JOptionPane.YES_NO_OPTION);
            if (confirmed == JOptionPane.YES_OPTION) {
                this.clearElements();
                currentProjectPath = "";
                outputMessages.add("[INFO]Neues Projekt erstellt.");
            }
    } 

        
    public void mItemPropertiesOnAction(ActionEvent event) { //Einstellungs-Fenster öffnen
        Stage stage;
        stage = GenFunctions.openFXML(properties.PropertiesController.class, "Properties.fxml", "Einstellungen", "icon.png", StageStyle.DECORATED); //Öffnen des "Einstellungen"-Fensters
        stage.setResizable(false);
        stage.show();
    }
    public void mItemHelpOnAction(ActionEvent event) { //Hilfe öffnen
        Stage stage;
        stage = GenFunctions.openFXML(help.HelpController.class,"Help.fxml", "Hilfe", "icon.png", StageStyle.DECORATED); //Öffnen des "Hilfe"-Fensters
        stage.setWidth(600);
        stage.setResizable(false);
        stage.show();
    }
    
    public void clearElements() { //Hilfe öffnen  
        elements.clear();
        simCanvas.getChildren().clear();
        simCanvas.addGrid(simCanvas.getPrefWidth(), simCanvas.getPrefHeight());
        ConnectionHandler.removeAllConnections();
    }
    public void btnStartOnAction(ActionEvent event) {  
        intThread = false;
        programMode = ProgramMode.SIMULATION;
        ConnectionHandler.setRebuildBundles(true);
        outputMessages.clear(); //Platz machen für Error meldungen
        btnStart.setDisable(true);
        btnPause.setDisable(false);
        runningThread = new SimThread(this);
        outputMessages.add("[INFO] Starten der Simulation.");
        runningThread.start(); //Den Thread starten, d.h alle Elemente & Connections werden regelmäßig geupdated
        disableAllButtons();
        locked = true; //Programm blockieren (siehe erklärung oben)
  }     
    public void btnPauseOnAction(ActionEvent event) {   
        intThread = true;
        programMode = ProgramMode.IDLE;
        runningThread.interrupt(); //Den Thread anhalten
        ConnectionHandler.resetConnectionStates();  //Alle Verbindungen resetten
        resetElements(); //Alle ELemente resetten
        btnPause.setDisable(true);
        btnStart.setDisable(false);
        outputMessages.add("[INFO]Simulation beendet!");
        enableAllButtons();
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
    
    public void onKeyPressed(KeyEvent event) {
        String key = event.getCode().toString();
        if(key == "ESCAPE") {
            if(programMode == ProgramMode.CONNECTIONEDITING) {
                Connection c =allConnections.get(allConnections.size()-1);
                c.removeTempGroup();
                c.removeLine();
                allConnections.remove(c);
                c = null;
                programMode = ProgramMode.IDLE;
            }
            if(programMode == ProgramMode.IDLE) {
                unselectAllButtons();
            }
        }
        if(key == "DELETE") {
            if(programMode == ProgramMode.CONNECTIONEDITING) {
                Connection c =allConnections.get(allConnections.size()-1);
                c.removeTempGroup();
                c.removeLine();
                allConnections.remove(c);
                c = null;
                programMode = ProgramMode.IDLE;
            }
            Element selectedElement = null;
            for(Element e : elements) {
                if(e.isHovered()) {
                    selectedElement = e;
                    break;
                }
            }
            
            if(selectedElement != null) {
                simCanvas.getChildren().remove(selectedElement.getGroup());
                ConnectionHandler.removeAllConnectionsRelatedToElement(selectedElement);
                elements.remove(selectedElement);
            }
            
        }
    }
    
    public void onKeyReleased(KeyEvent event) {
        
    }
    
    public File chooseFile(String description, String extension){ //Die Funktion öffnet einen Filebrowser um eine Datei auszuwählen und lädt dise anschließend.
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(description, extension);
        fc.getExtensionFilters().add(extFilter);
        File selectedFile = fc.showOpenDialog(null);  
        return selectedFile;
    }
    public File getFilePath(String description, String extension){ //Die Funktion öffnet einen Filebrowser um eine Datei auszuwählen und lädt dise anschließend.
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(description, extension);
        fc.getExtensionFilters().add(extFilter);
        File selectedFile = fc.showSaveDialog(null);  
        return selectedFile;
    }
    
    public void saveProject(){ //Speichert das Projekt
        SaveFormat project = new SaveFormat(elements.size(), allConnections.size(), findHighestSizeOfConnectedTo()); //Format in welcher das Prokekt geschrieben wird
        project.setSimSizeX(Properties.GetSimSizeX()); //Die Einstellungen Speichern
        project.setSimSizeY(Properties.GetSimSizeY());
        saveElementsToProject(project); //Elemente speichern
        saveConnectionsToProject(project); //Verbindungen speichern
        toolbox.XmlLoader.saveObject(currentProjectPath, project); //Datei anlegen
        outputMessages.add("[INFO]Erfolgreiches speichern!");
    }
    
    public void saveElementsToProject(SaveFormat project){ //Speichert die Elemente
         for(int i = 0; i < elements.size(); i++){ //Elemente abgehen und Werte in die zu speichernde Datei eintragen
            project.getType()[i] = elements.get(i).getTypeName();
            project.getPayload()[i] = elements.get(i).getPayload();
            project.geteNumInputs()[i] = elements.get(i).getNumInputs();
            project.geteNumOutputs()[i] = elements.get(i).getNumOutputs();
            project.getePosX()[i] = elements.get(i).getX();
            project.getePosY()[i] = elements.get(i).getY();
        }
    }
    
    public void saveConnectionsToProject(SaveFormat project){ //Speichert Verbindungen
        for(int i = 0; i < allConnections.size(); i++){ //Verbindungen abgehen und Werte in die zu speichernde Datei eintragen
            if(allConnections.get(i).getStartPartner().getPartnerType() == PartnerType.ELEMENT){
                project.getConType()[i][0] = PartnerType.ELEMENT.name();
                project.getConElementIndex()[i][0]  = this.findElementIndex(allConnections.get(i).getStartPartner().getElement());
                project.getConInOrOutput()[i][0]  = allConnections.get(i).getStartPartner().isIsInput();
                project.getConIOIndex()[i][0]  = allConnections.get(i).getStartPartner().getIndex();
            }else{
                project.getConType()[i][1]  = PartnerType.CONNECTION.name();
                project.getConIndex()[i][0]  = this.findConnectionIndex(allConnections.get(i).getStartPartner().getconnection());
                project.getConX()[i][0]  = allConnections.get(i).getStartPartner().getAnchorPoint().getCoords().getX();
                project.getConY()[i][0]  = allConnections.get(i).getStartPartner().getAnchorPoint().getCoords().getY();
                project.getConAnchorIndex()[i][0] = (int) allConnections.get(i).getStartPartner().getAnchorPoint().getIndex();
                project.getConAPconnectedToSize()[i][0] = allConnections.get(i).getStartPartner().getAnchorPoint().getConnectedToSize();
                for(int t = 0; t < allConnections.get(i).getStartPartner().getAnchorPoint().getConnectedToSize(); t++){
                    project.getConAPconnectedToIndices()[i][0][t] = this.findConnectionIndex(allConnections.get(i).getStartPartner().getAnchorPoint().getConnectedTo().get(t));
                }
            }
            if(allConnections.get(i).getEndPartner().getPartnerType() == PartnerType.ELEMENT){
                project.getConType()[i][1]  = PartnerType.ELEMENT.name();
                project.getConElementIndex()[i][1] = this.findElementIndex(allConnections.get(i).getEndPartner().getElement());
                project.getConInOrOutput()[i][1] = allConnections.get(i).getEndPartner().isIsInput();
                project.getConIOIndex()[i][1] = allConnections.get(i).getEndPartner().getIndex();
            }else{
                project.getConType()[i][1]  = PartnerType.CONNECTION.name();
                project.getConIndex()[i][1] = this.findConnectionIndex(allConnections.get(i).getEndPartner().getconnection());
                project.getConX()[i][1] = allConnections.get(i).getEndPartner().getAnchorPoint().getCoords().getX();
                project.getConY()[i][1]= allConnections.get(i).getEndPartner().getAnchorPoint().getCoords().getY();
               project.getConAnchorIndex()[i][1] = (int) allConnections.get(i).getEndPartner().getAnchorPoint().getIndex();
               project.getConAPconnectedToSize()[i][1] = allConnections.get(i).getEndPartner().getAnchorPoint().getConnectedToSize();
                for(int t = 0; i < allConnections.get(i).getEndPartner().getAnchorPoint().getConnectedToSize(); i++){
                    project.getConAPconnectedToIndices()[i][1][t] = this.findConnectionIndex(allConnections.get(i).getEndPartner().getAnchorPoint().getConnectedTo().get(t));
                }
            }
        }
    }
    
    public void loadProject(){ //Ein Projekt laden
        SaveFormat project = (SaveFormat) toolbox.XmlLoader.loadObject(currentProjectPath, SaveFormat.class); //Die Datei lesen
        if(Properties.GetSimSizeX() < project.getSimSizeX() || Properties.GetSimSizeY() < project.getSimSizeY()){ //Einstellungen anpassen
            Properties.setSimSizeX(project.getSimSizeX());
            Properties.setSimSizeY(project.getSimSizeY());
            this.simCanvas.setMaxHeight(Properties.GetSimSizeY());
            this.simCanvas.setMaxWidth(Properties.GetSimSizeX());
            this.clearElements(); //Altes Project löschen
        }
        loadElementsFromProject(project); //Elemente laden
        loadConnectionsFromProject(project); //Verbindungen laden
        while(allConnections.size() < project.getNumConnections()){
            loadConnectionsFromProject(project); //Verbindungen laden, so lange bis alle geladen sind
            if(tmpLoadCounter > 10){
                outputMessages.add("[WARNING]Nicht alle Verbindungen konnten geladen werden!");
                return;
            }
        }     
        allConnections.forEach(c -> c.updateConnectionLine());
        tmpLoadCounter = 0;
        outputMessages.add("[INFO]Erfolgreiches laden!");
    }
    
    public void loadElementsFromProject(SaveFormat project){ //Lädt die Elemente
          for(int i = 0; i < project.getNumElements(); i++){ //Elemente durchgehen & laden (nach jeweiligen Typ)
            if(project.getType()[i].equals(ElementType.Type.AND.name())){
                 elements.add(new Element_AND(project.getePosX()[i], project.getePosY()[i], project.geteNumInputs()[i], nodeGestures));
                 simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
            }else if(project.getType()[i].equals(ElementType.Type.SEVENSEG.name())){
                elements.add(new Element_7SEG(project.getePosX()[i], project.getePosY()[i], project.geteNumInputs()[i], nodeGestures));
                 simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
            }else if(project.getType()[i].equals(ElementType.Type.LED.name())){
                elements.add(new Element_LED(project.getePosX()[i], project.getePosY()[i], project.geteNumInputs()[i], nodeGestures));
                 simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
            }else if(project.getType()[i].equals(ElementType.Type.NAND.name())){
                elements.add(new Element_NAND(project.getePosX()[i], project.getePosY()[i], project.geteNumInputs()[i], nodeGestures));
                 simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
            }else if(project.getType()[i].equals(ElementType.Type.NOR.name())){
                elements.add(new Element_NOR(project.getePosX()[i], project.getePosY()[i], project.geteNumInputs()[i], nodeGestures));
                 simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
            }else if(project.getType()[i].equals(ElementType.Type.NOT.name())){
                elements.add(new Element_NOT(project.getePosX()[i], project.getePosY()[i], project.geteNumInputs()[i], nodeGestures));
                 simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
            }else if(project.getType()[i].equals(ElementType.Type.OR.name())){
                elements.add(new Element_OR(project.getePosX()[i], project.getePosY()[i], project.geteNumInputs()[i], nodeGestures));
                 simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
            }else if(project.getType()[i].equals(ElementType.Type.SIGNAL.name())){
                elements.add(new Element_SIGNAL(project.getePosX()[i], project.getePosY()[i], nodeGestures));
                 simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
            }else if(project.getType()[i].equals(ElementType.Type.TEXT.name())){
                elements.add(new Element_TEXT(project.getePosX()[i], project.getePosY()[i], 20,project.getPayload()[i], Color.BLACK, nodeGestures));
                 simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
            }else if(project.getType()[i].equals(ElementType.Type.THUMBSWITCH.name())){
                elements.add(new Element_THUMBSWITCH(project.getePosX()[i], project.getePosY()[i], nodeGestures));
                 simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
            }else if(project.getType()[i].equals(ElementType.Type.XNOR.name())){
                elements.add(new Element_XNOR(project.getePosX()[i], project.getePosY()[i], project.geteNumInputs()[i], nodeGestures));
                 simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
            }else if(project.getType()[i].equals(ElementType.Type.XOR.name())){
                elements.add(new Element_XOR(project.getePosX()[i], project.getePosY()[i], project.geteNumInputs()[i], nodeGestures));
                 simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
            }else if(project.getType()[i].equals(ElementType.Type.SEVENSEGBCD.name())){
                elements.add(new Element_7SEG_BCD(project.getePosX()[i], project.getePosY()[i], project.geteNumInputs()[i], nodeGestures));
                 simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
            }else if(project.getType()[i].equals(ElementType.Type.SEVENSEGBCD.name())){
                elements.add(new Element_7SEG_BCD(project.getePosX()[i], project.getePosY()[i], 4, nodeGestures));
                 simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
            } else if(project.getType()[i].equals(ElementType.Type.CLOCK.name())){
                elements.add(new Element_CLOCK(project.getePosX()[i], project.getePosY()[i],project.getPayload()[i], nodeGestures));
                 simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
            } else if(project.getType()[i].equals(ElementType.Type.DTFF.name())){
                elements.add(new Element_DTFF(project.getePosX()[i], project.getePosY()[i], nodeGestures));
                 simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
            } else if(project.getType()[i].equals(ElementType.Type.VA.name())){
                elements.add(new Element_FULLADDER(project.getePosX()[i], project.getePosY()[i], nodeGestures));
                 simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
            } else if(project.getType()[i].equals(ElementType.Type.JKFF.name())){
                elements.add(new Element_JKFF(project.getePosX()[i], project.getePosY()[i], nodeGestures));
                 simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
            } else if(project.getType()[i].equals(ElementType.Type.SRFF.name())){
                elements.add(new Element_SRFF(project.getePosX()[i], project.getePosY()[i], nodeGestures));
                 simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
            }
        }
    }
    
    public void loadConnectionsFromProject(SaveFormat project){//Lädt Verbindungen
        loadConnectionsFromProjectBasics(project); //Erst die Verbindungen laden die man auf jeden Fall laden kann
        loadConnectionsFromProjectAdvanced(project); //Restliche V. laden, die etwas komplizierter sind (da sie sich evnt. auf andere Verbindungen beziehen). Es kann sein das diese Methode merhmals aufgerufen werden muss falls es viele erweiterte Verbindungen gibt (also nicht von Element zu Element)
    }
    
    public void loadConnectionsFromProjectBasics(SaveFormat project){
        for(int i = 0; i < project.getNumConnections(); i++){ //Verbindung durchgehen aber erstmals nur Verbindungen laden die sich rein auf Elemente beziehen
            if(project.getConType()[i][0].equals(PartnerType.ELEMENT.name()) && project.getConType()[i][1].equals(PartnerType.ELEMENT.name())){ //Element auf Element
                ConnectionPartner startPartner = new ConnectionPartner(elements.get(project.getConElementIndex()[i][0]), project.getConInOrOutput()[i][0], project.getConIOIndex()[i][0]);
                ConnectionPartner endPartner = new ConnectionPartner(elements.get(project.getConElementIndex()[i][1]), project.getConInOrOutput()[i][1], project.getConIOIndex()[i][1]);
                allConnections.add(new Connection(this, startPartner, endPartner)); //Start- und EndPartner werden benötigt um eine Connection zu erstellen
            }
        }    
    }
    
    public void loadConnectionsFromProjectAdvanced(SaveFormat project){
        tmpLoadCounter++;
        for(int i = 0; i < project.getNumConnections(); i++){ //Verbindungen erneut durchgehen und diesmal den rest laden
            if(!(project.getConType()[i][0].equals(PartnerType.ELEMENT.name()) && project.getConType()[i][1].equals(PartnerType.ELEMENT.name()))){ //Alle Verbindungen die noch nicht geladen wurden sollen jetzt geladen werden
                ConnectionPartner startPartner = null; //Start- und EndPartner werden benötigt um eine Connection zu erstellen
                ConnectionPartner endPartner = null;
                boolean unknownCon = false; //Falls sich die Verbindung auf eine Verbindung bezieht die unbekannt ist
                //StartPartner
                if(project.getConType()[i][0].equals(PartnerType.ELEMENT)){ //Element
                     startPartner = new ConnectionPartner(elements.get(project.getConElementIndex()[i][0]), project.getConInOrOutput()[i][0], project.getConIOIndex()[i][0]);
                }else{ //Kein Element 
                    if(allConnections.size() > project.getConIndex()[i][0]){
                        AnchorPoint ap = new AnchorPoint(project.getConAnchorIndex()[i][0] ,new Vector2i(project.getConX()[i][0], project.getConY()[i][0]));
                        
                        for(int t = 0; t < project.getConAPconnectedToSize()[i][0]; t++){
                            if(project.getConAPconnectedToIndices()[i][0][t] < allConnections.size()){
                                ap.getConnectedTo().add(allConnections.get(project.getConAPconnectedToIndices()[i][0][t]));
                            }
                        }
                        startPartner = new ConnectionPartner(allConnections.get(project.getConIndex()[i][0]), ap);
                    }else{
                        unknownCon = true;
                    }
                }
                
                //EndPartner
                if(project.getConType()[i][1].equals(PartnerType.ELEMENT)){ //Element
                     endPartner = new ConnectionPartner(elements.get(project.getConElementIndex()[i][1]), project.getConInOrOutput()[i][1], project.getConIOIndex()[i][1]);
                }else{//Kein Element 
                    if(allConnections.size() > project.getConIndex()[i][1]){
                        AnchorPoint ap = new AnchorPoint(project.getConAnchorIndex()[i][1] ,new Vector2i(project.getConX()[i][1], project.getConY()[i][1]));
                        
                        for(int t = 0; t < project.getConAPconnectedToSize()[i][1]; t++){
                            if(project.getConAPconnectedToIndices()[i][1][t] < allConnections.size()){
                                ap.getConnectedTo().add(allConnections.get(project.getConAPconnectedToIndices()[i][1][t]));
                            }
                        }
                        endPartner = new ConnectionPartner(allConnections.get(project.getConIndex()[i][1]), ap);
                    }else{
                        unknownCon = true;
                    }
                }
                if(!unknownCon && startPartner != null && endPartner != null){
                    allConnections.add(new Connection(this, startPartner, endPartner));
                }
            }
        }
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
      else if(btn7SEG.isSelected()){ //7Seg
          elements.add(new Element_7SEG(getXAdaptGrid(event), getYAdaptGrid(event), 7, nodeGestures));
          simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
      }
      else if(btn7SegBCD.isSelected()){ //7SegBCD
          elements.add(new Element_7SEG_BCD(getXAdaptGrid(event), getYAdaptGrid(event), 4, nodeGestures));
          simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
      }
      else if(btnCLOCK.isSelected()){ //CLOCK
          elements.add(new Element_CLOCK(getXAdaptGrid(event), getYAdaptGrid(event), "1", nodeGestures));
          simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
      }
      else if(btnDTFF.isSelected()){ //DTFF
          elements.add(new Element_DTFF(getXAdaptGrid(event), getYAdaptGrid(event), nodeGestures));
          simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
      }
      else if(btnVA.isSelected()){ //FullAdder
          elements.add(new Element_FULLADDER(getXAdaptGrid(event), getYAdaptGrid(event), nodeGestures));
          simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
      }
      else if(btnJKFF.isSelected()){ //FullAdder
          elements.add(new Element_JKFF(getXAdaptGrid(event), getYAdaptGrid(event), nodeGestures));
          simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
      }
      else if(btnSRFF.isSelected()){ //SRFF
          elements.add(new Element_SRFF(getXAdaptGrid(event), getYAdaptGrid(event), nodeGestures));
          simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
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
    
    
    
    public void rebuildElement(Element e, int inputs){ //Löscht ein Element und baut es neu auf mit den gegebenen Inputs
        ConnectionHandler.removeAllConnectionsRelatedToElement(e);
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
          }else if(e.getClass().equals(Element_THUMBSWITCH.class)){
              elements.add(new Element_THUMBSWITCH(e.getX() + (e.getWidth() / 2), e.getY() + (e.getHeight() / 2), nodeGestures));
              simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
              simCanvas.getChildren().remove(e.getGroup());
              elements.remove(e);    
          }else if(e.getClass().equals(Element_FULLADDER.class)){
              elements.add(new Element_FULLADDER(e.getX() + (e.getWidth() / 2), e.getY() + (e.getHeight() / 2), nodeGestures));
              simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
              simCanvas.getChildren().remove(e.getGroup());
              elements.remove(e);    
          }else if(e.getClass().equals(Element_JKFF.class)){
              elements.add(new Element_JKFF(e.getX() + (e.getWidth() / 2), e.getY() + (e.getHeight() / 2), nodeGestures));
              simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
              simCanvas.getChildren().remove(e.getGroup());
              elements.remove(e);    
          }else if(e.getClass().equals(Element_SRFF.class)){
              elements.add(new Element_SRFF(e.getX() + (e.getWidth() / 2), e.getY() + (e.getHeight() / 2), nodeGestures));
              simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
              simCanvas.getChildren().remove(e.getGroup());
              elements.remove(e);    
          }
    }
    
    public void addConnection(Element e, boolean isInput, int index, MouseEvent event) {
        
        if(allConnections.isEmpty()) {
            this.unfinishedConnection = true;
            allConnections.add(new Connection(this, e, isInput, index));
            programMode = programMode.CONNECTIONEDITING;
        } else if (allConnections.get(allConnections.size()-1).isConnectionProcessFinished()) {
            this.unfinishedConnection = true;
            allConnections.add(new Connection(this, e, isInput, index));
            programMode = programMode.CONNECTIONEDITING;
        } else {
            this.unfinishedConnection = false;
            allConnections.get(allConnections.size()-1).finishLine(e, isInput, index);
            programMode = programMode.IDLE;
        }   
    }
    
    public void addConnection(Connection c, AnchorPoint aP) {
        
        if(allConnections.isEmpty()) {
            this.unfinishedConnection = true;
            allConnections.add(new Connection(this, c, aP));
            programMode = programMode.CONNECTIONEDITING;
        } else if (allConnections.get(allConnections.size()-1).isConnectionProcessFinished()) {
            this.unfinishedConnection = true;
            allConnections.add(new Connection(this, c, aP));
            programMode = programMode.CONNECTIONEDITING;
        } else {
            this.unfinishedConnection = false;
            allConnections.get(allConnections.size()-1).finishLine(c, aP);
            programMode = programMode.IDLE;
        }   
    }
    
    public void rebuildElement_TEXT(Element e, int fontSize, String content, Color color){
              elements.add(new Element_TEXT(e.getX(), e.getY(), fontSize, content, color, nodeGestures));
              simCanvas.getChildren().add(elements.get(elements.size() - 1).getGroup());
              simCanvas.getChildren().remove(e.getGroup());
              elements.remove(e); 
    }
    
    public void closeElement_THUMBSWITCH(Element e){
        ConnectionHandler.removeAllConnectionsRelatedToElement(e);
        simCanvas.getChildren().remove(e.getGroup());
        elements.remove(e); 
    }
    
    public void run(){
        elements.forEach(e -> e.update()); //Geht alle Elemente durch und Updaten sie. ACHTUNG: Lambda schreibweise! Infos -> https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html   
        ConnectionHandler.updateConnectionStates();
    }
    
    public static void outputMessage(String msg){
        String s = (String) outputMessages.get(outputMessages.size()-1);
        if(!s.equals(msg)) {
            outputMessages.add(msg);
        }
    }
    
    public static void outputMessage_UNDEFINED(){
        String msg = "Undefinierter zustand ist aufgetreten";
        String s = (String) outputMessages.get(outputMessages.size()-1);
        if(!s.equals(msg)) {
            outputMessages.add(msg);
        }
    }
    
    
    
    //Methode und Boolean sind jetzt static für LED (und signal vlcht?) *Lukas*
    public static boolean isLocked(){ //Schauen ob das Programm blokiert ist (erklärung: siehe DigitSimController oben)
        return locked;
    }
    
    public void resetElements(){
        elements.forEach(e -> e.reset()); //Alle Elemente reseten
        
    }
    
    public static ObservableList getOutputMessages() {
        return outputMessages;
    }
        
    //****************GET/SET*************************************************************************************
    public static DraggableCanvas getSimCanvas() {
        return simCanvas;
    }
    
    public static DigitSimController getReference(){ //Rückgabe einer Referenz (für Klassen die sonst keine besitzen)
        return refThis;
    }
    
    public void reloadGridColor(){
        simCanvas.redrawGrid();
    }

    public static List<Connection> getAllConnections() {
        return allConnections;
    }

    public static void setAllConnections(List<Connection> allConnections) {
        DigitSimController.allConnections = allConnections;
    }
    
    public static void removeConnectionFromAllConnections(Connection c) {
        allConnections.remove(c);
    }
    
    public static void clearConnections() {
        allConnections.clear();
    }
    
    public ArrayList<Element> getElements(){ //Über diese Methode können andere Klassen auf die Elemente zugreifen
        return elements;
    }
    
    public boolean isUnfinishedConnection() {
        return this.unfinishedConnection;
    }

    public Vector2i getMouseCoords() {
        return mouseCoords;
    }
    
    public boolean isIntThread(){
        return intThread;
    }

    public void setMouseCoords(Vector2i mouseCoords) {
        this.mouseCoords = mouseCoords;
    }
    
    public void removeTemporaryLine() {
        getSimCanvas().getChildren().remove(this.temporaryLine);
    }

    public static ProgramMode getProgramMode() {
        return programMode;
    }

    public static void setProgramMode(ProgramMode programMode) {
        DigitSimController.programMode = programMode;
    }

    public static boolean isElementHovered() {
        return elementHovered;
    }

    public static void setElementHovered(boolean elementHovered) {
        DigitSimController.elementHovered = elementHovered;
    }
    
    public int findElementIndex(Element e){
        for(int t = 0; t < elements.size(); t++){
            if(elements.get(t).hashCode() == e.hashCode()){
                return t;
            }
        }
        return -1;
    }
    
    public int findConnectionIndex(Connection c) {
        for(int t = 0; t < allConnections.size(); t++){
            if(allConnections.get(t).hashCode() == c.hashCode()){
                return t;
            }
        }
        return -1;
    }
    
    public void reloadMinAndMaxWindowSize(){
        Stage stage = (Stage) simPane.getScene().getWindow();
        stage.setMinWidth(Properties.GetWindowMinX());
        stage.setMinHeight(Properties.GetWindowMinY());
    }
    
    private int findHighestSizeOfConnectedTo(){
        int payload = 0;
        for(Connection c : allConnections){
            if(c.getStartPartner().getPartnerType() == PartnerType.CONNECTION){
                if(payload < c.getStartPartner().getAnchorPoint().getConnectedToSize()){
                    payload = c.getStartPartner().getAnchorPoint().getConnectedToSize();
                }
            }
            if(c.getEndPartner().getPartnerType() == PartnerType.CONNECTION){
                if(payload < c.getEndPartner().getAnchorPoint().getConnectedToSize()){
                    payload = c.getEndPartner().getAnchorPoint().getConnectedToSize();
                }
            }
        }
        return payload;
    }
}
