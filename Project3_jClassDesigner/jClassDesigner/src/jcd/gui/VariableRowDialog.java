package jcd.gui;

import javafx.collections.ObservableList;
import jcd.data.VariableRow;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author McKillaGorilla
 * @author Mary R. Taft
 */
public class VariableRowDialog  extends Stage {
    // THIS IS THE OBJECT DATA BEHIND THIS UI
    VariableRow variableRow;
    
    // GUI CONTROLS FOR OUR DIALOG
    GridPane gridPane;
    Scene dialogScene;
    Label headingLabel;
    Label nameLabel;
    TextField nameTextField;
    Label typeLabel;
    TextField typeTextField;
    CheckBox staticCheckBox;
    Label accessLabel;
    ToggleGroup accessGroup;
    RadioButton publicRadioButton;
    RadioButton privateRadioButton;
    RadioButton protectedRadioButton;
    RadioButton packagePrivateRadioButton;
    Button completeButton;
    
    // THIS IS FOR KEEPING TRACK OF WHICH BUTTON THE USER PRESSED
    String selection;
    
    // THIS IS FOR KEEPING TRACK OF WHETHER THE USER IS ADDING OR EDITING
    boolean isANewRow = false;
    
    // THIS IS THE WORKSPACE'S COMPONENT TOOLBAR'S TableView FOR VARIABLES
    TableView variablesTable = null;
    
    // CONSTANTS FOR OUR UI
    public static final String ADD_VARIABLE_ROW_TITLE = "Add New Variable";
    public static final String EDIT_VARIABLE_ROW_TITLE = "Edit Variable";
    public static final String VARIABLE_ROW_HEADING = "Variable Details";
    public static final String NAME_PROMPT = "Name: ";
    public static final String TYPE_PROMPT = "Type: ";
    public static final String STATIC_PROMPT = "Static ";
    public static final String ACCESS_PROMPT = "Access: ";
    public static final String COMPLETE = "Done";
    public static final String CANCEL = "Cancel";
    
    /**
     * Initializes this dialog so that it can be used for either adding
     * new schedule items or editing existing ones.
     * 
     * 
     */
    public VariableRowDialog() {
        // MAKE THIS DIALOG MODAL, MEANING OTHERS WILL WAIT
        // FOR IT WHEN IT IS DISPLAYED
        initModality(Modality.WINDOW_MODAL);
        //initOwner(null);
        
        // FIRST OUR CONTAINER
        gridPane = new GridPane();
        gridPane.setPadding(new Insets(10, 20, 20, 20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        
        // PUT THE HEADING IN THE GRID; NOTE THAT THE TEXT WILL DEPEND
        // ON WHETHER WE'RE ADDING OR EDITING
        headingLabel = new Label(VARIABLE_ROW_HEADING);
        
        // INITIALIZE THE DIALOG'S FIELDS
        nameLabel = new Label(NAME_PROMPT);
        nameTextField = new TextField();
        typeLabel = new Label(TYPE_PROMPT);
        typeTextField = new TextField();
        staticCheckBox = new CheckBox(STATIC_PROMPT);
        accessLabel = new Label(ACCESS_PROMPT);
        accessGroup = new ToggleGroup();
        publicRadioButton = new RadioButton("Public");
        privateRadioButton = new RadioButton("Private");
        protectedRadioButton = new RadioButton("Protected");
        packagePrivateRadioButton = new RadioButton("Package Private");
        //group the buttons so only one can be slected at a time
        privateRadioButton.setToggleGroup(accessGroup);
        publicRadioButton.setToggleGroup(accessGroup);
        protectedRadioButton.setToggleGroup(accessGroup);
        packagePrivateRadioButton.setToggleGroup(accessGroup);
        packagePrivateRadioButton.setSelected(true); //default
        
        // ADD LISTENERS
        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            variableRow.setName(newValue);
        });
        typeTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            variableRow.setType(newValue);
        });
        staticCheckBox.setOnAction(e -> {
            variableRow.setStatic(staticCheckBox.isSelected());
        });
        accessGroup.selectedToggleProperty().addListener(e -> {
            if (accessGroup.getSelectedToggle() != null) {
                variableRow.setAccess(accessGroup.getSelectedToggle().toString());
            }
        });
        
        
        // AND FINALLY, THE BUTTONS
        completeButton = new Button(COMPLETE);
        
        // REGISTER EVENT HANDLERS FOR OUR BUTTONS
        /*
        EventHandler completeHandler = (EventHandler<ActionEvent>) (ActionEvent ae) -> {
            VariableRowDialog.this.hide();
        };
        completeButton.setOnAction(completeHandler);
        */
        
        completeButton.setOnAction(e -> {
            VariableRowDialog.this.hide();
        });
        
        
        // NOW LET'S ARRANGE THEM ALL AT ONCE
        gridPane.add(headingLabel, 0, 0, 2, 1);
        gridPane.add(nameLabel, 0, 1, 1, 1);
        gridPane.add(nameTextField, 1, 1, 3, 1);
        gridPane.add(typeLabel, 0, 2, 1, 1);
        gridPane.add(typeTextField, 1, 2, 3, 1);
        gridPane.add(staticCheckBox, 0, 3, 1, 1);
        gridPane.add(privateRadioButton, 0, 4, 1, 1);
        gridPane.add(publicRadioButton, 1, 4, 1, 1);
        gridPane.add(protectedRadioButton, 2, 4, 1, 1);
        gridPane.add(packagePrivateRadioButton, 3, 4, 1, 1);
        gridPane.add(completeButton, 0, 5, 1, 1);

        // AND PUT THE GRID PANE IN THE WINDOW
        dialogScene = new Scene(gridPane);
        this.setScene(dialogScene);
    }
    
    /**
     * Accessor method for getting the selection the user made.
     * 
     * @return Either YES, NO, or CANCEL, depending on which
     * button the user selected when this dialog was presented.
     */
    /*
    public String getSelection() {
        return selection;
    }
    */
    /*
    public ScheduleItem getScheduleItem() { 
        return scheduleItem;
    }
    */
    /**
     * This method loads a custom message into the label and
     * then pops open the dialog.
     * 
     * 
     */
    
    public VariableRow showAddVariableRowDialog(TableView variablesTable) {
        //isANewRow = true;

        // SET THE DIALOG TITLE
        setTitle(ADD_VARIABLE_ROW_TITLE);
        
        // RESET THE SCHEDULE ITEM OBJECT WITH DEFAULT VALUES
        variableRow = new VariableRow();
        
        // SET DEFAULT UI VALUES
        nameTextField.setText("");
        typeTextField.setText("");
        staticCheckBox.setSelected(false);
        packagePrivateRadioButton.setSelected(true);
        
        // AND OPEN IT UP
        this.showAndWait();
        
        // AND ONCE THE "COMPLETE" BUTTON HAS BEEN CLICKED...
        return variableRow;
    }
    
    public boolean showEditVariableRowDialog(VariableRow vr){
        // SET THE DIALOG TITLE
        setTitle(EDIT_VARIABLE_ROW_TITLE);
        
        variableRow = vr;
        
        String initType = new String(vr.getType());
        
        // LOAD IN UI VALUES
        nameTextField.setText(vr.getName());
        typeTextField.setText(vr.getType());
        staticCheckBox.setSelected(vr.isStatic());
        String access = vr.getAccess().toString().toLowerCase();
        if(access.equals("private")){
            privateRadioButton.setSelected(true);
        }else if(access.equals("public")){
            publicRadioButton.setSelected(true);
        }else if(access.equals("protected")){
            protectedRadioButton.setSelected(true);
        }else{
            packagePrivateRadioButton.setSelected(true);
        }
        
        // AND OPEN IT UP
        this.showAndWait();
        
        return initType.equals(vr.getType());

    }
    
    /*
    public void loadGUIData() {
        // LOAD THE UI STUFF
        descriptionTextField.setText(scheduleItem.getDescription());
        datePicker.setValue(scheduleItem.getDate());
        urlTextField.setText(scheduleItem.getLink());       
    }
    
    public boolean wasCompleteSelected() {
        return selection.equals(COMPLETE);
    }
    
    public void showEditScheduleItemDialog(VariableRow rowToEdit) {
        isANewRow = false;
    
        // SET THE DIALOG TITLE
        setTitle(EDIT_SCHEDULE_ITEM_TITLE);
    
    //todo/test: set completeButton's event handler here so that var is *edited*
    // and not added; if so, do the same in the showAddEtc() method
    //todo/update: probably not, just edit VR's properties
    
        // LOAD THE SCHEDULE ITEM INTO OUR LOCAL OBJECT
        scheduleItem = new ScheduleItem();
        scheduleItem.setDescription(itemToEdit.getDescription());
        scheduleItem.setDate(itemToEdit.getDate());
        scheduleItem.setLink(itemToEdit.getLink());
        
        // AND THEN INTO OUR GUI
        loadGUIData();
               
        // AND OPEN IT UP
        this.showAndWait();
    }
    */
    
}