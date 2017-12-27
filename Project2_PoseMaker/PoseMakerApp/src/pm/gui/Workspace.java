package pm.gui;

import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import static javafx.geometry.Pos.CENTER;
import javafx.scene.layout.BorderPane;
import pm.PoseMaker;
import pm.controller.PageEditController;
import pm.data.DataManager;
import pm.file.FileManager;
import properties_manager.PropertiesManager;
import saf.ui.AppGUI;
import saf.AppTemplate;
import saf.components.AppWorkspaceComponent;
import saf.ui.AppMessageDialogSingleton;
import saf.ui.AppYesNoCancelDialogSingleton;

/**
 * This class serves as the workspace component for this application, providing
 * the user interface controls for editing work.
 *
 * @author Richard McKenna
 * @author Mary R. Taft
 * @version 1.0
 */
public class Workspace extends AppWorkspaceComponent {
    
    //NOTE ON ALL THE LINES "gui.updateToolbarControls(false)" --
    // SHOULD BE USING "markAsEdited(gui)" FROM SAF'S AppFileController.java
    // HOWEVER, I HAVE NO IDEA HOW TO ACCESS THIS CLASS
    //My solution (using the updateToolbarControls button) only accounts
    // for saving things after editing; it fails to prompt the user to save
    // unsaved work upon hitting the New, Load, or Exit button

    AppTemplate app;

    AppGUI gui;
    
    FileManager fileManager;
    DataManager dataManager;
    PageEditController pageEditController;
    
    
    VBox leftPane; //the left-hand controls menu
    Pane rightPane; //the right-hand shape-drawing area
    HBox shapes, order; //the top two subpanes of leftPane
    VBox bgColor, fillColor, outlineColor, outlineThickness, snapshot; //the remaining subpanes of leftPane
    ArrayList<Pane> controls; //the set of all the left-hand subpanes
    Button select, remove, rectangle, ellipse, moveToFront, moveToBack; //all the buttons used in the subpanes
    ColorPicker bgCP, fillCP, outlineCP; //color pickers of the subpanes
    Slider thicknessSlider; //thickness slider of its subpane
    
    //dialogs
    AppMessageDialogSingleton messageDialog;
    AppYesNoCancelDialogSingleton yesNoCancelDialog;
    
    /**
     * Constructor for initializing the workspace.  Panes are initialized and 
     * added to the workspace; buttons, color pickers, and sliders are
     * initialized and added to their respective subpanes.  Event handlers
     * are established for the various controls.
     *
     * @param initApp The application this workspace is part of.
     *
     * @throws IOException In case there's an error setting up the interface.
     */
    public Workspace(AppTemplate initApp) throws IOException {
        
	app = initApp;
	gui = app.getGUI();

        //managers of sorts
 	fileManager = (FileManager) app.getFileComponent();
	dataManager = (DataManager) app.getDataComponent();
        dataManager.setWorkspace(this);
        PropertiesManager propsSingleton = PropertiesManager.getPropertiesManager();
 	pageEditController = new PageEditController((PoseMaker) app);
        
        //left pane: controls 
        leftPane = new VBox();
        
        //shapes
        shapes = new HBox();
        shapes.setAlignment(CENTER);
        //select button
        select = new Button();
        Image sel = new Image("file:./images/SelectionTool.png");
        select.setGraphic(new ImageView(sel));
        select.setOnAction(e -> {
            workspace.setCursor(Cursor.DEFAULT);
            pageEditController.handleSelectRequest(rightPane, fillCP, outlineCP, thicknessSlider, gui);
        });
        select.setDisable(true);
        //remove button
        remove = new Button();
        Image rem = new Image("file:./images/Remove.png");
        remove.setGraphic(new ImageView(rem));
        remove.setOnAction(e -> {
            gui.updateToolbarControls(false);
            pageEditController.handleRemoveRequest(rightPane);
        });
        remove.setDisable(true);
        //rectangle button
        rectangle = new Button();
        Image rect = new Image("file:./images/Rect.png");
        rectangle.setGraphic(new ImageView(rect));
        rectangle.setOnAction(e -> {
            //note: would be nice if the cursor were only a crosshair when in rightPane
            // (and the default cursor when hovering over the controls pane, e.g.
            // the snapshot button), but I didn't implement this
            workspace.setCursor(Cursor.CROSSHAIR);
            //gui.updateToolbarControls(false);
            pageEditController.handleRectangleRequest(rightPane, fillCP, outlineCP, thicknessSlider, gui);
        });
        //ellipse button
        ellipse = new Button();
        Image el = new Image("file:./images/Ellipse.png");
        ellipse.setGraphic(new ImageView(el));
        ellipse.setOnAction(e -> {
            workspace.setCursor(Cursor.CROSSHAIR);
            //gui.updateToolbarControls(false);
            pageEditController.handleEllipseRequest(rightPane, fillCP, outlineCP, thicknessSlider, gui);
        });
        shapes.getChildren().add(select);
        shapes.getChildren().add(remove);
        shapes.getChildren().add(rectangle);
        shapes.getChildren().add(ellipse);
        
        //order
        order = new HBox();
        order.setAlignment(CENTER);
        //move to back button
        moveToBack = new Button();
        Image back = new Image("file:./images/Down.png");
        moveToBack.setGraphic(new ImageView(back));
        moveToBack.setOnAction(e -> {
            gui.updateToolbarControls(false);
            pageEditController.handleMoveToBackRequest();
        });
        moveToBack.setDisable(true);
        //move to front button
        moveToFront = new Button();
        Image front = new Image("file:./images/Up.png");
        moveToFront.setGraphic(new ImageView(front));
        moveToFront.setOnAction(e -> {
            gui.updateToolbarControls(false);
            pageEditController.handleMoveToFrontRequest();
        });
        moveToFront.setDisable(true);
        order.getChildren().add(moveToBack);
        order.getChildren().add(moveToFront);
        
        //background color
        bgColor = new VBox();
        Label bgLabel = new Label("Background Color");
        bgCP = new ColorPicker(Color.RED);
        bgColor.getChildren().add(bgLabel);
        bgColor.getChildren().add(bgCP);
        bgCP.setOnAction(e -> {
            gui.updateToolbarControls(false);
            reloadWorkspace();
        });
        
        //fill color
        fillColor = new VBox();
        Label fillLabel = new Label("Fill Color");
        fillCP = new ColorPicker(Color.CORAL);
        fillColor.getChildren().add(fillLabel);
        fillColor.getChildren().add(fillCP);
                
        //outline color
        outlineColor = new VBox();
        Label outlineLabel = new Label("Outline Color");
        outlineCP = new ColorPicker(Color.PURPLE);
        outlineColor.getChildren().add(outlineLabel);
        outlineColor.getChildren().add(outlineCP);
        
        //outline thickness
        outlineThickness = new VBox();
        Label thicknessLabel = new Label("Outline Thickness");
        thicknessSlider = new Slider(1, 25, 10);
        outlineThickness.getChildren().add(thicknessLabel);
        outlineThickness.getChildren().add(thicknessSlider);
        
        //snapshot
        snapshot = new VBox();
        snapshot.setAlignment(CENTER);
        Button capture = new Button();
        Image camera = new Image("file:./images/Snapshot.png");
        capture.setGraphic(new ImageView(camera));
        capture.setOnAction(e -> {
            try{
                pageEditController.handleSnapshotRequest(rightPane);
            }catch (IOException ioe){
                //handled in handleSnapshotRequest()
            }
        });
        snapshot.getChildren().add(capture);
        
        //add the subpanes to the left pane
        controls = new ArrayList<Pane>(){{
            add(shapes);
            add(order);
            add(bgColor);
            add(fillColor);
            add(outlineColor);
            add(outlineThickness);
            add(snapshot);
        }};
        for(Pane p : controls){
            leftPane.getChildren().add(p);
        }
        
        //right pane: shape-drawing area
        rightPane = new Pane();
        
        //workspace
        workspace = new BorderPane();
        ((BorderPane)workspace).setLeft(leftPane);
        ((BorderPane)workspace).setCenter(rightPane);
        workspaceActivated = false;
        
    } 
    
    /**
     * This method specifies the CSS style classes for all the UI components
     * known at the time the workspace is initially constructed.
     */
    @Override
    public void initStyle() {
        for(Pane hb : controls){
            hb.getStyleClass().add("color_chooser_pane");
        }
	leftPane.getStyleClass().add("left_pane");
        rightPane.getStyleClass().add("right_pane");
        workspace.getStyleClass().add("max_pane");
    }
    
    //helper to reloadWorkspace
    public static String toHex(Color color)
    {
        return String.format( "#%02X%02X%02X",
            (int)( color.getRed() * 255 ),
            (int)( color.getGreen() * 255 ),
            (int)( color.getBlue() * 255 ) );
    }

    /**
     * This function reloads the left-hand pane's controls when called.
     */
    @Override
    public void reloadWorkspace() {
	try {

	    pageEditController.enable(false);
            
            //disable buttons appropriately
            select.setDisable(rightPane.getChildren().isEmpty());
            if(rightPane.getChildren().isEmpty()){
                disableButtons();
            }
            
            //update background color
            rightPane.setStyle("-fx-background-color: " + toHex(bgCP.getValue()));
            
	    pageEditController.enable(true);

        } catch (Exception e) {
	    AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
	    PropertiesManager props = PropertiesManager.getPropertiesManager();
	    dialog.show("Error Updating", "There was an error updating the workspace.");
	}
        
    }
    
    //BELOW ARE METHODS USED IN FileManager.java FOR LOADING DATA
    // AND IN PageEditController.java FOR ENABLING/DISABLING BUTTONS
    // MORE CONVENIENTLY THAN VIA THE reloadWorkspace() METHOD
    
    public String getBackgroundColor(){ 
        return bgCP.getValue().toString();
    }
    
    public void setBackgroundColor(String s){
        bgCP.setValue(Color.valueOf(s));
        reloadWorkspace();
    }
    
    private void setBackgroundColor(Color c){
        bgCP.setValue(c);
        reloadWorkspace();
    }
    
    public Pane getRightPane(){
        return rightPane;
    }

    public void deselect(){
        pageEditController.deselect();
    }
    
     public void enableButtons(){
        remove.setDisable(false);
        moveToFront.setDisable(false);
        moveToBack.setDisable(false);
    }
    
    public void disableButtons(){
        remove.setDisable(true);
        moveToFront.setDisable(true);
        moveToBack.setDisable(true);
    }
    
    public void buttonsSelectTool(){
        rectangle.setDisable(false);
        ellipse.setDisable(false);
        select.setDisable(true);
    }
    
    public void buttonsRectangleTool(){
        rectangle.setDisable(true);
        ellipse.setDisable(false);
        select.setDisable(false);
    }
    
    public void buttonsEllipseTool(){
        rectangle.setDisable(false);
        ellipse.setDisable(true);
        select.setDisable(false);
    }
    
    public void resetWorkspace(){
        setBackgroundColor(Color.RED); //since that's the default I used
        rightPane.getChildren().clear();
    }
    
}
