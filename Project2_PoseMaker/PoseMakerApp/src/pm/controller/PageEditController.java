package pm.controller;

import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Slider;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javax.imageio.ImageIO;
import pm.PoseMaker;
import pm.gui.Workspace;
import properties_manager.PropertiesManager;
import saf.ui.AppGUI;
import saf.ui.AppMessageDialogSingleton;
import saf.ui.AppYesNoCancelDialogSingleton;


/**
 * This class provides event programmed responses to workspace interactions for
 * this application for things like adding elements, removing elements, and
 * editing them.
 *
 * @author Richard McKenna
 * @author Mary R. Taft
 * @version 1.0
 */
public class PageEditController {

    PoseMaker app;
    
    Workspace workspace;
    
    Shape selectedShape = null;
    Paint selectedShapeOutlineColor = null;
    double initSceneX, initSceneY, initTranslateX, initTranslateY;
    
    private boolean enabled;
    
    /**
     * Constructor for initializing this object, it will keep the app for later.
     *
     * @param initApp The JavaFX application this controller is associated with.
     */
    public PageEditController(PoseMaker initApp) {
	app = initApp;
    }

    /**
     * This mutator method lets us enable or disable this controller.
     *
     * @param enableSetting If false, this controller will not respond to
     * workspace editing. If true, it will.
     */
    public void enable(boolean enableSetting) {
	enabled = enableSetting;
    }
    
    //handles what happens when the select button is pressed in the controls pane
    //--shapes each become attentive to mouse events
    public void handleSelectRequest(Pane p, ColorPicker fillCP, ColorPicker outlineCP, Slider thicknessSlider, AppGUI gui){
        resetEventHandling(p);
        //disable buttons appropriately
        workspace = (Workspace) app.getWorkspaceComponent();
        workspace.buttonsSelectTool();
        //deselect currently selected node, if any, if the background is clicked on
        p.setOnMousePressed(e -> {
            Node tmp = e.getPickResult().getIntersectedNode();
            if(tmp instanceof Pane){ //as opposed to instanceof Shape
                deselect();
            }
        });
        for (Node s : p.getChildren()){
            s.setOnMousePressed(e -> {
                //deselect whatever may have been selected before
                deselect();
                //then select the new shape
                select((Shape)s);
                //load attributes of the newly-selected shape
                fillCP.setValue(Color.valueOf(selectedShape.getFill().toString()));
                outlineCP.setValue(Color.valueOf(selectedShapeOutlineColor.toString()));
                thicknessSlider.setValue(selectedShape.getStrokeWidth());
                //and make the shape responsive to changes in the controllers
                fillCP.setOnAction(f -> {
                    gui.updateToolbarControls(false);
                    if(selectedShape != null){
                        selectedShape.setFill(fillCP.getValue());
                    }
                });
                outlineCP.setOnAction(f -> {
                    gui.updateToolbarControls(false);
                    if(selectedShape != null){
                        selectedShapeOutlineColor = outlineCP.getValue();
                    }
                });
                thicknessSlider.setOnMouseDragged(f -> {
                    gui.updateToolbarControls(false);
                    if(selectedShape != null){
                        selectedShape.setStrokeWidth(thicknessSlider.getValue());
                    }
                });
                //dragging the shape around the pane
                //note: you can drag it all over the place... not just the right-hand pane
                initSceneX = e.getSceneX();
                initTranslateX = ((Shape)(e.getSource())).getTranslateX();
                initSceneY = e.getSceneY();
                initTranslateY = ((Shape)(e.getSource())).getTranslateY();
                selectedShape.setOnMouseDragged(f -> {
                    gui.updateToolbarControls(false);
                    double offsetX = f.getSceneX() - initSceneX;
                    double newTranslateX = initTranslateX + offsetX;
                    ((Shape)(f.getSource())).setTranslateX(newTranslateX);
                    double offsetY = f.getSceneY() - initSceneY;
                    double newTranslateY = initTranslateY + offsetY;
                    ((Shape)(f.getSource())).setTranslateY(newTranslateY);
                });
            });
        }
    }
    
    private void select(Shape s){
        selectedShape = s;
        selectedShapeOutlineColor = selectedShape.getStroke();
        selectedShape.setStroke(Color.YELLOW);
        workspace = (Workspace) app.getWorkspaceComponent();
        workspace.enableButtons(); //specifically: the remove, moveToFront, and moveToBack buttons
    }
    
    private void deselect(Shape s){
        if(s != null){
            s.setStroke(selectedShapeOutlineColor);
            selectedShape = null;
        }
        workspace = (Workspace) app.getWorkspaceComponent();
        workspace.disableButtons(); //specifically: the remove, moveToFront, and moveToBack buttons
    }
    
    public void deselect(){
        //the way I set this up seems to be less-than-ideal, but it works, so I'm not touching it
        deselect(selectedShape);
    }

    //handles the removal of elements
    //note: the if statement isn't really necessary, because the remove
    // button is only disabled when a shape is selected, anyway, BUT
    // it doesn't hurt to have it there
    public void handleRemoveRequest(Pane p){
        if(removalVerified()){
            if(selectedShape != null){
                p.getChildren().remove(selectedShape);
            }
            workspace = (Workspace) app.getWorkspaceComponent();
            workspace.reloadWorkspace();
            workspace.disableButtons();
        }
    }
    
    /**
     * This helper method verifies that the user really wants to remove
     * the node they've indicated they want to delete by pressing the
     * Remove Element button.  The user will be presented with 3 options:
     * YES, NO, and CANCEL.
     *
     * @return true if the user presses the YES option (delete the node),
     * false if the user presses NO (don't delete) or CANCEL (abort)
     */
    private boolean removalVerified(){
        // PROMPT THE USER TO DELETE THE NODE
	AppYesNoCancelDialogSingleton yesNoDialog = AppYesNoCancelDialogSingleton.getSingleton();
        yesNoDialog.show("Verify Removal", "Are you sure you want to remove this shape?");
        
        // AND NOW GET THE USER'S SELECTION
        String selection = yesNoDialog.getSelection();

        // RETURN TRUE IF THE USER SELECTED 'YES', FALSE IF 'NO' OR 'CANCEL'
        return selection.equals(AppYesNoCancelDialogSingleton.YES);
    }
    
    //handles what happens when the user wants to draw a rectangle
    public void handleRectangleRequest(Pane p, ColorPicker fillCP, ColorPicker outlineCP, Slider thicknessSlider, AppGUI gui){
        resetEventHandling(p);
        workspace = (Workspace) app.getWorkspaceComponent();
        workspace.buttonsRectangleTool();
        p.setOnMousePressed(e -> {
            drawRectangle(e, p, fillCP, outlineCP, thicknessSlider, gui);
        });
    }
    
    //but not of the rectangle-drawing happens here
    private void drawRectangle(MouseEvent e, Pane p, ColorPicker fillCP, ColorPicker outlineCP, Slider thicknessSlider, AppGUI gui){
        workspace = (Workspace) app.getWorkspaceComponent();
        //make new rectangle
        Rectangle r = new Rectangle();
        //add it to the right-hand pane
        p.getChildren().add(r);
        //set its attributes based on mouse position and controls panel selections
        r.setX(e.getX());
        r.setY(e.getY());
        r.setWidth(0);
        r.setHeight(0);
        r.setFill(fillCP.getValue());
        r.setStroke(outlineCP.getValue());
        r.setStrokeWidth(thicknessSlider.getValue());
        //make it grow as the mouse is dragged
        //note: you can draw beyond the boundaries of the right-hand pane... oh well.
        p.setOnMouseDragged(f -> {
            r.setWidth(f.getX() - r.getX());
            r.setHeight(f.getY() - r.getY());
        });
        //and remove the rectangle from pane if it's not visible
        // (happens when user pulls cursor left or up rather than right and down)
        p.setOnMouseReleased(f -> {
            if(r.getWidth() <= 0.0 || r.getHeight() <= 0.0){
                p.getChildren().remove(r);
            }else{
                gui.updateToolbarControls(false);
            }
            workspace.reloadWorkspace();
        });
    }
    
    //same spiel as the handleRectangleRequest method
    public void handleEllipseRequest(Pane p, ColorPicker fillCP, ColorPicker outlineCP, Slider thicknessSlider, AppGUI gui){
        resetEventHandling(p);
        workspace = (Workspace) app.getWorkspaceComponent();
        workspace.buttonsEllipseTool();
        p.setOnMousePressed(e -> {
            drawEllipse(e, p, fillCP, outlineCP, thicknessSlider, gui);
        });
    }

    //see drawRectangle for step-by-step comments
    public void drawEllipse(MouseEvent e, Pane p, ColorPicker fillCP, ColorPicker outlineCP, Slider thicknessSlider, AppGUI gui){
        workspace = (Workspace) app.getWorkspaceComponent();
        Ellipse r = new Ellipse();
        p.getChildren().add(r);
        r.setCenterX(e.getX());
        r.setCenterY(e.getY());
        r.setRadiusX(0);
        r.setRadiusY(0);
        r.setFill(fillCP.getValue());
        r.setStroke(outlineCP.getValue());
        r.setStrokeWidth(thicknessSlider.getValue());
        p.setOnMouseDragged(f -> {
            r.setRadiusX(f.getX() - r.getCenterX());
            r.setRadiusY(f.getY() - r.getCenterY());
        });
        p.setOnMouseReleased(f -> {
            //remove ellipse from pane if it's not visible
            if(r.getRadiusX() <= 0.0 || r.getRadiusY() <= 0.0){
                p.getChildren().remove(r);
            }else{
                gui.updateToolbarControls(false);
            }
            workspace.reloadWorkspace();
        });
    }
    
    //to be called when user toggles between one type of action and another.
    // don't want to draw new shapes when you're trying to select old ones,
    // don't want to select old shapes when you're trying to draw new ones.
    private void resetEventHandling(Pane p){
        deselect();
        p.setOnMousePressed(null);
        p.setOnMouseDragged(null);
        p.setOnMouseReleased(null);
        for(Node s : p.getChildren()){
            ((Shape)s).setOnMousePressed(null);
            ((Shape)s).setOnMouseDragged(null);
            ((Shape)s).setOnMouseReleased(null);
        }
    }
    
    //the following two methods move shapes to the front or back of the pane
    
    public void handleMoveToBackRequest(){
        selectedShape.toBack();
    }
    
    public void handleMoveToFrontRequest(){
        selectedShape.toFront();
    }

    //saves a snapshot of the right-hand pane
    public void handleSnapshotRequest(Pane p) throws IOException{
        WritableImage img = p.snapshot(new SnapshotParameters(), null);
        File f = new File("./snapshots/Pose.png");
        try{
            ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", f);
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            dialog.show("Snapshot Successful", "Your snapshot was saved as ./snapshots/Pose.png.");
        }catch (IOException ioe) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            dialog.show("Snapshot Error", "There was an error saving your snapshot.");
        }
    }
    
}
