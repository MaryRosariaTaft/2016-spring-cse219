package jcd.gui;

import java.util.ArrayList;
import javafx.collections.ObservableList;
import jcd.data.VariableRow;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jcd.data.DataManager;
import jcd.data.VBoxData;

/**
 *
 * @author McKillaGorilla
 * @author Mary R. Taft
 */
public class ConfigureParentsDialog  extends Stage {
    // THIS IS THE OBJECT DATA BEHIND THIS UI
    ArrayList<String> parentsArrayList = new ArrayList<String>();
    
    // GUI CONTROLS FOR OUR DIALOG
    VBox vbox;
    Scene dialogScene;
    Label headingLabel;
    ArrayList<CheckBox> parentCheckBoxes;
    HBox alternateHBox;
    Label alternateLabel;
    TextField alternateTextField;
    Button addButton;
    Button completeButton;
    
//    // THIS IS THE WORKSPACE'S COMPONENT TOOLBAR'S TableView FOR VARIABLES
//    TableView variablesTable = null;

    // CONSTANTS FOR OUR UI
    public static final String CONFIGURE_PARENTS_TITLE = "Configure Parents";
    public static final String PARENTS_HEADING = "Parent Details";
    public static final String ALTERNATE_PROMPT = "Add an API Class: ";
    public static final String NAME_PROMPT = "Parent: ";
    public static final String COMPLETE = "Done";
    public static final String CANCEL = "Cancel";
    
    /**
     * Initializes this dialog so that it can be used for either adding
     * new schedule items or editing existing ones.
     * 
     * 
     */
    public ConfigureParentsDialog() {
        // MAKE THIS DIALOG MODAL, MEANING OTHERS WILL WAIT
        // FOR IT WHEN IT IS DISPLAYED
        initModality(Modality.WINDOW_MODAL);
        //initOwner(null);
        
        setEverything(new ArrayList<String>());
        
    }

    public void setEverything(ArrayList<String> parentsStrings){
             // FIRST OUR CONTAINER
        vbox = new VBox();
        vbox.setPadding(new Insets(10, 20, 20, 20));
        
        // PUT THE HEADING IN THE GRID; NOTE THAT THE TEXT WILL DEPEND
        // ON WHETHER WE'RE ADDING OR EDITING
        headingLabel = new Label(CONFIGURE_PARENTS_TITLE);
        
        // INITIALIZE THE DIALOG'S FIELDS
        parentCheckBoxes = new ArrayList<CheckBox>();
        alternateHBox = new HBox();
        alternateLabel = new Label(ALTERNATE_PROMPT);
        alternateTextField = new TextField("");
        addButton = new Button("Add");
        alternateHBox.getChildren().add(alternateLabel);
        alternateHBox.getChildren().add(alternateTextField);
        alternateHBox.getChildren().add(addButton);
        completeButton = new Button(COMPLETE);
        
        // AND FINALLY, THE BUTTONS
        completeButton = new Button(COMPLETE);
        
        // REGISTER EVENT HANDLERS FOR OUR BUTTONS
        completeButton.setOnAction(e -> {
            ConfigureParentsDialog.this.hide();
        });
        
        
        // NOW LET'S ARRANGE THEM ALL AT ONCE
        vbox.getChildren().add(headingLabel);
        for(String c : parentsStrings){
            parentCheckBoxes.add(new CheckBox(c));
        }
        for(CheckBox c : parentCheckBoxes){
            vbox.getChildren().add(c);
        }
        //vbox.getChildren().add(alternateLabel);
        //vbox.getChildren().add(alternateTextField);
        //vbox.getChildren().add(addButton);
        vbox.getChildren().add(alternateHBox);
        vbox.getChildren().add(completeButton);

        // ADD LISTENERS
        for(CheckBox c : parentCheckBoxes){
            c.setOnAction(e -> {
                if(c.isSelected()){
                    parentsArrayList.add(c.getText());
                }else{
                    parentsArrayList.remove(c.getText());
                }
            });
        }
        addButton.setOnAction(e -> {
            CheckBox tmp = new CheckBox(alternateTextField.getText());
            tmp.setSelected(true);
            tmp.setOnAction(f -> {
                if(tmp.isSelected()){
                    parentsArrayList.add(tmp.getText());
                }else{
                    parentsArrayList.remove(tmp.getText());
                }
            });
            parentCheckBoxes.add(tmp);
            parentsArrayList.add(tmp.getText());
            vbox.getChildren().add(parentCheckBoxes.size(), tmp);
        });
        
        // AND PUT THE GRID PANE IN THE WINDOW
        dialogScene = new Scene(vbox);
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
    
    public ArrayList<String> showConfigureParentsDialog(DataManager dataManager, VBoxData data){
        //isANewRow = true;
        /*
        // SET THE DIALOG TITLE
        setTitle(ADD_VARIABLE_ROW_TITLE);
        
        // RESET THE SCHEDULE ITEM OBJECT WITH DEFAULT VALUES
        variableRow = new VariableRow();
        
        // SET DEFAULT UI VALUES
        nameTextField.setText("");
        typeTextField.setText("");
        staticCheckBox.setSelected(false);
        packagePrivateRadioButton.setSelected(true);
        */
        
        parentsArrayList = new ArrayList<String>();
        
        ArrayList<String> tmp = new ArrayList<String>();
        
        for(VBoxData v : dataManager.getNonAPIClasses()){
            tmp.add(v.getClassName());
        }

        setEverything(tmp);

        // AND OPEN IT UP
        this.showAndWait();
        
        // AND ONCE THE "COMPLETE" BUTTON HAS BEEN CLICKED...
        return parentsArrayList;
    }
    /*
    public void showEditVariableRowDialog(VariableRow vr){
        // SET THE DIALOG TITLE
        setTitle(EDIT_VARIABLE_ROW_TITLE);
        
        variableRow = vr;
        
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
        
        return;

    }
    */
    
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