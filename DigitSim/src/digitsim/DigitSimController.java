package digitsim;

import java.io.File;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
/**
 * Digitsim.fxml Controller class
 *
 * @author Elias
 * -Überarbeitet von Dominik 22.10.2016
 */
public class DigitSimController extends Pane{
    //************************ Globals ********************************
    DraggableCanvas simCanvas = new DraggableCanvas();
     /**
     * FXML OBJEKT-Erstellungs-Bereich:
     * Jedes Element, welches in der DigitSim.FXML verwendet wird, muss in folgendem wege im Code noch erstellt werden.
     */
    @FXML 
    private MenuItem mItemOpenFile;
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
    private AnchorPane simPane;
    @FXML
    private Button btnStart;
    
    //Constructor
    public DigitSimController() {
    }   
    
    @FXML
    public void initialize() {//initialize Funktion: wird direkt beim Starten der FXML aufgerufen.
        addSimCanvas();
        //AND and0 = new AND(gc);
        simCanvas.addGrid(simCanvas.getPrefWidth(), simCanvas.getPrefHeight());
        loadBtnGroup();     
               
        NodeGestures nodeGestures = new NodeGestures( simCanvas);

        //Zeichnen von Objekten
        Label label1 = new Label("Draggable node 1");
        label1.setTranslateX(10);
        label1.setTranslateY(10);
        label1.addEventFilter( MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
        label1.addEventFilter( MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());

        Circle circle1 = new Circle( 300, 300, 50);
        circle1.setTranslateX(50);
        circle1.setTranslateY(50);
        circle1.setStroke(Color.ORANGE);
        circle1.setFill(Color.ORANGE.deriveColor(1, 1, 1, 0.5));
        circle1.addEventFilter( MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
        circle1.addEventFilter( MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());

        Rectangle rect1 = new Rectangle(100,100);
        rect1.setTranslateX(450);
        rect1.setTranslateY(450);
        rect1.setStroke(Color.BLUE);
        rect1.setFill(Color.BLUE.deriveColor(1, 1, 1, 0.5));
        rect1.addEventFilter( MouseEvent.MOUSE_PRESSED, nodeGestures.getOnMousePressedEventHandler());
        rect1.addEventFilter( MouseEvent.MOUSE_DRAGGED, nodeGestures.getOnMouseDraggedEventHandler());

        simCanvas.getChildren().addAll(label1, circle1, rect1); //Gezeichnete Objekte hinzufügen
        
        SceneGestures sceneGestures = new SceneGestures(simCanvas);
        simCanvas.addEventFilter( MouseEvent.MOUSE_PRESSED, sceneGestures.getOnMousePressedEventHandler());
        simCanvas.addEventFilter( MouseEvent.MOUSE_DRAGGED, sceneGestures.getOnMouseDraggedEventHandler());
        simCanvas.addEventFilter( ScrollEvent.ANY, sceneGestures.getOnScrollEventHandler());
    }
    
    @FXML
    private void addSimCanvas() {
        simPane.getChildren().addAll(simCanvas);
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
        
    }
    //*********ON ACTION bereich: wird verwendet um z.B. Buttonclicks auszuwerten**********
    /**
    * Öffnet einen Filebrowser um eine Datei zu Öffnen.
    * 
    * @author Elias
    * -Bearbeitet von Dominik 22.10.16
    */
    public void mItemOpenFileAction(ActionEvent event) {        
        File selectedFile = chooseFile();  //Datei Auswählen
        //Öffnen der Datei (comming)
    }
    public void mItemPropertiesOnAction(ActionEvent event) {
        Stage stage;
        stage = GenFunctions.openFXML("Properties.fxml", "Einstellungen", "icon.png"); //Öffnen des "Einstellungen"-Fensters
        stage.setResizable(false);
    }
    public void mItemHelpOnAction(ActionEvent event) {
        Stage stage;
        stage = GenFunctions.openFXML("Help.fxml", "Hilfe", "icon.png"); //Öffnen des "Hilfe"-Fensters
        stage.setWidth(600);
        stage.setResizable(false);
    }
    public void btnStartOnAction(ActionEvent event) {
        System.out.println("simCanvas Weite: " +simCanvas.getWidth());
        System.out.println("simCanvas Höhe: " +simCanvas.getHeight());
        System.out.println("simCanvas TranslateX: " +simCanvas.getTranslateX());
        System.out.println("simCanvas TranslateY: " +simCanvas.getTranslateY());
    }
    
    public File chooseFile(){ //Die Funktion öffnet einen Filebrowser um eine Datei auszuwählen und lädt dise anschließend.
        FileChooser fc = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("DigitSimFiles (*.dgs)", "*.dgs");
        fc.getExtensionFilters().add(extFilter);
        File selectedFile = fc.showOpenDialog(null);  
        return selectedFile;
    }
    
    /**
     * 
     * @param event 
     * @return Returnd die Koordinaten der Maus
     * 
     */
    public double[] getMouseCoordinates(MouseEvent event) {
        //Coords[0] = x coordinate
        //Coords[1] = y Coordinate
        double coords[] = new double[2];
        coords[0] = event.getX();
        coords[1] = event.getY();
        return coords;
    }
}
      