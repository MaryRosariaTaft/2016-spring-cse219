package jcd.controller;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import static jcd.data.BoxType.*;
import jcd.data.DataManager;
import jcd.data.DraggableVBox;
import jcd.data.DraggableVBox;
import jcd.data.MethodRow;
import jcd.data.VBoxData;
import jcd.data.VariableRow;
import jcd.data.jClassDesignerState;
import jcd.gui.ConfigureParentsDialog;
import jcd.gui.VariableRowDialog;
import jcd.gui.MethodRowDialog;
import jcd.gui.Workspace;
import saf.AppTemplate;
import static saf.settings.AppPropertyType.LOAD_ERROR_MESSAGE;
import static saf.settings.AppPropertyType.LOAD_ERROR_TITLE;
import static saf.settings.AppPropertyType.SAVE_WORK_TITLE;
import static saf.settings.AppPropertyType.WORK_FILE_EXT;
import static saf.settings.AppPropertyType.WORK_FILE_EXT_DESC;
import static saf.settings.AppStartupConstants.PATH_WORK;
import saf.ui.AppMessageDialogSingleton;

/**
 * This class responds to interactions with other UI editing controls.
 * 
 * @author McKillaGorilla
 * @author Mary R. Taft
 * @version 1.0
 */
public class jClassEditController {
    AppTemplate app;
        
    DataManager dataManager;
    VariableRowDialog variableRowDialog;
    MethodRowDialog methodRowDialog;
    ConfigureParentsDialog configureParentsDialog;
    Group gridLines = new Group();
    
    public jClassEditController(AppTemplate initApp) {
	app = initApp;
	dataManager = (DataManager)app.getDataComponent();
        variableRowDialog = new VariableRowDialog();
        methodRowDialog = new MethodRowDialog();
        configureParentsDialog = new ConfigureParentsDialog();
    }

    //TODO: implement all the following methods.
    // placeholders are currently in--wait for it--place.
    
    public void processClassNameEdited() {
        //System.out.println("class name edited");
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	String classNameText = workspace.getClassNameTextField().getText();
        dataManager.setCurrentClassName(classNameText);
        //app.getGUI().updateToolbarControls(false); //todo don't know if necessary
        
        //structure from PoseMaker methods:
        //(thus might want to add an if (classNameText != null) thingamajig, unsure
//	if (selectedColor != null) {
//	    dataManager.setCurrentFillColor(selectedColor);
//	    app.getGUI().updateToolbarControls(false);
//	}

    }
    
    public void processPackageNameEdited() {
        //System.out.println("package name edited");
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	String classPackageText = workspace.getPackageNameTextField().getText();
        dataManager.setCurrentPackageName(classPackageText);
        //app.getGUI().updateToolbarControls(false); //todo don't know if necessary
    }
    
    public void processParentSelected() {
        //System.out.println("parent selected");
//        for(VBoxData v : dataManager.getNonAPIClasses()){
//            System.out.println(v);
//        }
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        //TODO:
        // - I don't know what to do
        // - I'm so tired
        /*
        TableView methodsTable = workspace.getMethodsTable();
        */
        DraggableVBox selectedVBox = dataManager.getSelectedShape();
        
        if(selectedVBox != null){
            if(selectedVBox.getData().getBoxType() != API_CLASS){
                ArrayList<String> s = configureParentsDialog.showConfigureParentsDialog(dataManager, selectedVBox.getData());
                selectedVBox.getData().setParentNames(s, workspace, dataManager);
            }
            workspace.setDisplayParentsLabel(selectedVBox.getData().getParentNamesString());
            //TODO:
            // - update ComponentToolbar's parentLabel rendering
            /*
            selectedVBox.updateText(); //update VBox rendering
            workspace.loadSelectedShapeSettings(selectedVBox); //update TableView rendering
            */
        }

    }

    public void processAddVariable() {
        //System.out.println("add var");
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
        TableView variablesTable = workspace.getVariablesTable();
        DraggableVBox selectedVBox = dataManager.getSelectedShape();
        
        if(selectedVBox != null){
            if(selectedVBox.getData().getBoxType() != API_CLASS){
                VariableRow vr = variableRowDialog.showAddVariableRowDialog(variablesTable);
                selectedVBox.getData().addVariable(vr, workspace, dataManager);
            }
            
            selectedVBox.updateText(); //update VBox rendering
            workspace.loadSelectedShapeSettings(selectedVBox); //update TableView rendering
        }
    }
    
    public void processEditVariable(VariableRow vr){
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        TableView variablesTable = workspace.getVariablesTable();
        DraggableVBox selectedVBox = dataManager.getSelectedShape();
        
        if(selectedVBox != null){
            boolean typeChanged = variableRowDialog.showEditVariableRowDialog(vr);
            if(!typeChanged){
                selectedVBox.getData().addCorrespondingClasses(vr, workspace, dataManager);
            }
            selectedVBox.updateText(); //update VBox rendering
            workspace.loadSelectedShapeSettings(selectedVBox); //update TableView rendering
        }
        //TODO: glitchy
    }

    public void processRemoveVariable() {
        //System.out.println("remove var");
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        TableView variablesTable = workspace.getVariablesTable();
        DraggableVBox selectedVBox = dataManager.getSelectedShape();

        if(variablesTable.getSelectionModel().getFocusedIndex() != -1){
            selectedVBox.getData().removeVariable(
                    variablesTable.getSelectionModel().getFocusedIndex());
        
        selectedVBox.updateText(); //update VBox rendering
        workspace.loadSelectedShapeSettings(selectedVBox); //update TableView rendering
        }
    }

    public void processAddMethod() {
        //System.out.println("add method");
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
        TableView methodsTable = workspace.getMethodsTable();
        DraggableVBox selectedVBox = dataManager.getSelectedShape();
        
        if(selectedVBox != null){
            if(selectedVBox.getData().getBoxType() != API_CLASS){
                MethodRow mr = methodRowDialog.showAddMethodRowDialog(methodsTable, selectedVBox.getData());
                selectedVBox.getData().addMethod(mr, workspace, dataManager);
            }
            // System.out.println("type: " + selectedVBox.getData().getBoxType());
            
            selectedVBox.updateText(); //update VBox rendering
            workspace.loadSelectedShapeSettings(selectedVBox); //update TableView rendering
        }
    }

    public void processEditMethod(MethodRow mr){
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        DraggableVBox selectedVBox = dataManager.getSelectedShape();

        if(selectedVBox != null){
            methodRowDialog.showEditMethodRowDialog(mr);
            //note on following line: incorrect, but better to add excess then show no change
            selectedVBox.getData().addCorrespondingClasses(mr, workspace, dataManager);
            selectedVBox.updateText(); //update VBox rendering
            workspace.loadSelectedShapeSettings(selectedVBox); //update TableView rendering
        }

    }

    public void processRemoveMethod() {
        //System.out.println("remove method");
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        TableView methodsTable = workspace.getMethodsTable();
        DraggableVBox selectedVBox = dataManager.getSelectedShape();

        if(methodsTable.getSelectionModel().getFocusedIndex() != -1){
            selectedVBox.getData().removeMethod(
                    methodsTable.getSelectionModel().getFocusedIndex());
        
        selectedVBox.updateText(); //update VBox rendering
        workspace.loadSelectedShapeSettings(selectedVBox); //update TableView rendering
        }
    }
    
    public void processPhotoTool() {
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	Pane canvas = workspace.getCanvas();
        boolean mustReturn = false;
        if(canvas.getChildren().contains(gridLines)){
            processGridToggle(false);
            mustReturn = true;
        }
        try {
            // PROMPT THE USER FOR A FILE NAME
            FileChooser fc = new FileChooser();
            fc.setInitialDirectory(new File(PATH_WORK)); //todo: edit path
//            File file = new File(PATH_WORK+"/UML Diagram.png");

            fc.setTitle("Choose a Location to Export Photo");
            fc.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("YOU HAVE TO ADD EXTENSION YOURSELF", ".png"));
            
            File file = fc.showSaveDialog(app.getGUI().getWindow());
            if (file != null) {
                    //                saveWork(selectedFile);
                    WritableImage image = canvas.snapshot(new SnapshotParameters(), null);
                    ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            }
                        
        } catch (IOException ioe) {
            //ioe.printStackTrace();
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    dialog.show("Error Exporting Photo", "There was an error exporting your diagram to photo.");
        }
        if(mustReturn){
            processGridToggle(true);
        }
    }

    public void processCodeTool() {
        //System.out.println("export code");
    }

    public void processSelectTool() {
        //System.out.println("select");
        
        // CHANGE THE CURSOR
	Scene scene = app.getGUI().getPrimaryScene();
	scene.setCursor(Cursor.DEFAULT);
	
	// CHANGE THE STATE
	dataManager.setState(jClassDesignerState.SELECTING_SHAPE);	
	
	// ENABLE/DISABLE THE PROPER BUTTONS
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace();
        
        //todo (for ALL relevant buttons): set jClassDesignerState and rewrite
        // Workspace's reloadWorkspace() method to appropriate disable buttons
        // AND CONTROLS (e.g., selection shouldn't be valid when the select
        // tool[/button] is not currently in use)
    }

    public void processResizeTool() {
        //System.out.println("resize");
        
        // CHANGE THE CURSOR
	Scene scene = app.getGUI().getPrimaryScene();
	scene.setCursor(Cursor.DEFAULT);
	
	// CHANGE THE STATE
	dataManager.setState(jClassDesignerState.SIZING_SHAPE);	
	
	// ENABLE/DISABLE THE PROPER BUTTONS
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace();

    }

    /*
    public void processAddClass() {
        //System.out.println("add class");
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        Pane canvas = workspace.getCanvas();
        
        VBoxData classData = new VBoxData(CLASS); //todo/note: currently only considering classes (not interfaces) in the UML diagram
        DraggableVBox classVBox = new DraggableVBox(classData);
        
        //note: always have to run VBoxData setter calls & DraggableVBox updateText()
        // in parallel (putting updateText() calls in VBoxData's setter methods
        // would add unwanted dependency)
        //TODO / updated note: instantiated a VBox upon the instantiation of its data,
        // i.e., call initNewShape() in the initNewData() method
        classData.setClassName(workspace.getClassNameTextField().getText());
        classData.setPackageName(workspace.getPackageNameTextField().getText());
        classVBox.updateText();
        canvas.getChildren().add(classVBox); //adds VBox to canvas
        dataManager.initNewShape(classVBox); //adds VBox to ObservableList
        dataManager.setSelectedShape(classVBox); //sets VBox as selectedShape
        dataManager.initNewData(classData); //adds data to ArrayList
        
	// ENABLE/DISABLE THE PROPER BUTTONS
	//Workspace workspace = (Workspace)app.getWorkspaceComponent();
	//workspace.reloadWorkspace();

        //todo: will likely want to factor out r.set* into a method in DataManager
        // (especially to be consistent with other methods)
    }
    */

    public void processAddClass(){
        //get stuff
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        //init a new class
        VBoxData classData = new VBoxData(CLASS, true);
        //init the new class's basic information
        classData.setClassName(workspace.getClassNameTextField().getText());
        classData.setPackageName(workspace.getPackageNameTextField().getText());
        workspace.setDisplayParentsLabel(classData.getParentNamesString());
        //add the class to dataManager's ArrayList of classes in the diagram
        //(note: graphical representation will be taken care of inside 
        dataManager.initNewData(classData);
    }

    public void processAddInterface() {
        //get stuff
        Workspace workspace = (Workspace)app.getWorkspaceComponent();
        //init a new class
        VBoxData classData = new VBoxData(INTERFACE, true);
        //init the new class's basic information
        classData.setClassName(workspace.getClassNameTextField().getText());
        classData.setPackageName(workspace.getPackageNameTextField().getText());
        //add the class to dataManager's ArrayList of classes in the diagram
        //(note: graphical representation will be taken care of inside 
        dataManager.initNewData(classData);
    }

    public void processRemove() {
        //System.out.println("remove");
        
        // REMOVE THE SELECTED SHAPE IF THERE IS ONE
	dataManager.removeSelectedShape();
	
	// ENABLE/DISABLE THE PROPER BUTTONS
	Workspace workspace = (Workspace)app.getWorkspaceComponent();
	workspace.reloadWorkspace();
	app.getGUI().updateToolbarControls(false);
        //todo: add dialog to verify user's decision to remove a node
        System.out.println("Just removed a class.");
        System.out.println("Remaining non-API classes in diagram: ");
        System.out.println(dataManager.getNonAPIClasses());
        System.out.println("Remaining classes in whole project (including API classes): ");
        System.out.println(dataManager.getDataItems());

    }

    public void processUndo() {
        System.out.println("The undo button doesn't work.");
    }

    public void processRedo() {
        System.out.println("The redo button doesn't work.");
    }

    public void processZoomIn() {
        //System.out.println("zoom in");
        Workspace workspace = (Workspace)app.getWorkspaceComponent();

        Scale scaleTransform = new Scale(2, 2, 0, 0);
        workspace.getCanvas().getTransforms().add(scaleTransform);
    }

    public void processZoomOut() {
        //System.out.println("zoom out");
        Workspace workspace = (Workspace)app.getWorkspaceComponent();

        Scale scaleTransform = new Scale(.5, .5, 0, 0);
        workspace.getCanvas().getTransforms().add(scaleTransform);
    }

    public void processGridToggle(boolean isSelected) {
        //System.out.println("grid: " + isSelected);
        if(isSelected){
            Workspace workspace = (Workspace)app.getWorkspaceComponent();
            Pane p = workspace.getCanvas();
            int i = 0;
            while(i < 14000){
                Line l = new Line(i, 0, i, 10000);
                gridLines.getChildren().add(l);
                i += 15;
            }
            i = 0;
            while(i < 10000){
                Line l = new Line(0, i, 14000, i);
                gridLines.getChildren().add(l);
                i += 15;
            }
            gridLines.toBack();
            if(gridLines != null){
                p.getChildren().add(0, gridLines);
            }
        }else{
            Workspace workspace = (Workspace)app.getWorkspaceComponent();
            Pane p = workspace.getCanvas();
            p.getChildren().remove(0);
        }
        
    }

    public void processSnapToggle(boolean isSelected) {
        //System.out.println("snap: " + isSelected);
        dataManager.isSnapped = isSelected;
    }
    
}
