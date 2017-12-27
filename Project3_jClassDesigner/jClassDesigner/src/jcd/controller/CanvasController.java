package jcd.controller;

import javafx.event.Event;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Shape;
import jcd.data.Connector;
import jcd.data.DataManager;
import jcd.data.Draggable;
import jcd.data.DraggableVBox;
import jcd.data.jClassDesignerState;
import static jcd.data.jClassDesignerState.DRAGGING_NOTHING;
import static jcd.data.jClassDesignerState.DRAGGING_SHAPE;
import static jcd.data.jClassDesignerState.SELECTING_SHAPE;
import static jcd.data.jClassDesignerState.SIZING_SHAPE;
import jcd.gui.Workspace;
import saf.AppTemplate;

/**
 * This class responds to interactions with the rendering surface.
 * 
 * @author McKillaGorilla
 * @version 1.0
 */
public class CanvasController {
    AppTemplate app;
    //boolean isSnapped = false;
    
    public CanvasController(AppTemplate initApp) {
	app = initApp;
    }
    
    public void processCanvasMouseExited(int x, int y) {
	DataManager dataManager = (DataManager)app.getDataComponent();
	if (dataManager.isInState(jClassDesignerState.DRAGGING_SHAPE)) {
	    
	}
	else if (dataManager.isInState(jClassDesignerState.SIZING_SHAPE)) {
	    
	}
    }
    
    public void processCanvasMousePress(int x, int y, MouseEvent e){//, Node selectedNode) {
	DataManager dataManager = (DataManager)app.getDataComponent();
/*
        if (dataManager.isInState(SELECTING_SHAPE)) {
            Scene scene = app.getGUI().getPrimaryScene();
            if(selectedNode != null) {
                scene.setCursor(Cursor.MOVE);
                dataManager.setState(jClassDesignerState.DRAGGING_SHAPE);
                app.getGUI().updateToolbarControls(false);
            } else {
                scene.setCursor(Cursor.DEFAULT);
                dataManager.setState(DRAGGING_NOTHING);
                app.getWorkspaceComponent().reloadWorkspace();
            }
        } else if (dataManager.isInState(jClassDesignerState.STARTING_RECTANGLE)) {
            dataManager.startNewRectangle(x, y);
        }
        */

	if (dataManager.isInState(SELECTING_SHAPE)) {
	    // SELECT THE TOP SHAPE
            VBox shape = dataManager.selectTopShape(x, y, e);
            
	    Scene scene = app.getGUI().getPrimaryScene();

	    // AND START DRAGGING IT
	    if (shape != null) {
		scene.setCursor(Cursor.MOVE);
		dataManager.setState(jClassDesignerState.DRAGGING_SHAPE);
                for(Connector c : dataManager.getConnectors()){
                    c.updateEndPoints();
                }
		app.getGUI().updateToolbarControls(false);
	    }
	    else {
		scene.setCursor(Cursor.DEFAULT);
		dataManager.setState(DRAGGING_NOTHING);
		app.getWorkspaceComponent().reloadWorkspace();
	    }
        }
        /*
	else if (dataManager.isInState(jClassDesignerState.STARTING_RECTANGLE)) {
	    dataManager.startNewRectangle(x, y);
	}
	else if (dataManager.isInState(jClassDesignerState.STARTING_ELLIPSE)) {
	    dataManager.startNewEllipse(x, y);
	}
        */

	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace();
    }
    
    public void processCanvasMouseMoved(int x, int y, MouseEvent e) {
	//Workspace workspace = (Workspace)app.getWorkspaceComponent();
	//workspace.setDebugText("(" + x + "," + y + ")");
        DataManager dataManager = (DataManager)app.getDataComponent();
	if (dataManager.isInState(SIZING_SHAPE)) {
	    DraggableVBox vbox = dataManager.getSelectedShape();
            if(vbox != null){
                Scene scene = app.getGUI().getPrimaryScene();
                if(e.getY() <= vbox.getY()+vbox.getHeight()+2 && e.getY() >= vbox.getY()+vbox.getHeight()-2 && 
                        e.getX() <= vbox.getX()+vbox.getWidth()+2 && e.getX() >= vbox.getX()+vbox.getWidth()-2){
                    scene.setCursor(Cursor.SE_RESIZE);
                }else if(e.getY() <= vbox.getY()+vbox.getHeight()+2 && e.getY() >= vbox.getY()+vbox.getHeight()-2){
                    scene.setCursor(Cursor.S_RESIZE);
                }else if(e.getX() <= vbox.getX()+vbox.getWidth()+2 && e.getX() >= vbox.getX()+vbox.getWidth()-2){
                    scene.setCursor(Cursor.E_RESIZE);
                }else{
                    scene.setCursor(Cursor.DEFAULT);
                }
            }
	    //System.out.println("eek " + vbox);
	}

    }
    
    public void processCanvasMouseDragged(int x, int y, MouseEvent e) {
	DataManager dataManager = (DataManager)app.getDataComponent();
	if (dataManager.isInState(SIZING_SHAPE)) {
	    DraggableVBox vbox = dataManager.getSelectedShape();
            if(vbox != null){
                Scene scene = app.getGUI().getPrimaryScene();
                if(scene.getCursor().equals(Cursor.SE_RESIZE)){
                    resizeBoth(vbox, e);
                }else if(scene.getCursor().equals(Cursor.S_RESIZE)){
                    resizeHeight(vbox, e);
                }else if(scene.getCursor().equals(Cursor.E_RESIZE)){
                    resizeWidth(vbox, e);
                }else{
                    scene.setCursor(Cursor.DEFAULT);
                }

            }
	    //System.out.println("eek " + vbox);
	} else if (dataManager.isInState(DRAGGING_SHAPE) && !dataManager.isSnapped) {
	    DraggableVBox selectedDraggableShape = dataManager.getSelectedShape();
            selectedDraggableShape.drag(x, y);
            for(Connector c : dataManager.getConnectors()){
                c.updateEndPoints();
            }
	    app.getGUI().updateToolbarControls(false);
	} else if (dataManager.isInState(DRAGGING_SHAPE) && dataManager.isSnapped) {
	    DraggableVBox selectedDraggableShape = dataManager.getSelectedShape();
	    selectedDraggableShape.dragSnapped(x, y);
            for(Connector c : dataManager.getConnectors()){
                c.updateEndPoints();
            }
	    app.getGUI().updateToolbarControls(false);
	}
    }
    
    private void resizeBoth(DraggableVBox vbox, MouseEvent e){
        vbox.setBoxWidth(e.getX() - vbox.getX());
        vbox.setBoxHeight(e.getY() - vbox.getY());
    }
    
    private void resizeHeight(DraggableVBox vbox, MouseEvent e){
        vbox.setBoxHeight(e.getY() - vbox.getY());
    }
    
    private void resizeWidth(DraggableVBox vbox, MouseEvent e){
        vbox.setBoxWidth(e.getX() - vbox.getX());
    }
    
    public void processCanvasMouseRelease(int x, int y) {
	DataManager dataManager = (DataManager)app.getDataComponent();
	if (dataManager.isInState(SIZING_SHAPE)) {
//	    dataManager.selectSizedShape();
//	    app.getGUI().updateToolbarControls(false);
	}
	else if (dataManager.isInState(jClassDesignerState.DRAGGING_SHAPE)) {
	    dataManager.setState(SELECTING_SHAPE);
	    Scene scene = app.getGUI().getPrimaryScene();
	    scene.setCursor(Cursor.DEFAULT);
            for(Connector c : dataManager.getConnectors()){
                c.updateEndPoints();
            }
	    app.getGUI().updateToolbarControls(false);
	}
	else if (dataManager.isInState(jClassDesignerState.DRAGGING_NOTHING)) {
	    dataManager.setState(SELECTING_SHAPE);
	}
    }
}
