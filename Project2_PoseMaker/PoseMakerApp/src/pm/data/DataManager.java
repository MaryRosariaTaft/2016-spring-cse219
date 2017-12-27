package pm.data;

import pm.gui.Workspace;
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
    // THIS IS A SHARED REFERENCE TO THE APPLICATION
    AppTemplate app;
    Workspace workspace;

    /**
     * THis constructor creates the data manager and sets up the
     *
     *
     * @param initApp The application within which this data manager is serving.
     */
    public DataManager(AppTemplate initApp) throws Exception {
	// KEEP THE APP FOR LATER
	app = initApp;
    }

    @Override
    public void reset() {
        getWorkspace().resetWorkspace();
    }
    
    public Workspace getWorkspace(){
        return workspace;
    }
    
    public void setWorkspace(Workspace w){
        workspace = w;
    }
}
