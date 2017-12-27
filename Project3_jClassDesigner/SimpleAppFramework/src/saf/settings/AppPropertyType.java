package saf.settings;

/**
 * This enum provides properties that are to be loaded via
 * XML files to be used for setting up the application.
 * 
 * @author Richard McKenna
 * @author ?
 * @version 1.0
 */
public enum AppPropertyType {
        // LOADED FROM simple_app_properties.xml
        APP_TITLE,
	APP_LOGO,
	APP_CSS,
	APP_PATH_CSS,
        
        // APPLICATION ICONS
        NEW_ICON,
        LOAD_ICON,
        SAVE_ICON,
	SAVE_AS_ICON,
        PHOTO_ICON,
        CODE_ICON,
        EXIT_ICON,
        
        SELECT_ICON,
        RESIZE_ICON,
        ADD_CLASS_ICON,
        ADD_INTERFACE_ICON,
        REMOVE_ICON,
        UNDO_ICON,
        REDO_ICON,
        
        ZOOM_IN_ICON,
        ZOOM_OUT_ICON,
                
        // APPLICATION TOOLTIPS FOR BUTTONS
        NEW_TOOLTIP,
        LOAD_TOOLTIP,
        SAVE_TOOLTIP,
	SAVE_AS_TOOLTIP,
	PHOTO_TOOLTIP,
        CODE_TOOLTIP,
        EXIT_TOOLTIP,
        
        SELECT_TOOLTIP,
        RESIZE_TOOLTIP,
        ADD_CLASS_TOOLTIP,
        ADD_INTERFACE_TOOLTIP,
        REMOVE_TOOLTIP,
        UNDO_TOOLTIP,
        REDO_TOOLTIP,
        
        ZOOM_IN_TOOLTIP,
        ZOOM_OUT_TOOLTIP,
	
	// ERROR MESSAGES
	NEW_ERROR_MESSAGE,
	LOAD_ERROR_MESSAGE,
	SAVE_ERROR_MESSAGE,
	PROPERTIES_LOAD_ERROR_MESSAGE,
	
	// ERROR TITLES
	NEW_ERROR_TITLE,
	LOAD_ERROR_TITLE,
	SAVE_ERROR_TITLE,
	PROPERTIES_LOAD_ERROR_TITLE,
	
	// AND VERIFICATION MESSAGES AND TITLES
        NEW_COMPLETED_MESSAGE,
	NEW_COMPLETED_TITLE,
        LOAD_COMPLETED_MESSAGE,
	LOAD_COMPLETED_TITLE,
        SAVE_COMPLETED_MESSAGE,
	SAVE_COMPLETED_TITLE,	
	SAVE_UNSAVED_WORK_TITLE,
        SAVE_UNSAVED_WORK_MESSAGE,
	
	SAVE_WORK_TITLE,
	LOAD_WORK_TITLE,
	WORK_FILE_EXT,
	WORK_FILE_EXT_DESC,
	PROPERTIES_
}
