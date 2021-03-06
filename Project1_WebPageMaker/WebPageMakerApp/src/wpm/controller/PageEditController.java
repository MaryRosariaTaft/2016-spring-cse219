package wpm.controller;

import java.io.IOException;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.web.WebEngine;
import properties_manager.PropertiesManager;
import saf.ui.AppMessageDialogSingleton;
import saf.ui.AppYesNoCancelDialogSingleton;
import static saf.settings.AppPropertyType.REMOVE_NODE_TITLE;
import static saf.settings.AppPropertyType.REMOVE_NODE_MESSAGE;
import static wpm.PropertyType.ADD_ELEMENT_ERROR_MESSAGE;
import static wpm.PropertyType.ADD_ELEMENT_ERROR_TITLE;
import static wpm.PropertyType.ATTRIBUTE_UPDATE_ERROR_MESSAGE;
import static wpm.PropertyType.ATTRIBUTE_UPDATE_ERROR_TITLE;
import static wpm.PropertyType.CSS_EXPORT_ERROR_MESSAGE;
import static wpm.PropertyType.CSS_EXPORT_ERROR_TITLE;
import static wpm.PropertyType.ILLEGAL_NODE_REMOVAL_ERROR_TITLE;
import static wpm.PropertyType.ILLEGAL_NODE_REMOVAL_ERROR_MESSAGE;
import wpm.WebPageMaker;
import wpm.data.DataManager;
import wpm.data.HTMLTagPrototype;
import wpm.file.FileManager;
import static wpm.file.FileManager.TEMP_CSS_PATH;
import static wpm.file.FileManager.TEMP_PAGE;
import wpm.gui.Workspace;

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

    // HERE'S THE FULL APP, WHICH GIVES US ACCESS TO OTHER STUFF
    WebPageMaker app;

    // WE USE THIS TO MAKE SURE OUR PROGRAMMED UPDATES OF UI
    // VALUES DON'T THEMSELVES TRIGGER EVENTS
    private boolean enabled;

    /**
     * Constructor for initializing this object, it will keep the app for later.
     *
     * @param initApp The JavaFX application this controller is associated with.
     */
    public PageEditController(WebPageMaker initApp) {
	// KEEP IT FOR LATER
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

    /**
     * This function responds live to the user typing changes into a text field
     * for updating element attributes. It will respond by updating the
     * appropriate data and then forcing an update of the temp site and its
     * display.
     *
     * @param selectedTag The element in the DOM (our tree) that's currently
     * selected and therefore is currently having its attribute updated.
     *
     * @param attributeName The name of the attribute for the element that is
     * currently being updated.
     *
     * @param attributeValue The new value for the attribute that is being
     * updated.
     */
    public void handleAttributeUpdate(HTMLTagPrototype selectedTag, String attributeName, String attributeValue) throws IOException {
	if (enabled) {
	    try {
		// FIRST UPDATE THE ELEMENT'S DATA
		selectedTag.addAttribute(attributeName, attributeValue);

		// THEN FORCE THE CHANGES TO THE TEMP HTML PAGE
		FileManager fileManager = (FileManager) app.getFileComponent();
		fileManager.exportData(app.getDataComponent(), TEMP_PAGE);

		// AND FINALLY UPDATE THE WEB PAGE DISPLAY USING THE NEW VALUES
		Workspace workspace = (Workspace) app.getWorkspaceComponent();
		workspace.getHTMLEngine().reload();
	    } catch (IOException ioe) {
		// AN ERROR HAPPENED WRITING TO THE TEMP FILE, NOTIFY THE USER
		PropertiesManager props = PropertiesManager.getPropertiesManager();
		AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
		dialog.show(props.getProperty(ATTRIBUTE_UPDATE_ERROR_TITLE), props.getProperty(ATTRIBUTE_UPDATE_ERROR_MESSAGE));
	    }
	}
    }

    /**
     * This function responds to when the user tries to add an element to the
     * tree being edited.
     *
     * @param element The element to add to the tree.
     */
    public void handleAddElementRequest(HTMLTagPrototype element) throws IOException {
	if (enabled) {
	    Workspace workspace = (Workspace) app.getWorkspaceComponent();

	    // GET THE TREE TO SEE WHICH NODE IS CURRENTLY SELECTED
	    TreeView tree = workspace.getHTMLTree();
	    TreeItem selectedItem = (TreeItem) tree.getSelectionModel().getSelectedItem();
	    HTMLTagPrototype selectedTag = (HTMLTagPrototype) selectedItem.getValue();

	    // MAKE A NEW HTMLTagPrototype AND PUT IT IN A NODE
	    HTMLTagPrototype newTag = element.clone();
	    TreeItem newNode = new TreeItem(newTag);

	    // ADD THE NEW NODE, only if the the currently selected
            //node is a legal parent of the node to be inserted
            //(there must be a better way to get the parent type name)
            String parentName = selectedItem.getValue().toString();
            parentName = parentName.substring(1,parentName.length()-1);
            if(newTag.isLegalParent(parentName)){
                selectedItem.getChildren().add(newNode);
            }

	    // SELECT THE NEW NODE
	    tree.getSelectionModel().select(newNode);
	    selectedItem.setExpanded(true);

	    // FORCE A RELOAD OF TAG EDITOR
	    workspace.reloadWorkspace();
            
            // RELOAD THE HTML ENGINE
            WebEngine htmlEngine = workspace.getHTMLEngine();
            htmlEngine.reload();            

	    try {
		FileManager fileManager = (FileManager) app.getFileComponent();
		fileManager.exportData(app.getDataComponent(), TEMP_PAGE);
	    } catch (IOException ioe) {
		// AN ERROR HAPPENED WRITING TO THE TEMP FILE, NOTIFY THE USER
		PropertiesManager props = PropertiesManager.getPropertiesManager();
		AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
		dialog.show(props.getProperty(ADD_ELEMENT_ERROR_TITLE), props.getProperty(ADD_ELEMENT_ERROR_MESSAGE));
	    }
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
    private boolean removalVerified() {
	PropertiesManager props = PropertiesManager.getPropertiesManager();
	
        // PROMPT THE USER TO DELETE THE NODE
	AppYesNoCancelDialogSingleton yesNoDialog = AppYesNoCancelDialogSingleton.getSingleton();
        yesNoDialog.show(props.getProperty(REMOVE_NODE_TITLE), props.getProperty(REMOVE_NODE_MESSAGE));
        
        // AND NOW GET THE USER'S SELECTION
        String selection = yesNoDialog.getSelection();

        // RETURN TRUE IF THE USER SELECTED 'YES', FALSE IF 'NO' OR 'CANCEL'
        return selection.equals(AppYesNoCancelDialogSingleton.YES);
        
    }

    
    /**
     * This function responds to when the user tries to remove an element from
     * the tree being edited.
     * 
     */
    public void handleRemoveElementRequest() throws IOException {

        if(enabled){
 	    Workspace workspace = (Workspace) app.getWorkspaceComponent();

	    // GET THE TREE TO SEE WHICH NODE IS CURRENTLY SELECTED
	    TreeView tree = workspace.getHTMLTree();
	    TreeItem selectedItem = (TreeItem) tree.getSelectionModel().getSelectedItem();
            String selectedTag = selectedItem.getValue().toString();

            // DISALLOW REMOVAL OF BASIC ELEMENTS
            if(selectedTag.contains("html") ||
               selectedTag.contains("head") ||
               selectedTag.contains("title")||
               selectedTag.contains("link") ||
               selectedTag.contains("body") ){
                // DISPLAY ERROR MESSAGE AND RETURN
                PropertiesManager props1 = PropertiesManager.getPropertiesManager();
		AppMessageDialogSingleton dialog1 = AppMessageDialogSingleton.getSingleton();
                dialog1.show(props1.getProperty(ILLEGAL_NODE_REMOVAL_ERROR_TITLE), props1.getProperty(ILLEGAL_NODE_REMOVAL_ERROR_MESSAGE));
                return;
            }
            
            // REMOVE THE NODE, UPON USER VERIFICATION
            if(removalVerified()){
                selectedItem.getParent().getChildren().remove(selectedItem);
             }

	    // FORCE A RELOAD OF TAG EDITOR
	    workspace.reloadWorkspace();

            // RELOAD THE HTML ENGINE
            WebEngine htmlEngine = workspace.getHTMLEngine();
            htmlEngine.reload();
	                
	    try {
		FileManager fileManager = (FileManager) app.getFileComponent();
		fileManager.exportData(app.getDataComponent(), TEMP_PAGE);
	    } catch (IOException ioe) {
		// AN ERROR HAPPENED WRITING TO THE TEMP FILE, NOTIFY THE USER
		PropertiesManager props = PropertiesManager.getPropertiesManager();
		AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
		dialog.show(props.getProperty(ILLEGAL_NODE_REMOVAL_ERROR_TITLE), props.getProperty(ILLEGAL_NODE_REMOVAL_ERROR_MESSAGE));
	    }
            
        }
    }

    /**
     * This function provides a response to when the user changes the CSS
     * content. It responds by updating the data manager with the new CSS text,
     * and by exporting the CSS to the temp css file.
     *
     * @param cssContent The css content.
     *
     */
    public void handleCSSEditing(String cssContent) throws IOException {
	if (enabled) {
	    try {
                
		// MAKE SURE THE DATA MANAGER GETS THE CSS TEXT
		DataManager dataManager = (DataManager) app.getDataComponent();
		dataManager.setCSSText(cssContent);

		// WRITE OUT THE TEXT TO THE CSS FILE
		FileManager fileManager = (FileManager) app.getFileComponent();
		fileManager.exportCSS(cssContent, TEMP_CSS_PATH);

		// REFRESH THE HTML VIEW VIA THE ENGINE / RELOAD WORKSPACE
		Workspace workspace = (Workspace) app.getWorkspaceComponent();
		WebEngine htmlEngine = workspace.getHTMLEngine();
		htmlEngine.reload();
		workspace.reloadWorkspace();

	    } catch (IOException ioe) {
		AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
		PropertiesManager props = PropertiesManager.getPropertiesManager();
		dialog.show(props.getProperty(CSS_EXPORT_ERROR_TITLE), props.getProperty(CSS_EXPORT_ERROR_MESSAGE));
	    }
	}
    }
}
