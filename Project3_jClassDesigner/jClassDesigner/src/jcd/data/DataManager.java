package jcd.data;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import static jcd.data.BoxType.*;
import static jcd.data.jClassDesignerState.SELECTING_SHAPE;
import static jcd.data.jClassDesignerState.SIZING_SHAPE;
import jcd.gui.Workspace;
import saf.components.AppDataComponent;
import saf.AppTemplate;

/**
 * This class serves as the data management component for this application.
 *
 * @author Richard McKenna
 * @author Mary R. Taft
 * @version 1.0
 */
public class DataManager implements AppDataComponent {

    ObservableList<Node> vboxes; //graphically displayed
    ArrayList<VBoxData> dataItems = new ArrayList<VBoxData>(); //underlying
    ArrayList<Connector> connectors = new ArrayList<Connector>(); //for the
    // purpose of saving 'n' whatever, connectors will be implicitly generated
    
    DraggableVBox newVBox;
    DraggableVBox selectedVBox;

    // FOR FILL AND OUTLINE
    Color currentFillColor;
    Color currentOutlineColor;
    double currentBorderWidth;

    // CURRENT STATE OF THE APP
    jClassDesignerState state;
    public boolean isSnapped = false;

    // THIS IS A SHARED REFERENCE TO THE APPLICATION
    AppTemplate app;
    
    // USE THIS WHEN THE SHAPE IS SELECTED
    Effect highlightedEffect;

    public static final String WHITE_HEX = "#FFFFFF";
    public static final String BLACK_HEX = "#000000";
    public static final String YELLOW_HEX = "#EEEE00";
    public static final Paint DEFAULT_BACKGROUND_COLOR = Paint.valueOf(WHITE_HEX);
    public static final Paint HIGHLIGHTED_COLOR = Paint.valueOf(YELLOW_HEX);
    public static final int HIGHLIGHTED_STROKE_THICKNESS = 3;

    /**
     * THis constructor creates the data manager and sets up the
     *
     *
     * @param initApp The application within which this data manager is serving.
     */
    public DataManager(AppTemplate initApp) throws Exception {
	// KEEP THE APP FOR LATER
	app = initApp;

	// NO SHAPE STARTS OUT AS SELECTED
	newVBox = null;
	selectedVBox = null;

	// INIT THE COLORS
	currentFillColor = Color.web(WHITE_HEX);
	currentOutlineColor = Color.web(BLACK_HEX);
	currentBorderWidth = 1;
	
	// THIS IS FOR THE SELECTED SHAPE
        //todo: make this prettier for DraggableVBoxes
	DropShadow dropShadowEffect = new DropShadow();
	dropShadowEffect.setOffsetX(0.0f);
	dropShadowEffect.setOffsetY(0.0f);
	dropShadowEffect.setSpread(0.6);
	dropShadowEffect.setColor(Color.YELLOW);
	dropShadowEffect.setBlurType(BlurType.GAUSSIAN);
	dropShadowEffect.setRadius(10);
	highlightedEffect = dropShadowEffect;
    }
    
    public DataManager(){
        newVBox = null;
        selectedVBox = null;
    }
    
    public ObservableList<Node> getVBoxes() {
	return vboxes;
    }
    
    public ArrayList<VBoxData> getDataItems(){
        return dataItems;
    }
    
    public ObservableList<VBoxData> getNonAPIClasses(){
        ArrayList<VBoxData> tmp = new ArrayList<VBoxData>();
        for(VBoxData v : dataItems){
            if(v.getBoxType() != API_CLASS){
                tmp.add(v);
            }
        }
        return FXCollections.observableArrayList(tmp);
    }
        
    public ArrayList<Connector> getConnectors(){
        return connectors;
    }
    
    public Color getCurrentFillColor() {
	return currentFillColor;
    }

    public Color getCurrentOutlineColor() {
	return currentOutlineColor;
    }

    public double getCurrentBorderWidth() {
	return currentBorderWidth;
    }
    
    public void setShapes(ObservableList<Node> initShapes) {
	vboxes = initShapes;
    }

    public void setCurrentClassName(String text) {
        if (selectedVBox != null){
            //System.out.println("in setCurrentClassName()");
            ((DraggableVBox)selectedVBox).getData().setClassName(text);
            ((DraggableVBox)selectedVBox).updateText();
        }
    }

    public void setCurrentPackageName(String text) {
        if (selectedVBox != null){
            //System.out.println("in setCurrentPackageName()");
            ((DraggableVBox)selectedVBox).getData().setPackageName(text);
        }
    }

    public void removeSelectedShape() {
	if (selectedVBox != null) {
            if(selectedVBox.getData().getBoxType() == API_CLASS){
                //don't actually remove from list of classes
                selectedVBox.getData().setDisplayed(false);
            }else{
                //remove from list of classes entirely; delete from project
                dataItems.remove(selectedVBox.getData());
            }
            //remove graphically regardless of type
            vboxes.remove(selectedVBox);
	    selectedVBox = null;
            //System.out.println("dataItems after remove: " + dataItems);
            //System.out.println("vboxes after remove: " + vboxes);
	}
    }
    
    /*
    public void moveSelectedShapeToBack() {
	if (selectedShape != null) {
	    shapes.remove(selectedShape);
	    if (shapes.isEmpty()) {
		shapes.add(selectedShape);
	    }
	    else {
		ArrayList<Node> temp = new ArrayList();
		temp.add(selectedShape);
		for (Node node : shapes)
		    temp.add(node);
		shapes.clear();
		for (Node node : temp)
		    shapes.add(node);
	    }
	}
    }
    
    public void moveSelectedShapeToFront() {
	if (selectedShape != null) {
	    shapes.remove(selectedShape);
	    shapes.add(selectedShape);
	}
    }
    */
 
    /**
     * This function clears out the HTML tree and reloads it with the minimal
     * tags, like html, head, and body such that the user can begin editing a
     * page.
     */
    @Override
    public void reset() {
	setState(SELECTING_SHAPE);
	newVBox = null;
	selectedVBox = null;

	// INIT THE COLORS
	currentFillColor = Color.web(WHITE_HEX);
	currentOutlineColor = Color.web(BLACK_HEX);
	
	vboxes.clear();
	((Workspace)app.getWorkspaceComponent()).getCanvas().getChildren().clear();
        app.getGUI().gridCheckBox.setSelected(false);
        app.getGUI().snapCheckBox.setSelected(false);
    }

    public void selectSizedShape() {
	if (selectedVBox != null)
	    unhighlightShape();
	selectedVBox = newVBox;
	highlightShape(selectedVBox);
	newVBox = null;
        /*
	if (state == SIZING_SHAPE) {
	    state = ((Draggable)selectedVBox).getStartingState();
	}
        */
    }
    
    public void unhighlightShape() { //todo: old param: Shape shape--? why?
        if(selectedVBox != null) {
            selectedVBox.setEffect(null);
        }
    }
    
    public void highlightShape(VBox vbox) {
	vbox.setEffect(highlightedEffect);
    }

    public void initNewShape(VBox newVBox) {
	// DESELECT THE SELECTED SHAPE IF THERE IS ONE
	if (selectedVBox != null) {
	    unhighlightShape();
	    selectedVBox = null;
	}
        if(!vboxes.contains(newVBox)){
            vboxes.add(newVBox);
        }
	state = jClassDesignerState.SIZING_SHAPE;
        //System.out.println("vboxes upon init: " + vboxes);
    }
    
    public void initNewData(VBoxData data){
        if(!dataItems.contains(data)){
            dataItems.add(data);
            if(data.isDisplayed()){
                Workspace workspace = (Workspace)app.getWorkspaceComponent();
                Pane canvas = workspace.getCanvas();
                
                DraggableVBox classVBox = data.getVBox();
                classVBox.updateText();
                canvas.getChildren().add(classVBox); //adds VBox to canvas
                initNewShape(classVBox); //adds VBox to ObservableList
                setSelectedShape(classVBox); //sets VBox as selectedShape
            }
        }
        //System.out.println("dataItems upon init: " + dataItems);
    }
    
    public void initNewShapeWithoutDeselecting(VBox newVBox) {
        if(!vboxes.contains(newVBox)){
            vboxes.add(newVBox);
        }
	state = jClassDesignerState.SIZING_SHAPE;
        //System.out.println("vboxes upon init: " + vboxes);
    }
    
    public void initNewDataWithoutSelecting(VBoxData data){
        if(!dataItems.contains(data)){
            dataItems.add(data);
            if(data.isDisplayed()){
                Workspace workspace = (Workspace)app.getWorkspaceComponent();
                Pane canvas = workspace.getCanvas();
                
                DraggableVBox classVBox = data.getVBox();
                classVBox.updateText();
                canvas.getChildren().add(classVBox); //adds VBox to canvas
                initNewShapeWithoutDeselecting(classVBox); //adds VBox to ObservableList
            }
        }
        //System.out.println("dataItems upon init: " + dataItems);
    }

    public void initNewConnector(Connector c){
        if(!connectors.contains(c)){
            connectors.add(c);
            
            Workspace workspace = (Workspace)app.getWorkspaceComponent();
            Pane canvas = workspace.getCanvas();
                
            canvas.getChildren().add(c.getLine());
        }
        //System.out.println("connectors upon init: " + connectors);
    }

    public DraggableVBox getNewShape() {
	return newVBox;
    }

    public DraggableVBox getSelectedShape() {
	return selectedVBox;
    }

    public void setSelectedShape(VBox initSelectedShape) {
        unhighlightShape();
	selectedVBox = (DraggableVBox)initSelectedShape;
        highlightShape(selectedVBox);
    }

    public VBox selectTopShape(int x, int y, MouseEvent e) {
        VBox shape;
        Node tmp = e.getPickResult().getIntersectedNode();
        shape = null;
        if(tmp instanceof VBox){
            shape = (VBox)tmp;
        }

	if (shape == selectedVBox)
	    return shape;
	
	if (selectedVBox != null) {
	    unhighlightShape();
	}
	if (shape != null) {
	    highlightShape(shape);
	    Workspace workspace = (Workspace)app.getWorkspaceComponent();
	    workspace.loadSelectedShapeSettings(shape);
	}
	selectedVBox = (DraggableVBox)shape;
        /*
        //as far as I know, this is useless for jClassDesigner
	if (shape != null) {
	    ((Draggable)shape).start(x, y);
	}
        */
	return shape;
    }

    public void addShape(VBox shapeToAdd) {
	vboxes.add(shapeToAdd);
    }

    public jClassDesignerState getState() {
	return state;
    }

    public void setState(jClassDesignerState initState) {
	state = initState;
    }

    public boolean isInState(jClassDesignerState testState) {
	return state == testState;
    }
}
