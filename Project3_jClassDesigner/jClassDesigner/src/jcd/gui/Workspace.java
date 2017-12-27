package jcd.gui;

import java.io.IOException;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import static jcd.PropertyType.MOVE_TO_BACK_ICON;
import static jcd.PropertyType.MOVE_TO_BACK_TOOLTIP;
import static jcd.PropertyType.MOVE_TO_FRONT_ICON;
import static jcd.PropertyType.MOVE_TO_FRONT_TOOLTIP;
import static jcd.PropertyType.REMOVE_ICON;
import static jcd.PropertyType.REMOVE_TOOLTIP;
import static jcd.PropertyType.SELECTION_TOOL_ICON;
import static jcd.PropertyType.SELECTION_TOOL_TOOLTIP;
import static jcd.PropertyType.SNAPSHOT_ICON;
import static jcd.PropertyType.SNAPSHOT_TOOLTIP;
import jcd.controller.CanvasController;
import jcd.controller.jClassEditController;
import jcd.data.DataManager;
import static jcd.data.DataManager.BLACK_HEX;
import static jcd.data.DataManager.WHITE_HEX;
import jcd.data.DraggableVBox;
import jcd.data.MethodRow;
import jcd.data.VBoxData;
import jcd.data.VariableRow;
import jcd.data.jClassDesignerState;
import saf.ui.AppYesNoCancelDialogSingleton;
import saf.ui.AppMessageDialogSingleton;
import properties_manager.PropertiesManager;
import saf.ui.AppGUI;
import saf.AppTemplate;
import saf.components.AppWorkspaceComponent;
import static saf.settings.AppStartupConstants.FILE_PROTOCOL;
import static saf.settings.AppStartupConstants.PATH_IMAGES;

/**
 * This class serves as the workspace component for this application, providing
 * the user interface controls for editing work.
 *
 * @author Richard McKenna
 * @author Mary R. Taft
 * @version 1.0
 */
public class Workspace extends AppWorkspaceComponent {

    // THESE CONSTANTS ARE FOR TYING THE PRESENTATION STYLE OF
    // THIS Workspace'S COMPONENTS TO A STYLE SHEET THAT IT USES
    static final String CLASS_MAX_PANE = "max_pane";
    static final String CLASS_RENDER_CANVAS = "render_canvas";
    static final String CLASS_BUTTON = "button";
    static final String CLASS_COMPONENT_TOOLBAR = "component_toolbar";
    static final String CLASS_COMPONENT_TOOLBAR_ROW = "component_toolbar_row";
    static final String CLASS_COLOR_CHOOSER_PANE = "color_chooser_pane";
    static final String CLASS_COLOR_CHOOSER_CONTROL = "color_chooser_control";
    static final String EMPTY_TEXT = "";
    static final int BUTTON_TAG_WIDTH = 75;

    // HERE'S THE APP
    AppTemplate app;

    // IT KNOWS THE GUI IT IS PLACED INSIDE
    AppGUI gui;

    // HAS ALL THE CONTROLS FOR EDITING
    VBox componentToolbar;
    
    // CLASS/INTERFACE ATTRIBUTES (NAME, PACKAGE, PARENT)
    GridPane attributesPane;
    Label classNameLabel;
    TextField classNameTextField;
    Label packageLabel;
    TextField packageNameTextField;
    Label parentLabel;
    Label displayParentsLabel;
    Button configureParentsButton;
    
    // VARIABLES PORTION OF COMPONENT TOOLBAR
    VBox variablesVBox;
    HBox variablesHeaderHBox;
    Label variablesLabel;
    Button addVariableButton;
    Button removeVariableButton;
    ScrollPane variablesTableScrollPane;
    TableView variablesTable;
    
    TableColumn vnameCol;
    TableColumn typeCol;
    TableColumn vstaticCol;
    TableColumn vaccessCol;

    
    // METHODS PORTION OF COMPONENT TOOLBAR
    VBox methodsVBox;
    HBox methodsHeaderHBox;
    Label methodsLabel;
    Button addMethodButton;
    Button removeMethodButton;
    ScrollPane methodsTableScrollPane;
    TableView methodsTable;
    
    TableColumn mnameCol;
    TableColumn returnCol;
    TableColumn mstaticCol;
    TableColumn abstractCol;
    TableColumn maccessCol;
    TableColumn argsCol;

    // THIS IS WHERE WE'LL RENDER OUR DRAWING
    ScrollPane canvasScrollPane;
    Pane canvas; //todo: put inside a scrollpane
    
    // HERE ARE THE CONTROLLERS
    CanvasController canvasController;
    jClassEditController jClassEditController;    

    // HERE ARE OUR DIALOGS
    AppMessageDialogSingleton messageDialog;
    AppYesNoCancelDialogSingleton yesNoCancelDialog;
    
    // FOR DISPLAYING DEBUG STUFF
    //Text debugText;

    /**
     * Constructor for initializing the workspace, note that this constructor
     * will fully setup the workspace user interface for use.
     *
     * @param initApp The application this workspace is part of.
     *
     * @throws IOException Thrown should there be an error loading application
     * data for setting up the user interface.
     */
    public Workspace(AppTemplate initApp) throws IOException {
	// KEEP THIS FOR LATER
	app = initApp;

	// KEEP THE GUI FOR LATER
	gui = app.getGUI();

	layoutGUI();
	setupHandlers();
    }
    
    //todo: add getters (and possibly setters) for globals
    
    public TextField getClassNameTextField(){
        return classNameTextField;
    }
    public TextField getPackageNameTextField(){
        return packageNameTextField;
    }
    
    public TableView getVariablesTable(){
        return variablesTable;
    }
    
    public TableView getMethodsTable(){
        return methodsTable;
    }
    
    public void setDisplayParentsLabel(String s){
        displayParentsLabel.setText(s);
    }
    
//    public ColorPicker getFillColorPicker() {
//	return fillColorPicker;
//    }
//    
//    public ColorPicker getOutlineColorPicker() {
//	return outlineColorPicker;
//    }
//    
//    public ColorPicker getBackgroundColorPicker() {
//	return backgroundColorPicker;
//    }
//    
//    public Slider getOutlineThicknessSlider() {
//	return outlineThicknessSlider;
//    }
    
    private void layoutGUI() {
	// THIS WILL GO IN THE RIGHT SIDE OF THE WORKSPACE
	componentToolbar = new VBox();
        
        // ATTRIBUTES (TOP) PORTION OF THE COMPONENT TOOLBAR
        attributesPane = new GridPane();
        classNameLabel = new Label("Class/Interface Name:\t");
        classNameTextField = new TextField("DefaultClassName");
        packageLabel = new Label("Package: ");
        packageNameTextField = new TextField("default");
        parentLabel = new Label("Parent: ");
        displayParentsLabel = new Label("(none)");
        configureParentsButton = new Button("Configure...");
        //todo: probably init above with a list of items
        // or somehow figure out what should be in the drop-down
	
        attributesPane.add(classNameLabel, 1, 1);
        attributesPane.add(classNameTextField, 2, 1);
        attributesPane.add(packageLabel, 1, 2);
        attributesPane.add(packageNameTextField, 2, 2);
        attributesPane.add(parentLabel, 1, 3);
        attributesPane.add(displayParentsLabel, 2, 3);
        attributesPane.add(configureParentsButton, 2, 4);
        
        // VARIABLES (MIDDLE) PORTION OF THE COMPONENT TOOLBAR
        variablesVBox = new VBox();
        variablesHeaderHBox = new HBox();
        variablesLabel = new Label("Variables: ");
        addVariableButton = new Button(); //todo: put in xml
        Image plus = new Image("file:./images/plus.png");
        addVariableButton.setGraphic(new ImageView(plus));
        removeVariableButton = new Button();
        Image minus = new Image("file:./images/minus.png");
        removeVariableButton.setGraphic(new ImageView(minus));
        variablesTableScrollPane = new ScrollPane();
        variablesTable = new TableView();
        
        //TODO: TMP: move contents of tables (vars&methods) elsewhere,
        // and/or have a handler class for rows or something like that
        
        vnameCol = new TableColumn("Name");
        typeCol = new TableColumn("Type");
        vstaticCol = new TableColumn("Static");
        vaccessCol = new TableColumn("Access");
        variablesTable.getColumns().addAll(vnameCol, typeCol, vstaticCol, vaccessCol);
        
        variablesVBox.getChildren().add(variablesHeaderHBox);
        variablesVBox.getChildren().add(variablesTableScrollPane);
        variablesHeaderHBox.getChildren().add(variablesLabel);
        variablesHeaderHBox.getChildren().add(addVariableButton);
        variablesHeaderHBox.getChildren().add(removeVariableButton);
        variablesTableScrollPane.setContent(variablesTable);
        
        // METHODS (BOTTOM) PORTION OF THE COMPONENT TOOLBAR
        methodsVBox = new VBox();
        methodsHeaderHBox = new HBox();
        methodsLabel = new Label("Methods: ");
        addMethodButton = new Button(); //todo: xml
        addMethodButton.setGraphic(new ImageView(plus));
        removeMethodButton = new Button();
        removeMethodButton.setGraphic(new ImageView(minus));
        methodsTableScrollPane = new ScrollPane();
        methodsTable = new TableView();

        mnameCol = new TableColumn("Name");
        returnCol = new TableColumn("Return");
        mstaticCol = new TableColumn("Static");
        abstractCol = new TableColumn("Abstract");
        maccessCol = new TableColumn("Access");
        argsCol = new TableColumn("Args");
        methodsTable.getColumns().addAll(mnameCol, returnCol, mstaticCol, abstractCol, maccessCol, argsCol);
        
        methodsVBox.getChildren().add(methodsHeaderHBox);
        methodsVBox.getChildren().add(methodsTableScrollPane);
        methodsHeaderHBox.getChildren().add(methodsLabel);
        methodsHeaderHBox.getChildren().add(addMethodButton);
        methodsHeaderHBox.getChildren().add(removeMethodButton);
        methodsTableScrollPane.setContent(methodsTable);
        
	// NOW ORGANIZE THE EDIT TOOLBAR
        componentToolbar.getChildren().add(attributesPane);
        componentToolbar.getChildren().add(variablesVBox);
        componentToolbar.getChildren().add(methodsVBox);

	// WE'LL RENDER OUR STUFF HERE IN THE CANVAS
        canvasScrollPane = new ScrollPane();
	canvas = new Pane();
//	debugText = new Text();
//	canvas.getChildren().add(debugText);
//	debugText.setX(100);
//	debugText.setY(100);
        canvasScrollPane.setContent(canvas);
	
	// AND MAKE SURE THE DATA MANAGER IS IN SYNCH WITH THE PANE
	DataManager data = (DataManager)app.getDataComponent();
	data.setShapes(canvas.getChildren());
        //todo: remove following lines; used for testing.
        //canvas.getChildren().add(new Rectangle());
        
        //System.out.println(canvas.getChildren());

	// AND NOW SETUP THE WORKSPACE
	workspace = new BorderPane();
	((BorderPane)workspace).setRight(componentToolbar);
	((BorderPane)workspace).setCenter(canvasScrollPane);
    }
    
//    public void setDebugText(String text) {
//	debugText.setText(text);
//    }
    
    
    private void setupHandlers() {
	// MAKE THE EDIT CONTROLLER
	jClassEditController = new jClassEditController(app);
	
        //todo: edit this section as necessary
	// NOW CONNECT THE BUTTONS TO THEIR HANDLERS
        classNameTextField.addEventFilter(Event.ANY, (e->{
            jClassEditController.processClassNameEdited();
        }));
        packageNameTextField.addEventFilter(Event.ANY, (e->{
            jClassEditController.processPackageNameEdited();
        }));
        configureParentsButton.setOnAction(e->{ //todo: not the right "setOn" method
            jClassEditController.processParentSelected();
        });
        addVariableButton.setOnAction(e->{
            jClassEditController.processAddVariable();
        });
        removeVariableButton.setOnAction(e->{
            jClassEditController.processRemoveVariable();            
        });
        addMethodButton.setOnAction(e->{
            jClassEditController.processAddMethod();            
        });
        removeMethodButton.setOnAction(e->{
            jClassEditController.processRemoveMethod();            
        });
        
	//todo: edit this section as necessary...
	// MAKE THE CANVAS CONTROLLER
	canvasController = new CanvasController(app);
	canvas.setOnMousePressed(e->{
            //Node selectedNode = e.getPickResult().getIntersectedNode();
	    canvasController.processCanvasMousePress((int)e.getX(), (int)e.getY(), e);//, selectedNode);
	});
	canvas.setOnMouseReleased(e->{
	    canvasController.processCanvasMouseRelease((int)e.getX(), (int)e.getY());
	});
	canvas.setOnMouseDragged(e->{
	    canvasController.processCanvasMouseDragged((int)e.getX(), (int)e.getY(), e);
	});
	canvas.setOnMouseExited(e->{
	    canvasController.processCanvasMouseExited((int)e.getX(), (int)e.getY());
	});
	canvas.setOnMouseMoved(e->{
	    canvasController.processCanvasMouseMoved((int)e.getX(), (int)e.getY(), e);
	});
        variablesTable.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                VariableRow vr = (VariableRow)variablesTable.getSelectionModel().getSelectedItem();
                jClassEditController.processEditVariable(vr);
            }
        });
        methodsTable.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                MethodRow mr = (MethodRow)methodsTable.getSelectionModel().getSelectedItem();
                jClassEditController.processEditMethod(mr);
            }
        });

    }
    
    public ScrollPane getScrollPane(){
        return canvasScrollPane;
    }
    
    public Pane getCanvas() {
	return canvas;
    }
    
    public void setImage(ButtonBase button, String fileName) {
	// LOAD THE ICON FROM THE PROVIDED FILE
        String imagePath = FILE_PROTOCOL + PATH_IMAGES + fileName;
        Image buttonImage = new Image(imagePath);
	
	// SET THE IMAGE IN THE BUTTON
        button.setGraphic(new ImageView(buttonImage));	
    }

    /**
     * This function specifies the CSS style classes for all the UI components
     * known at the time the workspace is initially constructed. Note that the
     * tag editor controls are added and removed dynamicaly as the application
     * runs so they will have their style setup separately.
     */
    @Override
    public void initStyle() {
	// NOTE THAT EACH CLASS SHOULD CORRESPOND TO
	// A STYLE CLASS SPECIFIED IN THIS APPLICATION'S
	// CSS FILE
        
        canvasScrollPane.getStyleClass().add(CLASS_COMPONENT_TOOLBAR_ROW); //todo: this is a temp style; update
	canvas.getStyleClass().add(CLASS_RENDER_CANVAS);
        
        attributesPane.getStyleClass().add(CLASS_COMPONENT_TOOLBAR_ROW);
        
        classNameLabel.setFont(new Font(22));
	
	// COLOR PICKER STYLE
//	fillColorPicker.getStyleClass().add(CLASS_BUTTON);
//	outlineColorPicker.getStyleClass().add(CLASS_BUTTON);
//	backgroundColorPicker.getStyleClass().add(CLASS_BUTTON);
	
        // TODO: style for new set of components/nodes/etc.
	componentToolbar.getStyleClass().add(CLASS_COMPONENT_TOOLBAR);
//	row1Box.getStyleClass().add(CLASS_COMPONENT_TOOLBAR_ROW);
//	row2Box.getStyleClass().add(CLASS_COMPONENT_TOOLBAR_ROW);
//	row3Box.getStyleClass().add(CLASS_COMPONENT_TOOLBAR_ROW);
//	backgroundColorLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
//	row4Box.getStyleClass().add(CLASS_COMPONENT_TOOLBAR_ROW);
//	fillColorLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
//	row5Box.getStyleClass().add(CLASS_COMPONENT_TOOLBAR_ROW);
//	outlineColorLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
//	row6Box.getStyleClass().add(CLASS_COMPONENT_TOOLBAR_ROW);
//	outlineThicknessLabel.getStyleClass().add(CLASS_COLOR_CHOOSER_CONTROL);
//	row7Box.getStyleClass().add(CLASS_COMPONENT_TOOLBAR_ROW);
    }

    /**
     * This function reloads all the controls for editing tag attributes into
     * the workspace.
     */
    @Override
    public void reloadWorkspace() {
	DataManager dataManager = (DataManager)app.getDataComponent();
        
        //TODO: disable buttons based on application's state
        //for now, all buttons are always enabled.
        
//	if (dataManager.isInState(jClassDesignerState.STARTING_RECTANGLE)) {
//	    selectionToolButton.setDisable(false);
//	    removeButton.setDisable(true);
//	    rectButton.setDisable(true);
//	    ellipseButton.setDisable(false);
//	}
//	else if (dataManager.isInState(jClassDesignerState.STARTING_ELLIPSE)) {
//	    selectionToolButton.setDisable(false);
//	    removeButton.setDisable(true);
//	    rectButton.setDisable(false);
//	    ellipseButton.setDisable(true);
//	}
//	else if (dataManager.isInState(jClassDesignerState.SELECTING_SHAPE) 
//		|| dataManager.isInState(jClassDesignerState.DRAGGING_SHAPE)
//		|| dataManager.isInState(jClassDesignerState.DRAGGING_NOTHING)) {
//	    boolean shapeIsNotSelected = dataManager.getSelectedShape() == null;
//	    selectionToolButton.setDisable(true);
//	    removeButton.setDisable(shapeIsNotSelected);
//	    rectButton.setDisable(false);
//	    ellipseButton.setDisable(false);
//	    moveToFrontButton.setDisable(shapeIsNotSelected);
//	    moveToBackButton.setDisable(shapeIsNotSelected);
//	}
//	
//	removeButton.setDisable(dataManager.getSelectedShape() == null);
//	backgroundColorPicker.setValue(dataManager.getBackgroundColor());
    }
    
    public void loadSelectedShapeSettings(VBox shape) {
        if (shape != null) {
            DraggableVBox vbox = (DraggableVBox)shape;
            String className = vbox.getData().getClassName();
            String packageName = vbox.getData().getPackageName();
            classNameTextField.setText(className);
            packageNameTextField.setText(packageName);
            vnameCol.setCellValueFactory(new PropertyValueFactory<VBoxData, String>("name"));
            typeCol.setCellValueFactory(new PropertyValueFactory<VBoxData, String>("type"));
            vstaticCol.setCellValueFactory(new PropertyValueFactory<VBoxData, Boolean>("isStatic"));
            vaccessCol.setCellValueFactory(new PropertyValueFactory<VBoxData, String>("access"));
            variablesTable.setItems(vbox.getData().getVariablesObservableList());
            mnameCol.setCellValueFactory(new PropertyValueFactory<VBoxData, String>("methodName"));
            returnCol.setCellValueFactory(new PropertyValueFactory<VBoxData, String>("returnType"));
            mstaticCol.setCellValueFactory(new PropertyValueFactory<VBoxData, String>("isStatic"));
            abstractCol.setCellValueFactory(new PropertyValueFactory<VBoxData, String>("isAbstract"));
            maccessCol.setCellValueFactory(new PropertyValueFactory<VBoxData, String>("access"));
            argsCol.setCellValueFactory(new PropertyValueFactory<VBoxData, String>("argsString"));
            methodsTable.setItems(vbox.getData().getMethodsObservableList());
            setDisplayParentsLabel(vbox.getData().getParentNamesString());
        }
    }

    // HANDLERS FOR BUTTONS IN THE TOP TOOLBAR
    // CALLED IN AppWorkspaceComponent (THIS CLASS'S SUPERCLASS) BY AppFileController
    // THEN REDIRECTS CALL TO jClassEditController
    // todo/note: might be beneficial to put processCodeTool()
    //   inside FileManager instead of in jClassEditController
    public void processPhotoTool(){
	jClassEditController.processPhotoTool();
    }
    public void processCodeTool(){
	jClassEditController.processCodeTool();
    }
    public void processSelectTool(){
	jClassEditController.processSelectTool();
    }
    public void processResizeTool(){
	jClassEditController.processResizeTool();
    }
    public void processAddClass(){
	jClassEditController.processAddClass();
    }
    public void processAddInterface(){
	jClassEditController.processAddInterface();
    }
    public void processRemove(){
	jClassEditController.processRemove();
    }
    public void processUndo(){
	jClassEditController.processUndo();
    }
    public void processRedo(){
	jClassEditController.processRedo();
    }
    public void processZoomIn(){
	jClassEditController.processZoomIn();
    }
    public void processZoomOut(){
	jClassEditController.processZoomOut();
    }
    public void processGridToggle(boolean isSelected){
	jClassEditController.processGridToggle(isSelected);
    }
    public void processSnapToggle(boolean isSelected){
	jClassEditController.processSnapToggle(isSelected);
    }
    
    
}
